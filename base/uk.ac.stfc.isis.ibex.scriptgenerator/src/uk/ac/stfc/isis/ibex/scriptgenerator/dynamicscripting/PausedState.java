package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.HashMap;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class PausedState extends DynamicScriptingState {

	public PausedState(HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction) {
		super(dynamicScriptIdsToAction);
	}

	@Override
	public DynamicScriptingStatus getStatus() {
		return DynamicScriptingStatus.PAUSED;
	}

}
