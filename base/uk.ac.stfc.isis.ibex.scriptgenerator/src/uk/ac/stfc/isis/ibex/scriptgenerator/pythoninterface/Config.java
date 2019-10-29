package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

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
	public void doAction(Object... arguments);
	
	/**
	 * Performs the check that the arguments are valid.
	 * @param arguments The current argument values.
	 * @return True if the current values are valid.
	 */
	public Boolean parametersValid(Object... arguments);
	
	/**
	 * @return The name of this configuration.
	 */
	public String getName();
}
