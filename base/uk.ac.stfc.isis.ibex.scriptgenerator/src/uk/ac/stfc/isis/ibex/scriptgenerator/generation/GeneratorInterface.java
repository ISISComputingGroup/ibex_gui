package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.util.Map;

import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;

/**
 * An interface to be implemented by each different language for the script generator.
 * 
 * @author James King
 *
 */
public interface GeneratorInterface {
	
	/**
	 * Generate a script in the language specified by what class implements 
	 * this interface from the passed script generator content.
	 * 
	 * @param actionsTable The script generator content to produce the script from.
	 * @param config The instrument config to generate the script with.
	 * @return The path to the generated script.
	 * @throws InvalidParamsException Thrown if the values for the parameters in the actionTable are invalid.
	 */
	public String generate(ActionsTable actionsTable, Config config) throws InvalidParamsException;
	
	/**
	 * Check if the contents of the script generator (actionsTable) are valid.
	 * 
	 * @param actionsTable The contents of the script generator to validate.
	 * @param config The instrument config to validate the script against.
	 * @return true if the contents are valid, false if not.
	 */
	public boolean areParamsValid(ActionsTable actionsTable, Config config);
	
	/**
	 * Get the validity errors returned when checking validity.
	 * 
	 * @param actionsTable The contents of the script generator to check for validity errors with.
	 * @param config The instrument config to validate the script against.
	 * @return a string of validity errors or null if no errors.
	 */
	public Map<Integer, String> getValidityErrors(ActionsTable actionsTable, Config config);
	
}
