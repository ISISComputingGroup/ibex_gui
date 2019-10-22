package uk.ac.stfc.isis.ibex.scriptgenerator.table;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;

/**
 * This class holds the script actions and their positions in the script
 *
 */
public class ActionsTable extends ModelObject {

	private List<ActionParameter> actionParameters;
	private ArrayList<ScriptGeneratorAction> actions = new ArrayList<ScriptGeneratorAction>();
	
	/**
	 * 
	 * @return List of actions in the ActionsTable
	 */
	public List<ScriptGeneratorAction> getActions() {
		return actions;
	}

	/**
	 * The actions table holds each action and its parameter values in an ordered list
	 * @param actionParameters
	 */
	public ActionsTable(List<ActionParameter> actionParameters) {
		setActionParameters(actionParameters);
	}

	/**
	 * Sets the parameters for the actions represented in this table
	 * @param actionParameters
	 * 		  		The list of parameters used to define each action
	 */
	public void setActionParameters(List<ActionParameter> actionParameters) {
		this.actionParameters = actionParameters;
	}
	
	/**
	 * @return The action parameters used in this table
	 * 
	 */
	public List<ActionParameter> getActionParameters() {
		return actionParameters;
	}

	/**
	 * Adds a new action with default parameters to the list of actions
	 */
	public void addEmptyAction() {
		var parametersMap = new HashMap<String, String>();
		// Make a parameter/string pair for each parameter in the action
		for (ActionParameter actionParameter: this.actionParameters) {
			// TODO: Add a sensible default for the action parameter
			parametersMap.put(actionParameter.getName(), actionParameter.getName()+Integer.toString(actions.size()));
		}
		
		var newAction = new ScriptGeneratorAction(parametersMap);
		
		this.actions.add(newAction);
		firePropertyChange("actions", null, null);
	}

	/**
	 * Removes an action from the list in specified location
	 * @param index
	 * 		  	The index to remove from the actions list
	 */
	public void deleteAction(int index) {
		this.actions.remove(index);
		firePropertyChange("actions", null, null);
	}

	/**
	 * Duplicates an action in the list at specified location
	 * @param index
	 * 			The index of the action to duplicate
	 */
	public void duplicateAction(int index) {
		var actionToDuplicate = actions.get(index);
		var newAction = new ScriptGeneratorAction(actionToDuplicate);
		
		this.actions.add(index+1, newAction);
		
		firePropertyChange("actions", null, null);
		
	}

	/**
	 * Moves action to a new position in the table
	 * @param oldIndex
	 * 			The current index of the action to be moved
	 * @param newIndex
	 * 			The index to move the action to, if valid
	 */
	public void moveAction(int oldIndex, int newIndex) {
		if (newIndex < 0) {
			newIndex = 0;
		} else if (newIndex >= this.actions.size()) {
			newIndex = this.actions.size() - 1;
		}
		
		Collections.swap(this.actions, oldIndex, newIndex);
	
		firePropertyChange("actions", null, null);
	}
	

}
