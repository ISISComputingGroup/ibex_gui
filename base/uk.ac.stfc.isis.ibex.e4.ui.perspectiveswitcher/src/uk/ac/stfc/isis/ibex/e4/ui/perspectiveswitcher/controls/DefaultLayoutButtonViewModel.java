package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * A view model class for setting the right colour to the ResetLayoutButton.
 *
 */
public class DefaultLayoutButtonViewModel extends ButtonViewModel {

    private static final Color RESET_COLOR = SWTResourceManager.getColor(250, 150, 150);

    /**
     * Initiate a new reset layout button view model.
     */
    public DefaultLayoutButtonViewModel() {
        DefaultLayoutButtonModel.getInstance().addPropertyChangeListener("layoutModified", new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                Display.getDefault().syncExec(new Runnable() {
                    @Override
                    public void run() {
                        if ((boolean) evt.getNewValue()) {
                            // Set colour - red
                            setColor(RESET_COLOR);
                        } else {
                            // set colour - normal
                            setColor(DEFOCUSSED);
                        }
                    }
                });
            }
        });
        setColor(DEFOCUSSED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setFocus(boolean inFocus) {
        this.inFocus = inFocus;
        if (DefaultLayoutButtonModel.getInstance().isChanged()) {
            setColor(inFocus ? FOCUSSED : RESET_COLOR);
        } else {
            setColor(inFocus ? FOCUSSED : DEFOCUSSED);
        }
    }

}
