package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.util.List;
import java.util.Map;

/**
 * A wrapper for the available script definitions in the system and utility
 *  methods to generate scripts and check for validity.
 * 
 * @author James King
 *
 */
public interface ScriptDefinitionsWrapper {
	
	/**
	 * Get all script definitions that have failed to load into the script generator and the reason they have failed to load.
	 * 
	 * @return A mapping of unloaded script definitions to the reason they could not be loaded.
	 */
	Map<String, String> getScriptDefinitionLoadErrors();
	
	/**
	 * Get all script definitions available for use in the script generator.
	 * 
	 * @return A list of script definitions for use.
	 */
	List<ScriptDefinitionWrapper> getScriptDefinitions();

	/**
	 * Generate a script from a list of script generator actions for the given script definition.
	 * 
	 * @param scriptGenContent The list of actions to generate a script for.
	 * @param jsonString string to write to script defintion file
	 * @param scriptDefinition The script definition to generate the script with.
	 * @return A string containing the generated script.
	 */
	String generate(List<Map<String, String>> scriptGenContent, String jsonString, ScriptDefinitionWrapper scriptDefinition);
	
	/**
	 * Get a mapping of validity errors of the scriptGenContent against the script definition.
	 * 
	 * @param scriptGenContent The list of actions to check.
	 * @param scriptDefinition The script definition to check with.
	 * @return A map where the key is the index of the action in the list and 
	 * 		the value is the invalidity reason. Empty if there are no invalidity errors.
	 */
	Map<Integer, String> getValidityErrors(List<Map<String, String>> scriptGenContent, ScriptDefinitionWrapper scriptDefinition);
	
	/**
	 * Check if a list of actions are valid under the passed script definitions.
	 * 
	 * @param scriptGenContent The list of actions to check.
	 * @param scriptDefinition The script definition to check with.
	 * @return True if valid, False if not.
	 */
	boolean areParamsValid(List<Map<String, String>> scriptGenContent, ScriptDefinitionWrapper scriptDefinition);
	
    /**
     * Estimate how long (in seconds) the current actions will take
     * 
     * @param scriptGenContent The list of actions to estimate
     * @param scriptDefinition The script definition
     * @return A map where the key is the index of the action in the list and 
     *      the value is the estimated time in seconds
     */
	Map<Integer, Integer> estimateTime(List<Map<String, String>> scriptGenContent, ScriptDefinitionWrapper scriptDefinition);
	
	/**
	 * Check if Python is ready.
	 * 
	 * @return True if python is ready, False if not.
	 */
	boolean isPythonReady();
	
}
