package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.persistence;

import com.google.common.collect.Streams;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.eclipse.e4.core.di.annotations.Execute;
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
        provider.getPerspectives().forEach(perspective -> savePerspective(modelService, window, perspective));
        
        saveSharedState(modelService, app);
        
    }
    
    
    private void saveSharedState(EModelService modelService, MApplication app) {
    	Resource resource = E4ResourceUtils.getEmptyResource();
    	resource.setTrackingModification(false);
    	
    	MWindow mainWindow = PersistenceUtils.findMainWindow(app, modelService)
    			.orElseThrow(() -> new RuntimeException("Couldn't find main window."));
    	
//    	resource.getContents().addAll(
//    		mainWindow.getSharedElements().stream()
//	    		// .map(element -> modelService.cloneElement(element, null))
//	    		.map(EObject.class::cast)
//	    		.collect(Collectors.toList())
//    	);
    	
    	resource.getContents().add((EObject) app);
    	
    	writeFile(PersistenceUtils.getSharedStateFile(), resource);
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

        writeFile(PersistenceUtils.getFileForPersistence(perspectiveId), resource);
    }
    
    private void writeFile(File file, Resource resource) {
    	
    	try (FileOutputStream outputStream = new FileOutputStream(file)) {
            resource.save(outputStream, null);
            LOG.info(String.format(
            		"Saved state to file at '%s'", file.toString()));
        } catch (IOException ex) {
        	LOG.error(String.format(
        			"Unable to save state to file at '%s'", file.toString()), ex);
        } catch (RuntimeException ex) {
        	LOG.error(String.format(
        			"Runtime error when attempting to save state to file at '%s'", file.toString()), ex);
        }
    }
}
