package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;

/**
 * The interface for the py4j python generator to implement.
 * 
 * @author James King
 *
 */
public interface PythonGeneratorInterface {
	
	/**
	 * Generate a script in python from the script generator content (actionsTable).
	 * 
	 * @param actionsTable The script generator content to produce the script from.
	 * @param config The instrument config to generate the script with.
	 * @return The path to the generated script.
	 * @throws InvalidParamsException Thrown if the values for the parameters in the actionTable are invalid.
	 */
	public String generate(List<List<String>> actionsTable, Config config) throws InvalidParamsException;
	
	/**
	 * Check if the contents of the script generator (actionsTable) are valid against the config.
	 * 
	 * @param actionsTable The contents of the script generator to validate.
	 * @param config The instrument config to validate the script against.
	 * @return true if the contents are valid, false if not.
	 */
	public HashMap<Integer, String> areParamsValid(List<List<String>> actionsTable, Config config);

}
