package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.persistence;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;

import org.eclipse.e4.ui.internal.workbench.E4XMIResourceFactory;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This class contains various utilities for the process of loading and saving perspectives.
 */
@SuppressWarnings("restriction")
public final class PersistenceUtils {
	
	private static final E4XMIResourceFactory RESOURCE_FACTORY = new E4XMIResourceFactory();
	private static final String MAIN_WINDOW_ID = "uk.ac.stfc.isis.ibex.client.e4.product.trimmedwindow.0";

    /**
     * Returns a path to an eclipse perspective layout file corresponding to a given
     * eclipse perspective ID.
     * 
     * @param perspectiveId the ID of the perspective
     * @return the path to the file where this perspective's settings will be saved
     */
    public static File getFileForPersistence(String perspectiveId) {
    	// TODO: make directory if not exist
    	// TODO: configure save path as eclipse preference
        return Paths.get(System.getProperty("user.home"), "ibex-gui", "perspective_layouts", String.format("%s.xml", perspectiveId)).toFile();
    }
    
    /**
     * Find the main window, given an application and a model service.
     * 
     * @param app the application
     * @param modelService the model service
     * @return an optional containing the main window, or an empty optional if not found.
     */
    public static Optional<MWindow> findMainWindow(MApplication app, EModelService modelService) {
    	return Optional.ofNullable(modelService.find(MAIN_WINDOW_ID, app)).map(MWindow.class::cast);
    }
	
    /**
     * Gets a new, empty E4 resource file.
     * @return the newly created resource.
     */
	public static Resource getEmptyResource() {
		return RESOURCE_FACTORY.createResource(null);
    }
}
