package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

public enum MatplotlibNavigationType {
	PAN("PAN"),
	ZOOM("ZOOM"),
	NONE("NONE");
	
	private final String websocketString;

	private MatplotlibNavigationType(String websocketString) {
		this.websocketString = websocketString;
	}
	
	public String getWebsocketString() { return websocketString; }
}
