package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.Optional;

import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class StoppedState extends DynamicScriptingState {

	public StoppedState(ScriptGeneratorSingleton scriptGeneratorModel, NicosModel nicosModel) {
		super(scriptGeneratorModel, nicosModel);
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
		return new PlayingState(scriptGeneratorModel, nicosModel);
	}

	@Override
	public DynamicScriptingState pause() {
		return this;
	}

	@Override
	public DynamicScriptingState stop() {
		return this;
	}

}
