package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

/**
 * An exception for when something is triggered that shouldn't be when parameters are invalid e.g. generate script is triggered.
 * 
 * @author James King
 *
 */
@SuppressWarnings("serial")
public class InvalidParamsException extends Exception {
	
	/**
	 * Create the error with a message.
	 * 
	 * @param errorMessage The message to create the error with.
	 */
	public InvalidParamsException(String errorMessage) {
		super(errorMessage);
	}

}
