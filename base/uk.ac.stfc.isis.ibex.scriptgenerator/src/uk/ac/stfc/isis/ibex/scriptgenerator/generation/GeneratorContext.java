package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.generation.GeneratedLanguage;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.GeneratorInterface;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.GeneratorPython;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.UnsupportedLanguageException;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonInterface;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;

/**
 * Part of the strategy pattern to generate a script in a specified language,
 * specifically used by GeneratorFacade to pass a language to and use the correct generator from that language.
 * 
 * @author James King
 *
 */
public class GeneratorContext {
	
	/**
	 * A mapping from the type of generated language to it's generator.
	 */
	private Map<GeneratedLanguage, GeneratorInterface> generatorStrategies = new HashMap<>();
	
	/**
	 * Add a generator to use (will replace the languages generator with a new one if it matches).
	 * 
	 * @param generatedLanguage The language the new generator uses.
	 * @param generator The generator to use.
	 */
	public void putGenerator(GeneratedLanguage generatedLanguage, GeneratorInterface generator) {
		generatorStrategies.put(generatedLanguage, generator);
	}
	
	/**
	 * Add each language's generator to the strategies.
	 * @param pythonInterface 
	 */
	public GeneratorContext(PythonInterface pythonInterface) {
		generatorStrategies.put(GeneratedLanguage.PYTHON, new GeneratorPython(pythonInterface));
	}
	
	/**
	 * Generate a script in the passed generatedLanguage from the actionsTable passed.
	 * 
	 * @param actionsTable The script generator contents to generate the script from.
	 * @param config The instrument config to generate to script from.
	 * @param generatedLanguage The language to generate the script in.
	 * @return The path to the generated script.
	 * @throws InvalidParamsException Thrown if the values for the parameters in the actionTable are invalid.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 */
	public String generate(ActionsTable actionsTable, Config config, GeneratedLanguage generatedLanguage) throws InvalidParamsException, UnsupportedLanguageException {
		return Optional.ofNullable(generatorStrategies.get(generatedLanguage).generate(actionsTable, config))
			    .orElseThrow(() -> new UnsupportedLanguageException("Language " + generatedLanguage + " not supported"));
	}
	
	/**
	 * Generate a script in the default language (Python) from the actionsTable passed.
	 * 
	 * @param actionsTable The script generator contents to generate the script from.
	 * @param config The instrument config to generate to script from.
	 * @param generatedLanguage The language to generate the script in.
	 * @return The path to the generated script.
	 * @throws InvalidParamsException Thrown if the values for the parameters in the actionTable are invalid.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 */
	public String generate(ActionsTable actionsTable, Config config) throws InvalidParamsException, UnsupportedLanguageException {
		return generate(actionsTable, config, GeneratedLanguage.PYTHON);
	}
	
	/**
	 * Check if the contents of the script generator (actionsTable) are valid against a generator (generatedLanguage).
	 * 
	 * @param actionsTable The contents of the script generator to validate.
	 * @param config The instrument config to validate to script against.
	 * @param generatedLanguage The language that the script will be generated in.
	 * @return true if the contents are valid, false if not.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 */
	public boolean areParamsValid(ActionsTable actionsTable, Config config, GeneratedLanguage generatedLanguage) throws UnsupportedLanguageException {
		return Optional.ofNullable(generatorStrategies.get(generatedLanguage).areParamsValid(actionsTable, config))
			    .orElseThrow(() -> new UnsupportedLanguageException("Language " + generatedLanguage + " not supported"));
	}
	
	/**
	 * Check if the contents of the script generator (actionsTable) are valid against the default language generator (Python).
	 * 
	 * @param actionsTable The contents of the script generator to validate.
	 * @param config The instrument config to validate to script against.
	 * @param generatedLanguage The language that the script will be generated in.
	 * @return true if the contents are valid, false if not.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 */
	public boolean areParamsValid(ActionsTable actionsTable, Config config) throws UnsupportedLanguageException {
		return areParamsValid(actionsTable, config, GeneratedLanguage.PYTHON);
	}
	
	/**
	 * Get the validity errors returned when checking validity.
	 * 
	 * @param actionsTable The contents of the script generator to check for validity errors with.
	 * @param config The instrument config to validate the script against.
	 * @return a hashmap of validity errors.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 */
	public Map<Integer, String> getValidityErrors(ActionsTable actionsTable, Config config, GeneratedLanguage generatedLanguage) throws UnsupportedLanguageException {
		return Optional.ofNullable(generatorStrategies.get(generatedLanguage).getValidityErrors(actionsTable, config))
			    .orElseThrow(() -> new UnsupportedLanguageException("Language " + generatedLanguage + " not supported"));
	}

	/**
	 * Get the validity errors returned when checking the validity in the default language (Python).
	 * 
	 * @param actionsTable The contents of the script generator to check for validity errors with.
	 * @param config The instrument config to validate the script against.
	 * @return a hashmap of validity errors.
	 * @throws UnsupportedLanguageException Thrown if the default language (python) to generate the script in is not supported.
	 */
	public Map<Integer, String> getValidityErrors(ActionsTable actionsTable, Config config) throws UnsupportedLanguageException {
		return getValidityErrors(actionsTable, config, GeneratedLanguage.PYTHON);
	}

}
