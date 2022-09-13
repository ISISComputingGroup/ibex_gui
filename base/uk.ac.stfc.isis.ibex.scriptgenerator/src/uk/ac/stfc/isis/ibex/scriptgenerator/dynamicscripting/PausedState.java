package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * A state to use in the dynamic scripting state pattern for when the script has been paused.
 */
public class PausedState extends DynamicScriptingState {
	
	private DynamicScriptingNicosAdapter nicosAdapter;
	private DynamicScriptingModelAdapter modelAdapter;
	private Optional<ScriptGeneratorAction> currentlyExecutingAction;
	private Boolean pauseComplete;
	private Boolean stopRequested = false;
	private Boolean resumeRequested = false;

	/**
	 * Create a paused state with the required nicos and script generator model dependencies, the action that the script has been paused on and the IDs and actions of the dynamic scripts.
	 *
	 * @param nicosAdapter The nicos dependency to use to control the executions scripts.
	 * @param modelAdapter The script generator model dependency to use to interact with actions.
     * @param currentlyExecutingAction The action that the script is paused on.
	 * @param dynamicScriptIdsToAction Script ids and their associated actions that are used in dynamic scripting.
	 * @param pauseComplete Whether the pause is complete.
	 */
	public PausedState(DynamicScriptingNicosAdapter nicosAdapter, DynamicScriptingModelAdapter modelAdapter, Optional<ScriptGeneratorAction> currentlyExecutingAction, HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction, Boolean pauseComplete) {
		super(dynamicScriptIdsToAction);
		this.nicosAdapter = nicosAdapter;
		this.modelAdapter = modelAdapter;
		this.currentlyExecutingAction = currentlyExecutingAction;
		this.pauseComplete = pauseComplete;
	}

	/**
	 * @return the action that the script is paused on.
	 */
	@Override
	public Optional<ScriptGeneratorAction> getCurrentlyExecutingAction() {
		return currentlyExecutingAction;
	}

	@Override
	public DynamicScriptingStatus getStatus() {
		return DynamicScriptingStatus.PAUSED;
	}
	
	@Override
	public Boolean pauseComplete() {
		return pauseComplete;
	}
	
	/**
	 * If the action was being executed in nicos then request that it is stopped.
	 * If the action was not yet being executed then remove the paused status from the action and change state to stopped. 
	 */
	@Override
	public void stop() {
		currentlyExecutingAction.ifPresentOrElse(action -> {
			if (action.wasPausedDuringExecution()) {
				stopRequested = true;
				nicosAdapter.stopExecution();
				// See property change event for change of state
			} else {
				action.clearDynamicScriptingStatus();
				changeState(DynamicScriptingStatus.STOPPED);
			}
		}, () -> {
			changeState(DynamicScriptingStatus.STOPPED);
		});
	}
	
	/**
	 * Resume playing of the script.
	 */
	@Override
	public void play() {
		if (pauseComplete) {
			changeState(DynamicScriptingStatus.PLAYING);
		} else {
			resumeRequested = true;
			// See property change event for change of state
		}
	}
	
	/**
	 * Handle when an action has finished executing (after a stop has been requested).
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch (evt.getPropertyName()) {
			case DynamicScriptingProperties.SCRIPT_FINISHED_PROPERTY:
				handleFinished();
				break;
			case DynamicScriptingProperties.SCRIPT_PAUSED_PROPERTY:
				handlePaused();
				break;
			default: // do nothing;
				break;
		}
	}
	
	private void handleFinished() {
		currentlyExecutingAction.ifPresent(action -> {
			action.clearDynamicScriptingStatus();
		});
		if (stopRequested) {
			changeState(DynamicScriptingStatus.STOPPED);
		} else {
			currentlyExecutingAction.ifPresentOrElse(action -> {
				Optional<ScriptGeneratorAction> nextActionOptional = modelAdapter.getActionAfter(action);
				nextActionOptional.ifPresentOrElse(nextAction -> {
					nextAction.setPausedBeforeExecution();
					currentlyExecutingAction = nextActionOptional;
					pauseComplete = true;
					changeState(DynamicScriptingStatus.PAUSED);
				}, () -> {
					changeState(DynamicScriptingStatus.STOPPED);
				});
			}, () -> {
				changeState(DynamicScriptingStatus.STOPPED);
			});
		}
	}
	
	private void handlePaused() {
		pauseComplete = true;
		if (resumeRequested) {
			changeState(DynamicScriptingStatus.PLAYING);
		}
	}

}
