package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.model.HasStatus;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * An abstract base class for the states of dynamic scripting to inherit from.
 */
public abstract class DynamicScriptingState extends ModelObject implements HasStatus<DynamicScriptingStatus>, PropertyChangeListener {
	
	/**
	 * A collection of dynamic script ids and their associated actions.
	 */
	protected HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction;
	
	/**
	 * Create a dynamic scripting state with the given ids and their associated actions.
	 *
	 * @param dynamicScriptIdsToAction Script ids and their associated actions that are used in dynamic scripting.
	 */
	public DynamicScriptingState(HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction) {
		this.dynamicScriptIdsToAction = dynamicScriptIdsToAction;
	}
	
	/**
	 * Check if the given script ID is a dynamic script.
	 * 
	 * @param scriptId The ID of the script to check.
	 */
	public Boolean isScriptDynamic(Integer scriptId) {
		return dynamicScriptIdsToAction.containsKey(scriptId);
	}
	
	/**
	 * Get the ids and associated actions of the dynamic scripts.
	 */
	public HashMap<Integer, ScriptGeneratorAction> getDynamicScriptIds() {
		return dynamicScriptIdsToAction;
	}
	
	/**
	 * Default function for getting the currently executing action (An optional empty).
	 *
	 * @return An empty optional.
	 */
	public Optional<ScriptGeneratorAction> getCurrentlyExecutingAction() {
		return Optional.empty();
	}

	/**
	 * Default function for getting the next action to execute (An optional empty).
	 *
	 * @return An empty optional.
	 */
	public Optional<ScriptGeneratorAction> getNextExecutingAction() {
		return Optional.empty();
	}
	
	/**
	 * Default function to play from this state (does nothing).
	 */
	public void play() { };
	
	/**
	 * Default function to play from this state (does nothing).
	 */
	public void stop() { };
	
	/**
	 * Default function to play from this state (does nothing).
	 */
	public void pause() { };
	
	/**
	 * Default function to start this state (does nothing).
	 */
	public void start() { };
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) { /* Default function does nothing */ };
	
	/**
	 * Get the status of the dynamic scripting in this state.
	 */
	public abstract DynamicScriptingStatus getStatus();
	
	/**
	 * Get whether a pause has been complete.
	 */
	public abstract Boolean pauseComplete();
	
	/**
	 * Fire a property change to indicate we need to change from this state to a state with the new status.
	 *
	 * @param newStatus The status of the new state to indicate what state needs changing to.
	 */
	protected void changeState(DynamicScriptingStatus newStatus) {
		firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, this, newStatus);
	}
	
}
