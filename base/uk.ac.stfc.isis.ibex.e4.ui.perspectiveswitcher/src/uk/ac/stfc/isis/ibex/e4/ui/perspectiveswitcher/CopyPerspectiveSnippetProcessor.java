package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

/**
 * Copies all snippet perspectives to perspective stack called "MainPerspectiveStack" In order to register/reset perspective and not have to sync two copies in
 * e4xmi.
 * 
 */
public class CopyPerspectiveSnippetProcessor {
    private static final String MAIN_PERSPECTIVE_STACK_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspectivestack.0";

    @Execute
    public void execute(EModelService modelService, MApplication application) {
        MPerspectiveStack perspectiveStack = (MPerspectiveStack) modelService.find(MAIN_PERSPECTIVE_STACK_ID, application);

        // Only do this when no other children, or the restored workspace state will be overwritten.
        if (!perspectiveStack.getChildren().isEmpty())
            return;

        // clone each snippet that is a perspective and add the cloned perspective into the main PerspectiveStack
        boolean isFirst = true;
        for (MUIElement snippet : application.getSnippets()) {
            if (snippet instanceof MPerspective) {
                MPerspective perspectiveClone = (MPerspective) modelService.cloneSnippet(application, snippet.getElementId(), null);
                perspectiveStack.getChildren().add(perspectiveClone);
                if (isFirst) {
                    perspectiveStack.setSelectedElement(perspectiveClone);
                    isFirst = false;
                }
            }
        }
    }
}