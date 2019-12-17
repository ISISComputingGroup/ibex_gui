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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.GeneratorFacade;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ConfigLoader;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonInterface;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;

/**
 * Acts as a permanent reference to the ActionsTable.
 *
 */
public class ScriptGeneratorSingleton extends ModelObject implements PropertyChangeListener {
	
	private ActionsTable scriptGeneratorTable = new ActionsTable(new ArrayList<ActionParameter>());
	private final ConfigLoader configLoader;
	private final PythonInterface pythonInterface;
	
	/**
	 * The constructor, will create a config loader and load an initial config.
	 */
	public ScriptGeneratorSingleton() {
		pythonInterface = new PythonInterface();
		configLoader = new ConfigLoader(pythonInterface);
		setUp();
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
	 * Called by the constructors to set up the class.
	 */
	private void setUp() {
		configLoader.addPropertyChangeListener("parameters", evt -> {
			setActionParameters(configLoader.getParameters());
		});
				
		setActionParameters(configLoader.getParameters());
		
		scriptGeneratorTable.addPropertyChangeListener(this);
	}

	/**
	 * Get the config loader.
	 * @return The class responsible for loading a new table configuration.
	 */
	public ConfigLoader getConfigLoader() {
		return configLoader;
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
	 * On a property change check the validity errors.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		onTableChange();
	}
	
	/**
	 * What to do on the actions in the table changing (check the validity errors).
	 */
	public void onTableChange() {
		HashMap<Integer, String> validityErrors = GeneratorFacade.getValidityErrors(
				scriptGeneratorTable, configLoader.getConfig());
		scriptGeneratorTable.setValidityErrors(validityErrors);
	}
	
	/**
	 * Get the first maxNumOfLines of validity errors to display to a user.
	 * 
	 * @param maxNumOfLines The number of lines to limit the validity errors to.
	 * @return The string of validity errors to display
	 */
	public String getFirstNLinesOfInvalidityErrors(int maxNumOfLines) {
		StringBuilder errors = new StringBuilder();
		ArrayList<String> invalidityErrorLines = scriptGeneratorTable.getInvalidityErrorLines();
		for (int i = 0; i < invalidityErrorLines.size(); i++) {
			if (maxNumOfLines <= i) {
				errors.append("\nLimited to " + maxNumOfLines + " lines. To see an error for a specific row hover over it.");
				break;
			}
			errors.append(invalidityErrorLines.get(i)+"\n");
		}
		return errors.toString();
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
