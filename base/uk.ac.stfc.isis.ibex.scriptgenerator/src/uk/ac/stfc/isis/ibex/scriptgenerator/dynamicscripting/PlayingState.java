package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class PlayingState extends DynamicScriptingState {
	
	private Optional<ScriptGeneratorAction> currentlyExecutingAction;
	private Optional<Integer> nextScriptId = Optional.empty();
	PropertyChangeListener nextActionSetup;
	PropertyChangeListener actionExecutor;
	
	public PlayingState(DynamicScriptingNicosFacade nicosFacade, DynamicScriptingModelFacade generatorFacade, HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction) {
		super(nicosFacade, generatorFacade, dynamicScriptIdsToAction);
		nextActionSetup = new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setUpNextExecutingAction();
			}
		};
		this.nicosFacade.addPropertyChangeListener(DynamicScriptingProperties.SCRIPT_CHANGED_PROPERTY, nextActionSetup);
		actionExecutor = new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				executeScript();
			}
		};
		this.scriptGeneratorFacade.addPropertyChangeListener(DynamicScriptingProperties.NICOS_SCRIPT_GENERATED_PROPERTY, actionExecutor);
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

	@Override
	public void tearDownListeners() {
		this.nicosFacade.removePropertyChangeListener(DynamicScriptingProperties.SCRIPT_CHANGED_PROPERTY, nextActionSetup);
		this.scriptGeneratorFacade.removePropertyChangeListener(DynamicScriptingProperties.NICOS_SCRIPT_GENERATED_PROPERTY, actionExecutor);
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
					firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, this, new ErrorState(nicosFacade, scriptGeneratorFacade, dynamicScriptIdsToAction));
				});
				
			},  () -> {
				changeState(DynamicScriptingStatus.ERROR);
			}
		);
	}

}
