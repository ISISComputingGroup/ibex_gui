package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.Optional;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.NoScriptDefinitionSelectedException;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.UnsupportedLanguageException;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class DynamicScriptingGeneratorFacade extends ModelObject {
	
	private ScriptGeneratorSingleton scriptGeneratorModel;
	private Optional<Integer> scriptId = Optional.empty();
	private Optional<String> scriptCode = Optional.empty();
	
	public DynamicScriptingGeneratorFacade(ScriptGeneratorSingleton scriptGeneratorModel) {
		this.scriptGeneratorModel = scriptGeneratorModel;
		this.scriptGeneratorModel.addPropertyChangeListener(ScriptGeneratorProperties.SCRIPT_GENERATION_ERROR_PROPERTY, event -> {
			firePropertyChange(ScriptGeneratorProperties.SCRIPT_GENERATION_ERROR_PROPERTY, event.getOldValue(), event.getNewValue());
		});
		this.scriptGeneratorModel.addPropertyChangeListener(ScriptGeneratorProperties.GENERATED_SCRIPT_PROPERTY, event -> {
			String newCode = (String) event.getNewValue();
			handleScriptGeneration(newCode);
		});
	}
	
	public void handleScriptGeneration(String newCode) {
		firePropertyChange(ScriptGeneratorProperties.NICOS_SCRIPT_GENERATED_PROPERTY, Optional.empty(), scriptCode = Optional.of(newCode));
	}

	public void refreshGeneratedScript(ScriptGeneratorAction action) throws DynamicScriptingException {
		try {
			scriptId = scriptGeneratorModel.refreshGeneratedScript(action);
		} catch (InvalidParamsException e) {
			throw new DynamicScriptingException("Invalid parameters, cannot generate dynamic script");
		} catch (UnsupportedLanguageException e) {
			throw new DynamicScriptingException("Unsupported language, cannot generate dynamic script");
		} catch (NoScriptDefinitionSelectedException e) {
			throw new DynamicScriptingException("No script definition selected, cannot generate dynamic script");
		}
	}
	
	public Optional<String> getScriptName() {
		if (scriptId.isPresent()) {
			return Optional.of(String.format("Script Generator: %d", scriptId.get()));
		} else {
			return Optional.empty();
		}
	}
	
    public Optional<Integer> getScriptId() {
		return scriptId;
	}
	
	public Optional<String> getScriptCode() {
		return scriptCode;
	}

}
