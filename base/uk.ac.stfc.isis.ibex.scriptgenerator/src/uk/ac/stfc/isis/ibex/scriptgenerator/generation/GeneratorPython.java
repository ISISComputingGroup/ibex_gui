package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.util.Map;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonInterface;

/**
 * Use to generate a python script or check parameter validity from script generator contents.
 * 
 * @author James King
 *
 */
public class GeneratorPython implements GeneratorInterface {
	
	private final PythonInterface pythonInterface;
	
	/**
	 * Create a python generator with the specified python interface.
	 * 
	 * @param pythonInterface The py4j python interface to use to communicate with python.
	 */
	public GeneratorPython(PythonInterface pythonInterface) {
		this.pythonInterface = pythonInterface;
	}

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
		return pythonInterface.generate(actionsTable.getActions(), config);
	}
	
	/**
	 * Check if the contents of the script generator (actionsTable) are valid against Python.
	 * 
	 * @param actionsTable The contents of the script generator to validate.
	 * @param config The instrument config to validate the script against.
	 * @return true if the contents are valid, false if not.
	 */
	public boolean areParamsValid(ActionsTable actionsTable, Config config) {
		return pythonInterface.areParamsValid(actionsTable.getActions(), config);
	}

	/**
	 * Get the validity errors returned when checking validity.
	 * 
	 * @param actionsTable The contents of the script generator to check for validity errors with.
	 * @param config The instrument config to validate the script against.
	 * @return a hashmap of validity errors.
	 */
	@Override
	public Map<Integer, String> getValidityErrors(ActionsTable actionsTable, Config config) {
		return pythonInterface.getValidityErrors(actionsTable.getActions(), config);
	}
	
}
