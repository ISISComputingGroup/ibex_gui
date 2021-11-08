package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.HashMap;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class PlayingState extends DynamicScriptingState {
	
	private Optional<ScriptGeneratorAction> currentlyExecutingAction;
	private Optional<Integer> nextScriptId = Optional.empty();
	
	public PlayingState(DynamicScriptingNicosFacade nicosFacade, DynamicScriptingModelFacade generatorFacade, HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction) {
		super(nicosFacade, generatorFacade, dynamicScriptIdsToAction);
		this.nicosFacade.addPropertyChangeListener(DynamicScriptingProperties.SCRIPT_CHANGED_PROPERTY, event -> {
			setUpNextExecutingAction();
		});
		this.scriptGeneratorFacade.addPropertyChangeListener(DynamicScriptingProperties.NICOS_SCRIPT_GENERATED_PROPERTY, event -> {
			executeScript();
		});
		setUpFirstExecutingAction();
	}
	
	private void setUpFirstExecutingAction() {
		currentlyExecutingAction = scriptGeneratorFacade.getFirstAction();
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
					dynamicScriptIdsToAction.put(scriptId, action);
				});
			} catch (DynamicScriptingException e) {
				firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, this, new ErrorState(nicosFacade, scriptGeneratorFacade, dynamicScriptIdsToAction));
			}
		} else {
			firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, this, new StoppedState(nicosFacade, scriptGeneratorFacade, getClearScriptIdMap()));
		}
	}
	
	private void executeScript()  {
		Optional<DynamicScript> dynamicScript = scriptGeneratorFacade.getDynamicScript();
		dynamicScript.ifPresentOrElse(script -> {
				script.getCode().ifPresentOrElse(code -> {
					try {
						nicosFacade.executeAction(script.getName(), code);
						currentlyExecutingAction.ifPresent(action -> {
							action.setExecuting();
						});
					} catch (DynamicScriptingException e) {
						firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, this, new ErrorState(nicosFacade, scriptGeneratorFacade, dynamicScriptIdsToAction));
					}
				}, () -> {
					firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, this, new ErrorState(nicosFacade, scriptGeneratorFacade, dynamicScriptIdsToAction));
				});
				
			},  () -> {
				firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, this, new ErrorState(nicosFacade, scriptGeneratorFacade, dynamicScriptIdsToAction));
			}
		);
	}

	@Override
	public Optional<ScriptGeneratorAction> getCurrentlyExecutingAction() {
		return currentlyExecutingAction;
	}

	@Override
	public Optional<ScriptGeneratorAction> getNextExecutingAction() {
		if (currentlyExecutingAction.isPresent()) {
			var currentAction = currentlyExecutingAction.get();
			return scriptGeneratorFacade.getActionAfter(currentAction);
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
	public DynamicScriptingState stop() {
		return new StoppedState(nicosFacade, scriptGeneratorFacade, getClearScriptIdMap());
	}

}
