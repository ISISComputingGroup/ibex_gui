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

    private static final String SPACING = "  ";
    
    /**
     * Colour for focused buttons.
     */
    protected static final Color FOCUSSED = SWTResourceManager.getColor(220, 235, 245);
    
    /**
     * Colour for unfocused buttons.
     */
    protected static final Color DEFOCUSSED = SWTResourceManager.getColor(247, 245, 245);

    /**
     * Font for buttons.
     */
    protected static final Font BUTTON_FONT = SWTResourceManager.getFont("Arial", 12, SWT.NORMAL);

    /**
     * The current button colour.
     */
    protected Color color = DEFOCUSSED;
    
    /**
     * The current button font.
     */
    protected Font font = BUTTON_FONT;
    
    /**
     * Whether this button is in focus.
     */
    protected boolean inFocus = false;
    
    /**
     * The text on this button.
     */
    protected String text = "";
    private boolean maximised = true;
    
    /**
     * Whether this button is visible.
     */
    protected boolean visible = true;

    /**
     * Initialises button focus and font, and text.
     * @param buttonLabel the button label text
     */
    public ButtonViewModel(String buttonLabel) {
        setText(buttonLabel);
        setFocus(inFocus);
        setFont(font);
    }

    /**
     * Get the colour of the button.
     * 
     * @return colour (Color)
     */
    public Color getColor() {
        return color;
    }

    /**
     * Get the font of the button label.
     * 
     * @return font (Font)
     */
    public Font getFont() {
        return font;
    }

    /**
     * Set the button colour.
     * 
     * @param newColor
     *            Colour to change button to.
     */
    protected void setColor(Color newColor) {
        firePropertyChange("color", color, color = newColor);
    }

    /**
     * Set the button label font.
     * 
     * @param newFont
     *            new font for the button label.
     */
    protected void setFont(Font newFont) {
        firePropertyChange("font", font, font = newFont);
    }

    /**
     * Set whether the button is in focus.
     * 
     * @param inFocus
     *            boolean, true if button focused, false otherwise.
     */
    protected void setFocus(boolean inFocus) {
        this.inFocus = inFocus;
        setColor(inFocus ? FOCUSSED : DEFOCUSSED);
    }

    /**
     * Returns the text for the button.
     * 
     * @return The text for the button.
     */
    public String getText() {
        if (maximised) {
            return SPACING + text;
        } else {
            return "";
        }
    }

    /**
     * Set the button label text.
     * 
     * @param newText
     *            new label for button.
     */
    protected void setText(String newText) {
        firePropertyChange("text", "", text = newText);
    }
    
    /**
     * Minimises the button to display the icon only.
     * @param width the width to maximise to
     */
    public void minimise(int width) {
        maximised = false;
        firePropertyChange("text", text, "");
        setWidth(width);
    }
    
    /**
     * Maximises the button to its full size.
     * @param width the width to minimise to
     */
    public void maximise(int width) {
        maximised = true;
        setText(text);
        setWidth(width);
    }
    
    /**
     * Set the button width.
     * @param newWidth the new button width
     */
    public void setWidth(int newWidth) {
        firePropertyChange("width", 0, newWidth);
    }
    
    /**
     * Set whether the button is visible.
     * @param newVisible True to make the button visible
     */
    public void setVisible(boolean newVisible) {
        firePropertyChange("visible", visible, visible = newVisible);
    }
    
    /**
     * Get whether the button is visible.
     * @return True if the button is visible
     */
    public boolean getVisible() {
        return visible;
    }
}
