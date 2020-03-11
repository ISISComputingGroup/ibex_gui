package uk.ac.stfc.isis.ibex.scriptgenerator;

/**
 * An exception for when we are overwriting a file to say we need to check
 *  with the user about whether to overwrite or not.
 */
@SuppressWarnings("serial")
public class CheckForOverwriteException extends Exception {
	
	public CheckForOverwriteException(String message) {
		super(message);
	}

}
