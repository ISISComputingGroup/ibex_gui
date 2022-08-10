package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
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

/**
 * A model holding information about which perspectives are to be made visible.
 * 
 * Provides a list of the perspectives and whether they are set to visible locally (e.g. in the preferences) or on the server
 * as well as information on which settings should be used and whether the remote settings can be altered.
 *
 */
public class PerspectivesVisibleModel extends ModelObject {
    private PerspectivesProvider perspectivesProvider;
    private ArrayList<PerspectiveInfo> perspectiveInfos = new ArrayList<PerspectiveInfo>();
    
    private PerspectivePreferenceSupplier preferenceSupplier = new PerspectivePreferenceSupplier();
    
    /**
     * The error to display to a user if in manager mode.
     */
    public static final String MANAGER_MODE_ERR = "Manager mode is required";
    
    /**
     * The error to display to the user if the server cannot be reached.
     */
    public static final String SERVER_COMMS_ERR = "Server unavailable";
    
    private Writable<String> writePerspectiveSettings;
    private SwitchableObservable<String> perspectiveSettings;
    
    private List<String> remoteErrorReasons = new ArrayList<String>();
    
    private boolean remoteInError = false;
    private boolean remoteInManager = false;
    private boolean useLocal = false;
    
    private static final Gson GSON = new Gson();
    private static final Type SERVER_PERSPECTIVE_SETTINGS = new TypeToken<Map<String, Boolean>>() { }.getType();
    
    private WritableFactory switchingWritableFactory = new WritableFactory(OnInstrumentSwitch.SWITCH);
    private ObservableFactory switchingObsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);
    
    private static final String PERSPECTIVE_CONFIG_PV = "CS:PERSP:SETTINGS";
    
    /**
     * Constructor for the model of which perspectives to show or hide.
     * @param app The eclipse application.
     * @param partService The part service.
     * @param modelService The model service.
     */
    public PerspectivesVisibleModel(MApplication app, EPartService partService, EModelService modelService) {
        perspectivesProvider = new PerspectivesProvider(app, partService, modelService);
        
        perspectiveSettings = switchingObsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                InstrumentUtils.addPrefix(PERSPECTIVE_CONFIG_PV));
        writePerspectiveSettings = switchingWritableFactory.getSwitchableWritable(new CompressedCharWaveformChannel(),
                InstrumentUtils.addPrefix(PERSPECTIVE_CONFIG_PV));
        
        ManagerModeModel.getInstance().addPropertyChangeListener(ManagerModeModel.IN_MANAGER_MODE_STR, event -> {
            setRemoteInManager();
        });
        setRemoteInManager();
        
        for (MPerspective perspective : perspectivesProvider.getInitialPerspectives()) {
            boolean locallyVisible = !preferenceSupplier.perspectivesToHide().contains(perspective.getElementId());
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
        
        useLocal = preferenceSupplier.getUseLocalPerspectives();
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
    
    /**
     * Get any errors on whether the remote perspectives can be changed.
     * An empty list means that there are no such errors.
     * @return A list of the human readable errors that mean the remote settings cannot be altered.
     */
    public List<String> getRemoteErrors() {
        return remoteErrorReasons;
    }
    
    /**
     * Get the list of perspectives and their associated settings.
     * @return A list of {@link PerspectiveInfo}s for all available perspectives
     */
    public ArrayList<PerspectiveInfo> getPerspectiveInfo() {
        return perspectiveInfos;
    }
    
    /**
     * Get whether the local or server side should be used.
     * @return True if the local settings should be used.
     */
    public Boolean getUseLocal() {
        return useLocal;
    }
    
    /**
     * Set whether the local or remote settings should be used.
     * @param useLocal True to use the local settings.
     */
    public void setUseLocal(Boolean useLocal) {
        this.useLocal = useLocal; 
    }
    
    /**
     * Save the settings to both the server and the local preferences.
     * 
     * The remote settings will be sent to the server whereas the local settings and which settings to use are saved to the preference supplier.
     */
    public void saveState() {
        List<String> locallyVisiblePerspectiveIDs = perspectiveInfos.stream()
                .filter(persp -> !persp.getVisibleLocally())
                .map(persp -> persp.getId())
                .collect(Collectors.toList());
        preferenceSupplier.setPerspectivesToHide(locallyVisiblePerspectiveIDs);
        preferenceSupplier.setUseLocalPerspectives(useLocal);

        JsonSerialisingConverter<Map<String, Boolean>> remoteVisibleSerialiser = new JsonSerialisingConverter<Map<String, Boolean>>(new TypeToken<Map<String, Boolean>>() { }.getType());
        try {
            String json = remoteVisibleSerialiser.apply(perspectiveInfos.stream().collect(Collectors.toMap(PerspectiveInfo::getId, PerspectiveInfo::getVisibleRemotely)));
            writePerspectiveSettings.write(json);
        } catch (ConversionException | IOException e) {
            MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error setting remote views", "There was an error setting the remote available views, please contact the IBEX team");
        }
    }
}
