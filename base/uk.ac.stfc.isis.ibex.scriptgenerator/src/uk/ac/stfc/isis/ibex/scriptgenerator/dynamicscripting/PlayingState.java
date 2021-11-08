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
	private Optional<Integer> nextScriptId = Optional.empty();
	
	public PlayingState(DynamicScriptingNicosFacade nicosFacade, DynamicScriptingModelFacade generatorFacade) {
		super(nicosFacade, generatorFacade);
		this.nicosFacade.addPropertyChangeListener(DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY, event -> {
			ScriptStatus newStatus = (ScriptStatus) event.getNewValue();
			ScriptStatus oldStatus = (ScriptStatus) event.getOldValue();
			if (newStatus == ScriptStatus.IDLE && oldStatus == ScriptStatus.RUNNING) {
				setUpNextExecutingAction();
			}
		});
		this.scriptGeneratorFacade.addPropertyChangeListener(DynamicScriptingProperties.NICOS_SCRIPT_GENERATED_PROPERTY, event -> {
			executeScript();
		});
		setUpFirstExecutingAction();
	}
	
	private void setUpFirstExecutingAction() {
		currentlyExecutingActionIndex = Optional.of(0);
		currentlyExecutingAction = scriptGeneratorFacade.getAction(currentlyExecutingActionIndex.get());
		refreshGeneratedScriptWithCurrentAction();
	}
	
	private void setUpNextExecutingAction() {
		currentlyExecutingActionIndex = Optional.of(currentlyExecutingActionIndex.get() + 1);
		currentlyExecutingAction = getNextExecutingAction();
		refreshGeneratedScriptWithCurrentAction();
	}
	
	private void refreshGeneratedScriptWithCurrentAction() {
		if (currentlyExecutingAction.isPresent()) {
			try {
				ScriptGeneratorAction action = currentlyExecutingAction.get();
				nextScriptId = scriptGeneratorFacade.refreshGeneratedScript(action);
			} catch (DynamicScriptingException e) {
				firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, this, new ErrorState(nicosFacade, scriptGeneratorFacade));
			}
		} else {
			firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, this, new StoppedState(nicosFacade, scriptGeneratorFacade));
		}
	}
	
	private void executeScript()  {
		Optional<Integer> scriptId = scriptGeneratorFacade.getScriptId();
		Optional<String> scriptName = scriptGeneratorFacade.getScriptName();
		Optional<String> scriptCode = scriptGeneratorFacade.getScriptCode();
		if (scriptName.isPresent() && scriptCode.isPresent() && scriptId.isPresent()) {
			try {
				nicosFacade.executeAction(scriptName.get(), scriptCode.get());
				firePropertyChange(DynamicScriptingProperties.NEW_SCRIPT_ID_PROPERTY, Optional.empty(), nextScriptId);
			} catch (DynamicScriptingException e) {
				firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, this, new ErrorState(nicosFacade, scriptGeneratorFacade));
			}
		} else {
			firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, this, new ErrorState(nicosFacade, scriptGeneratorFacade));
		}
	}

	@Override
	public Optional<ScriptGeneratorAction> getCurrentlyExecutingAction() {
		return currentlyExecutingAction;
	}

	@Override
	public Optional<ScriptGeneratorAction> getNextExecutingAction() {
		List<ScriptGeneratorAction> actions = scriptGeneratorFacade.getActions();
		Optional<ScriptGeneratorAction> nextAction = Optional.empty();
		if (currentlyExecutingAction.isPresent()) {
			ScriptGeneratorAction currentAction = currentlyExecutingAction.get();
			Integer actionIndex = actions.indexOf(currentAction);
			// indexOf returns -1 if action is not in table
			if (actionIndex >= 0) {
				Integer nextActionIndex = actionIndex + 1;
				nextAction = scriptGeneratorFacade.getAction(nextActionIndex);
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
		return new StoppedState(nicosFacade, scriptGeneratorFacade);
	}

}
