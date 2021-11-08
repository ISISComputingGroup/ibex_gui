package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.NoScriptDefinitionSelectedException;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.UnsupportedLanguageException;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class DynamicScriptingModelFacade extends ModelObject {
	
	private ScriptGeneratorSingleton scriptGeneratorModel;
	private Optional<DynamicScript> dynamicScript = Optional.empty();
	private HashMap<Integer, DynamicScriptingException> errors = new HashMap<>();
	
	public DynamicScriptingModelFacade(ScriptGeneratorSingleton scriptGeneratorModel) {
		this.scriptGeneratorModel = scriptGeneratorModel;
		this.scriptGeneratorModel.addPropertyChangeListener(ScriptGeneratorProperties.SCRIPT_GENERATION_ERROR_PROPERTY, event -> {
			firePropertyChange(ScriptGeneratorProperties.SCRIPT_GENERATION_ERROR_PROPERTY, event.getOldValue(), event.getNewValue());
		});
		this.scriptGeneratorModel.addPropertyChangeListener(ScriptGeneratorProperties.GENERATED_SCRIPT_PROPERTY, event -> {
			Integer newScriptId = (Integer) event.getNewValue();
			Optional<String> newCode = scriptGeneratorModel.getScriptFromId(newScriptId);
			newCode.ifPresentOrElse(code -> {
					handleScriptGeneration(code);
				}, () -> {
					errors.put(newScriptId, new DynamicScriptingException("Error getting script from ID"));
				}
			);			
		});
	}
	
	public void handleScriptGeneration(String newCode) {
		dynamicScript.ifPresent(script -> {
			script.setCode(newCode);
		});
		firePropertyChange(DynamicScriptingProperties.NICOS_SCRIPT_GENERATED_PROPERTY, Optional.empty(), dynamicScript);
	}

	public Optional<Integer> refreshGeneratedScript(ScriptGeneratorAction action) throws DynamicScriptingException {
		try {
			var scriptId = scriptGeneratorModel.refreshGeneratedScript(action);
			scriptId.ifPresent(id -> {
				var newDynamicScript = new DynamicScript(id);
				dynamicScript = Optional.of(newDynamicScript);
			});
			return scriptId;
		} catch (InvalidParamsException e) {
			throw new DynamicScriptingException("Invalid parameters, cannot generate dynamic script");
		} catch (UnsupportedLanguageException e) {
			throw new DynamicScriptingException("Unsupported language, cannot generate dynamic script");
		} catch (NoScriptDefinitionSelectedException e) {
			throw new DynamicScriptingException("No script definition selected, cannot generate dynamic script");
		}
	}
	
	public Optional<ScriptGeneratorAction> getAction(Integer actionIndex) {
		return scriptGeneratorModel.getAction(actionIndex);
	}
	
	public List<ScriptGeneratorAction> getActions() {
		return scriptGeneratorModel.getActions();
	}
	
	public Optional<DynamicScript> getDynamicScript() {
		return dynamicScript;
	}

}
