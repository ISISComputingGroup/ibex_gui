package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import uk.ac.stfc.isis.ibex.scriptgenerator.generation.GeneratedLanguage;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.AbstractGenerator;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.GeneratorPython;
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
public class GeneratorContext extends ModelObject {
	
	
	private static final String VALIDITY_ERROR_MESSAGE_PROPERTY = "validity error messages";
	private static final String PARAM_VALIDITY_PROPERTY = "parameter validity";
	private static final String GENERATED_SCRIPT_PROPERTY = "generated script";
	
	/**
	 * A mapping from the type of generated language to it's generator.
	 */
	private Map<GeneratedLanguage, AbstractGenerator> generatorStrategies = new HashMap<>();
	
	
	/**
	 * Add a generator to use (will replace the languages generator with a new one if it matches).
	 * 
	 * @param generatedLanguage The language the new generator uses.
	 * @param generator The generator to use.
	 */
	public void putGenerator(GeneratedLanguage generatedLanguage, AbstractGenerator generator) {
		generator.addPropertyChangeListener(VALIDITY_ERROR_MESSAGE_PROPERTY, evt -> {
			firePropertyChange(VALIDITY_ERROR_MESSAGE_PROPERTY, evt.getOldValue(), evt.getNewValue());
		});
		generator.addPropertyChangeListener(PARAM_VALIDITY_PROPERTY, evt -> {
			firePropertyChange(PARAM_VALIDITY_PROPERTY, evt.getOldValue(), evt.getNewValue());
		});
		// If the returned string is not null update the generated script,
		// or else fire a property change notifying that the parameters are invalid
		generator.addPropertyChangeListener(GENERATED_SCRIPT_PROPERTY, evt -> {
			Optional.ofNullable(evt.getNewValue())
			.ifPresentOrElse(
				generatedScript -> firePropertyChange(GENERATED_SCRIPT_PROPERTY, null, String.class.cast(generatedScript)),
				() -> firePropertyChange(PARAM_VALIDITY_PROPERTY, null, false)
			);
		});
		generatorStrategies.put(generatedLanguage, generator);
	}
	
	
	/**
	 * Get a generator for the specified generator language. Throws exception if not supported
	 * 
	 * @param generatedLanguage The language to generate or check validity with.
	 * @return The generator to carry out the generation/check validity with.
	 * @throws UnsupportedLanguageException If the language has no supported generator throw this.
	 */
	private AbstractGenerator getGenerator(GeneratedLanguage generatedLanguage) throws UnsupportedLanguageException {
		return Optional.ofNullable(generatorStrategies.get(generatedLanguage))
				.orElseThrow(() -> new UnsupportedLanguageException("Language " + generatedLanguage + " not supported"));
	}
	
	/**
	 * Add each language's generator to the strategies.
	 * @param pythonInterface 
	 */
	public GeneratorContext(PythonInterface pythonInterface) {
		putGenerator(GeneratedLanguage.PYTHON, new GeneratorPython(pythonInterface));
	}
	
	/**
	 * Refresh the generated script in the passed generatedLanguage from the actionsTable passed.
	 * The generated script may not be refreshed if the parameters are invalid. 
	 * The parameter validity property will change and alert listeners at this point.
	 * 
	 * @param actionsTable The script generator contents to generate the script from.
	 * @param config The instrument config to generate to script from.
	 * @param generatedLanguage The language to generate the script in.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 * @throws ExecutionException A failure to execute the call to generate
	 * @throws InterruptedException The call to generate was interrupted
	 */
	public void refreshGeneratedScript(ActionsTable actionsTable, Config config, GeneratedLanguage generatedLanguage) 
			throws UnsupportedLanguageException, InterruptedException, ExecutionException {
		AbstractGenerator generator = getGenerator(generatedLanguage);
		generator.refreshGeneratedScript(actionsTable.getActions(), config);
	}
	
	/**
	 * Refresh the generated script in the default language (Python).
	 * 
	 * @param actionsTable The script generator contents to generate the script from.
	 * @param config The instrument config to generate to script from.
	 * @param generatedLanguage The language to generate the script in.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 * @throws ExecutionException A failure to execute the call to generate
	 * @throws InterruptedException The call to generate was interrupted
	 */
	public void refreshGeneratedScript(ActionsTable actionsTable, Config config) 
			throws UnsupportedLanguageException, InterruptedException, ExecutionException {
		refreshGeneratedScript(actionsTable, config, GeneratedLanguage.PYTHON);
	}
	
	/**
	 * Check if the contents of the script generator (actionsTable) 
	 *  are valid against a generator (generatedLanguage) and refresh the property.
	 * 
	 * @param actionsTable The contents of the script generator to validate.
	 * @param config The instrument config to validate to script against.
	 * @param generatedLanguage The language that the script will be generated in.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 * @throws ExecutionException A failure to execute the call to generate
	 * @throws InterruptedException The call to generate was interrupted
	 */
	public void refreshAreParamsValid(ActionsTable actionsTable, Config config, GeneratedLanguage generatedLanguage) 
			throws UnsupportedLanguageException, InterruptedException, ExecutionException {
		AbstractGenerator generator = getGenerator(generatedLanguage);
		generator.refreshAreParamsValid(actionsTable.getActions(), config);
	}
	
	/**
	 * Check if the contents of the script generator (actionsTable) 
	 *  are valid against the default language generator (Python) and refresh the property.
	 * 
	 * @param actionsTable The contents of the script generator to validate.
	 * @param config The instrument config to validate to script against.
	 * @param generatedLanguage The language that the script will be generated in.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 * @throws ExecutionException A failure to execute the call to generate
	 * @throws InterruptedException The call to generate was interrupted
	 */
	public void refreshAreParamsValid(ActionsTable actionsTable, Config config) 
			throws UnsupportedLanguageException, InterruptedException, ExecutionException {
		refreshAreParamsValid(actionsTable, config, GeneratedLanguage.PYTHON);
	}
	
	/**
	 * Refresh the validity errors returned when checking validity.
	 * 
	 * @param actionsTable The contents of the script generator to check for validity errors with.
	 * @param config The instrument config to validate the script against.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 * @throws ExecutionException A failure to execute the call to generate
	 * @throws InterruptedException The call to generate was interrupted
	 */
	public void refreshValidityErrors(ActionsTable actionsTable, Config config, GeneratedLanguage generatedLanguage) 
			throws UnsupportedLanguageException, InterruptedException, ExecutionException {
		AbstractGenerator generator = getGenerator(generatedLanguage);
		generator.refreshValidityErrors(actionsTable.getActions(), config);
	}

	/**
	 * Refresh the validity errors returned when checking the validity in the default language (Python).
	 * 
	 * @param actionsTable The contents of the script generator to check for validity errors with.
	 * @param config The instrument config to validate the script against.
	 * @throws UnsupportedLanguageException Thrown if the default language (python) to generate the script in is not supported.
	 * @throws ExecutionException A failure to execute the call to generate
	 * @throws InterruptedException The call to generate was interrupted
	 */
	public void refreshValidityErrors(ActionsTable actionsTable, Config config) 
			throws UnsupportedLanguageException, InterruptedException, ExecutionException {
		refreshValidityErrors(actionsTable, config, GeneratedLanguage.PYTHON);
	}

}
