package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

/**
 * An exception for when python is not ready to be called in to with a custom message.
 * 
 * @author James King
 */
@SuppressWarnings("serial")
public class PythonNotReadyException extends Exception {
	
	/**
	 * An exception for when python is not ready to be called in to with a custom message.
	 * 
	 * @param message The custom message to throw the exception with.
	 */
	public PythonNotReadyException(String message) {
		super(message);
	}

}
