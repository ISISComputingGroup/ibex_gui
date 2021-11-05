package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.List;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class PlayingState extends DynamicScriptingState {
	
	private Optional<ScriptGeneratorAction> currentlyExecutingAction;
	private Optional<Integer> currentlyExecutingActionIndex;
	
	public PlayingState(ScriptGeneratorSingleton scriptGeneratorModel, NicosModel nicosModel, DynamicScriptingNicosFacade nicosFacade) {
		super(scriptGeneratorModel, nicosModel, nicosFacade);
		this.nicosFacade.addPropertyChangeListener(DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY, event -> {
			ScriptStatus newStatus = (ScriptStatus) event.getNewValue();
			ScriptStatus oldStatus = (ScriptStatus) event.getOldValue();
			if (newStatus == ScriptStatus.IDLE && oldStatus == ScriptStatus.RUNNING) {
				setUpNextExecutingAction();
			}
		});
		setUpFirstExecutingAction();
	}
	
	private void setUpFirstExecutingAction() {
		currentlyExecutingActionIndex = Optional.of(0);
		currentlyExecutingAction = scriptGeneratorModel.getAction(currentlyExecutingActionIndex.get());
		executeCurrentAction();
	}
	
	private void setUpNextExecutingAction() {
		currentlyExecutingActionIndex = Optional.of(currentlyExecutingActionIndex.get() + 1);
		currentlyExecutingAction = getNextExecutingAction();
		executeCurrentAction();
	}
	
	private void executeCurrentAction()  {
		if (currentlyExecutingAction.isPresent()) {
			try {
				ScriptGeneratorAction action = currentlyExecutingAction.get();
				nicosFacade.executeAction(action);
			} catch (DynamicScriptingException e) {
				firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, this, new ErrorState(scriptGeneratorModel, nicosModel, nicosFacade));
			}
		} else {
			firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, this, new StoppedState(scriptGeneratorModel, nicosModel, nicosFacade));
		}
	}

	@Override
	public Optional<ScriptGeneratorAction> getCurrentlyExecutingAction() {
		return currentlyExecutingAction;
	}

	@Override
	public Optional<ScriptGeneratorAction> getNextExecutingAction() {
		List<ScriptGeneratorAction> actions = scriptGeneratorModel.getActions();
		Optional<ScriptGeneratorAction> nextAction = Optional.empty();
		if (currentlyExecutingAction.isPresent()) {
			ScriptGeneratorAction currentAction = currentlyExecutingAction.get();
			Integer actionIndex = actions.indexOf(currentAction);
			// indexOf returns -1 if action is not in table
			if (actionIndex >= 0) {
				Integer nextActionIndex = actionIndex + 1;
				nextAction = scriptGeneratorModel.getAction(nextActionIndex);
			}
		}
		return nextAction;
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
	public DynamicScriptingState stop() {
		return new StoppedState(scriptGeneratorModel, nicosModel, nicosFacade);
	}

}
