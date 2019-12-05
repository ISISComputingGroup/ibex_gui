package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;

import java.util.HashMap;
import java.util.Objects;

import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;

/**
 * Use to generate a python script from script generator contents.
 * 
 * @author James King
 *
 */
public class GeneratorPython implements GeneratorInterface {

	/**
	 * Generate a script in python.
	 * 
	 * @param actionsTable The script generator content to produce the script from.
	 * @param config The instrument config to generate the script with.
	 * @return The path to the generated script.
	 * @throws InvalidParamsException Thrown if the values for the parameters in the actionTable are invalid.
	 */
	@Override
	public String generate(ActionsTable actionsTable, Config config) throws InvalidParamsException {
		return ScriptGeneratorSingleton.getPythonInterface().generate(actionsTable, config);
	}
	
	/**
	 * Check if the contents of the script generator (actionsTable) are valid against Python.
	 * 
	 * @param actionsTable The contents of the script generator to validate.
	 * @param config The instrument config to validate the script against.
	 * @return true if the contents are valid, false if not.
	 */
	public boolean areParamsValid(ActionsTable actionsTable, Config config) {
		HashMap<Integer, String> errorMessages = ScriptGeneratorSingleton.getPythonInterface().
				areParamsValid(actionsTable, config);
		return errorMessages.isEmpty();
	}

	/**
	 * Get the validity errors returned when checking validity.
	 * 
	 * @param actionsTable The contents of the script generator to check for validity errors with.
	 * @param config The instrument config to validate the script against.
	 * @return a hashmap of validity errors.
	 */
	@Override
	public HashMap<Integer, String> getValidityErrors(ActionsTable actionsTable, Config config) {
		return ScriptGeneratorSingleton.getPythonInterface().areParamsValid(actionsTable, config);
	}
	
}
