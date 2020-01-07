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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.GeneratorFacade;
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
	private ConfigLoader configLoader;
	private PythonInterface pythonInterface;
	
	/**
	 * Get the config loader.
	 * 
	 * @return The config loader
	 */
	public ConfigLoader getConfigLoader() {
		return configLoader;
	}
	
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
		List<String> errors = scriptGeneratorTable.getInvalidityErrorLines();
		String message = errors.stream().limit(maxNumOfLines).map((String error) -> String.format("- %s", error)).collect(Collectors.joining("\n"));
		if (errors.size() > maxNumOfLines) {
		    message = String.format("%s\n ... plus %d suppressed errors. To see an error for a specific row hover over it.", 
		    		message, (errors.size() - maxNumOfLines));
		}
		return message;
	}
}
