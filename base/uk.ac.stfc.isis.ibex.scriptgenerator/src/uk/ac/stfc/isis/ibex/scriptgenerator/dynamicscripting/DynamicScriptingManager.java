package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class DynamicScriptingManager {
	
	private DynamicScriptingState dynamicScriptingState;
	
	public DynamicScriptingManager(DynamicScriptingState dynamicScriptingState) {
		handleStateChange(dynamicScriptingState);
	}
	
	public void playScript() throws DynamicScriptingException {
		DynamicScriptingState newState = dynamicScriptingState.play();
		handleStateChange(newState);
	}
	
	public void stopScript() {
		DynamicScriptingState newState = dynamicScriptingState.stop();
		handleStateChange(newState);
	}
	
	private void handleStateChange(DynamicScriptingState newState) {
		if (newState != dynamicScriptingState) {
			dynamicScriptingState = newState;
			dynamicScriptingState.addPropertyChangeListener(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, event -> {
				DynamicScriptingState nextState = (DynamicScriptingState) event.getNewValue();
				handleStateChange(nextState);
			});
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
