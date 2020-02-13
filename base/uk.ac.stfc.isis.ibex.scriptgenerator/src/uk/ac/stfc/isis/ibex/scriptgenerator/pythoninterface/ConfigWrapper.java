package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.util.List;
import java.util.Map;

/**
 * A wrapper for the available configs/action definitions in the system and utility
 *  methods to generate scripts and check for validity.
 * 
 * @author James King
 *
 */
public interface ConfigWrapper {
	
	/**
	 * Get all action definitions that have failed to load into the script generator and the reason they have failed to load.
	 * 
	 * @return A mapping of unloaded configs to the reason they could not be loaded.
	 */
	Map<String, String> getConfigLoadErrors();
	
	/**
	 * Get all action definitions available for use in the script generator.
	 * 
	 * @return A list of action definitions for use.
	 */
	List<Config> getActionDefinitions();

	/**
	 * Generate a script from a list of script generator actions for the given action definition.
	 * 
	 * @param scriptGenContent The list of actions to generate a script for.
	 * @param config The action definition to generate the script with.
	 * @return A string containing the generated script.
	 */
	String generate(List<Map<String, String>> scriptGenContent, Config config);
	
	/**
	 * Get a mapping of validity errors of the scriptGenContent against the config.
	 * 
	 * @param scriptGenContent The list of actions to check.
	 * @param config The action definition to check with.
	 * @return A map where the key is the index of the action in the list and 
	 * 		the value is the invalidity reason. Empty if there are no invalidity errors.
	 */
	Map<Integer, String> getValidityErrors(List<Map<String, String>> scriptGenContent, Config config);
	
	/**
	 * Check if a list of actions are valid under the passed action definition.
	 * 
	 * @param scriptGenContent The list of actions to check.
	 * @param config The action definition to check with.
	 * @return True if valid, False if not.
	 */
	boolean areParamsValid(List<Map<String, String>> scriptGenContent, Config config);
	
	/**
	 * Check if Python is ready.
	 */
	boolean isPythonReady();
	
}
