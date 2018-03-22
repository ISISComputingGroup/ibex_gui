package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;

/**
 * Copies all snippet perspectives to perspective stack called "MainPerspectiveStack" In order to register/reset perspective and not have to sync two copies in
 * e4xmi.
 * 
 */
public class CopyPerspectiveSnippetProcessor {

    @Execute
    public void execute(MApplication app, EPartService partService, EModelService modelService) {
    	
    	PerspectivesProvider perspectivesProvider = new PerspectivesProvider(app, partService, modelService);    	
        MPerspectiveStack perspectiveStack = perspectivesProvider.getTopLevelStack();

        // Only do this when no other children, or the restored workspace state will be overwritten.
        if (!perspectiveStack.getChildren().isEmpty())
            return;

        // clone each snippet that is a perspective and add the cloned perspective into the main PerspectiveStack
        boolean isFirst = true;
        for (MPerspective perspective : perspectivesProvider.getInitialPerspectives()) {
           perspectiveStack.getChildren().add(perspective);
           if (isFirst) {
        	   perspectiveStack.setSelectedElement(perspective);
               isFirst = false;
           }
        }
    }
}