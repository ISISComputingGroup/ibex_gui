package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

/**
 * An exception to throw when the format of a dynamic script is incorrect.
 */
@SuppressWarnings("serial")
public class DynamicScriptNameFormatException extends Exception {
	
	/**
	 * Create an exception with the given message.
	 * 
	 * @param message The message to create the exception with.
	 */
	public DynamicScriptNameFormatException(String message) {
		super(message);
	}

}
