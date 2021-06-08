/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2019 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

package uk.ac.stfc.isis.ibex.scriptgenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.GeneratorContext;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.GeneratorPython;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.GeneratedLanguage;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;

import uk.ac.stfc.isis.ibex.scriptgenerator.generation.UnsupportedLanguageException;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ScriptDefinitionWrapper;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ScriptDefinitionLoader;

import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonInterface;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * The Model of the script generator responsible for generating scripts,
 * checking parameter validity, loading script definitions and containing the
 * actions table for the script generator.
 */
public class ScriptGeneratorSingleton extends ModelObject {
	
	/**
	 * JSON file extension.
	 */
	@SuppressWarnings("unused")
	private static final String JSON_EXT = ".json";
	/**
	 * Python file extension.
	 */
	public static final String PYTHON_EXT = ".py";
	/**
	 * Script generator parameters file extension.
	 */
	public static final String SCRIPT_GEN_PARAMS_EXT = ".sgp";
	
	/**
	 * The preferences supplier to get the area to generate scripts from.
	 */
	private final PreferenceSupplier preferenceSupplier = new PreferenceSupplier();

	/**
	 * The table containing the script generator contents (actions).
	 */
	private ActionsTable scriptGeneratorTable = new ActionsTable(new ArrayList<JavaActionParameter>());

	/**
	 * The loader to select and update the script definition being used.
	 */
	private ScriptDefinitionLoader scriptDefinitionLoader;

	/**
	 * The python interface to check parameter validity with, generate scripts with,
	 * load script definitions with.
	 */
	private PythonInterface pythonInterface;

	/**
	 * The property to listen for changes to actions in the script generator table
	 * on.
	 */
	private static final String ACTIONS_PROPERTY = "actions";

	/**
	 * The property to listen for changes in the generator on, as to whether the
	 * current selected language is supported.
	 */
	private static final String LANGUAGE_SUPPORT_PROPERTY = "language_supported";

	/**
	 * Whether the current used language is supported or not.
	 */
	public boolean languageSupported = true;

	/**
	 * The property to listen for changes in the generator on, as to whether there
	 * has been a thread error when checking parameter validity or generating a
	 * script.
	 */
	private static final String THREAD_ERROR_PROPERTY = "thread error";

	/**
	 * Whether there has been a thread error or not that affects the current state
	 * of the script generator.
	 */
	private boolean threadError = false;

	/**
	 * The property to listen for changes in the generator containing the mapping
	 * Map<Integer, String> of validity checks for each row of the script generator.
	 */
	private static final String VALIDITY_ERROR_MESSAGE_PROPERTY = "validity error messages";

	/**
	 * The property to listen for changes in a Generator containing whether or not
	 * all script generator contents are valid or not (bool).
	 */
	private static final String PARAM_VALIDITY_PROPERTY = "parameter validity";
	
	private static final String TIME_ESTIMATE_PROPERTY = "time estimate";

	/**
	 * The property to listen for changes in a Generator containing the generated
	 * script (String).
	 */
	private static final String GENERATED_SCRIPT_PROPERTY = "generated script";

	/**
	 * The property to listen for changes in a Generator containing the generated
	 * script (String).
	 */
	private static final String GENERATED_SCRIPT_FILENAME_PROPERTY = "generated script filename";
	
	/**
	 * The current state of parameter validity.
	 */
	private boolean paramValidity = false;

	/**
	 * The generator to generate scripts with and check parameter validity with.
	 */
	private GeneratorContext generator;

	/**
	 * A property to fire a change of when there is an error generating a script.
	 */
	private static final String SCRIPT_GENERATION_ERROR_PROPERTY = "script generation error";

	/**
	 * A property to notify listeners when python becomes ready or not ready.
	 */
	private static final String PYTHON_READINESS_PROPERTY = "python ready";

	/**
	 * The date format to use when generating a script name.
	 */
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

	private static final Logger LOG = IsisLog.getLogger(ScriptGeneratorSingleton.class);

	/**
	 * The Integer containing the last generated script.
	 */
	private Optional<Integer> lastGeneratedScriptId;
	
	/**
	 * JSON file handler for saving parameters
	 */
	private ScriptGeneratorJsonFileHandler scriptGenFileHandler = new ScriptGeneratorJsonFileHandler();

	/**
	 * The file handler to write and open scripts with.
	 */
	private ScriptGeneratorFileHandler fileHandler = new ScriptGeneratorFileHandler();
	

	/**
	 * The constructor, will create without a script definition loader and without loading
	 * an initial script definition.
	 */
	public ScriptGeneratorSingleton() {   
	}

	/**
	 * Pass a python interface to run with, then create a script definition loader
	 * and load an initial script definition.
	 * 
	 * @param pythonInterface        The python interface to run with.
	 * @param scriptDefinitionLoader The object to load script definitions with.
	 * @param scriptGeneratorTable   The table containing actions for the model to
	 *                               use.
	 */
	public ScriptGeneratorSingleton(PythonInterface pythonInterface, ScriptDefinitionLoader scriptDefinitionLoader,
			ActionsTable scriptGeneratorTable) {
		this.pythonInterface = pythonInterface;
		this.scriptDefinitionLoader = scriptDefinitionLoader;
		this.scriptGeneratorTable = scriptGeneratorTable;
		setUp();
	}

	private Path scriptDefinitionsRepoPath;
	
	private Path getScriptDefinitionPath(ScriptDefinitionWrapper scriptDefinition) {
		return Paths.get(scriptDefinitionsRepoPath.toString(), scriptDefinition.getName() + PYTHON_EXT);
	}
	
	/**
	 * Called by the constructor with three arguments during tests or in the View
	 * Model constructor to set up the class. Set up listeners for the generator.
	 */
	@SuppressWarnings("unchecked")
	public void setUp() {
		generator = new GeneratorContext(GeneratedLanguage.PYTHON, new GeneratorPython(pythonInterface));

		// If the validity error message property of the generator is changed update the
		// validity errors in the scriptGeneratorTable
		generator.addPropertyChangeListener(VALIDITY_ERROR_MESSAGE_PROPERTY, evt -> {
			scriptGeneratorTable.setValidityErrors(convertToMap(evt.getNewValue(), Integer.class, String.class));
			firePropertyChange(VALIDITY_ERROR_MESSAGE_PROPERTY, evt.getOldValue(), evt.getNewValue());
		});
        // If the time estimation message property of the generator is changed update the
        // time estimation in the scriptGeneratorTable
        generator.addPropertyChangeListener(TIME_ESTIMATE_PROPERTY, evt -> {
            scriptGeneratorTable.setEstimatedTimes(convertToMap(evt.getNewValue(), Integer.class, Number.class));
            firePropertyChange(TIME_ESTIMATE_PROPERTY, evt.getOldValue(), evt.getNewValue());
        });
		// If the parameter validity property is changed update the models field that
		// denotes
		// whether the parameters are valid and notify any listeners
		generator.addPropertyChangeListener(PARAM_VALIDITY_PROPERTY, evt -> {
			Optional.ofNullable(evt.getNewValue()).ifPresentOrElse(
					paramValidity -> this.paramValidity = Boolean.class.cast(paramValidity),
					() -> this.paramValidity = false);
			firePropertyChange(PARAM_VALIDITY_PROPERTY, evt.getOldValue(), evt.getNewValue());
		});
		// Detect when the generated script is refreshed
		// Write the script to file, send up generated script filepath
		generator.addPropertyChangeListener(GENERATED_SCRIPT_PROPERTY, evt -> {

			lastGeneratedScriptId = (Optional<Integer>) evt.getNewValue();
			lastGeneratedScriptId.ifPresentOrElse(scriptId -> {
				try {
					String generatedScriptFilename = generateScriptFileName();
					firePropertyChange(GENERATED_SCRIPT_FILENAME_PROPERTY, null, generatedScriptFilename);
					firePropertyChange(GENERATED_SCRIPT_PROPERTY, null, scriptId);
				} catch (NoScriptDefinitionSelectedException e) {
					LOG.error(e);
				}
			}, () -> {
				firePropertyChange(SCRIPT_GENERATION_ERROR_PROPERTY, null, true);
			});
			
		});

		scriptDefinitionLoader.addPropertyChangeListener("parameters", evt -> {
			setActionParameters(scriptDefinitionLoader.getParameters());
		});
		this.scriptGeneratorTable.addPropertyChangeListener(ACTIONS_PROPERTY, evt -> {
			// The table has changed so update the validity checks and time estimate
			try {
				refreshParameterValidityChecking();
				refreshTimeEstimation();
			} catch (NoScriptDefinitionSelectedException e) {
				LOG.error(e);
			}
		});

		setActionParameters(scriptDefinitionLoader.getParameters());
	}

	/**
	 * Reload the table actions by firing a property change.
	 */
	public void reloadActions() {
		this.scriptGeneratorTable.reloadActions();
	}

	/**
	 * Creates the script definition loader.
	 */
	public void createScriptDefinitionLoader() {
		pythonInterface = new PythonInterface();
		scriptDefinitionLoader = new ScriptDefinitionLoader(pythonInterface);
		pythonInterface.addPropertyChangeListener(PYTHON_READINESS_PROPERTY, evt -> {
			firePropertyChange(PYTHON_READINESS_PROPERTY, evt.getOldValue(), evt.getNewValue());
		});
		pythonInterface.workerSetUpPythonThread();
    }
    
    /**
     * Get the script definition loader.
     * 
     * @return The script definition loader
     */
    public ScriptDefinitionLoader getScriptDefinitionLoader() {
        return scriptDefinitionLoader;
    }
    
    /**
     * Get the default area to generate scripts to.
     * 
     * @return The default directory path to generate scripts to.
     */
    public String getDefaultScriptDirectory() {
    	return preferenceSupplier.scriptGenerationFolder();
    }
    
    /**
     * Get the last generated script.
     * 
     * @return An optional that is empty if no script has been generated successfully,
     *   or the id of the generated script.
     */
    public Optional<Integer> getLastGeneratedScriptId() {
    	return lastGeneratedScriptId;
    }

	/**
	 * Convert the VALIDITY_ERROR_MESSAGE_PROPERTY return to the Map<Integer,
	 * String> representation. Required because of casting generics in Java.
	 * 
	 * @param validityMessages The validity messages to convert.
	 * @param <T> class for keys in map.
	 * @param <S> class for values in map.
	 * @param keyClass The class the key entry should be cast to before placing in the generated map
	 * @param valueClass The class the value entry should be cast to before placing in the generated map
	 * @return The converted messages property.
	 */
	@SuppressWarnings("rawtypes")
	private static <T, S> Map<T, S> convertToMap(Object validityMessages, Class<T> keyClass, Class<S> valueClass) {
		try {
			Map mapCastValidityMessages = Map.class.cast(validityMessages);
			Map<T, S> castValidityMessages = new HashMap<>();
			for (Object nonCastEntry : mapCastValidityMessages.entrySet()) {
				Map.Entry castEntry = Map.Entry.class.cast(nonCastEntry);
				castValidityMessages.put(keyClass.cast(castEntry.getKey()),
						valueClass.cast(castEntry.getValue()));
			}
			return castValidityMessages;
		} catch (ClassCastException e) {
			LOG.error(e);
			return new HashMap<T, S>();
		}
	}

	/**
	 * Get the python interface.
	 * 
	 * @return The class responsible for interfacing with python.
	 */
	public PythonInterface getPythonInterface() {
		return pythonInterface;
	}

	/**
	 * Adds the new action parameters to the empty ActionsTable.
	 * 
	 * @param actionParameters the action parameters to use in the script generator
	 *                         table
	 */
	private void setActionParameters(ArrayList<JavaActionParameter> actionParameters) {
		// Setting new action parameters drops the current table
		scriptGeneratorTable.setActionParameters(actionParameters);
		scriptGeneratorTable.clearActions();
	}

	/**
	 * Get the actions table containing the parameters for the script.
	 * 
	 * @return scriptGeneratorTable The instance of the scriptGeneratorTable.
	 */
	public ActionsTable getScriptGeneratorTable() {
		return scriptGeneratorTable;
	}

	/**
	 * Adds a new action (row) to the ActionsTable, with default parameter values.
	 */
	public void addEmptyAction() {
		scriptGeneratorTable.addEmptyAction();
	}
	
	/**
	 * Adds a new action with default parameters to the list of actions to a specified location in the table.
	 * 
	 * @param insertionLocation The index to add the specified 
	 */
	public void insertEmptyAction(Integer insertionLocation) {
		scriptGeneratorTable.insertEmptyAction(insertionLocation);
	}

	/**
	 * Removes action at position index from ActionsTable.
	 * 
	 * @param actionsToDeletes the actions to delete.
	 */
	public void deleteAction(List<ScriptGeneratorAction> actionsToDeletes) {
		scriptGeneratorTable.deleteAction(actionsToDeletes);
	}

	/**
	 * Duplicates actions at position indices in ActionsTable.
	 * 
	 * @param actionsToDuplicate
	 * 			The actions to duplicate.
	 * @param insertionLocation
	 *          The index in the list to do the insertion.
	 */
	public void duplicateAction(List<ScriptGeneratorAction> actionsToDuplicate, Integer insertionLocation) {
		scriptGeneratorTable.duplicateAction(actionsToDuplicate, insertionLocation);
	}

    /**
     * Clears all actions from the ActionsTable.
     */
    public void clearAction() {
        scriptGeneratorTable.clearAction();
    }
    
	/**
	 * Moves actions one row up in table.
	 * 
	 * @param actionsToMove the actions to move.
	 */
	public void moveActionUp(List<ScriptGeneratorAction> actionsToMove) {
		scriptGeneratorTable.moveActionUp(actionsToMove);
	}

	/**
	 * Moves actions one row down in table.
	 * 
	 * @param actionsToMove the actions to move.
	 */
	public void moveActionDown(List<ScriptGeneratorAction> actionsToMove) {
		scriptGeneratorTable.moveActionDown(actionsToMove);
	}

	/**
	 * Get the list of actions in the ActionsTable.
	 * 
	 * @return List of actions in the ActionsTable.
	 */
	public List<ScriptGeneratorAction> getActions() {
		return scriptGeneratorTable.getActions();
	}

	/**
	 * @return The action parameters used in this table.
	 * 
	 */
	public List<JavaActionParameter> getActionParameters() {
		return scriptGeneratorTable.getActionParameters();
	}

	/**
	 * Clean up resources when the plugin is destroyed.
	 */
	public void cleanUp() {
		scriptDefinitionLoader.cleanUp();
	}

	/**
	 * Get a list of available script definitions.
	 * 
	 * @return A list of available script definitions.
	 */
	public List<ScriptDefinitionWrapper> getAvailableScriptDefinitions() {
		return scriptDefinitionLoader.getAvailableScriptDefinitions();
	}

	/**
	 * Gets all script definitions that could not be loaded and the reason.
	 * 
	 * @return A map of errors when loading script definitions, the script
	 *         definition name is the key and the value is the reason.
	 */
	public Map<String, String> getScriptDefinitionLoadErrors() {
		return scriptDefinitionLoader.getScriptDefinitionLoadErrors();
	}

	/**
	 * Get an optional of the currently loaded script definition.
	 * 
	 * @return An optional of the currently loaded script definition.
	 */
	public Optional<ScriptDefinitionWrapper> getScriptDefinition() {
		return Optional.ofNullable(scriptDefinitionLoader.getScriptDefinition());
	}

	/**
	 * Get the first maxNumOfLines of validity errors to display to a user.
	 * 
	 * @param maxNumOfLines The number of lines to limit the validity errors to.
	 * @return The string of validity errors to display
	 */
	public String getFirstNLinesOfInvalidityErrors(int maxNumOfLines) {
		if (!languageSupported) {
			firePropertyChange(LANGUAGE_SUPPORT_PROPERTY, true, false);
		}
		List<String> errors = scriptGeneratorTable.getInvalidityErrorLines();
		String message = errors.stream().limit(maxNumOfLines).map((String error) -> String.format("- %s", error))
				.collect(Collectors.joining("\n"));
		if (errors.size() > maxNumOfLines) {
			message = String.format(
					"%s\n\n ... plus %d suppressed errors." + " To see an error for a specific row hover over it.",
					message, (errors.size() - maxNumOfLines));
		}
		return message;
	}
	
	/**
	 * Get the estimated time taken for the script.
	 * @return The estimated time for the script.
	 */
	public Optional<Long> getTotalEstimatedTime() {
	    return scriptGeneratorTable.getTotalEstimatedTime();
	}

	/**
	 * Get whether the contents of the script generator are valid or not.
	 * 
	 * @return true if the params are valid or false if not.
	 */
	public boolean areParamsValid() {
		return this.paramValidity;
	}

	/**
	 * Refresh the validity checking of the parameters, or if it fails refresh the
	 * error state of the model to be listened to by the ViewModel.
	 * 
	 * @throws NoScriptDefinitionSelectedException If there is no script definition
	 *                                             selected to refresh checking
	 *                                             against.
	 */
	public void refreshParameterValidityChecking() throws NoScriptDefinitionSelectedException {
		ScriptDefinitionWrapper scriptDefinition = getScriptDefinition()
				.orElseThrow(() -> new NoScriptDefinitionSelectedException(
						"Tried to refresh parameter validity with no script definition selected"));
		try {
			generator.refreshAreParamsValid(scriptGeneratorTable, scriptDefinition);
			generator.refreshValidityErrors(scriptGeneratorTable, scriptDefinition);
			languageSupported = true;
			threadError = false;
		} catch (UnsupportedLanguageException e) {
			firePropertyChange(LANGUAGE_SUPPORT_PROPERTY, languageSupported, languageSupported = false);
			LOG.error(e);
		} catch (InterruptedException | ExecutionException e) {
			registerThreadError(e);
		}
	}
	
    /**
     * Refresh the time estimate of the parameters, or if it fails refresh the
     * error state of the model to be listened to by the ViewModel.
     * 
     * @throws NoScriptDefinitionSelectedException If there is no script definition
     *                                             selected to refresh checking
     *                                             against.
     */
    public void refreshTimeEstimation() throws NoScriptDefinitionSelectedException {
        ScriptDefinitionWrapper scriptDefinition = getScriptDefinition()
                .orElseThrow(() -> new NoScriptDefinitionSelectedException(
                        "Tried to refresh time estimation with no script definition selected"));
        try {
            generator.refreshTimeEstimation(scriptGeneratorTable, scriptDefinition);
            languageSupported = true;
            threadError = false;
        } catch (UnsupportedLanguageException e) {
            firePropertyChange(LANGUAGE_SUPPORT_PROPERTY, languageSupported, languageSupported = false);
            LOG.error(e);
        } catch (InterruptedException | ExecutionException e) {
        	registerThreadError(e);
        }
    }

	/**
	 * Generate a script and save it to file.
	 * 
	 * @throws InvalidParamsException              If the parameters are invalid a
	 *                                             script cannot be generated.
	 * @throws IOException                         If we fail to create and write to
	 *                                             a file.
	 * @throws UnsupportedLanguageException        If the language we are trying to
	 *                                             generate a script in is
	 *                                             unsupported.
	 * @throws NoScriptDefinitionSelectedException If there is no script definition
	 *                                             selected to refresh checking
	 *                                             against.
	 * @return An ID for the generated script.
	 */
	public Optional<Integer> refreshGeneratedScript()
			throws InvalidParamsException, UnsupportedLanguageException, NoScriptDefinitionSelectedException {
		ScriptDefinitionWrapper scriptDefinition = getScriptDefinition()
				.orElseThrow(() -> new NoScriptDefinitionSelectedException(
						"Tried to generate a script with no script definition selected to generate it with"));
		try {
			if (areParamsValid()) {
				Path filePath = getScriptDefinitionPath(scriptDefinition);
				String jsonContent = scriptGenFileHandler.createJsonString(scriptGeneratorTable.getActions(), scriptGenFileHandler.readFileContent(filePath), filePath);
				return generator.refreshGeneratedScript(scriptGeneratorTable, scriptDefinition, jsonContent);
			} else {
				throw new InvalidParamsException("Parameters are invalid, cannot generate script");
			}
		} catch (InterruptedException | ExecutionException e) {
			registerThreadError(e);
		} catch (IOException e) {
			LOG.error(e);
		}
		return Optional.empty();
	}

	private void registerThreadError(Exception e) {
		firePropertyChange(THREAD_ERROR_PROPERTY, threadError, true);
		LOG.error(e);
		threadError = true;
	}
	/**
	 * Generate a suggested filename to write the script to.
	 *
	 * @return The filename to prepend a path to write the script to.
	 * @throws NoScriptDefinitionSelectedException Thrown when we have no config selected to generate the script file with.
	 */
	public String generateScriptFileName() throws NoScriptDefinitionSelectedException {
		String configName = getScriptDefinition()
			.orElseThrow(() -> new NoScriptDefinitionSelectedException("Tried to generate a script with no config selected to generate it with"))
			.getName();
		return String.format("%s-%s", configName, DATE_FORMAT.format(new Date()));
	}

	/**
	 * Reload the script definitions.
	 */
	public void reloadScriptDefinitions() {
		scriptDefinitionLoader.reloadScriptDefinitions();
	}
	
	/**
	 * Set the location of the repository containing script definitions.
	 */
	public void setRepoPath() {
		this.scriptDefinitionsRepoPath = Paths.get(pythonInterface.getRepoPath());
	}
	
	/**
	 * Loads parameter values from a file and return the contents.
	 * 
	 * @param fileName name of data file user wants to load
	 * @return A list of the parameters that have been loaded
	 * @throws NoScriptDefinitionSelectedException when script definition has not been selected
	 * @throws ScriptDefinitionNotMatched when the script definition used to generate data file does not match with the one used to load it
	 */
	public List<Map<JavaActionParameter, String>> loadParameterValues(Path fileName) throws NoScriptDefinitionSelectedException, ScriptDefinitionNotMatched, UnsupportedOperationException {
		ScriptDefinitionWrapper scriptDefinition = getScriptDefinition()
				.orElseThrow(() -> new NoScriptDefinitionSelectedException("No Configuration Selected"));
		return scriptGenFileHandler.getParameterValues(fileName, getScriptDefinitionPath(scriptDefinition), getActionParameters());
	}

	/**
	 * Adds a set of actions to the table.
	 * @param actionsToAdd a list of actions and their parameters to add
	 * @param replace if true replace current list of actions, if false just append loaded actions
	 */
	public void addActionsToTable(List<Map<JavaActionParameter, String>> actionsToAdd, Boolean replace) {
		if (replace) {
			scriptGeneratorTable.clearAction();
		}
		scriptGeneratorTable.addMultipleActions(actionsToAdd);
	}
	
	/**
	 * Save parameter values to a file.
	 * 
	 * @param filePath full path to save the file to
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public void saveParameters(String filePath) throws NoScriptDefinitionSelectedException {
		ScriptDefinitionWrapper scriptDefinition = getScriptDefinition()
				.orElseThrow(() -> new NoScriptDefinitionSelectedException("No Configuration Selected"));
		
		try {
			if (!filePath.endsWith(SCRIPT_GEN_PARAMS_EXT)) {
				//Strip out other any pre-existing file extension and add the SGP (script generator parameters) file extension
				filePath = filePath.substring(0, filePath.lastIndexOf('.')) + SCRIPT_GEN_PARAMS_EXT;
			}
			scriptGenFileHandler.saveParameters(scriptGeneratorTable.getActions(), getScriptDefinitionPath(scriptDefinition), filePath);
		} catch (InterruptedException | ExecutionException e) {
			registerThreadError(e);
		}
	}
	
	
	/**
	 * Get the file writer to use to write scripts to file.
	 * 
	 * @return the file handler to write scripts to file.
	 */
	public ScriptGeneratorFileHandler getFileHandler() {
		return fileHandler;
	}

	/**
	 * Get whether there are updates available for the git repository.
	 * @return true if there are updates available.
	 */
	public boolean updatesAvailable() {
		return pythonInterface.updatesAvailable();
	}

	/**
	 * Get the list of git errors raised while loading.
	 * @return List of error messages from git loading.
	 */
	public List<String> getGitLoadErrors() {
		return pythonInterface.getGitLoadErrors();
	}

	/**
	 * Gets whether the remote git repo URL is accessible.
	 * @return true if the remote repo URL can be accessed.
	 */
	public boolean remoteAvailable() {
		return pythonInterface.remoteAvailable();
	}

	/**
	 * Determine from the python whether the git repository is dirty.
	 * 
	 * @return true if the git repository is dirty.
	 */
	public boolean isDirty() {
		return pythonInterface.isDirty();
	}

	/**
	 * Merges git repository from upstream.
	 */
	public void mergeOrigin() {
		pythonInterface.mergeOrigin();
		
	}
	
	/**
	 * Paste actions in given row/location.
	 * @param listOfActions list of map which contains mapped parameters to its values.
	 * @param pasteLocation location where user wants to paste the copied actions.
	 */
	public void pasteActions(ArrayList<Map<JavaActionParameter, String>> listOfActions, int pasteLocation) {
		scriptGeneratorTable.insertMultipleActions(listOfActions, pasteLocation);
	}
	
	/**
     * Get the generated script from the given ID.
     * 
     * @param scriptId The ID of the script to get.
     * @return The script
     */
	public Optional<String> getScriptFromId(Integer scriptId) {
		return generator.getScriptFromId(scriptId);
	}

}
