package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

public enum MatplotlibButtonType {
	HOME("home"),
	BACK("back"),
	FORWARD("forward"),
	PAN("pan"),
	ZOOM("zoom");
	
	private final String websocketString;

	private MatplotlibButtonType(String websocketString) {
		this.websocketString = websocketString;
	}
	
	public String getWebsocketString() { return websocketString; }
}
