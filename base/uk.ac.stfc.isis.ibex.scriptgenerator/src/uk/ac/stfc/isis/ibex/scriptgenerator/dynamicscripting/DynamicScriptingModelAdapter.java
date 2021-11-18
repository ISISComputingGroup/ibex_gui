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

/**
 * An adapter for dynamic scripting to use when interacting with the script generator model.
 */
public class DynamicScriptingModelAdapter extends ModelObject implements PropertyChangeListener {
	
	private ScriptGeneratorSingleton scriptGeneratorModel;
	private Optional<DynamicScript> dynamicScript = Optional.empty();
	private HashMap<Integer, DynamicScriptingException> errors = new HashMap<>();
	
	/**
	 * Create an adapter with the script generator model it depends on injected.
	 *
	 * @param scriptGeneratorModel The injected script generator model dependency.
	 */
	public DynamicScriptingModelAdapter(ScriptGeneratorSingleton scriptGeneratorModel) {
		this.scriptGeneratorModel = scriptGeneratorModel;
	}

	/**
	 * Refresh the generated script using the given action.
	 *
     * @param action The action to create the script from.
	 * @return An optional of the script ID or empty if generation fails.
	 * @throws DynamicScriptingException If generation fails.
	 */
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
	
	/**
	 * Get the action in the table that is to be executed after the given action.
	 *
	 * @param action The action that comes just before the action to be returned.
	 * @return The next action after the one given or an optional empty if there isn't one.
	 */
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
	
	/**
	 * @return the first action to execute.
	 */
	public Optional<ScriptGeneratorAction> getFirstAction() {
		return scriptGeneratorModel.getAction(0);
	}
	
	/**
	 * @return an optional of the dynamic script that is being/has just been generated, or empty if there isn't one.
	 */
	public Optional<DynamicScript> getDynamicScript() {
		return dynamicScript;
	}

	/**
	 * Handle property changes fired when generating scripts.
	 * {@inheritdoc}
	 */
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
