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
import org.eclipse.e4.ui.workbench.modeling.EPlaceholderResolver;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.ResetLayoutButtonModel;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.persistence.PerspectiveLayoutLoader;
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
    private IEventBroker broker;

    /**
     * Clone each snippet that is a perspective and add the cloned perspective
     * to the main PerspectiveStack.
     *
     * @param app
     *            The MApplication used.
     * @param partService
     *            The EPartService used.
     * @param modelService
     *            The EModelService used.
     * @param broker
     *            The IEventBroker used.           
     */
    @Execute
    public void execute(MApplication app, EPartService partService, EModelService modelService, IEventBroker broker, EPlaceholderResolver placeholderResolver) {
        perspectivesProvider = new PerspectivesProvider(app, partService, modelService);
        perspectiveStack = perspectivesProvider.getTopLevelStack();
        this.broker = broker;

        // Only do this when no other children, or the restored workspace state
        // will be overwritten.
//        if (!perspectiveStack.getChildren().isEmpty()) {
//            return;
//        }
        
        Display.getDefault().syncExec(new Runnable() {
        	public void run() {
        		
        		PerspectiveLayoutLoader perspectiveLoader = new PerspectiveLayoutLoader(app, modelService, placeholderResolver);
                
                perspectivesProvider.snippetIds()
                	.filter(id -> !PreferenceSupplier.perspectivesToHide().contains(id))
                    .map(perspectiveLoader::load)
                    .forEach(CopyPerspectiveSnippetProcessor.this::addPerspective);
                
                ResetLayoutButtonModel.getInstance().reset(perspectiveStack.getSelectedElement());
        	}
        });
    }
    
    private void addPerspective(MPerspective perspective) {
    	perspectiveStack.getChildren().add(perspective);
    	if (perspectiveStack.getChildren().size() == 1) {
    		// If it is the only perspective added so far, then select it.
    		perspectiveStack.setSelectedElement(perspective);
    	}
    	
    	subscribeChangedElement(broker, perspective);
    	subscribeSelectedPerspective(broker, perspective);
    }

    /**
     * Listen to perspective changes and set the new perspective as current
     * perspective in ResetLayoutButtonModel.
     *
     * @param broker
     *            IEventBroker
     * @param perspective
     *            The new current perspective
     */
    public void subscribeSelectedPerspective(IEventBroker broker, MPerspective perspective) {

        EventHandler handler = new EventHandler() {
            @Override
            public void handleEvent(Event event) {
                MUIElement element = (MUIElement) event.getProperty(EventTags.NEW_VALUE);

                if (!perspectiveStack.getSelectedElement().equals(perspective)) {
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
     * @param broker
     *            IEventBroker
     * @param perspective
     *            The new current perspective
     */
    public void subscribeChangedElement(IEventBroker broker, MPerspective perspective) {

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