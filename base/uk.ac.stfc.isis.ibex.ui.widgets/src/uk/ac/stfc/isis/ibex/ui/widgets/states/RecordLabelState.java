package uk.ac.stfc.isis.ibex.ui.widgets.states;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

public enum RecordLabelState {
	DISCONNECTED (255, 0, 0, ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.widgets", "icons/warning_red.png")),	// Red
	CONNECTED (0, 0, 0, null);																							// Black
	
	private final Color color;
	private final Image image;
	
	private RecordLabelState(int r, int g, int b, Image image) {
		this.color = SWTResourceManager.getColor(r, g, b);
		this.image = image;
	}

	public Color getColor() {
		return color;
	}
	
	public Image getImage() {
		return image;
	}
	
	public boolean isConnected() {
		return this == CONNECTED;
	}
}
