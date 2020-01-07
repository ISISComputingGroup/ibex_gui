package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.util.Map;
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
	 * @param arguments The experiment parameters to run the action with.
	 * @return The error if there has been one.
	 */
	public String doAction(Map<String, String> arguments);
	
	/**
	 * Performs the check that the arguments are valid.
	 * @param arguments The parameters to check for validity,
	 *    where the key is the keyword argument and the value is the parameter value to check.
	 * @return The error if the arguments are not valid.
	 */
	public String parametersValid(Map<String, String> arguments);
	
	/**
	 * @return The name of this configuration.
	 */
	public String getName();
	
}
