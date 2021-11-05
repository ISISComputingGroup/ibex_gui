package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.Optional;

import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class ErrorState extends DynamicScriptingState {

	public ErrorState(ScriptGeneratorSingleton scriptGeneratorModel, NicosModel nicosModel, DynamicScriptingNicosFacade nicosFacade) {
		super(scriptGeneratorModel, nicosModel, nicosFacade);
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
	public DynamicScriptingState play() {
		return this;
	}

	@Override
	public DynamicScriptingState stop() {
		return this;
	}

}
