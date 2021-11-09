package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class DynamicScriptingManager {
	
	private DynamicScriptingState dynamicScriptingState;
	private DynamicScriptingStateFactory dynamicScriptingStateFactory;
	
	public DynamicScriptingManager(DynamicScriptingStateFactory dynamicScriptingStateFactory) {
		this.dynamicScriptingStateFactory = dynamicScriptingStateFactory;
		setupNewState(dynamicScriptingStateFactory.getCurrentState());
	}
	
	public void playScript() throws DynamicScriptingException {
		dynamicScriptingState.play();
	}
	
	public void stopScript() {
		dynamicScriptingState.stop();
	}
	
	private void setupNewState(DynamicScriptingState newState) {
		dynamicScriptingState = newState;
		dynamicScriptingState.addPropertyChangeListener(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, event -> {
			DynamicScriptingStatus nextStatus = (DynamicScriptingStatus) event.getNewValue();
			DynamicScriptingState nextState = dynamicScriptingStateFactory.changeState(nextStatus);
			handleStateChange(nextState);
		});
	}
	
	private void handleStateChange(DynamicScriptingState newState) {
		if (newState != dynamicScriptingState) {
			setupNewState(newState);
		}
	}
	
	public Optional<ScriptGeneratorAction> getCurrentlyExecutingAction() {
		return dynamicScriptingState.getCurrentlyExecutingAction();
	}
	
	public DynamicScriptingStatus getDynamicScriptingStatus() {
		return dynamicScriptingState.getStatus();
	}

	public Boolean isScriptDynamic(Integer integer) {
		return dynamicScriptingState.isScriptDynamic(integer);
	}
	
}
