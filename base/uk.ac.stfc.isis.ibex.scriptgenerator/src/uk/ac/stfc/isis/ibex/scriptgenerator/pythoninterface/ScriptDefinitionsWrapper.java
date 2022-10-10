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
	 * true if the repository containing the script definitions is dirty and cannot be pulled.
	 * 
	 * @return true if git reports the repository is dirty
	 */
	boolean updatesAvailable();
	
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
	 * @param jsonString string to write to script definition file
	 * @param scriptDefinition The script definition to generate the script with.
	 * @param globalParams The global parameters to generate the script with.
	 * @return A string containing the generated script.
	 */
	String generate(List<Map<String, String>> scriptGenContent, String jsonString, List<String> globalParams, ScriptDefinitionWrapper scriptDefinition);
	
	/**
	 * Get a list of mappings of validity errors of the scriptGenContent against the script definition.
	 * 
	 * @param scriptGenContent The list of actions to check.
	 * @param scriptDefinition The script definition to check with.
	 * @param globalParams The global parameters to get the validity errors with.
	 * @return A list of maps where the key is the index of the action in the list and 
	 * 		the value is the invalidity reason. Empty if there are no invalidity errors.
	 */
	List<Map<Integer, String>> getValidityErrors(List<String> globalParams, List<Map<String, String>> scriptGenContent, ScriptDefinitionWrapper scriptDefinition);
	
	/**
	 * Check if a list of actions are valid under the passed script definitions.
	 * 
	 * @param scriptGenContent The list of actions to check.
	 * @param scriptDefinition The script definition to check with.
	 * @param globalParams The global parameters to check parameter validity with.
	 * @return True if valid, False if not.
	 */
	boolean areParamsValid(List<Map<String, String>> scriptGenContent, List<String> globalParams, ScriptDefinitionWrapper scriptDefinition);
	
    /**
     * Estimate how long (in seconds) the current actions will take.
     * 
     * @param scriptGenContent The list of actions to estimate
     * @param scriptDefinition The script definition
     * @param globalParams The global parameters to estimate the time with.
     * @return A map where the key is the index of the action in the list and 
     *      the value is the estimated time in seconds
     */
	Map<Integer, Number> estimateTime(List<Map<String, String>> scriptGenContent, ScriptDefinitionWrapper scriptDefinition, List<String> globalParams);
	
	/**
     * A custom Estimate value for the current actions.
     * 
     * @param scriptGenContent The list of actions to estimate
     * @param scriptDefinition The script definition
     * @param globalParams The global parameters to estimate the custom values with
     * @return A map where the key is the index of the action in the list and 
     *      the value is the estimated time in seconds
     */
	Map<Integer, Map<String, String>> estimateCustom(List<Map<String, String>> scriptGenContent, ScriptDefinitionWrapper scriptDefinition, List<String> globalParams);
	
	/**
	 * Check if Python is ready.
	 * 
	 * @return True if python is ready, False if not.
	 */
	boolean isPythonReady();

	/**
	 * @return Returns a list of git errors
	 */
	List<String> getGitErrors();

	/**
	 * @return True if the git remote is available
	 */
	boolean remoteAvailable();

	/**
	 * @return True if the git repository has uncommitted changes
	 */
	boolean isDirty();

	/**
	 * Merges origin/master into script definitions repository.
	 */
	void mergeOrigin();

	/**
	 * @return Returns the path to the script definitions repository
	 */
	String getRepoPath();
	
}
