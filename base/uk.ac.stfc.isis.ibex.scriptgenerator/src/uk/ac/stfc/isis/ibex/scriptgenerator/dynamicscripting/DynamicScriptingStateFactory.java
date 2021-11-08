package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.HashMap;

public class DynamicScriptingStateFactory {

	public static DynamicScriptingState getNewState(DynamicScriptingState oldState, DynamicScriptingStatus newStatus) {
		DynamicScriptingModelFacade generatorFacade = oldState.getModelFacade();
		DynamicScriptingNicosFacade nicosFacade = oldState.getNicosFacade();
		switch (newStatus) {
			case PLAYING:
				return new PlayingState(nicosFacade, generatorFacade, new HashMap<>());
			case STOPPED:
				return new StoppedState(nicosFacade, generatorFacade, new HashMap<>());
			default:
				return new ErrorState(nicosFacade, generatorFacade, new HashMap<>());
		}
	}

}
