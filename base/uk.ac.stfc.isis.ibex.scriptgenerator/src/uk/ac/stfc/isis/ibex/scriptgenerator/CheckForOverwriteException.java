package uk.ac.stfc.isis.ibex.scriptgenerator;

/**
 * An exception for when we are overwriting a file to say we need to check
 *  with the user about whether to overwrite or not.
 */
@SuppressWarnings("serial")
public class CheckForOverwriteException extends Exception {
	
	/**
	 * Fill in the exception details with a message.
	 * 
	 * @param message The message to use to describe the specific exception instance.
	 */
	public CheckForOverwriteException(String message) {
		super(message);
	}

}
