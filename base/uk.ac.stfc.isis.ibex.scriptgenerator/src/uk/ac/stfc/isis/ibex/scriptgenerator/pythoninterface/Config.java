package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.util.List;
import java.util.Map;

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
	public String doAction(Map<String, String> action);
	
	/**
	 * Performs the check that the arguments are valid.
	 * @param action The action to check for validity.
	 * @return The error if the arguments are not valid.
	 */
	public String parametersValid(Map<String, String> action);
	
	/**
	 * @return The name of this configuration.
	 */
	public String getName();
	
}
