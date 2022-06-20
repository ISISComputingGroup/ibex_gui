package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.HashMap;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;


/**
 * A state to use in the dynamic scripting state pattern for when the script is in error.
 */
public class ErrorState extends DynamicScriptingState {

	/**
	 * Create a stopped state with the given ids to actions map.
	 * 
	 * @param dynamicScriptIdsToAction Script ids and their associated actions that are used in dynamic scripting.
	 */
	public ErrorState(HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction) {
		super(dynamicScriptIdsToAction);
	}

	@Override
	public DynamicScriptingStatus getStatus() {
		return DynamicScriptingStatus.ERROR;
	}

	@Override
	public Boolean pauseComplete() {
		return false;
	}

}
