package uk.ac.stfc.isis.ibex.ui.widgets.buttons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

import uk.ac.stfc.isis.ibex.model.Action;

public class IBEXButtonBuilder {
    private String text;
    private String tooltip;
    private Style style;
    private ButtonType buttonType;
    private ActionButton actionButton;
    private HelpButton helpButton;
    private Composite parent;
    private Integer width;
    private Integer height;

    public IBEXButtonBuilder setLabel(String label) {
        this.text = label;
        return this;
    }

    public IBEXButtonBuilder setTooltip(String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public IBEXButtonBuilder setStyle(Style style) {
        this.style = style;
        return this;
    }

    public IBEXButtonBuilder setButtonType(ButtonType buttonType) {
        this.buttonType = buttonType;
        return this;
    }

    public IBEXButtonBuilder setActionButton(ActionButton actionButton) {
        this.actionButton = actionButton;
        return this;
    }

    public IBEXButtonBuilder setHelpButton(HelpButton helpButton) {
        this.helpButton = helpButton;
        return this;
    }

    public Button build() {
        Button button;
        
        if (parent != null) {
            button = new Button(parent, SWT.NONE);
        }

        if (text != null) {
            button.setText(text);
        }

        if (tooltip != null) {
            button.setToolTipText(tooltip);
        }

        if (style != null) {
            button.setLayoutData(style.getLayoutData());
        }

        // Handle different button types
        switch (buttonType) {
            case TEXT:
                // Handle text button specifics
                break;
            case RADIO:
                // Handle radio button specifics
                break;
            case CHECKBOX:
                // Handle checkbox specifics
                break;
            // Add more cases as needed

            default:
                break;
        }

        // Attach action button and help button
        if (actionButton != null) {
            
        }

        if (helpButton != null) {
            // Attach help button logic
        }

        return button;
    }

}


enum Style {
    COMPACT,
    EXPANDING,
    CUSTOM;

    public void getLayoutData() {
        // Apply the style to the component
        switch (this) {
            case COMPACT:
                // Apply compact style
                break;
            case EXPANDING:
                // Apply expanding style
                break;
            case CUSTOM:
                // Apply custom style (custom width and height)
                break;
            default:
                break;
        }
    }
}

// Placeholder enum for button types
enum ButtonType {
    TEXT,
    RADIO,
    CHECKBOX,
    // Add more button types as needed
}

