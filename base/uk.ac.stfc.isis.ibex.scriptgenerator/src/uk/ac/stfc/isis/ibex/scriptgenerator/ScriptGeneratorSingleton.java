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

import java.util.ArrayList;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ConfigLoader;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonInterface;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;

/**
 * Acts as a permanent reference to the ActionsTable.
 *
 */
public class ScriptGeneratorSingleton extends ModelObject {
	private ActionsTable scriptGeneratorTable = new ActionsTable(new ArrayList<ActionParameter>());
	
	private ConfigLoader configLoader;
	
	/**
	 * Create a config loader and load an initial config.
	 * You cannot do this in the instantiation of the class because the class is 
	 * created when the plugin is and surefire tests will fail.
	 * 
	 * @return The config loader
	 */
	public ConfigLoader createConfigLoader() {
		configLoader = new ConfigLoader(new PythonInterface());
		configLoader.addPropertyChangeListener("parameters", evt -> {
			setActionParameters(configLoader.getParameters());
		});
				
		setActionParameters(configLoader.getParameters());
		return configLoader;
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
	 * Clean up resources when the plugin is destroyed.
	 */
	public void cleanUp() {
		configLoader.cleanUp();
	}
}
