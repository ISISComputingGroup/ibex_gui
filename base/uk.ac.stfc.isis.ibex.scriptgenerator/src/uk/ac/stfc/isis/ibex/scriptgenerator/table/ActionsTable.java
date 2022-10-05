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

import static java.lang.Math.min;
import static java.lang.Math.max;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.JavaActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorProperties;

/**
 * This class holds the script actions and their positions in the script.
 *
 */
public class ActionsTable extends ModelObject {

	/**
	 * The action parameters (columns) of the table that all actions in the table conform to.
	 */
	private List<JavaActionParameter> actionParameters;
	
	private Map<Integer, String> globalValidError = Collections.emptyMap();
	
	/**
	 * The actions (rows) of the table that have values for the action parameters.
	 */
	private List<ScriptGeneratorAction> actions = new ArrayList<ScriptGeneratorAction>();
	
	/**
	 * 
	 * @return List of actions in the ActionsTable.
	 */
	public List<ScriptGeneratorAction> getActions() {
		return actions;
	}
	
	/**
	 * Get the action at the corresponding index or an empty optional.
	 * @param actionIndex - The index of action to get.
	 * @return the action at the corresponding index or an empty optional.
	 */
	public Optional<ScriptGeneratorAction> getAction(Integer actionIndex) {
		if (actionIndexInRange(actionIndex)) {
			var action = actions.get(actionIndex);
			return Optional.of(action);
		} else {
			return Optional.empty();
		}
	}
	
	private Boolean actionIndexInRange(Integer actionIndex) {
		return actionIndex >= 0 && actionIndex < actions.size();
	}
	
	/**
	 * 
	 * @return The globalValidError string on this object.
	 */
	public Map<Integer, String> getGlobalValidityErrors() {
		return this.globalValidError;
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
		firePropertyChange(ScriptGeneratorProperties.ACTION_PARAMETERS_PROPERTY, this.actionParameters, this.actionParameters = actionParameters);
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
		newAction.addPropertyChangeListener(ScriptGeneratorProperties.VALUE_PROPERTY, evt -> {
			firePropertyChange(ScriptGeneratorProperties.ACTIONS_PROPERTY, null, actions);
		});
		return newAction;
	}
	
	/**
	 * Create the default action for the last row in the table.
	 * 
	 * @return The default action for the bottom row of the table.
	 */
	private ScriptGeneratorAction createDefaultAction() {
		return createDefaultAction(Optional.empty());
	}
	
	/**
	 * Create an action with default values for it's given location.
	 * It may copy some values from the row directly above it.
	 * 
	 * @param insertionLocation The row we are creating the action for.
	 * @return The default action for the given row.
	 */
	private ScriptGeneratorAction createDefaultAction(Optional<Integer> insertionLocation) {
		var parametersMap = new HashMap<JavaActionParameter, String>();
		// Make a parameter/string pair for each parameter in the action
		for (JavaActionParameter actionParameter: this.actionParameters) {
			createDefaultActionParameter(insertionLocation, parametersMap, actionParameter);
		}
		return createAction(parametersMap);
	}

	/**
	 * Get the default value for the action parameter given the insertionLocation (may copy value from previous row).
	 * Add that default value to the parametersMap.
	 * 
	 * @param insertionLocation The row in the table we are inserting the action containing the parametersMap in.
	 * @param parametersMap A map of action parameter to string that is used to create an action.
	 * @param actionParameter The key of the parametersMap we are setting the value for.
	 */
	private void createDefaultActionParameter(Optional<Integer> insertionLocation,
			HashMap<JavaActionParameter, String> parametersMap, JavaActionParameter actionParameter) {
		if (actionParameter.getCopyPreviousRow() && this.actions.size() > 0) {
			ScriptGeneratorAction rowToCopy = getRowToCopy(insertionLocation);
			parametersMap.put(actionParameter, rowToCopy.getActionParameterValue(actionParameter));
		} else { 
			parametersMap.put(actionParameter, actionParameter.getDefaultValue());
		}
	}

	/**
	 * If the insertionLocation is present get the row action directly 
	 * above the insertionLocation, else get the last action in the table.
	 * 
	 * @param insertionLocation The location we are getting the row to copy for.
	 * @return A script generator action.
	 */
	private ScriptGeneratorAction getRowToCopy(Optional<Integer> insertionLocation) {
		int positionOfRowToCopy;
		if (insertionLocation.isPresent()) {
			positionOfRowToCopy = (insertionLocation.get() - 1);
		} else {
			// Copy the last row's value as we are adding to the end
			positionOfRowToCopy = (this.actions.size() - 1);
		}
		ScriptGeneratorAction lastRow = this.actions.get(positionOfRowToCopy);
		return lastRow;
	}

	/**
	 * Adds a new action with default parameters to the list of actions.
	 */
	public void addEmptyAction() {		
		var newAction = createDefaultAction();
		
		final List<ScriptGeneratorAction> newList = new ArrayList<ScriptGeneratorAction>(actions);
		newList.add(newAction);
		firePropertyChange(ScriptGeneratorProperties.ACTIONS_PROPERTY, actions, actions = newList);
	}
	
	/**
	 * @param actionIndex The index to coerce
	 * @return Coerce the given actionIndex into the range of available action indexes.
	 */
	private Integer coerceActionIndexIntoRange(Integer actionIndex) {
		return min(max(actionIndex, 0), actions.size());
	}
	
	/**
	 * Adds a new action with default parameters to the list of actions to a specified location in the table.
	 * If the index is out of bounds we coerce the value to be at the beginning or end.
	 * 
	 * @param insertionLocation The index to add the specified 
	 */
	public void insertEmptyAction(Integer insertionLocation) {
		var newAction = createDefaultAction(Optional.of(insertionLocation));
		var correctedInsertionLocation = coerceActionIndexIntoRange(insertionLocation);
		final List<ScriptGeneratorAction> newList = new ArrayList<ScriptGeneratorAction>(actions);
		newList.add(correctedInsertionLocation, newAction);
		firePropertyChange(ScriptGeneratorProperties.ACTIONS_PROPERTY, actions, actions = newList);
	}
	
	/**
	 * Add multiple actions at given row.
	 * @param list contains mapped action parameters to its values
	 * @param insertLocation location in table to insert the actions
	 */
	public void insertMultipleActions(List<Map<JavaActionParameter, String>> list, int insertLocation) {
		final List<ScriptGeneratorAction> currentListOfActions = new ArrayList<ScriptGeneratorAction>(actions);
		for (Map<JavaActionParameter, String> map : list) {
			var newAction = createAction(map);
			currentListOfActions.add(insertLocation, newAction);
			insertLocation++;
		}
		firePropertyChange(ScriptGeneratorProperties.ACTIONS_PROPERTY, actions, actions = currentListOfActions);
	}

	/**
	 * Add multiple actions to the end of the table.
	 * @param list contains mapped action parameters to its values
	 */
	public void addMultipleActions(List<Map<JavaActionParameter, String>> list) {
	    insertMultipleActions(list, actions.size());
	}

	/**
	 * Removes an action from the list in specified location.
	 * @param actionsToDelete
	 * 		  	The actions to remove from the actions list.
	 */
	public void deleteAction(List<ScriptGeneratorAction> actionsToDelete) {
		final List<ScriptGeneratorAction> newList = new ArrayList<ScriptGeneratorAction>(actions);
		newList.removeAll(actionsToDelete);
		firePropertyChange(ScriptGeneratorProperties.ACTIONS_PROPERTY, actions, actions = newList);
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
		var correctedInsertionLocation = coerceActionIndexIntoRange(insertionLocation);
		newActionsList.addAll(correctedInsertionLocation, actionsToAdd);
		firePropertyChange(ScriptGeneratorProperties.ACTIONS_PROPERTY, actions, actions = newActionsList);
	}
    
    /**
     * Clears the list of actions.
     */
    public void clearAction() {
        final List<ScriptGeneratorAction> newList = new ArrayList<ScriptGeneratorAction>();
        firePropertyChange(ScriptGeneratorProperties.ACTIONS_PROPERTY, actions, actions = newList);
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
		
		firePropertyChange(ScriptGeneratorProperties.ACTIONS_PROPERTY, actions, actions = newActions);
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
		
		firePropertyChange(ScriptGeneratorProperties.ACTIONS_PROPERTY, actions, actions = newActions);
	}
	
	/**
	 * Clears the list of actions.
	 */
	public void clearActions() {
		firePropertyChange(ScriptGeneratorProperties.ACTIONS_PROPERTY, actions, actions = new ArrayList<ScriptGeneratorAction>());
	}

	/**
	 * Set the validity errors for each action based on the hashmap.
	 * 
	 * @param validityErrors The list of hashmaps to set validity errors based on.
	 */
	public void setValidityErrors(List<Map<Integer, String>> validityErrors) {
		this.globalValidError = validityErrors.get(0);
		for (int i = 0; i < actions.size(); i++) {
			if (validityErrors.get(1).containsKey(i)) {
				actions.get(i).setInvalid(validityErrors.get(1).get(i));
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
     * Set the estimated customs for each action based on the hashmap.
     * 
     * @param estimatedCustoms Hashmap key is action and value is list of custom estimates.
     */
    public void setEstimatedCustom(Map<Integer, List<Number>> estimatedCustoms) {
        for (int i = 0; i < actions.size(); i++) {
        	actions.get(i).setCustomEstimate(Optional.ofNullable(estimatedCustoms.get(i)));
        }
    }
	
	/**
	 * Get strings of validity errors.
	 * 
	 * @return The strings of validity error lines to display.
	 */
	public ArrayList<String> getInvalidityErrorLines() {
		var errors = new ArrayList<String>();
		if (this.globalValidError.size() > 0) {
			String errorString = "Global Parameter Errors: \n";
			int initialLen = errorString.length();
			for (int i = 0; i < globalValidError.size(); i++) {
				String reason = globalValidError.get(i);
				if (reason != "") {
					errorString += "Global Parameter: " + (i + 1) + ", Reason: " + "\n" + globalValidError.get(i) + "\n";
				}
			}
			if (errorString.length() > initialLen) {
				errors.addAll(Arrays.asList(errorString.split("\n")));
			}
				
		}
		boolean first = true;
		for (int i = 0; i < actions.size(); i++) {
			ScriptGeneratorAction action = actions.get(i);
			if (!action.isValid()) {
				if (first) {
					errors.add("Action Errors:");
					first = false;
				}
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
		firePropertyChange(ScriptGeneratorProperties.ACTIONS_PROPERTY, null, actions);
	}
}
