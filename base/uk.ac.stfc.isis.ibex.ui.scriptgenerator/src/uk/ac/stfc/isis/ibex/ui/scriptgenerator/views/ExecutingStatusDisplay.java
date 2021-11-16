package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionDynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class ExecutingStatusDisplay {
	
    private static final String EXECUTING_MARK = "\u25B6";
    private static final String PAUSED_BEFORE_MARK = "\u23F8";
    private static final String PAUSED_DURING_MARK = "\u23EF";
    private static final String NOT_EXECUTING_MARK = "";
	
    /**
     * @return The mark to display whether executing or not.
     */
	public static String getText(ActionDynamicScriptingStatus status) {
		switch (status) {
			case EXECUTING:
				return EXECUTING_MARK;
			case PAUSED_BEFORE_EXECUTION:
				return PAUSED_BEFORE_MARK;
			case PAUSED_DURING_EXECUTION:
				return PAUSED_DURING_MARK;
			default:
				return NOT_EXECUTING_MARK;
		}
	}
	
	/**
	 * Get the ValidityDisplay from it's displayed text.
	 * 
	 * @param text The string to generate a Validity display element from.
	 * @return A ValidityDisplay element generated from the given string.
	 */
	public static ActionDynamicScriptingStatus fromText(String text) {
		if (text.contains(EXECUTING_MARK)) {
			return ActionDynamicScriptingStatus.EXECUTING;
		} else if (text.contains(PAUSED_BEFORE_MARK)) {
			return ActionDynamicScriptingStatus.PAUSED_BEFORE_EXECUTION;
		} else if (text.contains(PAUSED_DURING_MARK)) {
			return ActionDynamicScriptingStatus.PAUSED_DURING_EXECUTION;
		} else {
			return ActionDynamicScriptingStatus.NO_STATUS;
		}
	}
	
	/**
	 * Whether the actions executing status equals this enum.
	 * 
	 * @param action The action to check the executing status of.
	 * @return True if the action and this enum have the same executing status or false if not.
	 */
	public static boolean equalsAction(ActionDynamicScriptingStatus status, ScriptGeneratorAction action) {
		return status.equals(action.getDynamicScriptingStatus());
	}

}
