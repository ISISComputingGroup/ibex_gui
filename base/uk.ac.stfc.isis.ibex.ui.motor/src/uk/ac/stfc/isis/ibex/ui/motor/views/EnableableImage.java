package uk.ac.stfc.isis.ibex.ui.motor.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class EnableableImage {

	private final Image enabled;
	private final Image disabled;
	
	public EnableableImage(Display display, Image enabled) {
		this.enabled = enabled;
		disabled = new Image(display, enabled, SWT.IMAGE_DISABLE);
	}
	
	public Image enabled() {
		return enabled;
	}
	
	public Image disabled() {
		return disabled;
	}
	
	public Image isEnabled(boolean enable) {
		return enable ? enabled : disabled;
	}
	
	public void dispose() {
		disabled.dispose();
	}
}
