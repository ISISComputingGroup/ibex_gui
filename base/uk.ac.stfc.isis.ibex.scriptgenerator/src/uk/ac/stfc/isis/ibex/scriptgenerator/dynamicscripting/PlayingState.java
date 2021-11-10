package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class PlayingState extends DynamicScriptingState {
	
	private Optional<ScriptGeneratorAction> currentlyExecutingAction;
	private Optional<Integer> nextScriptId = Optional.empty();
	private DynamicScriptingNicosAdapter nicosAdapter;
	private DynamicScriptingModelAdapter modelAdapter;
	
	public PlayingState(DynamicScriptingNicosAdapter nicosAdapter, DynamicScriptingModelAdapter modelAdapter, HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction) {
		super(dynamicScriptIdsToAction);
		this.nicosAdapter = nicosAdapter;
		this.modelAdapter = modelAdapter;
		setUpFirstExecutingAction();
	}
	
	@Override
    public Optional<ScriptGeneratorAction> getCurrentlyExecutingAction() {
		return currentlyExecutingAction;
    }
	
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
	public void stop() {
		nicosAdapter.stopExecution();
	}
	
	private void handleStop() {
		currentlyExecutingAction.ifPresent(action -> {
			action.setNotExecuting();
		});
		changeState(DynamicScriptingStatus.STOPPED);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch (evt.getPropertyName()) {
			case DynamicScriptingProperties.SCRIPT_CHANGED_PROPERTY:
				setUpNextExecutingAction();
				break;
			case DynamicScriptingProperties.NICOS_SCRIPT_GENERATED_PROPERTY:
				executeScript();
				break;
			case DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY:
				handleStop();
				break;
		}
	}
	
	private void setUpFirstExecutingAction() {
		currentlyExecutingAction = modelAdapter.getFirstAction();
		refreshGeneratedScriptWithCurrentAction();
	}
	
	private void setUpNextExecutingAction() {
		currentlyExecutingAction.ifPresent(action -> {
			action.setNotExecuting();
		});
		currentlyExecutingAction = getNextExecutingAction();
		refreshGeneratedScriptWithCurrentAction();
	}
	
	private void refreshGeneratedScriptWithCurrentAction() {
		if (currentlyExecutingAction.isPresent()) {
			try {
				ScriptGeneratorAction action = currentlyExecutingAction.get();
				nextScriptId = modelAdapter.refreshGeneratedScript(action);
				nextScriptId.ifPresent(scriptId -> {
					dynamicScriptIdsToAction.put(scriptId, action);
				});
			} catch (DynamicScriptingException e) {
				changeState(DynamicScriptingStatus.ERROR);
			}
		} else {
			changeState(DynamicScriptingStatus.STOPPED);
		}
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
