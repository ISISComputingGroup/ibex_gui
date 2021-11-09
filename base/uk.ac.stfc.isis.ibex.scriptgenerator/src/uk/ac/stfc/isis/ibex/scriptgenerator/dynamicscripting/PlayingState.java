package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class PlayingState extends DynamicScriptingState {
	
	private Optional<ScriptGeneratorAction> currentlyExecutingAction;
	private Optional<Integer> nextScriptId = Optional.empty();
	protected DynamicScriptingNicosFacade nicosFacade;
	protected DynamicScriptingModelFacade scriptGeneratorFacade;
	
	public PlayingState(DynamicScriptingNicosFacade nicosFacade, DynamicScriptingModelFacade generatorFacade, HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction) {
		super(dynamicScriptIdsToAction);
		this.nicosFacade = nicosFacade;
		this.scriptGeneratorFacade = generatorFacade;
		setUpFirstExecutingAction();
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
	public void play() {
		// Do nothing
	}

	@Override
	public void stop() {
		changeState(DynamicScriptingStatus.STOPPED);
	}
	
	private void setUpFirstExecutingAction() {
		currentlyExecutingAction = scriptGeneratorFacade.getFirstAction();
		refreshGeneratedScriptWithCurrentAction();
	}
	
	private void setUpNextExecutingAction() {
		currentlyExecutingAction.ifPresent(action -> {
			action.setNotExecuting();
		});
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
				changeState(DynamicScriptingStatus.ERROR);
			}
		} else {
			changeState(DynamicScriptingStatus.STOPPED);
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
						changeState(DynamicScriptingStatus.ERROR);
					}
				}, () -> {
					changeState(DynamicScriptingStatus.ERROR);
				});
				
			},  () -> {
				changeState(DynamicScriptingStatus.ERROR);
			}
		);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch (evt.getPropertyName()) {
			case DynamicScriptingProperties.SCRIPT_CHANGED_PROPERTY:
				setUpNextExecutingAction();
				break;
			case DynamicScriptingProperties.NICOS_SCRIPT_GENERATED_PROPERTY:
				executeScript();
				break;
		}
	}

}
