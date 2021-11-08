package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.HashMap;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class ErrorState extends DynamicScriptingState {

	public ErrorState(DynamicScriptingNicosFacade nicosFacade, DynamicScriptingModelFacade generatorFacade,  HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction) {
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
		return DynamicScriptingStatus.ERROR;
	}

	@Override
	public void play() {
		//Do nothing
	}

	@Override
	public void stop() {
		// Do nothing
	}

	@Override
	public void tearDownListeners() {
		// Do nothing
	}

}
