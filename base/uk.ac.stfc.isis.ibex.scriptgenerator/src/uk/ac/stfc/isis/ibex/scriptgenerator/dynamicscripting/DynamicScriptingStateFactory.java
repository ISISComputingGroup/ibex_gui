package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.HashMap;

public class DynamicScriptingStateFactory {
	
	private DynamicScriptingModelAdapter modelFacade;
	private DynamicScriptingNicosFacade nicosFacade;
	private DynamicScriptingState state;
	
	public DynamicScriptingStateFactory(DynamicScriptingModelAdapter modelFacade, DynamicScriptingNicosFacade nicosFacade, DynamicScriptingState state) {
		this.modelFacade = modelFacade;
		this.nicosFacade = nicosFacade;
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
		this.nicosFacade.removePropertyChangeListener(state);
		this.modelFacade.removePropertyChangeListener(state);
	}
	
	private DynamicScriptingState getNewState(DynamicScriptingStatus newStatus) {
		switch (newStatus) {
			case PLAYING:
				return new PlayingState(nicosFacade, modelFacade, new HashMap<>());
			case STOPPED:
				return new StoppedState(new HashMap<>());
			default:
				return new ErrorState(new HashMap<>());
		}
	}
	
	private void addStateAsPropertyListener() {
		this.nicosFacade.addPropertyChangeListener(state);
		this.modelFacade.addPropertyChangeListener(state);
	}

}
