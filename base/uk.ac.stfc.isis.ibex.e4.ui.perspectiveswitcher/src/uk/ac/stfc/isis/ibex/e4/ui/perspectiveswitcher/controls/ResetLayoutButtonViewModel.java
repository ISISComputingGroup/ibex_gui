package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.model.UIThreadUtils;

/**
 * A view model class for setting the right colour to the ResetLayoutButton.
 *
 */
public class ResetLayoutButtonViewModel extends ButtonViewModel {

    private static final Color RESET_COLOR = SWTResourceManager.getColor(250, 150, 150);

    /**
     * Initiate a new reset layout button view model.
     */
    public ResetLayoutButtonViewModel() {
        super("Reset Layout");
        ResetLayoutButtonModel.getInstance().addPropertyChangeListener("layoutModified", new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                UIThreadUtils.syncExec(new Runnable() {
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
        if (ResetLayoutButtonModel.getInstance().isChanged()) {
            setColor(inFocus ? FOCUSSED : RESET_COLOR);
        } else {
            setColor(inFocus ? FOCUSSED : DEFOCUSSED);
        }
    }

}
