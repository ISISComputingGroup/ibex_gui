package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

/**
 * Describes the type of a navigation button. 
 */
public enum MatplotlibNavigationType {
	/**
	 * Navigation type 'pan' is enabled.
	 */
	PAN("PAN"),
	/**
	 * Navigation type 'zoom' is enabled.
	 */
	ZOOM("ZOOM"),
	/**
	 * No navigation type is enabled.
	 */
	NONE("NONE");
	
	private final String websocketString;

	MatplotlibNavigationType(String websocketString) {
		this.websocketString = websocketString;
	}
	
	/**
	 * @return the websocket string associated with each navigation button type
	 */
	public String getWebsocketString() { 
		return websocketString; 
	}
}
