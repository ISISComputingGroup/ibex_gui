package uk.ac.stfc.isis.ibex.scriptgenerator.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.JavaActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ActionParameter;

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
			if (actionParameter.getCopyPreviousRow() == true) {
				String value;
				if (this.actions.size() > 0) {
				ScriptGeneratorAction lastRow = this.actions.get(this.actions.size() - 1);
				value = lastRow.getActionParameterValue(actionParameter);
				} else {
					value = actionParameter.getDefaultValue();
				}
				parametersMap.put(actionParameter, value);
			} else { 
			 parametersMap.put(actionParameter, actionParameter.getDefaultValue());
			}
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
	 * @param actionsToDeletes
	 * 		  	The actions to remove from the actions list.
	 */
	public void deleteAction(List<ScriptGeneratorAction> actionsToDeletes) {
		final List<ScriptGeneratorAction> newList = new ArrayList<ScriptGeneratorAction>(actions);
		newList.removeAll(actionsToDeletes);
		firePropertyChange(ACTIONS_PROPERTY, actions, actions = newList);
	}

	/**
	 * Duplicates a set of selected actions in the table and places them in the specified position.
	 * @param actionsToDuplicate
	 * 			The actions to duplicate.
	 * @param insertionLocation
	 *          The index in the list to do the insertion.
	 */
	public void duplicateAction(List<ScriptGeneratorAction> actionsToDuplicate, Integer insertionLocation) {	
		final var newActionsList = new ArrayList<ScriptGeneratorAction>(actions);
		
		List<ScriptGeneratorAction> actionsToAdd = actionsToDuplicate.stream()
				.map(action -> createAction(action.getActionParameterValueMap()))
				.collect(Collectors.toList());
		
		newActionsList.addAll(insertionLocation, actionsToAdd);
		firePropertyChange(ACTIONS_PROPERTY, actions, actions = newActionsList);
	}
    
    /**
     * Clears the list of actions.
     */
    public void clearAction() {
        final List<ScriptGeneratorAction> newList = new ArrayList<ScriptGeneratorAction>();
        firePropertyChange(ACTIONS_PROPERTY, actions, actions = newList);
    }
	
	/**
	 * Moves a selection of actions down one row.
	 * @param actionsToMove
	 * 			The actions to be moved
	 */
	public void moveActionDown(List<ScriptGeneratorAction> actionsToMove) {
		if (actionsToMove.contains(actions.get(actions.size() - 1))) {
			return;
		}
		final var newActions = new ArrayList<ScriptGeneratorAction>(actions);
		ListIterator<ScriptGeneratorAction> iterator = actionsToMove.listIterator(actionsToMove.size());

		// Iterate in reverse.
		while (iterator.hasPrevious()) {
			Integer currentIndex = actions.indexOf(iterator.previous());
			Collections.swap(newActions, currentIndex, currentIndex + 1);
		}
		
		firePropertyChange(ACTIONS_PROPERTY, actions, actions = newActions);
	}
	
	/**
	 * Moves a selection of actions up a row.
	 * @param actionsToMove
	 * 			The indices of actions to be moved
	 */
	public void moveActionUp(List<ScriptGeneratorAction> actionsToMove) {
		if (actionsToMove.contains(actions.get(0))) {
			return;
		}
		final var newActions = new ArrayList<ScriptGeneratorAction>(actions);
		
		for (var action : actionsToMove) {
			Integer currentIndex = actions.indexOf(action);
			Collections.swap(newActions, currentIndex, currentIndex - 1);
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
     * @param estimatedTimes The hashmap to set estimated time based on.
     */
    public void setEstimatedTimes(Map<Integer, Number> estimatedTimes) {
        for (int i = 0; i < actions.size(); i++) {
        	actions.get(i).setEstimatedTime(Optional.ofNullable(estimatedTimes.get(i)));
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
     * Get the total estimated time of all actions in the table.
     * 
     * @return An Optional containing the total if at least one action has been estimated,
     *         empty optional otherwise
     */
    public Optional<Long> getTotalEstimatedTime() {
    	
        List<Number> actualEstimates = actions.stream().map(action -> action.getEstimatedTime()).flatMap(Optional::stream).collect(Collectors.toList());    	  
        Long total = actualEstimates.stream().map(x -> x.longValue()).reduce(0L, Long::sum);
        return actualEstimates.size() == 0 ? Optional.empty() : Optional.of(total);
    }

	/**
	 * Reload the actions by firing a property change.
	 */
	public void reloadActions() {
		firePropertyChange(ACTIONS_PROPERTY, null, actions);
	}
}
