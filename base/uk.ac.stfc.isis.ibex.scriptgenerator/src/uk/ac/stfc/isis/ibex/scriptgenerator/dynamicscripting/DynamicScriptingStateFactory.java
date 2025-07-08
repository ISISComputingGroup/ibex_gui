package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.HashMap;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * A factory to create states for dynamic scripting.
 */
public class DynamicScriptingStateFactory {
	
	private DynamicScriptingModelAdapter modelAdapter;
	private DynamicScriptingNicosAdapter nicosAdapter;
	private DynamicScriptingState state;
	
	/**
	 * Create a factory with the given initial state and the injected script generator mode and nicos dependencies.
	 *
	 * @param modelFacade The script generator model that the created states depend on and listen to.
	 * @param nicosFacade The nicos adapter that the created states depend on and listen to.
	 * @param state The initial state to use.
	 */
	public DynamicScriptingStateFactory(DynamicScriptingModelAdapter modelFacade, DynamicScriptingNicosAdapter nicosFacade, DynamicScriptingState state) {
		this.modelAdapter = modelFacade;
		this.nicosAdapter = nicosFacade;
		this.state = state;
		addStateAsPropertyListener();
	}

	/**
	 * Create a new state using the given status, replacing the old state as a listener of the nicos and script generator model adapters with the new state.
	 *
	 * @param newStatus The status of the new state to create.
	 * @return The new state.
	 */
	public DynamicScriptingState changeState(DynamicScriptingStatus newStatus) {
		removeStateAsPropertyListener();
		state = getNewState(newStatus);
		addStateAsPropertyListener();
		return state;
	}
	
	/**
	 * @return the last state created by the factory.
	 */
	public DynamicScriptingState getCurrentState() {
		return state;
	}
	
	private void removeStateAsPropertyListener() {
		this.nicosAdapter.removePropertyChangeListener(state);
		this.modelAdapter.removePropertyChangeListener(state);
	}
	
	private DynamicScriptingState getNewState(DynamicScriptingStatus newStatus) {
		HashMap<Integer, ScriptGeneratorAction> dynamicScriptIds = state.getDynamicScriptIds();
		Optional<ScriptGeneratorAction> action = state.getCurrentlyExecutingAction();
		
		switch (newStatus) {
			case PLAYING:
				return new PlayingState(nicosAdapter, modelAdapter, action, dynamicScriptIds);
			case PAUSED:
				return new PausedState(nicosAdapter, modelAdapter, action, dynamicScriptIds, state.pauseComplete());
			case STOPPED:
				return new StoppedState(dynamicScriptIds);
			default:
				return new ErrorState(dynamicScriptIds);
		}
	}
	
	private void addStateAsPropertyListener() {
		this.nicosAdapter.addPropertyChangeListener(state);
		this.modelAdapter.addPropertyChangeListener(state);
	}

}
