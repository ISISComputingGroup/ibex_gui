package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.HashMap;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class StoppedState extends DynamicScriptingState {

	public StoppedState(HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction) {
		super(dynamicScriptIdsToAction);
	}

	@Override
	public DynamicScriptingStatus getStatus() {
		return DynamicScriptingStatus.STOPPED;
	}

	@Override
	public void play() {
		changeState(DynamicScriptingStatus.PLAYING);
	}

}
