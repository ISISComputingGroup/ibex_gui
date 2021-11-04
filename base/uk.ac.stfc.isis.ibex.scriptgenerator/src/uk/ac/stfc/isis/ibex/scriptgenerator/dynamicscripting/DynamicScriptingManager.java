package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.HashSet;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.NoScriptDefinitionSelectedException;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.UnsupportedLanguageException;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class DynamicScriptingManager {
	
	private ScriptGeneratorSingleton scriptGeneratorModel;
	private Optional<ScriptGeneratorAction> currentlyExecutingAction;
	private HashSet<Integer> dynamicScriptIds = new HashSet<>();
	private DynamicScriptingState dynamicScriptingState;
	
	public DynamicScriptingManager(ScriptGeneratorSingleton scriptGeneratorModel) {
		this.scriptGeneratorModel = scriptGeneratorModel;
	}
	
	public synchronized void playScript() throws DynamicScriptingException {
		currentlyExecutingAction = scriptGeneratorModel.getAction(0);
		currentlyExecutingAction.ifPresent(action -> {
			try {
				refreshGeneratedScriptWithSingleAction(action);
			} catch (DynamicScriptingException e) {
				currentlyExecutingAction = Optional.empty();
			}
		});
	}
	
	private void refreshGeneratedScriptWithSingleAction(ScriptGeneratorAction action) throws DynamicScriptingException {
		try {
			Optional<Integer> dynamicScriptId = scriptGeneratorModel.refreshGeneratedScript(action);
			dynamicScriptId.ifPresent(scriptId -> {
				dynamicScriptIds.add(scriptId);
			});
		} catch (InvalidParamsException e) {
			throw new DynamicScriptingException("Invalid params, cannot play script");
		} catch (UnsupportedLanguageException e) {
			throw new DynamicScriptingException("Unsupported language selected, cannot play script");
		} catch (NoScriptDefinitionSelectedException e) {
			throw new DynamicScriptingException("No script definition has been selected, cannot play script");
		}
	}
	
	public Optional<ScriptGeneratorAction> getCurrentlyExecutingAction() {
		return currentlyExecutingAction;
	}

	public Boolean isScriptDynamic(Integer integer) {
		return dynamicScriptIds.contains(integer);
	}

}
