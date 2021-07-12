package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import uk.ac.stfc.isis.ibex.managermode.ManagerModeModel;
import uk.ac.stfc.isis.ibex.managermode.ManagerModePvNotConnectedException;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;

public class PerspectivesVisibleModel extends ModelObject {
    private PerspectivesProvider perspectivesProvider;
    private ArrayList<PerspectiveInfo> perspectiveInfos = new ArrayList<PerspectiveInfo>();
    
    public static String MANAGER_MODE_ERR = "Manager mode is required";
    public static String SERVER_COMMS_ERR = "Server unavailable";
    
    private Writable<String> writePerspectiveSettings;
    
    private List<String> remoteErrorReasons = new ArrayList<String>();
    
    private boolean remoteInError = false;
    private boolean remoteInManager = false;
    
    private static final Gson GSON = new Gson();
    private static final Type SERVER_PERSPECTIVE_SETTINGS = new TypeToken<Map<String, Boolean>>() { }.getType();
    
    //TODO: Definitely need to close this
    private WritableFactory switchingWritableFactory = new WritableFactory(OnInstrumentSwitch.SWITCH);
    private ObservableFactory switchingObsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);
    
    private final String PERSPECTIVE_CONFIG_PV = "CS:PERSP:SETTINGS";
    
    public PerspectivesVisibleModel(MApplication app, EPartService partService, EModelService modelService) {
        perspectivesProvider = new PerspectivesProvider(app, partService, modelService);
        
        SwitchableObservable<String> perspectiveSettings = switchingObsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                InstrumentUtils.addPrefix(PERSPECTIVE_CONFIG_PV));
        writePerspectiveSettings = switchingWritableFactory.getSwitchableWritable(new CompressedCharWaveformChannel(),
                InstrumentUtils.addPrefix(PERSPECTIVE_CONFIG_PV));
        
        ManagerModeModel.getInstance().addPropertyChangeListener(ManagerModeModel.IN_MANAGER_MODE_STR, event -> {
            setRemoteInManager();
        });
        setRemoteInManager();
        
        for (MPerspective perspective : perspectivesProvider.getInitialPerspectives()) {
            boolean locallyVisible = !(new PreferenceSupplier().perspectivesToHide().contains(perspective.getElementId()));
            perspectiveInfos.add(new PerspectiveInfo(perspective.getLabel(), perspective.getElementId(), true, locallyVisible));
        }
        
        perspectiveSettings.subscribe(new BaseObserver<String>() {
            public void onValue(String value) {
                Map<String, Boolean> visiblePerspectives = GSON.fromJson(value, SERVER_PERSPECTIVE_SETTINGS);
                perspectiveInfos.forEach(p -> p.setVisibleRemotely(visiblePerspectives.getOrDefault(p.getId(), true)));
                setRemoteInError(false);
            }
            
            public void onConnectionStatus(boolean isConnected) {
                setRemoteInError(!isConnected);
            }
            
            public void onError(Exception e) {
                setRemoteInError(true);
            }
        });
    }

    private void setRemoteInManager() {
        try {
            this.remoteInManager = ManagerModeModel.getInstance().isInManagerMode();
        } catch (ManagerModePvNotConnectedException e) {
            this.remoteInManager = true;
        }
        updateRemoteErrors();
    }
    
    private void setRemoteInError(boolean remoteInError) {
        this.remoteInError = remoteInError;
        updateRemoteErrors();
    }
    
    private void updateRemoteErrors() {
        ArrayList<String> newReasons = new ArrayList<String>();
        if (remoteInError) {
            newReasons.add(SERVER_COMMS_ERR);
        }
        if (!remoteInManager) {
            newReasons.add(MANAGER_MODE_ERR);
        }
        
        perspectiveInfos.forEach(p -> p.setRemoteEditable(newReasons.isEmpty()));
        
        firePropertyChange("remoteErrors", this.remoteErrorReasons, this.remoteErrorReasons = newReasons);
    }
    
    public List<String> getRemoteErrors() {
        return remoteErrorReasons;
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
        Display.getDefault().asyncExec(() ->
                perspectiveStack.getChildren().stream()
                .forEach(persp -> persp.setVisible(!locallyVisiblePerspectiveIDs.contains(persp.getElementId()))));
        JsonSerialisingConverter<Map<String, Boolean>> remoteVisibleSerialiser = new JsonSerialisingConverter<Map<String, Boolean>>(new TypeToken<Map<String, Boolean>>(){}.getType());
        try {
            String json = remoteVisibleSerialiser.convert(perspectiveInfos.stream().collect(Collectors.toMap(PerspectiveInfo::getId, PerspectiveInfo::getVisibleRemotely)));
            writePerspectiveSettings.write(json);
        } catch (ConversionException | IOException e) {
            MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error setting remote views", "There was an error setting the remote available views, please contact the IBEX team");
        }
    }
}
