package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

/**
 * Describes the press type of a mouse click event.
 */
public enum MatplotlibPressType {
	/**
	 * Mouse button has been pressed.
	 */
	BUTTON_PRESS("button_press"),
	/**
	 * Mouse button has been released.
	 */
	BUTTON_RELEASE("button_release"),
	/**
	 * Mouse button has been double clicked.
	 */
	DBLCLICK("dblclick");
	
	private final String websocketString;

	MatplotlibPressType(String websocketString) {
		this.websocketString = websocketString;
	}
	
	/**
	 * @return the websocket string associated with each press type
	 */
	public String getWebsocketString() { 
		return websocketString; 
	}
}
