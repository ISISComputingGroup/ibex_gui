package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

/**
 * An exception that may occur when executing a script dynamically.
 */
@SuppressWarnings("serial")
public class DynamicScriptingException extends Exception {

	/**
	 * Create an exception with the given message.
	 * 
	 * @param message The message to create the exception with.
	 */
	public DynamicScriptingException(String message) {
		super(message);
	}

}
