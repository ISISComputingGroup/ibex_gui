package uk.ac.stfc.isis.ibex.scriptgenerator;

/**
 * Thrown when failing to open a file (generated script).
 */
@SuppressWarnings("serial")
public class OpenFileException extends Exception {
	
	/**
	 * Initialise an instance of an exception when failing to open a file (generated script).
	 * 
	 * @param message The message to descibe the exception.
	 */
	public OpenFileException(String message) {
		super(message);
	}

}
