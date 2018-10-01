package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.Logger;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.internal.workbench.E4XMIResourceFactory;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * This class is responsible for saving perspective layouts to file.
 */
@SuppressWarnings("restriction")
public class SavePerspectiveLayout {
	
	private static final Logger LOG = IsisLog.getLogger(SavePerspectiveLayout.class);
	
	/**
	 * Entry point for this class, called when the handler is executed.
	 * 
	 * All parameters are injected by the eclipse framework - they are mostly internal eclipse classes
	 * for dealing with
	 * 
	 * @param app - The MApplication instance (injected by eclipse)
	 * @param partService - The part service (injected)
	 * @param modelService - The model service
	 * @param window - The Eclipse window
	 */
    @Execute
    public void execute(MApplication app, EPartService partService, EModelService modelService, MWindow window) {
    	PerspectivesProvider provider = new PerspectivesProvider(app, partService, modelService);
        for (MPerspective perspective : provider.getPerspectives()) {
        	try {
        	    savePerspective(modelService, window, perspective);
        	} catch (RuntimeException e) {
        		LOG.error(String.format("Runtime error when attempting to save perspective %s", perspective.getElementId()), e);
        	}
        }
    }
    
    
    /**
     * Saves a provided perspective to file.
     * @param modelService - the eclipse model service
     * @param window - the eclipse window
     * @param perspective - the perspective instance to save
     */
    private void savePerspective(EModelService modelService, MWindow window, MPerspective perspective) {
    	
    	final String perspectiveId = perspective.getElementId();

        Resource resource = E4ResourceUtils.getEmptyResource();

        // You must clone the perspective as snippet, otherwise the running
        // application would break, because the saving process of the resource
        // removes the element from the running application model
        MUIElement clonedPerspective = modelService.cloneElement(perspective, window);

        // add the cloned model element to the resource so that it may be stored
        resource.getContents().add((EObject) clonedPerspective);

        File file = FilenameProvider.getPath(perspectiveId).toFile();

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            resource.save(outputStream, null);
            LOG.info(String.format("Saved perspective '%s' to file at '%s'", perspectiveId, file.toString()));
        } catch (IOException ex) {
        	LOG.error(String.format("Unable to save perspective '%s' to file at '%s'", perspectiveId, file.toString()), ex);
        }
    }
}
