package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.HashSet;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class DynamicScriptingManager {
	
	private HashSet<Integer> dynamicScriptIds = new HashSet<>();
	private DynamicScriptingState dynamicScriptingState;
	
	public DynamicScriptingManager(ScriptGeneratorSingleton scriptGeneratorModel, NicosModel nicosModel, DynamicScriptingNicosFacade nicosFacade, DynamicScriptingGeneratorFacade generatorFacade) {
		dynamicScriptingState = new StoppedState(scriptGeneratorModel, nicosModel, nicosFacade, generatorFacade);
		handleStateChange(dynamicScriptingState);
	}
	
	public void playScript() throws DynamicScriptingException {
		DynamicScriptingState newState = dynamicScriptingState.play();
		handleStateChange(newState);
	}
	
	public void stopScript() {
		DynamicScriptingState newState = dynamicScriptingState.stop();
		handleStateChange(newState);
	}
	
	private void handleStateChange(DynamicScriptingState newState) {
		if (newState != dynamicScriptingState) {
			dynamicScriptingState = newState;
			dynamicScriptingState.addPropertyChangeListener(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, event -> {
				DynamicScriptingState nextState = (DynamicScriptingState) event.getNewValue();
				handleStateChange(nextState);
			});
			dynamicScriptingState.addPropertyChangeListener(DynamicScriptingProperties.NEW_SCRIPT_ID_PROPERTY, event -> {
				Optional<Integer> optionalDynamicScriptId = (Optional<Integer>) event.getNewValue();
				if (optionalDynamicScriptId.isPresent()) {
					Integer scriptId = optionalDynamicScriptId.get();
					dynamicScriptIds.add(scriptId);
				}
			});
		}
	}
	
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
