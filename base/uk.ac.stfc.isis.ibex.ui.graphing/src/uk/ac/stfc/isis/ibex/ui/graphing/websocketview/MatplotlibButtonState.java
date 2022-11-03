package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

/**
 * Describes the current activity state of a button.
 */
public enum MatplotlibButtonState {
	/**
	 * Button is disabled. 
	 */
	DISABLED(false),
	/**
	 * Button is enabled but inactive.
	 */
	ENABLED_INACTIVE(true), 
	/** Button is enabled and active.
	 */
	ENABLED_ACTIVE(true);
	
	private final Boolean buttonState;

	MatplotlibButtonState(Boolean buttonState) {
		this.buttonState = buttonState;
	}
	
	/**
	 * @return the current button state 
	 */
	public Boolean getButtonState() { 
		return buttonState; 
	}
}
