package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * A model to provide easy access to the listeners for the interaction with the
 * alarm system; chiefly the number of active alarms.
 */
public class ButtonViewModel extends ModelObject {
	
	protected static final Color FOCUSSED = SWTResourceManager.getColor(220, 235, 245);
	protected static final Color DEFOCUSSED = SWTResourceManager.getColor(247, 245, 245);

    protected static final Font BUTTON_FONT = SWTResourceManager.getFont("Arial", 12, SWT.NORMAL);
    
	protected Color color = DEFOCUSSED;
	protected Font font = BUTTON_FONT;
	protected boolean inFocus = false;
	
	public ButtonViewModel() {
		setFocus(inFocus);
		setFont(font);
	}
	
	public Color getColor() {
		return color;
	}
	
	public Font getFont() {
		return font;
	}
	
	protected void setColor(Color newColor) {
		firePropertyChange("color", color, color = newColor);
	}
	
	protected void setFont(Font newFont) {
		firePropertyChange("font", font, font = newFont);
	}
	
	protected void setFocus(boolean inFocus) {
		setColor(inFocus ? FOCUSSED : DEFOCUSSED);
	}
}
