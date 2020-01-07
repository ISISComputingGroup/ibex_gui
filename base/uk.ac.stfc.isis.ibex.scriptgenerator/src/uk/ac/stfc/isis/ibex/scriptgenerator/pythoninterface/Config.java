package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.util.HashMap;
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
	 * @param arguments The arguments for doing the action.
	 */
	public String doAction(HashMap<Integer, String> arguments);
	
	/**
	 * Performs the check that the arguments are valid.
	 * @param arguments The current argument values.
	 * @return The error if the arguments are not valid.
	 */
	public String parametersValid(HashMap<Integer, String> arguments);
	
	/**
	 * @return The name of this configuration.
	 */
	public String getName();
	
}
