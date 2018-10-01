package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.persistence;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class is responsible for providing filenames for loading/saving perspective layouts.
 */
public class FilenameProvider {

    /**
     * Returns a path to an eclipse perspective layout file corresponding to a given
     * eclipse perspective ID.
     * 
     * @param perspectiveId the ID of the perspective
     * @return the path to the file where this perspective's settings will be saved
     */
    public static Path getPath(String perspectiveId) {
    	// TODO: make directory if not exist
    	// TODO: configure save path as eclipse preference
        return Paths.get(System.getProperty("user.home"), "ibex-gui", "perspective_layouts", String.format("%s.xml", perspectiveId));
    }
}
