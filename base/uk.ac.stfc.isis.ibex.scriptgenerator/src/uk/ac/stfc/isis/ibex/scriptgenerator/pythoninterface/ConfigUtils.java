package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

public class ConfigUtils {

	/**
	 * Gets the full path to a file given the path relative to this plugin.
	 * @param relativePath The path of the file relative to this plugin.
	 * @return The full path.
	 * @throws IOException if the file could not be found.
	 */
	public String relativePathToFull(String relativePath) throws IOException {
		URL resourcePath = getClass().getResource(relativePath);
		String fullPath = FileLocator.resolve(resourcePath).getPath();
		return Path.forWindows(fullPath).toOSString();
	}
	
	
}
