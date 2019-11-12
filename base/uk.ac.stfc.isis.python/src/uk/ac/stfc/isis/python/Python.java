package uk.ac.stfc.isis.python;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;


public class Python {
	
	/**
	 * Gets the python that's been bundled with the gui.
	 * @return The string path to the python executable.
	 * @throws IOException if python could not be found.
	 */
	public static String getPythonPath() {
		try {
			String pythonPath = relativePathToFull("/Python3/python.exe");
			System.out.println(pythonPath);
			return pythonPath;
		} catch(IOException e) {
			String pythonPath = Path.forWindows("C:/Instrument/Apps/Python3/python.exe").toOSString();
			System.out.println(pythonPath);
			return pythonPath;
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
			System.out.println(resourcePath.toString());
			String fullPath = FileLocator.resolve(resourcePath).getPath();
			return Path.forWindows(fullPath).toOSString();
		} catch(NullPointerException e) {
			throw new IOException("Cannot find python on relative path: ".concat(relativePath));
		}
	}
	

}
