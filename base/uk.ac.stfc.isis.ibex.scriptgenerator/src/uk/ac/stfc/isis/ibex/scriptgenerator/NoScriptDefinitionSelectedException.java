package uk.ac.stfc.isis.ibex.scriptgenerator;

/**
 * An exception for when we are trying to refer to the selected script definition when
 *  there is no script definition selected e.g. when there is no script definition to select.
 * 
 * @author James King
 *
 */
@SuppressWarnings("serial")
public class NoScriptDefinitionSelectedException extends Exception {
	
    /**
     * Constructor.
     * @param message message for log
     */
	public NoScriptDefinitionSelectedException(String message) {
		super(message);
	}

}
