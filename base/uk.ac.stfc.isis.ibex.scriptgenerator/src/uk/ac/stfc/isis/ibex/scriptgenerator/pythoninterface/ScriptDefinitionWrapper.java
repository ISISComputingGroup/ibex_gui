package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.util.List;
import java.util.Map;

/**
 * A definition describing how the script generator should be configured.
 */
public interface ScriptDefinitionWrapper {
	
	/**
	 * @return The parameter names and their values. e.g. ["param1", "val1", "param2", "val2"]
	 */
	List<ActionParameter> getParameters();
	
	/**
	 * @return The global parameter names and their values. e.g. ["param1", "val1", "param2", "val2"]
	 */
	List<ActionParameter> getGlobalParameters();
	/**
	 * Performs the defined action.
	 * @param action The action to do.
	 * @return The error if there has been one.
	 */
	String doAction(Map<String, String> action);
	
	/**
	 * checks global params are valid
	 * @param the global param to check validity of
	 * @return the error if the arugements are not valid.
	 */
    String globalParamsValid(List<String> globalParams);
      
	
	/**
	 * Performs the check that the arguments are valid.
	 * @param action The action to check for validity.
	 * @return The error if the arguments are not valid.
	 */
	String parametersValid(Map<String, String> action);
	
    /**
     * Estimate how long the action is expected to take.
     * @param action The action to estimate
     * @return An estimate in seconds
     */
    Number estimateTime(Map<String, String> action);
	
	/**
	 * @return The name of this script definition.
	 */
	String getName();
	
	/**
	 * @return A string to be displayed in the script generator UI to help a user when using a script definition.
	 */
	String getHelp();
	
}
