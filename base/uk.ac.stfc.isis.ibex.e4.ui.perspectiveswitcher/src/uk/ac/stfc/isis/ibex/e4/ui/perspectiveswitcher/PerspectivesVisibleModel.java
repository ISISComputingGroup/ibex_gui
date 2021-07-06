package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonSerialisingConverter;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableObservable;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;

public class PerspectivesVisibleModel extends ModelObject {
    private PerspectivesProvider perspectivesProvider;
    private ArrayList<PerspectiveInfo> perspectiveInfos = new ArrayList<PerspectiveInfo>();
    
    private static final Gson GSON = new Gson();
    private static final Type SERVER_IOC_DATA_FORMAT = new TypeToken<Map<String, Boolean>>() { }.getType();
    
    //TODO: Definitely need to close this
    private WritableFactory switchingWritableFactory = new WritableFactory(OnInstrumentSwitch.SWITCH);
    private ObservableFactory switchingObsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);
    
    private final String PERSPECTIVE_CONFIG_PV = "CS:PERSP:SETTINGS";
    
    public PerspectivesVisibleModel(MApplication app, EPartService partService, EModelService modelService) {
        perspectivesProvider = new PerspectivesProvider(app, partService, modelService);
        
        SwitchableObservable<String> displayLog = switchingObsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                InstrumentUtils.addPrefix(PERSPECTIVE_CONFIG_PV));

        
        for (MPerspective perspective : perspectivesProvider.getInitialPerspectives()) {
            boolean locallyVisible = !(new PreferenceSupplier().perspectivesToHide().contains(perspective.getElementId()));
            perspectiveInfos.add(new PerspectiveInfo(perspective.getLabel(), perspective.getElementId(), true, locallyVisible));
        }
        
        displayLog.subscribe(new BaseObserver<String>() {
            public void onValue(String value) {
                Map<String, Boolean> visiblePerspectives = GSON.fromJson(value, SERVER_IOC_DATA_FORMAT);
                perspectiveInfos.forEach(p -> p.setVisibleRemotely(visiblePerspectives.getOrDefault(p.getId(), true)));
            }
        });
    }
    
    public ArrayList<PerspectiveInfo> getPerspectiveInfo() {
        return perspectiveInfos;
    }
    
    public void saveState() {
        List<String> locallyVisiblePerspectiveIDs = perspectiveInfos.stream()
                .filter(persp -> !persp.getVisibleLocally())
                .map(persp -> persp.getId())
                .collect(Collectors.toList());
        new PreferenceSupplier().setPerspectivesToHide(locallyVisiblePerspectiveIDs);
        MPerspectiveStack perspectiveStack = perspectivesProvider.getTopLevelStack();
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                perspectiveStack.getChildren().stream()
                .forEach(persp -> persp.setVisible(!locallyVisiblePerspectiveIDs.contains(persp.getElementId())));
            }
        });
        JsonSerialisingConverter<Map<String, Boolean>> remoteVisibleSerialiser = new JsonSerialisingConverter<Map<String, Boolean>>(new TypeToken<Map<String, Boolean>>(){}.getType());
        try {
            String json = remoteVisibleSerialiser.convert(perspectiveInfos.stream().collect(Collectors.toMap(PerspectiveInfo::getId, PerspectiveInfo::getVisibleRemotely)));
            Writable<String> pv = switchingWritableFactory.getSwitchableWritable(new CompressedCharWaveformChannel(),
                    InstrumentUtils.addPrefix(PERSPECTIVE_CONFIG_PV));
            pv.write(json);
        } catch (ConversionException | IOException e) {
            MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error setting remote views", "There was an error setting the remote available views, please contact the IBEX team");
        }
    }
}
