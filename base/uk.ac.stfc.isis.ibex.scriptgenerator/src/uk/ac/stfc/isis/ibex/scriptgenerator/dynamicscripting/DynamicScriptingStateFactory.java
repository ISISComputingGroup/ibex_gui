package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.HashMap;

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
		switch (newStatus) {
			case PLAYING:
				return new PlayingState(nicosAdapter, modelAdapter, new HashMap<>());
			case STOPPED:
				return new StoppedState(new HashMap<>());
			default:
				return new ErrorState(new HashMap<>());
		}
	}
	
	private void addStateAsPropertyListener() {
		this.nicosAdapter.addPropertyChangeListener(state);
		this.modelAdapter.addPropertyChangeListener(state);
	}

}
