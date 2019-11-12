package uk.ac.stfc.isis.python;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

public class Python {
	
	private String pythonRelativePath = "/Python3/python.exe";
	
	/**
	 * Gets the python that's been bundled with the gui.
	 * @return The string path to the python executable.
	 * @throws IOException if python could not be found.
	 */
	public String getPythonPath() {
		try {
			return relativePathToFull(pythonRelativePath);
		} catch(IOException e) {
			return Path.forWindows("C:/Instrument/Apps" + pythonRelativePath).toOSString();
		}
	}
	
	/**
	 * Gets the full path to a file given the path relative to this plugin.
	 * @param relativePath The path of the file relative to this plugin.
	 * @return The full path.
	 * @throws IOException if the file could not be found.
	 */
	private static String relativePathToFull(String relativePath) throws IOException {
		try {
			URL resourcePath = Python.class.getResource(relativePath);
			String fullPath = FileLocator.resolve(resourcePath).getPath();
			return Path.forWindows(fullPath).toOSString();
		} catch(NullPointerException e) {
			throw new IOException("Cannot find python on relative path: " + relativePath);
		}
	}

}
