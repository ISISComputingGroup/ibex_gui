package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.List;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.nicos.NicosErrorState;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class PlayingState extends DynamicScriptingState {
	
	private static final String SCRIPT_STATUS_PROPERTY = "scriptStatus";
	
	private Optional<ScriptGeneratorAction> currentlyExecutingAction;
	private Optional<Integer> currentlyExecutingActionIndex;
	
	public PlayingState(ScriptGeneratorSingleton scriptGeneratorModel, NicosModel nicosModel) {
		super(scriptGeneratorModel, nicosModel);
		this.nicosModel.addPropertyChangeListener(SCRIPT_STATUS_PROPERTY, event -> {
			ScriptStatus newStatus = (ScriptStatus) event.getNewValue();
			ScriptStatus oldStatus = (ScriptStatus) event.getOldValue();
			if (oldStatus != ScriptStatus.IDLE && newStatus == ScriptStatus.IDLE) {
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
				String name = "Script Generator: " + currentlyExecutingAction.toString();
				String code = "print(" + currentlyExecutingActionIndex.get() + ")";
				executeScript(name, code);
			} catch (DynamicScriptingException e) {
				firePropertyChange(STATE_CHANGE_PROPERTY, this, new ErrorState(scriptGeneratorModel, nicosModel));
			}
		} else {
			firePropertyChange(STATE_CHANGE_PROPERTY, this, new StoppedState(scriptGeneratorModel, nicosModel));
		}
	}
	
	private void executeScript(String name, String code) throws DynamicScriptingException {
		if (!nicosInError()) {
    		var scriptToSend = new QueuedScript();
    		scriptToSend.setName(name);
        	scriptToSend.setCode(code);
        	nicosModel.sendScript(scriptToSend);
    	} else {
    		throw new DynamicScriptingException("Nicos in error, cannot play script.");
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
	public DynamicScriptingState pause() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DynamicScriptingState stop() {
		return new StoppedState(scriptGeneratorModel, nicosModel);
	}
	
	private boolean nicosInError() {
		var nicosError = nicosModel.getError();
		return nicosError != NicosErrorState.NO_ERROR && nicosError != NicosErrorState.SCRIPT_SEND_FAIL;
	}

}
