package uk.ac.stfc.isis.ibex.scriptgenerator.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
	 * @param index
	 * 		  	The index to remove from the actions list.
	 */
	public void deleteAction(int index) {
		if (isValidIndex(index)) {
			final List<ScriptGeneratorAction> newList = new ArrayList<ScriptGeneratorAction>(actions);
			newList.remove(index);
			firePropertyChange(ACTIONS_PROPERTY, actions, actions = newList);
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
			var newAction = createAction(actionToDuplicate.getActionParameterValueMap());
			final var newActions = new ArrayList<ScriptGeneratorAction>();
			
			newActions.addAll(actions);
			
			newActions.add(index + 1, newAction);
			
			firePropertyChange(ACTIONS_PROPERTY, actions, this.actions = newActions);
		}
	}
    
    /**
     * Clears the list of actions.
     */
    public void clearAction() {
        final List<ScriptGeneratorAction> newList = new ArrayList<ScriptGeneratorAction>();
        firePropertyChange(ACTIONS_PROPERTY, actions, actions = newList);
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
			final var newActions = new ArrayList<ScriptGeneratorAction>();
			
			newActions.addAll(actions);
			
			if (newIndex < 0) {
				newIndex = 0;
			} else if (newIndex >= this.actions.size()) {
				newIndex = this.actions.size() - 1;
			}
			
			Collections.swap(newActions, oldIndex, newIndex);
		
			firePropertyChange(ACTIONS_PROPERTY, actions, actions = newActions);
		}
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
     * Set the estimated time for each action based on the hashmap.
     * 
     * @param validityErrors The hashmap to set estimated time based on.
     */
    public void setEstimatedTime(Map<Integer, Double> estimatedTimes) {
        for (int i = 0; i < actions.size(); i++) {
            if (estimatedTimes.containsKey(i)) {
                Optional<Double> estimate = Optional.of(estimatedTimes.get(i));
                actions.get(i).setEstimatedTime(estimate);
            } else {
                actions.get(i).setEstimatedTime(Optional.empty());
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
     * Get the total estimated time of all actions in the table
     * 
     * @return An Optional containing the total if at least one action has been estimated,
     *         empty optional otherwise
     */
    public Optional<Double> getTotalEstimatedTime() {
        boolean isAnyActionEstimated = false;
        Double total = 0.0;
        
        for (ScriptGeneratorAction action : actions) {
            if (action.getEstimatedTime().isPresent()) {
                isAnyActionEstimated = true;
                total += action.getEstimatedTime().get();
            }
        }
        
        return isAnyActionEstimated ? Optional.of(total) : Optional.empty();
    }

	/**
	 * Reload the actions by firing a property change.
	 */
	public void reloadActions() {
		firePropertyChange(ACTIONS_PROPERTY, null, actions);
	}
}
