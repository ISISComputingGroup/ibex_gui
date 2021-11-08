package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public enum ExecutingStatusDisplay {
	
	EXECUTING,
	NOT_EXECUTING;
	
    private static final String EXECUTING_MARK = "\u25B6";
    private static final String NOT_EXECUTING_MARK = "";
	
    /**
     * @return The mark to display whether executing or not.
     */
	public String getText() {
		switch (this) {
			case EXECUTING:
				return EXECUTING_MARK;
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
	public static ExecutingStatusDisplay fromText(String text) {
		if (text.contains(EXECUTING_MARK)) {
			return ExecutingStatusDisplay.EXECUTING;
		} else {
			return ExecutingStatusDisplay.NOT_EXECUTING;
		}
	}
	
	/**
	 * Whether the actions executing status equals this enum.
	 * 
	 * @param action The action to check the executing status of.
	 * @return True if the action and this enum have the same executing status or false if not.
	 */
	public boolean equalsAction(ScriptGeneratorAction action) {
		switch (this) {
			case EXECUTING:
				return action.isExecuting();
			default:
				return !action.isExecuting();
		}
	}

}
