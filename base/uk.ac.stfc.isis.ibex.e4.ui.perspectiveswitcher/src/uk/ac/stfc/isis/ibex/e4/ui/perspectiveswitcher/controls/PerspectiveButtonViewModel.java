package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * A model to provide easy access to the listeners for the interaction with the
 * alarm system; chiefly the number of active alarms.
 */
public class PerspectiveButtonViewModel extends ButtonViewModel {
	
    protected static final Color ACTIVE = SWTResourceManager.getColor(120, 170, 210);
    private static final Font ACTIVE_FONT = SWTResourceManager.getFont("Arial", 12, SWT.BOLD);
    
	protected boolean active = false;
	
	public PerspectiveButtonViewModel() {
		super();
		setActive(active);
	}

	public void setActive(boolean newActive) {
		active = newActive;
		setColor(active ? ACTIVE : DEFOCUSSED);
		setFont(active ? ACTIVE_FONT : BUTTON_FONT);
	}

	@Override
	public void setFocus(boolean inFocus) {
		if (! active) {
			super.setFocus(inFocus);
		}
	}
}
