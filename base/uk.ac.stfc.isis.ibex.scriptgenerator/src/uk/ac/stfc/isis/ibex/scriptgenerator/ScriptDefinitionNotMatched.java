package uk.ac.stfc.isis.ibex.scriptgenerator;

/**
 * An exception for when we are trying to load a data file using different script definition file than it was used to create it.
 * 
 * @author Bishal Rai
 *
 */
@SuppressWarnings("serial")
public class ScriptDefinitionNotMatched extends Exception {
	
	public ScriptDefinitionNotMatched(String message) {
		super(message);
	}
}
