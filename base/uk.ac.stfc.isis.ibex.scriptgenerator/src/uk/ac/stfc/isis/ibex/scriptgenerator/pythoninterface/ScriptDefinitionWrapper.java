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
	 * @return The custom parameter names e.g. ["param1", "param2"]
	 */
	List<String> getCustomOutputs();
	
	/**
	 * Performs the defined action.
	 * 
	 * @param action The action to do.
	 * @return The error if there has been one.
	 */
	String doAction(Map<String, String> action);
	
	/**
	 * Checks global params are valid.
	 * 
	 * @param globalParam The global param to check validity of.
	 * @param index The index of the global parameters to check.
	 * @return the error if the arguments are not valid.
	 */
    String globalParamsValid(String globalParam, int index);
	
	/**
	 * Performs the check that the arguments are valid.
	 * 
	 * @param action The action to check for validity.
	 * @param global_params The global parameters to check validity with.
	 * @return The error if the arguments are not valid.
	 */
    @SuppressWarnings("checkstyle:parametername")
	String parametersValid(Map<String, String> action, List<String> global_params);
	
    /**
     * Estimate how long the action is expected to take.
     * 
     * @param action The action to estimate
     * @param global_params The global parameters to refresh the time estimation with.
     * @return An estimate in seconds
     */
    @SuppressWarnings("checkstyle:parametername")
    Number estimateTime(Map<String, String> action, List<String> global_params);
    
    /**
     * Custom estimates of an action.
     * 
     * @param action The action to estimate
     * @param global_params The global parameters to refresh the time estimation with.
     * @return A list of estimate numbers
     */
    @SuppressWarnings("checkstyle:parametername")
    List<Number> estimateCustom(Map<String, String> action, List<String> global_params);
	
	/**
	 * @return The name of this script definition.
	 */
	String getName();
	
	/**
	 * @return A string to be displayed in the script generator UI to help a user when using a script definition.
	 */
	String getHelp();
	
}
