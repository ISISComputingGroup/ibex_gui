package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * A model to provide the logic behind how a perspective button should be displayed.
 */
public class PerspectiveButtonViewModel extends ButtonViewModel {

    /**
     * The colour to set the button to when it is active.
     */
    protected static final Color ACTIVE = SWTResourceManager.getColor(120, 170, 210);
    private static final Font ACTIVE_FONT = SWTResourceManager.getFont("Arial", 12, SWT.BOLD);

    /**
     * Whether the perspective associated with this button is active or not.
     */
    protected boolean active = false;

    /**
     * Calls constructor for ButtonViewModel, then sets the label to buttonLabel
     * and defaults the active state to false.
     * 
     * @param buttonLabel
     *            String: label for the button
     */
    public PerspectiveButtonViewModel(String buttonLabel) {
        super(buttonLabel);
        setText(buttonLabel);
        setActive(active);
    }

    /**
     * Set the button to active (focused) or inactive (unfocused) by changing
     * its colour and font.
     * 
     * @param newActive
     *            boolean: true if active, false otherwise
     */
    public void setActive(boolean newActive) {
        active = newActive;
        setColor(chooseColor());
        setFont(active ? ACTIVE_FONT : BUTTON_FONT);
        setFocus(false);
    }

    /**
     * Choose a colour based on the state of the button.
     * 
     * @return colour
     */
    protected Color chooseColor() {
        return active ? ACTIVE : DEFOCUSSED;
    }

    @Override
    public void setFocus(boolean inFocus) {
        if (!active) {
            super.setFocus(inFocus);
        }
    }
}
