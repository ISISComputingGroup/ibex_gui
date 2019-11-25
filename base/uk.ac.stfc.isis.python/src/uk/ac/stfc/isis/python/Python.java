package uk.ac.stfc.isis.python;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

/**
 * A class to serve the correct python for the gui to use
 * 
 * @author james King
 *
 */
public class Python {

	private static final String PYTHON_RELATIVE_PATH = "/Python3/python.exe";
	private static final String DEV_PYTHON_PATH = "C:/Instrument/Apps/Python3/python.exe";

	
	/**
	 * Gets the python that's been bundled with the gui, unless it hasn't been bundled and then gets the dev python.
	 * 
	 * @return The string path to the python executable.
	 * @throws IOException if python could not be found.
	 */
	public static String getPythonPath() {
		try {
			return relativePathToFull(PYTHON_RELATIVE_PATH);
		} catch (IOException e) {
			return Path.forWindows(DEV_PYTHON_PATH).toOSString();
		}
	}

	
	/**
	 * Gets the full path to a file given the path relative to this plugin.
	 * 
	 * @param relativePath The path of the file relative to this plugin.
	 * @return The full path.
	 * @throws IOException if the file could not be found.
	 */
	private static String relativePathToFull(String relativePath) throws IOException {
		try {
			URL resourcePath = Python.class.getResource(relativePath);
			String fullPath = FileLocator.resolve(resourcePath).getPath();
			return Path.forWindows(fullPath).toOSString();
		} catch (NullPointerException e) {
			throw new IOException("Cannot find python on relative path: " + relativePath);
		}
	}

}