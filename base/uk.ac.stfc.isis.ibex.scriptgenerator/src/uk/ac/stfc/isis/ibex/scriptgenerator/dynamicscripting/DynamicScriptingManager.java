package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class DynamicScriptingManager {
	
	private DynamicScriptingState state;
	private DynamicScriptingStateFactory stateFactory;
	
	public DynamicScriptingManager(DynamicScriptingStateFactory dynamicScriptingStateFactory) {
		this.stateFactory = dynamicScriptingStateFactory;
		setupNewState(dynamicScriptingStateFactory.getCurrentState());
	}
	
	public void playScript() throws DynamicScriptingException {
		state.play();
	}
	
	public void stopScript() {
		state.stop();
	}
	
	public void pauseScript() {
		state.pause();
	}

	public Boolean isScriptDynamic(Integer integer) {
		return state.isScriptDynamic(integer);
	}
	
	public Optional<ScriptGeneratorAction> getCurrentlyExecutingAction() {
		return state.getCurrentlyExecutingAction();
	}
	
	public DynamicScriptingStatus getDynamicScriptingStatus() {
		return state.getStatus();
	}
	
	private void setupNewState(DynamicScriptingState newState) {
		state = newState;
		state.addPropertyChangeListener(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, event -> {
			DynamicScriptingStatus nextStatus = (DynamicScriptingStatus) event.getNewValue();
			DynamicScriptingState nextState = stateFactory.changeState(nextStatus);
			handleStateChange(nextState);
		});
		state.start();
	}
	
	private void handleStateChange(DynamicScriptingState newState) {
		if (newState != state) {
			setupNewState(newState);
		}
	}
	
}
