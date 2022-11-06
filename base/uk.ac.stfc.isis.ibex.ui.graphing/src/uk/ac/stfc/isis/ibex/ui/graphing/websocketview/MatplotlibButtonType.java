package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

/**
 * Describes the type of navigation button.
 */
public enum MatplotlibButtonType {
	/**
	 * The 'home' button.
	 */
	HOME("home"),
	/**
	 * The 'navigate backwards' button.
	 */
	BACK("back"),
	/**
	 * The 'navigate forwards' button.
	 */
	FORWARD("forward"),
	/**
	 * The 'pan' button.
	 */
	PAN("pan"),
	/**
	 * The 'zoom' button.
	 */
	ZOOM("zoom");
	
	private final String websocketString;
	
	MatplotlibButtonType(String websocketString) {
		this.websocketString = websocketString;
	}
	
	/**
	 * @return the websocket string associated with each button type
	 */
	public String getWebsocketString() { 
		return websocketString; 
	}
}
