package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class PausedState extends DynamicScriptingState {
	
	private DynamicScriptingNicosAdapter nicosAdapter;
	private DynamicScriptingModelAdapter modelAdapter;
	private Optional<ScriptGeneratorAction> currentlyExecutingAction;
	private Boolean stopRequested = false;

	public PausedState(DynamicScriptingNicosAdapter nicosAdapter, DynamicScriptingModelAdapter modelAdapter, Optional<ScriptGeneratorAction> currentlyExecutingAction, HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction) {
		super(dynamicScriptIdsToAction);
		this.nicosAdapter = nicosAdapter;
		this.modelAdapter = modelAdapter;
		this.currentlyExecutingAction = currentlyExecutingAction;
	}

	@Override
	public Optional<ScriptGeneratorAction> getCurrentlyExecutingAction() {
		return currentlyExecutingAction;
	}

	@Override
	public DynamicScriptingStatus getStatus() {
		return DynamicScriptingStatus.PAUSED;
	}
	
	@Override
	public void stop() {
		currentlyExecutingAction.ifPresentOrElse(action -> {
			if (action.wasPausedDuringExecution()) {
				stopRequested = true;
				nicosAdapter.stopExecution();
			} else {
				action.clearDynamicScriptingStatus();
				changeState(DynamicScriptingStatus.STOPPED);
			}
		}, () -> {
			changeState(DynamicScriptingStatus.STOPPED);
		});
	}
	
	@Override
	public void play() {
		changeState(DynamicScriptingStatus.PLAYING);
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
					changeState(DynamicScriptingStatus.PAUSED);
				}, () -> {
					changeState(DynamicScriptingStatus.STOPPED);
				});
			}, () -> {
				changeState(DynamicScriptingStatus.STOPPED);
			});
		}
		
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch (evt.getPropertyName()) {
			case DynamicScriptingProperties.SCRIPT_FINISHED_PROPERTY:
				handleFinished();
				break;
		}
	}

}
