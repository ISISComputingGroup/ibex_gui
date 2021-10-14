package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public enum ValidityDisplay {
	
	VALID,
	INVALID,
	UNCERTAIN;
	
	/**
     * The unicode for a question mark.
     */
    public static final String QUESTION_MARK = "\u003F";
    
    /**
     * The unicode for a tick mark.
     */
    public static final String TICK_MARK = "\u2714";
    
    /**
     * The unicode for a cross mark.
     */
    public static final String CROSS_MARK = "\u2718";
	
    /**
     * @return Text to display the enum in the table.
     */
	public String getText() {
		switch(this) {
			case VALID:
				return TICK_MARK;
			case INVALID:
				return CROSS_MARK;
			default:
				return QUESTION_MARK;
		}
	}
	
	/**
	 * Get the ValidityDisplay from it's displayed text.
	 * @param text
	 * @return
	 */
	public static ValidityDisplay fromText(String text) {
		switch(text) {
			case TICK_MARK:
				return ValidityDisplay.VALID;
			case CROSS_MARK:
				return ValidityDisplay.INVALID;
			default:
				return ValidityDisplay.UNCERTAIN;
		}
	}
	
	/**
	 * Whether the actions validity equals this enum.
	 * 
	 * @param action The action to check the validity of.
	 * @return True if the action and this enum have the same validity or false if not.
	 */
	public boolean equalsAction(ScriptGeneratorAction action) {
		switch(this) {
			case VALID:
				return action.isValid();
			case INVALID:
				return !action.isValid();
			default:
				return false; // Default to false so then we update the action to being refreshed in the display
		}
	}

}
