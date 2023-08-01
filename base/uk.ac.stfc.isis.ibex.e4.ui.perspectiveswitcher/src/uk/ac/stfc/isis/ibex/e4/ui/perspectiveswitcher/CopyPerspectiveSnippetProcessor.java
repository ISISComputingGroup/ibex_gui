package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainerElement;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.UIEvents.EventTags;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.ResetLayoutButtonModel;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableObservable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Copies all snippet perspectives to perspective stack called
 * "MainPerspectiveStack" In order to register/reset perspective and not have to
 * sync two copies in e4xmi.
 *
 */
public class CopyPerspectiveSnippetProcessor {

    private static final Logger LOG = IsisLog.getLogger(CopyPerspectiveSnippetProcessor.class);
    
    private PerspectivesProvider perspectivesProvider;
    private MPerspectiveStack perspectiveStack;
    private SwitchableObservable<String> perspectiveSettings;

    private final PerspectivePreferenceSupplier preferenceSupplier = new PerspectivePreferenceSupplier();
    
    private static final Gson GSON = new Gson();
    private static final Type SERVER_IOC_DATA_FORMAT = new TypeToken<Map<String, Boolean>>() { }.getType();
    
    private ObservableFactory switchingObsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);

    private static final String PERSPECTIVE_CONFIG_PV = "CS:PERSP:SETTINGS";
    private static final String PERSPECTIVE_CONFIG_PV_DISCONNECTED_MSG = "Remote perspective PV disconnected, using last known good value.";
    private static final String PERSPECTIVE_CONFIG_PV_ERROR_MSG = "Remote perspective PV in error, using last known good value.";
    
    /* We are expecting the PV to take a while to connect after starting the client.
     * This is to prevent excessive logging on the client startup.
     */
    private final long connectionCheckTimeout = 10000;
    private boolean connectionCheck = false;
    
    /**
     * Clone each snippet that is a perspective and add the cloned perspective to
     * the main PerspectiveStack.
     *
     * @param app          The MApplication used.
     * @param partService  The EPartService used.
     * @param modelService The EModelService used.
     * @param broker       The IEventBroker used.
     */
    @Execute
    public void execute(MApplication app, EModelService modelService, EPartService partService, IEventBroker broker) {
        perspectivesProvider = new PerspectivesProvider(app, partService, modelService);
        perspectiveStack = perspectivesProvider.getTopLevelStack();

        perspectiveSettings = switchingObsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                InstrumentUtils.addPrefix(PERSPECTIVE_CONFIG_PV));

        // Only do this when no other children, or the restored workspace state
        // will be overwritten.
        if (!perspectiveStack.getChildren().isEmpty()) {
            return;
        }

        // clone each snippet that is a perspective and add the cloned
        // perspective into the main PerspectiveStack
        for (MPerspective perspective : perspectivesProvider.getInitialPerspectives()) {
            
            perspectiveStack.getChildren().add(perspective);
            
            // set default (welcome) perspective
            if (perspective.getElementId().equals(perspectivesProvider.getDefaultPerspectiveId())) {
                perspectiveStack.setSelectedElement(perspective);
            }
            
            subscribeChangedElement(broker, perspective);
            subscribeSelectedPerspective(broker, perspective);
        }
        
        perspectiveSettings.subscribe(new BaseObserver<String>() {
            @Override
            public void onValue(String value) {
                if (!preferenceSupplier.getUseLocalPerspectives()) {
                    Map<String, Boolean> visiblePerspectives = GSON.fromJson(value, SERVER_IOC_DATA_FORMAT);
                    setVisiblePerspectivesAsync(visiblePerspectives);
                }
            }

            @Override
            public void onError(Exception e) {
                LOG.error(PERSPECTIVE_CONFIG_PV_DISCONNECTED_MSG);
            }

            @Override
            public void onConnectionStatus(boolean isConnected) {
                if (!isConnected && connectionCheck) {
                    LOG.error(PERSPECTIVE_CONFIG_PV_ERROR_MSG);
                }
            }
        });
        setupConnectionCheckTimeout(connectionCheckTimeout);
        
        preferenceSupplier.addUseLocalPerspectivesListener(useLocal -> {
            if (useLocal) {
                setVisiblePerspectives(preferenceSupplier.perspectivesToHide());
            } else {
                Map<String, Boolean> visiblePerspectives = GSON.fromJson(perspectiveSettings.getValue(), SERVER_IOC_DATA_FORMAT);
                setVisiblePerspectivesAsync(visiblePerspectives);
            }
        });
        
        preferenceSupplier.addHiddenPerspectivesListener(hiddenPerspectives -> {
            if (preferenceSupplier.getUseLocalPerspectives()) {
                setVisiblePerspectives(hiddenPerspectives);
            }
        });

        ResetLayoutButtonModel.getInstance().reset(perspectiveStack.getSelectedElement());
    }

    /**
     * Set which perspectives are visible based on a list of the ones that are hidden.
     * @param hiddenPerspectives The list of hidden perspectives
     */
    private void setVisiblePerspectives(List<String> hiddenPerspectives) {
        Map<String, Boolean> visibilityMap = new HashMap<String, Boolean>();
        for (MPerspective perspective : perspectivesProvider.getInitialPerspectives()) {
            visibilityMap.put(perspective.getElementId(), !hiddenPerspectives.contains(perspective.getElementId()));
        }
        setVisiblePerspectivesAsync(visibilityMap);
    }
    
    /**
     * Set which perspectives are visible based on a map of the perspective ID against and true if visible, false if hidden.
     * @param visiblePerspectiveMap A map of perspective ID vs true if visible, false if hidden.
     */
    private void setVisiblePerspectivesAsync(Map<String, Boolean> visiblePerspectiveMap) {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
            	List<MPerspective> perspectives = perspectiveStack.getChildren();
                if (perspectives.size() > 0) {
                	perspectives.forEach(c -> c.setVisible(true));
                	perspectives.forEach(c -> c.setVisible(visiblePerspectiveMap.getOrDefault(c.getElementId(), true)));
                	if (!perspectiveStack.getSelectedElement().isVisible()) {
                		// If selected perspective is no longer visible then
                		// find first visible perspective and set it as current
                		perspectiveStack.setSelectedElement(perspectives.stream()
                				.filter(c -> c.isVisible()).findFirst().orElse(perspectives.get(0)));
                	}
                } else {
                	LOG.error("No perspectives available to show.");
                }
            }
        });
    }
    
    /**
     * Listen to perspective changes and set the new perspective as current
     * perspective in ResetLayoutButtonModel.
     *
     * @param broker      IEventBroker
     * @param perspective The new current perspective
     */
    private void subscribeSelectedPerspective(IEventBroker broker, MPerspective perspective) {

        EventHandler handler = new EventHandler() {
            @Override
            public void handleEvent(Event event) {
                MUIElement element = (MUIElement) event.getProperty(EventTags.NEW_VALUE);

                if (perspectiveStack.getSelectedElement() != null
                        && !perspectiveStack.getSelectedElement().equals(perspective)) {
                    return;
                }

                if (!(element instanceof MPerspective)) {
                    return;
                }

                ResetLayoutButtonModel.getInstance().setCurrentPerspective((MPerspective) element);

            }
        };

        broker.subscribe(UIEvents.ElementContainer.TOPIC_SELECTEDELEMENT, handler);
    }

    /**
     * Listen to perspective content changes set the current perspective in
     * ResetLayoutButtonModel to changed.
     *
     * @param broker      IEventBroker
     * @param perspective The new current perspective
     */
    private void subscribeChangedElement(IEventBroker broker, MPerspective perspective) {

        EventHandler handler = new EventHandler() {
            boolean alreadyCalled = false;

            @Override
            public void handleEvent(Event event) {
                MUIElement element = (MUIElement) event.getProperty(EventTags.ELEMENT);

                if (!perspectiveStack.getSelectedElement().equals(perspective)) {
                    return;
                }

                if (!(element instanceof MPartSashContainerElement)) {
                    return;
                }

                // The event is called when the workbench first starts up even
                // though nothing has changed.
                if (!alreadyCalled) {
                    alreadyCalled = true;
                    return;
                }

                ResetLayoutButtonModel.getInstance().setChanged(true);
            }
        };

        broker.subscribe(UIEvents.UIElement.TOPIC_CONTAINERDATA, handler);
    }
    
    /**
     * Enables checking the connection of perspective PV after a variable time. After that, performs
     * an additional connection check.
     * @param time Time in milliseconds of how long to wait before enabling PV connection check.
     */
    private void setupConnectionCheckTimeout(long time) {
        Thread connectionTimeoutThread = new Thread("CopyPerspectiveSnippetProcessor timeout thread") {
        	public void run() {
        		try {
					Thread.sleep(time);
					connectionCheck = true;
					if (!perspectiveSettings.isConnected()) {
						LOG.error(PERSPECTIVE_CONFIG_PV_DISCONNECTED_MSG);
					}
				} catch (InterruptedException e) {
					LOG.error("Connection timeout interrupted.");
				}
        	}
        };
        connectionTimeoutThread.start();
    }
}