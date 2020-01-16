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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.GeneratorContext;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.UnsupportedLanguageException;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ConfigLoader;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonInterface;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;

/**
 * The Model of the script generator responsible for generating scripts, checking parameter validity, loading configs
 *  and containing the actions table for the script generator.
 */
public class ScriptGeneratorSingleton extends ModelObject {
	
	/**
	 * The table containing the script generator contents (actions).
	 */
	private ActionsTable scriptGeneratorTable =
			new ActionsTable(new ArrayList<ActionParameter>());
	
	/**
	 * The loader to select and update the configs being used.
	 */
	private ConfigLoader configLoader;
	
	/**
	 * The python interface to check parameter validity with,
	 *  generate scripts with, load configs with.
	 */
	private PythonInterface pythonInterface;
	
	/**
	 * The property to listen for changes to actions in the script generator table on.
	 */
	private static final String ACTIONS_PROPERTY = "actions";
	
	/**
	 * The property to listen for changes in the generator on,
	 *  as to whether the current selected language is supported.
	 */
	private static final String LANGUAGE_SUPPORT_PROPERTY = "language_supported";
	
	/**
	 * Whether the current used language is supported or not.
	 */
	public boolean languageSupported = true;
	
	/**
	 * The property to listen for changes in the generator on,
	 *  as to whether there has been a thread error when checking
	 *   parameter validity or generating a script.
	 */
	private static final String THREAD_ERROR_PROPERTY = "thread error";
	
	/**
	 * Whether there has been a thread error or not that affects
	 *  the current state of the script generator.
	 */
	private boolean threadError = false;
	
	/**
	 * The property to listen for changes in the generator containing the mapping Map<Integer, String> of
	 *  validity checks for each row of the script generator.
	 */
	private static final String VALIDITY_ERROR_MESSAGE_PROPERTY = "validity error messages";
	
	/**
	 * The property to listen for changes in a Generator containing whether or not all
	 *  script generator contents are valid or not (bool).
	 */
	private static final String PARAM_VALIDITY_PROPERTY = "parameter validity";
	
	/**
	 * The property to listen for changes in a Generator containing the generated script (String).
	 */
	private static final String GENERATED_SCRIPT_PROPERTY = "generated script";
	
	/**
	 * The current state of parameter validity.
	 */
	private boolean paramValidity = false;
	
	/**
	 * The generator to generate scripts with and check parameter validity with.
	 */
	private GeneratorContext generator;
	
	private static final Logger LOG = IsisLog.getLogger(ScriptGeneratorSingleton.class);
	
	/**
	 * The constructor, will create without a config loader and without loading
	 * an initial config.
	 */
	public ScriptGeneratorSingleton() {
	    
	}
	
	/**
	 * Pass a python interface to run with, then create a config loader and load an initial config.
	 * 
	 * @param pythonInterface The python interface to run with.
	 */
	public ScriptGeneratorSingleton(PythonInterface pythonInterface, ConfigLoader configLoader, ActionsTable scriptGeneratorTable) {
		this.pythonInterface = pythonInterface;
		this.configLoader = configLoader;
		this.scriptGeneratorTable = scriptGeneratorTable;
		setUp();
	}
	
	/**
	 * Called by the constructor with three arguments during tests or in the View
	 * Model constructor to set up the class.
	 * Set up listeners for the generator.
	 */
	public void setUp() {
		generator = new GeneratorContext(pythonInterface);
		// If the validity error message property of the generator is changed update the
		// validity errors in the scriptGeneratorTable
		generator.addPropertyChangeListener(VALIDITY_ERROR_MESSAGE_PROPERTY, evt -> {
			scriptGeneratorTable.setValidityErrors(convertValidityMessagesToMap(evt.getNewValue()));
			firePropertyChange(VALIDITY_ERROR_MESSAGE_PROPERTY, evt.getOldValue(), evt.getNewValue());
		});
		// If the parameter validity property is changed update the models field that denotes
		// whether the parameters are valid and notify any listeners
		generator.addPropertyChangeListener(PARAM_VALIDITY_PROPERTY, evt -> {
			Optional.ofNullable(evt.getNewValue())
				.ifPresentOrElse(
					paramValidity -> this.paramValidity = Boolean.class.cast(paramValidity),
					() -> this.paramValidity = false
				);
			firePropertyChange(PARAM_VALIDITY_PROPERTY, evt.getOldValue(), evt.getNewValue());
		});
		generator.addPropertyChangeListener(GENERATED_SCRIPT_PROPERTY, evt -> {
			firePropertyChange(GENERATED_SCRIPT_PROPERTY, evt.getOldValue(), evt.getNewValue());
		});
		configLoader.addPropertyChangeListener("parameters", evt -> {
			setActionParameters(configLoader.getParameters());
		});
		this.scriptGeneratorTable.addPropertyChangeListener(ACTIONS_PROPERTY, evt -> {
			// The table has changed so update the validity checks
			refreshParameterValidityChecking();
		});
		
		setActionParameters(configLoader.getParameters());
	}
	
	/**
     * Creates the config loader.
     */
    public void createConfigLoader() {
        pythonInterface = new PythonInterface();
        configLoader = new ConfigLoader(pythonInterface);
    }
    
    /**
     * Get the config loader.
     * 
     * @return The config loader
     */
    public ConfigLoader getConfigLoader() {
        return configLoader;
    }
	
	/**
	 * Convert the VALIDITY_ERROR_MESSAGE_PROPERTY return to the Map<Integer, String> representation.
	 * Required because of casting generics in Java.
	 * 
	 * @param validityMessages The validity messages to convert.
	 * @return The converted messages property.
	 */
	@SuppressWarnings("rawtypes")
	private static Map<Integer, String> convertValidityMessagesToMap(Object validityMessages) {
		try {
			Map mapCastValidityMessages = Map.class.cast(validityMessages);
			Map<Integer, String> castValidityMessages = new HashMap<>();
			for(Object nonCastEntry : mapCastValidityMessages.entrySet()) {
				Map.Entry castEntry = Map.Entry.class.cast(nonCastEntry);
				castValidityMessages.put(Integer.class.cast(castEntry.getKey()),
						String.class.cast(castEntry.getValue()));
			}
			return castValidityMessages;
		} catch(ClassCastException e) {
			LOG.error(e);
	        return new HashMap<Integer, String>();
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
	 * @param actionParameters
	 * 			the action parameters to use in the script generator table
	 */
	private void setActionParameters(ArrayList<ActionParameter> actionParameters) {
		// Setting new action parameters drops the current table
		scriptGeneratorTable.setActionParameters(actionParameters);
		scriptGeneratorTable.clearActions();
	}
	
	/**
	 * Get the actions table containing the parameters for the script.
	 * 
	 * @return scriptGeneratorTable
	 * 			The instance of the scriptGeneratorTable.
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
	 * Removes action at position index from ActionsTable.
	 * 
	 * @param index
	 * 			the index to delete.
	 */
	public void deleteAction(int index) {
		scriptGeneratorTable.deleteAction(index);
	}
	
	/**
	 * Duplicates action at position index in ActionsTable.
	 * 
	 * @param index
	 * 			the index to duplicate.
	 */
	public void duplicateAction(int index) {
		scriptGeneratorTable.duplicateAction(index);
	}
	
	/**
	 * Moves action one row up in table.
	 * 
	 * @param index
	 * 			the index to move.
	 */
	public void moveActionUp(int index) {
		scriptGeneratorTable.moveAction(index, index - 1);
	}

	/**
	 * Moves action one row down in table.
	 * 
	 * @param index
	 * 			the index to move.
	 */
	public void moveActionDown(int index) {
		scriptGeneratorTable.moveAction(index, index + 1);
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
	public List<ActionParameter> getActionParameters() {
		return scriptGeneratorTable.getActionParameters();
	}

	/**
	 * Clean up resources when the plugin is destroyed.
	 */
	public void cleanUp() {
		configLoader.cleanUp();
	}
	
	/**
	 * Get a list of available configs.
	 * 
	 * @return A list of available configs.
	 */
	public List<Config> getAvailableConfigs() {
		return configLoader.getAvailableConfigs();
	}
	
	/**
	 * Get the currently loaded configuration.
	 * 
	 * @return The currently loaded configuration.
	 */
	public Config getConfig() {
		return configLoader.getConfig();
	}
	
	/**
	 * Get the first maxNumOfLines of validity errors to display to a user.
	 * 
	 * @param maxNumOfLines The number of lines to limit the validity errors to.
	 * @return The string of validity errors to display
	 */
	public String getFirstNLinesOfInvalidityErrors(int maxNumOfLines) {
		if(!languageSupported) {
			firePropertyChange(LANGUAGE_SUPPORT_PROPERTY, true, false);
		}
		List<String> errors = scriptGeneratorTable.getInvalidityErrorLines();
		String message = errors.stream()
				.limit(maxNumOfLines)
				.map((String error) -> String.format("- %s", error))
				.collect(Collectors.joining("\n"));
		if (errors.size() > maxNumOfLines) {
		    message = String.format("%s\n\n ... plus %d suppressed errors."
		    		+ " To see an error for a specific row hover over it.", 
		    		message, (errors.size() - maxNumOfLines));
		}
		return message;
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
	 * Refresh the validity checking of the parameters,
	 *  or if it fails refresh the error state of the model to be listened to by the ViewModel.
	 */
	public void refreshParameterValidityChecking() {
		try {
			Config config = getConfig();
			generator.refreshAreParamsValid(scriptGeneratorTable, config);
			generator.refreshValidityErrors(scriptGeneratorTable, config);
			languageSupported = true;
			threadError = false;
		} catch(UnsupportedLanguageException e) {
			firePropertyChange(LANGUAGE_SUPPORT_PROPERTY, languageSupported, false);
			LOG.error(e);
			languageSupported = false;
		} catch (InterruptedException | ExecutionException e) {
			firePropertyChange(THREAD_ERROR_PROPERTY, threadError, true);
			LOG.error(e);
			threadError = true;
		}
	}
	
	/**
	 * Generate a script and save it to file.
	 * 
	 * @return The filepath the script is saved to.
	 * @throws InvalidParamsException If the parameters are invalid a script cannot be generated.
	 * @throws IOException If we fail to create and write to a file.
	 */
	public String generate() throws InvalidParamsException, IOException {
		return generateTo("C:/Scripts/");
	}
	
	/**
	 * Generate a script and save it to file with the given prefix.
	 * 
	 * @return The filepath the script is saved to.
	 * @throws InvalidParamsException If the parameters are invalid a script cannot be generated.
	 * @throws IOException If we fail to create and write to a file.
	 */
	public String generateTo(String filepathPrefix) throws InvalidParamsException, IOException {
		// Generate the script
		String[] generatedScript = generateScript();
		// Create the filename and file
		String timestamp = getTimestamp();
		int version = 0;
		String filepath;
		File file;
		do {
			if (version == 0) {
				filepath = String.format("%s%s-%s.py", filepathPrefix, getConfig().getName(), timestamp);
			} else {
				filepath = String.format("%s%s-%s(%s).py", filepathPrefix, getConfig().getName(), timestamp, version);
			}
			file = new File(filepath);
			file.getParentFile().mkdirs();
			version += 1;
		} while(!file.createNewFile());
		// Write the script to the file
		BufferedWriter scriptWriter = new BufferedWriter(new FileWriter(file));
		for (String scriptLine : generatedScript) {
			scriptWriter.write(scriptLine);
			scriptWriter.newLine();
		}
		scriptWriter.flush();
		scriptWriter.close();
		return filepath;
	}
	
	/**
	 * Get the current timestamp to put in a filename. (Allows testing).
	 * 
	 * @return The timestamp as a string.
	 */
	public String getTimestamp() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	
	/**
	 * Get a generated script. (Allows testing).
	 * 
	 * @return The lines of the generated script.
	 */
	public String[] generateScript() throws InvalidParamsException {
		return GeneratorFacade.generate(scriptGeneratorTable, getConfig());
	}
}
