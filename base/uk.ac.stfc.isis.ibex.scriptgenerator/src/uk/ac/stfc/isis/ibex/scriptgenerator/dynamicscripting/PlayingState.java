package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.List;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class PlayingState extends DynamicScriptingState {
	
	private Optional<ScriptGeneratorAction> currentlyExecutingAction;
	
	public PlayingState(ScriptGeneratorSingleton scriptGeneratorModel, NicosModel nicosModel) {
		super(scriptGeneratorModel, nicosModel);
		setUpFirstExecutingAction();
	}
	
	private void setUpFirstExecutingAction() {
		currentlyExecutingAction = scriptGeneratorModel.getAction(0);
	}

	@Override
	public Optional<ScriptGeneratorAction> getCurrentlyExecutingAction() {
		return currentlyExecutingAction;
	}

	@Override
	public Optional<ScriptGeneratorAction> getNextExecutingAction() {
		List<ScriptGeneratorAction> actions = scriptGeneratorModel.getActions();
		if (currentlyExecutingAction.isPresent()) {
			ScriptGeneratorAction action = currentlyExecutingAction.get();
			Integer actionIndex = actions.indexOf(action);
			// indexOf returns -1 if action is not in table
			if (actionIndex >= 0) {
				Integer nextActionIndex = actionIndex + 1;
				return scriptGeneratorModel.getAction(nextActionIndex);
			} else {
				return Optional.empty();
			}
		} else {
			return Optional.empty();
		}
	}

	@Override
	public DynamicScriptingStatus getStatus() {
		return DynamicScriptingStatus.PLAYING;
	}

	@Override
	public DynamicScriptingState play() {
		return this;
	}

	@Override
	public DynamicScriptingState pause() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DynamicScriptingState stop() {
		return new StoppedState(scriptGeneratorModel, nicosModel);
	}	

}
