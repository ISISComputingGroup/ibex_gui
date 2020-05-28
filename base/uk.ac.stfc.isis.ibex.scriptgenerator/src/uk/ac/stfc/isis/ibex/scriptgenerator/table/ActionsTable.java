package uk.ac.stfc.isis.ibex.scriptgenerator.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.JavaActionParameter;

/**
 * This class holds the script actions and their positions in the script.
 *
 */
public class ActionsTable extends ModelObject {

	/**
	 * The action parameters (columns) of the table that all actions in the table conform to.
	 */
	private List<JavaActionParameter> actionParameters;
	
	/**
	 * The actions (rows) of the table that have values for the action parameters.
	 */
	private List<ScriptGeneratorAction> actions = new ArrayList<ScriptGeneratorAction>();
	
	/**
	 * The property to fire a change for if the actions in the table change.
	 */
	private static final String ACTIONS_PROPERTY = "actions";
	
	/**
	 * The property of an action to listen to for changes.
	 */
	private static final String VALUE_PROPERTY = "value";
	
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
	 * 			The parameters taken from the script definition to use in this table.
	 */
	public ActionsTable(List<JavaActionParameter> actionParameters) {
		setActionParameters(actionParameters);
	}

	/**
	 * Sets the parameters for the actions represented in this table.
	 * @param actionParameters
	 * 		  		The list of parameters used to define each action.
	 */
	public void setActionParameters(List<JavaActionParameter> actionParameters) {
		firePropertyChange("actionParameters", this.actionParameters, this.actionParameters = actionParameters);
	}
	
	/**
	 * @return The action parameters used in this table.
	 * 
	 */
	public List<JavaActionParameter> getActionParameters() {
		return actionParameters;
	}
	
	/**
	 * Create a new script generator action.
	 * 
	 * @param parametersMap The user-set value (string) for the specified ActionParameter.
	 * @return the action.
	 */
	private ScriptGeneratorAction createAction(Map<JavaActionParameter, String> parametersMap) {
		// Ensure is not shallow copy
		Map<JavaActionParameter, String> newParamsMap = new HashMap<JavaActionParameter, String>();
		for (Map.Entry<JavaActionParameter, String> entry: parametersMap.entrySet()) {
			newParamsMap.put(entry.getKey(), entry.getValue());
		}
		// Create action and attach listeners
		ScriptGeneratorAction newAction = new ScriptGeneratorAction(newParamsMap);
		newAction.addPropertyChangeListener(VALUE_PROPERTY, evt -> {
			firePropertyChange(ACTIONS_PROPERTY, null, actions);
		});
		return newAction;
	}

	/**
	 * Adds a new action with default parameters to the list of actions.
	 */
	public void addEmptyAction() {
		var parametersMap = new HashMap<JavaActionParameter, String>();
		// Make a parameter/string pair for each parameter in the action
		for (JavaActionParameter actionParameter: this.actionParameters) {
			parametersMap.put(actionParameter, actionParameter.getDefaultValue());
		}
		
		var newAction = createAction(parametersMap);
		
		final List<ScriptGeneratorAction> newList = new ArrayList<ScriptGeneratorAction>(actions);
		newList.add(newAction);
		firePropertyChange(ACTIONS_PROPERTY, actions, actions = newList);
	}
	
	/**
	 * Add multiple actions.
	 * @param list contains mapped action parameters to its values
	 */
	public void addMultipleActions(List<Map<JavaActionParameter, String>> list) {
		final List<ScriptGeneratorAction> newList = new ArrayList<ScriptGeneratorAction>(actions);
		for (Map<JavaActionParameter, String> map : list) {
			var newAction = createAction(map);
			newList.add(newAction);
		}
		firePropertyChange(ACTIONS_PROPERTY, actions, actions = newList);

	}

	/**
	 * Removes an action from the list in specified location.
	 * @param indices
	 * 		  	The indices to remove from the actions list.
	 */
	public void deleteAction(int[] indices) {
		Arrays.sort(indices);
		final List<ScriptGeneratorAction> newList = new ArrayList<ScriptGeneratorAction>(actions);
		// remove selected indices from highest to lowest
		for (int i = indices.length - 1; i >= 0; i--) {
			if (isValidIndex(indices[i])) {
				newList.remove(indices[i]);
			}
		}
		firePropertyChange(ACTIONS_PROPERTY, actions, actions = newList);
	}

	/**
	 * Duplicates a set of selected actions in the table and places them after the lowest selected action in the table
	 * @param indices
	 * 			The indices of the actions to duplicate.
	 */
	public void duplicateAction(int[] indices) {
		Arrays.sort(indices);
		
		final var newActionsList = new ArrayList<ScriptGeneratorAction>();
		newActionsList.addAll(actions);
		
		final var actionsToAdd = new ArrayList<ScriptGeneratorAction>();
		
		for (int i = 0; i < indices.length; i++) {
			if (isValidIndex(indices[i])) {
				var actionToDuplicate = actions.get(indices[i]);
				var newAction = createAction(actionToDuplicate.getActionParameterValueMap());			
				actionsToAdd.add(newAction);
			}
		}
		
		// add new actions after the last selected action
		newActionsList.addAll(indices[indices.length - 1] + 1, actionsToAdd);
		firePropertyChange(ACTIONS_PROPERTY, actions, this.actions = newActionsList);
	}
    
    /**
     * Clears the list of actions.
     */
    public void clearAction() {
        final List<ScriptGeneratorAction> newList = new ArrayList<ScriptGeneratorAction>();
        firePropertyChange(ACTIONS_PROPERTY, actions, actions = newList);
    }
	
	/**
	 * Moves a selection of actions down one row
	 * @param indices
	 * 			The indices of actions to be moved
	 */
	public void moveActionDown(int[] indices) {
		final var newActions = new ArrayList<ScriptGeneratorAction>();
		newActions.addAll(actions);
		
		for (int i = indices.length - 1; i >= 0; i--) {
			if (isValidIndex(indices[i])) {
				if (indices[i] + 1 >= this.actions.size()) {
					break;
				}
				Collections.swap(newActions, indices[i], indices[i] + 1);
			}
		}
		
		firePropertyChange(ACTIONS_PROPERTY, actions, actions = newActions);
	}
	
	/**
	 * Moves a selection of actions down up row
	 * @param indices
	 * 			The indices of actions to be moved
	 */
	public void moveActionUp(int[] indices) {
		final var newActions = new ArrayList<ScriptGeneratorAction>();
		newActions.addAll(actions);
		
		for (int i = 0; i < indices.length; i++) {
			if (isValidIndex(indices[i])) {
				if (indices[i] - 1 < 0) {
					break;
				}
				Collections.swap(newActions, indices[i], indices[i] - 1);
			}
		}
		
		firePropertyChange(ACTIONS_PROPERTY, actions, actions = newActions);
	}
	
	/**
	 * Clears the list of actions.
	 */
	public void clearActions() {
		firePropertyChange(ACTIONS_PROPERTY, actions, actions = new ArrayList<ScriptGeneratorAction>());
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

	/**
	 * Set the validity errors for each action based on the hashmap.
	 * 
	 * @param validityErrors The hashmap to set validity errors based on.
	 */
	public void setValidityErrors(Map<Integer, String> validityErrors) {
		for (int i = 0; i < actions.size(); i++) {
			if (validityErrors.containsKey(i)) {
				actions.get(i).setInvalid(validityErrors.get(i));
			} else {
				actions.get(i).setValid();
			}
		}
	}
	
	/**
	 * Get strings of validity errors.
	 * 
	 * @return The strings of validity error lines to display.
	 */
	public ArrayList<String> getInvalidityErrorLines() {
		var errors = new ArrayList<String>();
		for (int i = 0; i < actions.size(); i++) {
			ScriptGeneratorAction action = actions.get(i);
			if (!action.isValid()) {
				String errorString = "Row: " + (i + 1) + ", Reason: " + "\n" + action.getInvalidityReason().get() + "\n";
				
				errors.addAll(Arrays.asList(errorString.split("\n")));
			}
		}
		return errors;
	}

	/**
	 * Reload the actions by firing a property change.
	 */
	public void reloadActions() {
		firePropertyChange(ACTIONS_PROPERTY, null, actions);
	}
}
