package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class DynamicScriptingManager {
	
	private ScriptGeneratorSingleton scriptGeneratorModel;
	private Optional<ScriptGeneratorAction> currentlyExecutingAction;
	
	public DynamicScriptingManager(ScriptGeneratorSingleton scriptGeneratorModel) {
		this.scriptGeneratorModel = scriptGeneratorModel;
	}
	
	public void playScript() {
		currentlyExecutingAction = scriptGeneratorModel.getAction(0);
	}
	
	public Optional<ScriptGeneratorAction> currentlyExecutingAction() {
		return currentlyExecutingAction;
	}

}
