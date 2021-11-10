package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.HashMap;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class DynamicScriptingStateFactory {
	
	private DynamicScriptingModelAdapter modelAdapter;
	private DynamicScriptingNicosAdapter nicosAdapter;
	private DynamicScriptingState state;
	
	public DynamicScriptingStateFactory(DynamicScriptingModelAdapter modelFacade, DynamicScriptingNicosAdapter nicosFacade, DynamicScriptingState state) {
		this.modelAdapter = modelFacade;
		this.nicosAdapter = nicosFacade;
		this.state = state;
		addStateAsPropertyListener();
	}

	public DynamicScriptingState changeState(DynamicScriptingStatus newStatus) {
		removeStateAsPropertyListener();
		state = getNewState(newStatus);
		addStateAsPropertyListener();
		return state;
	}
	
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
				return new PausedState(nicosAdapter, action, dynamicScriptIds);
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
