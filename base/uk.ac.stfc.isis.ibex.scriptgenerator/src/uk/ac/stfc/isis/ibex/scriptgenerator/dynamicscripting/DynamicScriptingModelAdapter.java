package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

public class DynamicScriptingModelAdapter extends ModelObject implements PropertyChangeListener {
	
	private ScriptGeneratorSingleton scriptGeneratorModel;
	private Optional<DynamicScript> dynamicScript = Optional.empty();
	private HashMap<Integer, DynamicScriptingException> errors = new HashMap<>();
	
	public DynamicScriptingModelAdapter(ScriptGeneratorSingleton scriptGeneratorModel) {
		this.scriptGeneratorModel = scriptGeneratorModel;
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
	
	public Optional<ScriptGeneratorAction> getActionAfter(ScriptGeneratorAction action) {
		List<ScriptGeneratorAction> actions = scriptGeneratorModel.getActions();
		Optional<ScriptGeneratorAction> nextAction = Optional.empty();
		Integer actionIndex = actions.indexOf(action);
		// indexOf returns -1 if action is not in table
		if (actionIndex >= 0) {
			Integer nextActionIndex = actionIndex + 1;
			nextAction = scriptGeneratorModel.getAction(nextActionIndex);
		}
		return nextAction;
	}
	
	public Optional<ScriptGeneratorAction> getFirstAction() {
		return scriptGeneratorModel.getAction(0);
	}
	
	public Optional<DynamicScript> getDynamicScript() {
		return dynamicScript;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ScriptGeneratorProperties.SCRIPT_GENERATION_ERROR_PROPERTY)) {
			firePropertyChange(ScriptGeneratorProperties.SCRIPT_GENERATION_ERROR_PROPERTY, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ScriptGeneratorProperties.GENERATED_SCRIPT_PROPERTY)) {
			Integer newScriptId = (Integer) evt.getNewValue();
			Optional<String> newCode = scriptGeneratorModel.getScriptFromId(newScriptId);
			newCode.ifPresentOrElse(code -> {
					handleScriptGeneration(code);
				}, () -> {
					errors.put(newScriptId, new DynamicScriptingException("Error getting script from ID"));
					firePropertyChange(ScriptGeneratorProperties.SCRIPT_GENERATION_ERROR_PROPERTY, evt.getOldValue(), evt.getNewValue());
				}
			);
		}
	}
	
	private void handleScriptGeneration(String newCode) {
		dynamicScript.ifPresent(script -> {
			script.setCode(newCode);
		});
		firePropertyChange(DynamicScriptingProperties.NICOS_SCRIPT_GENERATED_PROPERTY, Optional.empty(), dynamicScript);
	}

}
