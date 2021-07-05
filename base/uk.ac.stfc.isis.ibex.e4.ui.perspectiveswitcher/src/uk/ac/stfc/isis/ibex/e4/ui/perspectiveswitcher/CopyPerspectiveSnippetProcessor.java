package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

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

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.ResetLayoutButtonModel;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableObservable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.BooleanChannel;
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;

/**
 * Copies all snippet perspectives to perspective stack called
 * "MainPerspectiveStack" In order to register/reset perspective and not have to
 * sync two copies in e4xmi.
 *
 */
public class CopyPerspectiveSnippetProcessor {

    private PerspectivesProvider perspectivesProvider;
    private MPerspectiveStack perspectiveStack;

    // TODO: need to close this
    private ObservableFactory switchingObsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);

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

        SwitchableObservable<Boolean> displayLog = switchingObsFactory.getSwitchableObservable(new BooleanChannel(),
                InstrumentUtils.addPrefix("SIMPLE:BI"));

        displayLog.subscribe(new BaseObserver<Boolean>() {
            @Override
            public void onValue(Boolean value) {
                System.out.println("Hiding perspectives");
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        perspectiveStack.getChildren().forEach(c -> c.setVisible(value));
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onConnectionStatus(boolean isConnected) {
                // TODO Auto-generated method stub

            }
        });

        // Only do this when no other children, or the restored workspace state
        // will be overwritten.
        if (!perspectiveStack.getChildren().isEmpty()) {
            return;
        }

        // clone each snippet that is a perspective and add the cloned
        // perspective into the main PerspectiveStack
        boolean isFirst = true;
        for (MPerspective perspective : perspectivesProvider.getInitialPerspectives()) {
            if (!new PreferenceSupplier().perspectivesToHide().contains(perspective.getElementId())) {
                perspectiveStack.getChildren().add(perspective);
                if (isFirst) {
                    perspectiveStack.setSelectedElement(perspective);
                    isFirst = false;
                }
            }
            subscribeChangedElement(broker, perspective);
            subscribeSelectedPerspective(broker, perspective);
        }

        ResetLayoutButtonModel.getInstance().reset(perspectiveStack.getSelectedElement());
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
}