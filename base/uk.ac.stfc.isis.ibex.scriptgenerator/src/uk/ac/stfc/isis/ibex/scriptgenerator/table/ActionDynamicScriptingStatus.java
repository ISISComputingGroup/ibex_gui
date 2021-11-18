package uk.ac.stfc.isis.ibex.scriptgenerator.table;

/**
 * The possible statuses of an action with regards to execution in dynamic scripting.
 */
public enum ActionDynamicScriptingStatus {
	
	/**
	 * The action is currently being executed in dynamic scripting.
	 */
	EXECUTING,
	/**
	 * The action was paused whilst it is being executed.
	 */
	PAUSED_DURING_EXECUTION,
	/**
	 * The dynamic scripting was paused just before this action was executed.
	 */
	PAUSED_BEFORE_EXECUTION,
	/**
	 * The action has no status in regards to dynamic scripting.
	 */
	NO_STATUS;

}
