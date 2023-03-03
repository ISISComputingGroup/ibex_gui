package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * A state to use in the dynamic scripting state pattern for when a script is executing in the script generator.
 */
public class PlayingState extends DynamicScriptingState {
	
	private Optional<ScriptGeneratorAction> currentlyExecutingAction;
	private Optional<Integer> nextScriptId = Optional.empty();
	private DynamicScriptingNicosAdapter nicosAdapter;
	private DynamicScriptingModelAdapter modelAdapter;
	private Boolean pauseComplete = false;
	
	/**
	 * Create a playing state with the injected nicos and scripting generator model dependencies, an action to begin executing with and the script IDs and actions associated with dynamic scripting.
     * start() will need to be called to start the execution of actions.
	 *
	 * @param nicosAdapter The nicos dependency to use to control the executions scripts.
	 * @param modelAdapter The script generator model dependency to use to interact with actions and generate scripts.
     * @param currentlyExecutingAction The first action to execute.
	 * @param dynamicScriptIdsToAction Script ids and their associated actions that are used in dynamic scripting.
	 */
	public PlayingState(DynamicScriptingNicosAdapter nicosAdapter, DynamicScriptingModelAdapter modelAdapter, Optional<ScriptGeneratorAction> currentlyExecutingAction, HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction) {
		super(dynamicScriptIdsToAction);
		this.nicosAdapter = nicosAdapter;
		this.modelAdapter = modelAdapter;
		this.currentlyExecutingAction = currentlyExecutingAction;
	}
	
	/**
	 * @return the action currently executing.
	 */
	@Override
    public Optional<ScriptGeneratorAction> getCurrentlyExecutingAction() {
		return currentlyExecutingAction;
    }
	
	/**
	 * @return the action to execute after the current action.
	 */
	@Override
	public Optional<ScriptGeneratorAction> getNextExecutingAction() {
		if (currentlyExecutingAction.isPresent()) {
			var currentAction = currentlyExecutingAction.get();
			return modelAdapter.getActionAfter(currentAction);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public DynamicScriptingStatus getStatus() {
		return DynamicScriptingStatus.PLAYING;
	}
	
	@Override
	public Boolean pauseComplete() {
		return pauseComplete;
	}

	/**
	 * Request nicos to stop the execution of the current script, set the currently executing actions status to not executing and change the state to stopped.
	 */
	@Override
	public void stop() {
		nicosAdapter.stopExecution();
		currentlyExecutingAction.ifPresent(action -> {
			action.clearDynamicScriptingStatus();
		});
		changeState(DynamicScriptingStatus.STOPPED);
	}
	
	/**
	 * Request nicos to stop the execution of the current script, set the currently executing actions status to paused during exection and change the state to stopped.
	 */
	@Override
	public void pause() {
		nicosAdapter.pauseExecution();
		currentlyExecutingAction.ifPresent(action -> {
			action.setPausedDuringExecution();
		});
		changeState(DynamicScriptingStatus.PAUSED);
	}
	
	/**
	 * Start the playing state by setting up the first action to execute.
	 * The state will then handle future queueing of the next actions.
	 */
	@Override
	public void start() {
		setUpFirstExecutingAction();
	}
	
	/**
	 * Handle property changes from the nicos model for when a script is finished so we can generate a script for the next action to execute.
	 * Handle property changes from the script generator model for when a script is generated, so we can execute the action.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch (evt.getPropertyName()) {
			case DynamicScriptingProperties.SCRIPT_FINISHED_PROPERTY:
				setUpNextExecutingAction();
				break;
			case DynamicScriptingProperties.NICOS_SCRIPT_GENERATED_PROPERTY:
				executeScript();
				break;
			case DynamicScriptingProperties.SCRIPT_PAUSED_PROPERTY:
				handlePaused();
				break;
			default: // do nothing;
				break;
		}
	}
	
	private void handlePaused() {
		pauseComplete = true;
	}

	private void setUpFirstExecutingAction() {
		currentlyExecutingAction.ifPresentOrElse(action -> {
			if (action.wasPausedDuringExecution()) {
				nicosAdapter.resumeExecution();
			} else {
				refreshGeneratedScriptWithCurrentAction();
			}
		}, () -> {
			currentlyExecutingAction = modelAdapter.getFirstAction();
			refreshGeneratedScriptWithCurrentAction();
		});		
	}
	
	private void setUpNextExecutingAction() {
		currentlyExecutingAction.ifPresent(action -> {
			action.clearDynamicScriptingStatus();
		});
		currentlyExecutingAction = getNextExecutingAction();
		refreshGeneratedScriptWithCurrentAction();
	}
	
	private void refreshGeneratedScriptWithCurrentAction() {
		currentlyExecutingAction.ifPresentOrElse(action -> {
			if (!action.isValid()) {
				action.setPausedBeforeExecution();
				pauseComplete = true;
				changeState(DynamicScriptingStatus.PAUSED);
			} else {
				try {
					nextScriptId = modelAdapter.refreshGeneratedScript(action);
					nextScriptId.ifPresent(scriptId -> {
						dynamicScriptIdsToAction.put(scriptId, action);
					});
				} catch (DynamicScriptingException e) {
					changeState(DynamicScriptingStatus.ERROR);
				}
			}
		}, () -> {
			changeState(DynamicScriptingStatus.STOPPED);
		});
	}
	
	private void executeScript()  {
		Optional<DynamicScript> dynamicScript = modelAdapter.getDynamicScript();
		dynamicScript.ifPresentOrElse(script -> {
				script.getCode().ifPresentOrElse(code -> {
					try {
						nicosAdapter.executeAction(script.getName(), code);
						currentlyExecutingAction.ifPresent(action -> {
							action.setExecuting();
						});
					} catch (DynamicScriptingException e) {
						changeState(DynamicScriptingStatus.ERROR);
					}
				}, () -> {
					changeState(DynamicScriptingStatus.ERROR);
				});
			},  () -> {
				changeState(DynamicScriptingStatus.ERROR);
			}
		);
	}

}
