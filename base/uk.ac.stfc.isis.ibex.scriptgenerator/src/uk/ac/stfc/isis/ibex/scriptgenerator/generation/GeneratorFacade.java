package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import uk.ac.stfc.isis.ibex.scriptgenerator.Activator;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.GeneratedLanguage;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.GeneratorContext;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.UnsupportedLanguageException;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

/**
 * Class to use to generate a script or check parameters are valid in a specified language.
 * 
 * @author James King
 *
 */
public class GeneratorFacade {
		
	/**
	 * Part of the strategy design pattern to generate based on which language to use.
	 */
	private static final GeneratorContext generatorContext = new GeneratorContext(Activator.getModel().getPythonInterface());	
	
	/**
	 * Generate a python script from the actionsTable passed.
	 * 
	 * @param actionsTable The script generator contents to generate the script from.
	 * @param config The instrument config to generate the script with.
	 * @return The path to the generated script.
	 * @throws InvalidParamsException Thrown if the values for the parameters in the actionTable are invalid.
	 * @throws UnsupportedLanguageException Thrown if the generator language (python) is not supported.
	 */
	public static String generate(ActionsTable actionsTable, Config config) throws InvalidParamsException, UnsupportedLanguageException {
		return generatorContext.generate(actionsTable, config);
	}
	
	/**
	 * Generate a script in the passed generatedLanguage from the actionsTable passed.
	 * 
	 * @param actionsTable The script generator contents to generate the script from.
	 * @param config The instrument config to generate the script with.
	 * @param generatedLanguage The language to generate the script in.
	 * @return The path to the generated script.
	 * @throws InvalidParamsException Thrown if the values for the parameters in the actionTable are invalid.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 */
	public static String generate(ActionsTable actionsTable, Config config, GeneratedLanguage generatedLanguage) throws InvalidParamsException, UnsupportedLanguageException {
		return generatorContext.generate(actionsTable, config, generatedLanguage);
	}
	
	/**
	 * Check if the contents of the script generator (actionsTable) are valid against a config and python generator.
	 * 
	 * @param actionsTable The contents of the script generator to validate.
	 * @param config The instrument config to check the parameters are valid against.
	 * @return true if the contents are valid, false if not.
	 * @throws UnsupportedLanguageException Thrown if the generator language (python) is not supported.
	 */
	public static boolean areParamsValid(ActionsTable actionsTable, Config config) throws UnsupportedLanguageException {
		return generatorContext.areParamsValid(actionsTable, config);
	}
	
	/**
	 * Check if the contents of the script generator (actionsTable) are valid against a generator (generatedLanguage).
	 * 
	 * @param actionsTable The contents of the script generator to validate.
	 * @param config The instrument config to check the parameters are valid against.
	 * @param generatedLanguage The language that the script will be generated in.
	 * @return true if the contents are valid, false if not.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 */
	public static boolean areParamsValid(ActionsTable actionsTable, Config config,  GeneratedLanguage generatedLanguage) throws UnsupportedLanguageException {
		return generatorContext.areParamsValid(actionsTable, config, generatedLanguage);
	}
	
	/**
	 * Get the validity errors returned when checking validity against python.
	 * 
	 * @param actionsTable The contents of the script generator to check for validity errors with.
	 * @param config The instrument config to validate the script against.
	 * @return a hashmap of validity errors.
	 * @throws UnsupportedLanguageException Thrown if the generator language (python) is not supported.
	 */
	public static Map<Integer, String> getValidityErrors(ActionsTable actionsTable, Config config) throws UnsupportedLanguageException {
		return generatorContext.getValidityErrors(actionsTable, config);
	}
	
	/**
	 * Get the validity errors returned when checking validity.
	 * 
	 * @param actionsTable The contents of the script generator to check for validity errors with.
	 * @param config The instrument config to validate the script against.
	 * @return a string of validity errors or null if no errors.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 */
	public static Map<Integer, String> getValidityErrors(ActionsTable actionsTable, Config config,  GeneratedLanguage generatedLanguage) throws UnsupportedLanguageException {
		return generatorContext.getValidityErrors(actionsTable, config, generatedLanguage);
	}
	
}
