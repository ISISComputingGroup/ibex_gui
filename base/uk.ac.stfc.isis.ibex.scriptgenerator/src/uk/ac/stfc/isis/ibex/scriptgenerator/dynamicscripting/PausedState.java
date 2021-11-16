package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class PausedState extends DynamicScriptingState {
	
	private DynamicScriptingNicosAdapter nicosAdapter;
	private Optional<ScriptGeneratorAction> currentlyExecutingAction;

	public PausedState(DynamicScriptingNicosAdapter nicosAdapter, Optional<ScriptGeneratorAction> currentlyExecutingAction, HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction) {
		super(dynamicScriptIdsToAction);
		this.nicosAdapter = nicosAdapter;
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
		nicosAdapter.stopExecution();
	}
	
	@Override
	public void play() {
		changeState(DynamicScriptingStatus.PLAYING);
	}
	
	private void handleStop() {
		currentlyExecutingAction.ifPresent(action -> {
			action.clearDynamicScriptingStatus();
		});
		changeState(DynamicScriptingStatus.STOPPED);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch (evt.getPropertyName()) {
			case DynamicScriptingProperties.SCRIPT_STOPPED_PROPERTY:
				handleStop();
				break;
		}
	}

}
