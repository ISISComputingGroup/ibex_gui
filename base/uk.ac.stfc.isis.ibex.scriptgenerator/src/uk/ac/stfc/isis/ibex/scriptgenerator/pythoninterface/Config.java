package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

import java.util.List;

/**
 * A configuration describing how the script generator should be configured.
 */
public interface Config {
	/**
	 * @return The names of the parameters that each action requires.
	 */
	public List<String> getParameters();
	
	/**
	 * Performs the defined action.
	 * @param action The action to do.
	 * @return The error if there has been one.
	 */
	public String doAction(ScriptGeneratorAction action);
	
	/**
	 * Performs the check that the arguments are valid.
	 * @param action The action to check for validity.
	 * @return The error if the arguments are not valid.
	 */
	public String parametersValid(ScriptGeneratorAction action);
	
	/**
	 * @return The name of this configuration.
	 */
	public String getName();
	
}
