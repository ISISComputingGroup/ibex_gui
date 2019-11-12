package uk.ac.stfc.isis.python;

import java.io.IOException;

import org.eclipse.core.runtime.Path;

import uk.ac.stfc.isis.utils.Utils;


public class Python {
	
	/**
	 * Gets the python that's been bundled with the gui.
	 * @return The string path to the python executable.
	 * @throws IOException if python could not be found.
	 */
	public String getPythonPath() {
		try {
			return Utils.relativePathToFull("/Python3/python.exe", this);
		} catch(IOException e) {
			return Path.forWindows("C:/Instrument/Apps/Python3/python.exe").toOSString();
		}
	}
	

}
