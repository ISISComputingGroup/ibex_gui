package uk.ac.stfc.isis.ibex.scriptgenerator;

/**
 * An exception for when trying to generate a file from a generated script.
 */
@SuppressWarnings("serial")
public class FileGeneratorException extends Exception {
	
	/**
	 * Initialise an exception that happens when trying to generate a file, with a message to describe the exception.
	 * 
	 * @param message The message to describe the exception.
	 */
	public FileGeneratorException(String message) {
		super(message);
	}

}
