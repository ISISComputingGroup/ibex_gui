package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.HashMap;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class StoppedState extends DynamicScriptingState {

	public StoppedState(DynamicScriptingNicosFacade nicosFacade, DynamicScriptingModelFacade generatorFacade,  HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction) {
		super(nicosFacade, generatorFacade, dynamicScriptIdsToAction);
	}

	@Override
	public Optional<ScriptGeneratorAction> getCurrentlyExecutingAction() {
		return Optional.empty();
	}

	@Override
	public Optional<ScriptGeneratorAction> getNextExecutingAction() {
		return Optional.empty();
	}

	@Override
	public DynamicScriptingStatus getStatus() {
		return DynamicScriptingStatus.STOPPED;
	}

	@Override
	public DynamicScriptingState play() {
		return new PlayingState(nicosFacade, scriptGeneratorFacade, getClearScriptIdMap());
	}

	@Override
	public DynamicScriptingState stop() {
		return this;
	}

}
