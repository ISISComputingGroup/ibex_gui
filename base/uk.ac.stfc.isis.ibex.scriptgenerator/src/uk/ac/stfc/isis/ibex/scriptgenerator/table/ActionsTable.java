package uk.ac.stfc.isis.ibex.scriptgenerator.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;

/**
 * This class holds the script actions and their positions in the script.
 *
 */
public class ActionsTable extends ModelObject {

	private List<ActionParameter> actionParameters;
	private ArrayList<ScriptGeneratorAction> actions = new ArrayList<ScriptGeneratorAction>();
	
	/**
	 * 
	 * @return List of actions in the ActionsTable.
	 */
	public List<ScriptGeneratorAction> getActions() {
		return actions;
	}

	/**
	 * The actions table holds each action and its parameter values in an ordered list.
	 * @param actionParameters
	 */
	public ActionsTable(List<ActionParameter> actionParameters) {
		setActionParameters(actionParameters);
	}

	/**
	 * Sets the parameters for the actions represented in this table.
	 * @param actionParameters
	 * 		  		The list of parameters used to define each action.
	 */
	public void setActionParameters(List<ActionParameter> actionParameters) {
		firePropertyChange("actionParameters", this.actionParameters, this.actionParameters = actionParameters);
	}
	
	/**
	 * @return The action parameters used in this table.
	 * 
	 */
	public List<ActionParameter> getActionParameters() {
		return actionParameters;
	}

	/**
	 * Adds a new action with default parameters to the list of actions.
	 */
	public void addEmptyAction() {
		var parametersMap = new HashMap<String, String>();
		// Make a parameter/string pair for each parameter in the action
		for (ActionParameter actionParameter: this.actionParameters) {
			parametersMap.put(actionParameter.getName(), actionParameter.getName()+Integer.toString(actions.size()));
		}
		
		var newAction = new ScriptGeneratorAction(parametersMap);
		
		firePropertyChange("actions", actions, actions.add(newAction));
	}

	/**
	 * Removes an action from the list in specified location.
	 * @param index
	 * 		  	The index to remove from the actions list.
	 */
	public void deleteAction(int index) {
		if (isValidIndex(index)) {
			firePropertyChange("actions", actions, actions.remove(index));
		}
	}

	/**
	 * Duplicates an action in the list at specified location.
	 * @param index
	 * 			The index of the action to duplicate.
	 */
	public void duplicateAction(int index) {
		if (isValidIndex(index)) {
			var actionToDuplicate = actions.get(index);
			var newAction = new ScriptGeneratorAction(actionToDuplicate);
			var newActions = new ArrayList<ScriptGeneratorAction>();
			
			newActions.addAll(actions);
			
			newActions.add(index+1, newAction);
			
			firePropertyChange("actions", actions, this.actions = newActions);
		}
	}

	/**
	 * Moves action to a new position in the table.
	 * @param oldIndex
	 * 			The current index of the action to be moved.
	 * @param newIndex
	 * 			The index to move the action to, if valid.
	 */
	public void moveAction(int oldIndex, int newIndex) {
		if (isValidIndex(oldIndex)) {
			var newActions = new ArrayList<ScriptGeneratorAction>();
			
			newActions.addAll(actions);
			
			if (newIndex < 0) {
				newIndex = 0;
			} else if (newIndex >= this.actions.size()) {
				newIndex = this.actions.size() - 1;
			}
			
			Collections.swap(newActions, oldIndex, newIndex);
		
			firePropertyChange("actions", actions, actions = newActions);
		}
	}
	
	/**
	 * Checks if the supplied index is a valid position in the table.
	 * @param index
	 * 			The index to test.
	 * @return isValid
	 * 			true if index is a valid position in the table.
	 */
	private Boolean isValidIndex(int index) {
		return index >= 0 && index <= this.actions.size();
	}
	
	
}
