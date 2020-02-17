package uk.ac.stfc.isis.ibex.scriptgenerator;

/**
 * Thrown when failing to open a file (generated script).
 */
@SuppressWarnings("serial")
public class OpenFileException extends Exception {
	
	public OpenFileException(String message) {
		super(message);
	}

}