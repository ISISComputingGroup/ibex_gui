package uk.ac.stfc.isis.ibex.scriptgenerator;

import java.util.ArrayList;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ConfigLoader;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;

/**
 * Acts as a permanent reference to the ActionsTable.
 *
 */
public class ScriptGeneratorSingleton extends ModelObject {
	private ActionsTable scriptGeneratorTable = new ActionsTable(new ArrayList<ActionParameter>());
	
	private final ConfigLoader configLoader;
	
	/**
	 * The constructor, will create a config loader and load an initial config.
	 */
	public ScriptGeneratorSingleton() {
		configLoader = new ConfigLoader();
		configLoader.addPropertyChangeListener("parameters", evt -> {
			setActionParameters(configLoader.getParameters());
		});
				
		setActionParameters(configLoader.getParameters());
	}

	/**
	 * Get the config loader.
	 * @return The class responsible for loading a new table configuration.
	 */
	public ConfigLoader getConfigLoader() {
		return configLoader;
	}

	/**
	 * Adds the new action parameters to the empty ActionsTable.
	 * 
	 * @param actionParameters
	 */
	private void setActionParameters(ArrayList<ActionParameter> actionParameters) {
		// Setting new action parameters drops the whole table
		//this.scriptGeneratorTable = new ActionsTable(new ArrayList<ActionParameter>());
		var newParameters = new ArrayList<ActionParameter>();
		for (ActionParameter actionParameter : actionParameters) {
			newParameters.add(actionParameter);
		}
		scriptGeneratorTable.setActionParameters(newParameters);
		scriptGeneratorTable.clearActions();
	}
	
	/**
	 * Get the actions table containing the parameters for the script.
	 * 
	 * @return the scriptGeneratorTable.
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
	 * @param the index to delete.
	 */
	public void deleteAction(int index) {
		scriptGeneratorTable.deleteAction(index);
	}
	
	/**
	 * Duplicates action at position index in ActionsTable.
	 * 
	 * @param the index to duplicate.
	 */
	public void duplicateAction(int index) {
		scriptGeneratorTable.duplicateAction(index);
	}
	
	/**
	 * Moves action one row up in table.
	 * 
	 * @param the index to move.
	 */
	public void moveActionUp(int index) {
		scriptGeneratorTable.moveAction(index, index-1);
	}

	/**
	 * Moves action one row down in table.
	 * 
	 * @param the index to move.
	 */
	public void moveActionDown(int index) {
		scriptGeneratorTable.moveAction(index, index+1);
	}

	/**
	 * Clean up resources when the plugin is destroyed.
	 */
	public void cleanUp() {
		configLoader.cleanUp();
	}
}
