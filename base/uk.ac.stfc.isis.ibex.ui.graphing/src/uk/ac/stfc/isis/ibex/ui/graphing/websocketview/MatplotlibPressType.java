package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

public enum MatplotlibPressType {
	BUTTON_PRESS("button_press"),
	BUTTON_RELEASE("button_release"),
	DBLCLICK("dblclick");
	
	private final String websocketString;

	private MatplotlibPressType(String websocketString) {
		this.websocketString = websocketString;
	}
	
	public String getWebsocketString() { return websocketString; }
}
