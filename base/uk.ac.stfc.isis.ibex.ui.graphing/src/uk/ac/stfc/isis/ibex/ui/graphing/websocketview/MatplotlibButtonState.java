package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

public enum MatplotlibButtonState {
	DISABLED(false),
	ENABLED_INACTIVE(true), 
	ENABLED_ACTIVE(true);
	
	private final Boolean buttonState;

	private MatplotlibButtonState(Boolean buttonState) {
		this.buttonState = buttonState;
	}
	
	public Boolean getButtonState() { return buttonState; }
}
