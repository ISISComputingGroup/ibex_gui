package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ScriptDefinitionWrapper;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * Part of the strategy pattern to generate a script in a specified language,
 * specifically used by GeneratorFacade to pass a language to and use the correct generator from that language.
 * 
 * @author James King
 *
 */
public class GeneratorContext extends ModelObject {
	
	/**
	 * A mapping from the type of generated language to it's generator.
	 */
	private Map<GeneratedLanguage, AbstractGenerator> generatorStrategies = new HashMap<>();
	
	
	/**
	 * Add a generator to use (will replace the languages generator with a new one if it matches).
	 * Listen to changes in the generators validity checking and script generation.
	 * 
	 * @param generatedLanguage The language the new generator uses.
	 * @param generator The generator to use.
	 */
	public void putGenerator(GeneratedLanguage generatedLanguage, AbstractGenerator generator) {
		generator.addPropertyChangeListener(ScriptGeneratorProperties.VALIDITY_ERROR_MESSAGE_PROPERTY, evt -> {
			firePropertyChange(ScriptGeneratorProperties.VALIDITY_ERROR_MESSAGE_PROPERTY, evt.getOldValue(), evt.getNewValue());
		});
		generator.addPropertyChangeListener(ScriptGeneratorProperties.PARAM_VALIDITY_PROPERTY, evt -> {
			firePropertyChange(ScriptGeneratorProperties.PARAM_VALIDITY_PROPERTY, evt.getOldValue(), evt.getNewValue());
		});
        generator.addPropertyChangeListener(ScriptGeneratorProperties.TIME_ESTIMATE_PROPERTY, evt -> {
            firePropertyChange(ScriptGeneratorProperties.TIME_ESTIMATE_PROPERTY, evt.getOldValue(), evt.getNewValue());
        });
        generator.addPropertyChangeListener(ScriptGeneratorProperties.CUSTOM_ESTIMATE_PROPERTY, evt -> {
            firePropertyChange(ScriptGeneratorProperties.CUSTOM_ESTIMATE_PROPERTY, evt.getOldValue(), evt.getNewValue());
        });
		generator.addPropertyChangeListener(ScriptGeneratorProperties.GENERATED_SCRIPT_PROPERTY, evt -> {
			firePropertyChange(ScriptGeneratorProperties.GENERATED_SCRIPT_PROPERTY, null, evt.getNewValue());
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
	 * @param language The language of that generator
	 * @param generator Generator for generating script
	 */
	public GeneratorContext(GeneratedLanguage language, AbstractGenerator generator) {
		putGenerator(language, generator);
	}
	
	/**
	 * Refresh the generated script in the passed generatedLanguage from the actions passed.
	 * The generated script may not be refreshed if the parameters are invalid. 
	 * The parameter validity property will change and alert listeners at this point.
	 * 
	 * @param actions The script generator contents to generate the script from.
	 * @param scriptDefinition The script definition to generate the script from.
	 * @param generatedLanguage The language to generate the script in.
	 * @param jsonContent Content of the JSON file.
	 * @param globalParams The global parameters to generate the script with.
	 * @return An optional ID of the script that will be generated, the ID can be used to get the generated script once generated.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 * @throws ExecutionException A failure to execute the call to generate.
	 * @throws InterruptedException The call to generate was interrupted.
	 */
	public Optional<Integer> refreshGeneratedScript(List<ScriptGeneratorAction> actions, ScriptDefinitionWrapper scriptDefinition, GeneratedLanguage generatedLanguage, String jsonContent, List<String> globalParams)
			throws UnsupportedLanguageException, InterruptedException, ExecutionException {
		AbstractGenerator generator = getGenerator(generatedLanguage);
		return generator.refreshGeneratedScript(actions, scriptDefinition, jsonContent, globalParams);
	}
	
	/**
	 * Refresh the generated script in the default language (Python).
	 * 
	 * @param actions The script generator contents to generate the script from.
	 * @param scriptDefinition The script definition to generate the script from.
	 * @param jsonContent The JSON content created when generating script.
	 * @param globalParams The global parameters to generate the script with.
	 * @return An optional ID of the script that will be generated, the ID can be used to get the generated script once generated.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 * @throws ExecutionException A failure to execute the call to generate.
	 * @throws InterruptedException The call to generate was interrupted.
	 */
	public Optional<Integer> refreshGeneratedScript(List<ScriptGeneratorAction> actions, ScriptDefinitionWrapper scriptDefinition, String jsonContent, List<String> globalParams) 
			throws UnsupportedLanguageException, InterruptedException, ExecutionException {
		return refreshGeneratedScript(actions, scriptDefinition, GeneratedLanguage.PYTHON, jsonContent, globalParams);
	}
	
	/**
	 * Check if the actions are valid against a generator (generatedLanguage) and refresh the property.
	 * 
	 * @param actions The contents of the script generator to validate.
	 * @param scriptDefinition The script definition to validate the script against.
	 * @param generatedLanguage The language that the script will be generated in.
	 * @param globalParams The global parameters to check parameter validity with.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 * @throws ExecutionException A failure to execute the call to generate.
	 * @throws InterruptedException The call to generate was interrupted.
	 */
	public void refreshAreParamsValid(List<ScriptGeneratorAction> actions, ScriptDefinitionWrapper scriptDefinition, GeneratedLanguage generatedLanguage, List<String> globalParams) 
			throws UnsupportedLanguageException, InterruptedException, ExecutionException {
		AbstractGenerator generator = getGenerator(generatedLanguage);
		generator.refreshAreParamsValid(actions, scriptDefinition, globalParams);
	}
	
	/**
	 * Check if the actions
	 *  are valid against the default language generator (Python) and refresh the property.
	 * 
	 * @param actions The contents of the script generator to validate.
	 * @param scriptDefinition The script definition to validate the script against.
	 * @param globalParams The global parameters to check parameter validity with.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 * @throws ExecutionException A failure to execute the call to generate.
	 * @throws InterruptedException The call to generate was interrupted.
	 */
	public void refreshAreParamsValid(List<ScriptGeneratorAction> actions, ScriptDefinitionWrapper scriptDefinition, List<String> globalParams) 
			throws UnsupportedLanguageException, InterruptedException, ExecutionException {
		refreshAreParamsValid(actions, scriptDefinition, GeneratedLanguage.PYTHON, globalParams);
	}
	
    /**
     * Estimate the time necessary to complete the actions.
     * 
     * @param actions The contents of the script generator
     * @param scriptDefinition The script definition
     * @param generatedLanguage The language that the script will be generated in.
	 * @param globalParams The global parameters to refresh time estimation with.
     * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
     * @throws ExecutionException A failure to execute the call to generate.
     * @throws InterruptedException The call to generate was interrupted.
     */
    public void refreshTimeEstimation(List<ScriptGeneratorAction> actions, ScriptDefinitionWrapper scriptDefinition, GeneratedLanguage generatedLanguage, List<String> globalParams)
            throws UnsupportedLanguageException, InterruptedException, ExecutionException {
        AbstractGenerator generator = getGenerator(generatedLanguage);
        generator.refreshTimeEstimation(actions, scriptDefinition, globalParams);
    }
    
    /**
     * Estimate the time necessary to complete the actions using the default language generator (Python).
     * 
     * @param actions The contents of the script generator
     * @param scriptDefinition The script definition
	 * @param globalParams The global parameters to refresh time estimation with.
     * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
     * @throws ExecutionException A failure to execute the call to generate.
     * @throws InterruptedException The call to generate was interrupted.
     */
    public void refreshTimeEstimation(List<ScriptGeneratorAction> actions, ScriptDefinitionWrapper scriptDefinition, List<String> globalParams)
            throws UnsupportedLanguageException, InterruptedException, ExecutionException {
        refreshTimeEstimation(actions, scriptDefinition, GeneratedLanguage.PYTHON, globalParams);
    }
    
    /**
     * Estimate the custom defined values related to the actions.
     * 
     * @param actions The contents of the script generator
     * @param scriptDefinition The script definition
     * @param generatedLanguage The language that the script will be generated in.
     * @param globalParams The global parameters to refresh the custom estimation with.
     * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
     * @throws ExecutionException A failure to execute the call to generate.
     * @throws InterruptedException The call to generate was interrupted.
     */
    public void refreshCustomEstimation(List<ScriptGeneratorAction> actions, ScriptDefinitionWrapper scriptDefinition, GeneratedLanguage generatedLanguage, List<String> globalParams)
            throws UnsupportedLanguageException, InterruptedException, ExecutionException {
        AbstractGenerator generator = getGenerator(generatedLanguage);
        generator.refreshCustomEstimation(actions, scriptDefinition, globalParams);
    }

    /**
     * Estimate the custom defined values related to the actions using the default language generator (Python).
     * 
     * @param actions The contents of the script generator
     * @param scriptDefinition The script definition
     * @param globalParams The global parameters to refresh the custom estimation with.
     * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
     * @throws ExecutionException A failure to execute the call to generate.
     * @throws InterruptedException The call to generate was interrupted.
     */
    public void refreshCustomEstimation(List<ScriptGeneratorAction> actions, ScriptDefinitionWrapper scriptDefinition, List<String> globalParams)
            throws UnsupportedLanguageException, InterruptedException, ExecutionException {
        refreshCustomEstimation(actions, scriptDefinition, GeneratedLanguage.PYTHON, globalParams);
    }
	
	/**
	 * Refresh the validity errors returned when checking validity.
	 * 
	 * @param actions The contents of the script generator to check for validity errors with.
	 * @param scriptDefinition The script definition to validate the script against.
	 * @param generatedLanguage The language that the script will be generated in.
	 * @param globalParams The global parameters to check validity with.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 * @throws ExecutionException A failure to execute the call to generate.
	 * @throws InterruptedException The call to generate was interrupted.
	 */
	public void refreshValidityErrors(List<String> globalParams, List<ScriptGeneratorAction> actions, ScriptDefinitionWrapper scriptDefinition, GeneratedLanguage generatedLanguage) 
			throws UnsupportedLanguageException, InterruptedException, ExecutionException {
		AbstractGenerator generator = getGenerator(generatedLanguage);
		generator.refreshValidityErrors(globalParams, actions, scriptDefinition);
	}

	/**
	 * Refresh the validity errors returned when checking the validity in the default language (Python).
	 * 
	 * @param actionsTable The contents of the script generator to check for validity errors with.
	 * @param scriptDefinition The script definition to validate the script against.
	 * @param globalParams The global parameters to check validity with.
	 * @throws UnsupportedLanguageException Thrown if the default language (python) to generate the script in is not supported.
	 * @throws ExecutionException A failure to execute the call to generate.
	 * @throws InterruptedException The call to generate was interrupted.
	 */
	public void refreshValidityErrors(List<String> globalParams, List<ScriptGeneratorAction> actionsTable, ScriptDefinitionWrapper scriptDefinition) 
			throws UnsupportedLanguageException, InterruptedException, ExecutionException {
		refreshValidityErrors(globalParams, actionsTable, scriptDefinition, GeneratedLanguage.PYTHON);
	}
	
	/**
     * Get the generated script from the given ID.
     * 
     * @param scriptId The ID of the script to get.
     * @param language The language used to generate the script.
     * @return The script or an optional empty if it doesn't exist.
     */
	public Optional<String> getScriptFromId(Integer scriptId, GeneratedLanguage language) {
		var generator = generatorStrategies.get(language);
		return generator.getScriptFromId(scriptId);
	}
	
	/**
     * Get the generated script from the given ID.
     * 
     * @param scriptId The ID of the script to get.
     * @return The script or an optional empty if it doesn't exist.
     */
	public Optional<String> getScriptFromId(Integer scriptId) {
		return getScriptFromId(scriptId, GeneratedLanguage.PYTHON);
	}

}
