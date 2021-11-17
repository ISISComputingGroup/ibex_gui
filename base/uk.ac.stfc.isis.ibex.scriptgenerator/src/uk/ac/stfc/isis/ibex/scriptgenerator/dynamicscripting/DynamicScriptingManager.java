package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * Manages the dynamic scripting functionality of the script generator, acting as a facade.
 */
public class DynamicScriptingManager {
	
	private DynamicScriptingState state;
	private DynamicScriptingStateFactory stateFactory;
	
	/**
	 * Create a new manager with the given factory which will be used to create states.
	 * 
	 * @param dynamicScriptingStateFactory The factory used to create states.
	 */
	public DynamicScriptingManager(DynamicScriptingStateFactory dynamicScriptingStateFactory) {
		this.stateFactory = dynamicScriptingStateFactory;
		setupNewState(dynamicScriptingStateFactory.getCurrentState());
	}
	
	/**
	 * Play the script. Functionality depends on the current state of the dynamic scripting.
	 * 
	 * @throws DynamicScriptingException If executing the script causes an exception.
	 */
	public void playScript() throws DynamicScriptingException {
		state.play();
	}
	
	/**
	 * Stops the script. Functionality depends on the current state of the dynamic scripting.
	 */
	public void stopScript() {
		state.stop();
	}
	
	/**
	 * Pauses the script. Functionality depends on the current state of the dynamic scripting.
	 */
	public void pauseScript() {
		state.pause();
	}

	/**
	 * Is the given script ID a dynamic script?
	 * 
	 * @param scriptId The ID to check.
	 * @return true if script is dynamic, false if not.
	 */
	public Boolean isScriptDynamic(Integer scriptId) {
		return state.isScriptDynamic(scriptId);
	}
	
	/**
	 * @return An optional of the currently executing action or empty if nothing is executing.
	 */
	public Optional<ScriptGeneratorAction> getCurrentlyExecutingAction() {
		return state.getCurrentlyExecutingAction();
	}
	
	/**
	 * @return The status of dynamic scripting currently e.g. playing, paused, stopped or error.
	 */
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
