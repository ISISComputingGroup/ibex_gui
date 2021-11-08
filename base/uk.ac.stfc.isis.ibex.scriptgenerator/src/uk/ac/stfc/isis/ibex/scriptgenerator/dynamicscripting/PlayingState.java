package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class PlayingState extends DynamicScriptingState {
	
	private Optional<ScriptGeneratorAction> currentlyExecutingAction;
	private Optional<Integer> nextScriptId = Optional.empty();
	
	public PlayingState(DynamicScriptingNicosFacade nicosFacade, DynamicScriptingModelFacade generatorFacade, HashSet<Integer> dynamicScriptIds) {
		super(nicosFacade, generatorFacade, dynamicScriptIds);
		this.nicosFacade.addPropertyChangeListener(DynamicScriptingProperties.SCRIPT_CHANGED_PROPERTY, event -> {
			setUpNextExecutingAction();
		});
		this.scriptGeneratorFacade.addPropertyChangeListener(DynamicScriptingProperties.NICOS_SCRIPT_GENERATED_PROPERTY, event -> {
			executeScript();
		});
		setUpFirstExecutingAction();
	}
	
	private void setUpFirstExecutingAction() {
		currentlyExecutingAction = scriptGeneratorFacade.getAction(0);
		refreshGeneratedScriptWithCurrentAction();
	}
	
	private void setUpNextExecutingAction() {
		currentlyExecutingAction = getNextExecutingAction();
		refreshGeneratedScriptWithCurrentAction();
	}
	
	private void refreshGeneratedScriptWithCurrentAction() {
		if (currentlyExecutingAction.isPresent()) {
			try {
				ScriptGeneratorAction action = currentlyExecutingAction.get();
				nextScriptId = scriptGeneratorFacade.refreshGeneratedScript(action);
				nextScriptId.ifPresent(scriptId -> {
					dynamicScriptIds.add(scriptId);
				});
			} catch (DynamicScriptingException e) {
				firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, this, new ErrorState(nicosFacade, scriptGeneratorFacade, dynamicScriptIds));
			}
		} else {
			firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, this, new StoppedState(nicosFacade, scriptGeneratorFacade, dynamicScriptIds));
		}
	}
	
	private void executeScript()  {
		Optional<DynamicScript> dynamicScript = scriptGeneratorFacade.getDynamicScript();
		dynamicScript.ifPresentOrElse(script -> {
				script.getCode().ifPresentOrElse(code -> {
					try {
						nicosFacade.executeAction(script.getName(), code);
					} catch (DynamicScriptingException e) {
						firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, this, new ErrorState(nicosFacade, scriptGeneratorFacade, dynamicScriptIds));
					}
				}, () -> {
					firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, this, new ErrorState(nicosFacade, scriptGeneratorFacade, dynamicScriptIds));
				});
				
			},  () -> {
				firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, this, new ErrorState(nicosFacade, scriptGeneratorFacade, dynamicScriptIds));
			}
		);
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
		return new StoppedState(nicosFacade, scriptGeneratorFacade, dynamicScriptIds);
	}

}
