package uk.ac.stfc.isis.ibex.ui.widgets.states;

import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

public enum Status {
	ON (0, 255, 0, "ON"),	// Green
	OFF (255, 0, 0, "OFF"),	// Red
	UNKNOWN (255, 127, 0, "UNKNOWN"); // Orange
	
	private final Color color;
	private final String text;
	
	private Status(int r, int g, int b, String text) {
		this.color = SWTResourceManager.getColor(r, g, b);
		this.text = text;
	}

	public Color getColor() {
		return color;
	}
	
	public String getText() {
		return text;
	}
}
