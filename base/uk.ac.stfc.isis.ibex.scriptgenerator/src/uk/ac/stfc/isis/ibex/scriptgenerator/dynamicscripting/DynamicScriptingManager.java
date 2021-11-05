package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.HashSet;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class DynamicScriptingManager {
	
	private HashSet<Integer> dynamicScriptIds = new HashSet<>();
	private DynamicScriptingState dynamicScriptingState;
	
	public DynamicScriptingManager(ScriptGeneratorSingleton scriptGeneratorModel, NicosModel nicosModel, DynamicScriptingNicosFacade nicosFacade) {
		dynamicScriptingState = new StoppedState(scriptGeneratorModel, nicosModel, nicosFacade);
		handleStateChange(dynamicScriptingState);
	}
	
	public void playScript() throws DynamicScriptingException {
		dynamicScriptingState.play();
	}
	
	public void stopScript() {
		dynamicScriptingState.stop();
	}
	
	private void handleStateChange(DynamicScriptingState newState) {
		dynamicScriptingState = newState;
		dynamicScriptingState.addPropertyChangeListener(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, event -> {
			DynamicScriptingState nextState = (DynamicScriptingState) event.getNewValue();
			handleStateChange(nextState);
		});
	}
	
//	private void refreshGeneratedScriptWithSingleAction(ScriptGeneratorAction action) throws DynamicScriptingException {
//		try {
//			Optional<Integer> dynamicScriptId = scriptGeneratorModel.refreshGeneratedScript(action);
//			dynamicScriptId.ifPresent(scriptId -> {
//				dynamicScriptIds.add(scriptId);
//			});
//		} catch (InvalidParamsException e) {
//			throw new DynamicScriptingException("Invalid params, cannot play script");
//		} catch (UnsupportedLanguageException e) {
//			throw new DynamicScriptingException("Unsupported language selected, cannot play script");
//		} catch (NoScriptDefinitionSelectedException e) {
//			throw new DynamicScriptingException("No script definition has been selected, cannot play script");
//		}
//	}
	
	public Optional<ScriptGeneratorAction> getCurrentlyExecutingAction() {
		return dynamicScriptingState.getCurrentlyExecutingAction();
	}
	
	public DynamicScriptingStatus getDynamicScriptingStatus() {
		return dynamicScriptingState.getStatus();
	}

	public Boolean isScriptDynamic(Integer integer) {
		return dynamicScriptIds.contains(integer);
	}
	

}
