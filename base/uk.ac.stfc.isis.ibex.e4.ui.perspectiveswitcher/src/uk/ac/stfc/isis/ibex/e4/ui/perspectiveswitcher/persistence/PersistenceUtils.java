package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.persistence;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

/**
 * This class is responsible for providing filenames for loading/saving perspective layouts.
 */
public class PersistenceUtils {
	
	private static final String MAIN_WINDOW_ID = "uk.ac.stfc.isis.ibex.client.e4.product.trimmedwindow.0";
	private static final String SHARED_STATE_FILE_NAME = "shared_state";

    /**
     * Returns a path to an eclipse perspective layout file corresponding to a given
     * eclipse perspective ID.
     * 
     * @param perspectiveId the ID of the perspective
     * @return the path to the file where this perspective's settings will be saved
     */
    public static File getFileForPersistence(String name) {
    	// TODO: make directory if not exist
    	// TODO: configure save path as eclipse preference
        return Paths.get(System.getProperty("user.home"), "ibex-gui", "perspective_layouts", String.format("%s.xml", name)).toFile();
    }
    
    public static Optional<MWindow> findMainWindow(MApplication app, EModelService modelService) {
    	return Optional.ofNullable(modelService.find(MAIN_WINDOW_ID, app))
    			.map(MWindow.class::cast);
    }

	public static File getSharedStateFile() {
		return getFileForPersistence(SHARED_STATE_FILE_NAME);
	}
}
