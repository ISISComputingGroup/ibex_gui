package uk.ac.stfc.isis.utils;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

/**
 * A class containing various utilities for use throughout IBEX and the script generator.
 * 
 * @author James King
 *
 */
public class Utils {
	
	/**
	 * Gets the full path to a file given the path relative to this plugin.
	 * @param relativePath The path of the file relative to this plugin.
	 * @return The full path.
	 * @throws IOException if the file could not be found.
	 */
	public static String relativePathToFull(String relativePath, Object askingObject) throws IOException {
		try {
			URL resourcePath = askingObject.getClass().getResource(relativePath);
			String fullPath = FileLocator.resolve(resourcePath).getPath();
			return Path.forWindows(fullPath).toOSString();
		} catch(NullPointerException e) {
			throw new IOException("Cannot find python on relative path: ".concat(relativePath));
		}
	}

}
