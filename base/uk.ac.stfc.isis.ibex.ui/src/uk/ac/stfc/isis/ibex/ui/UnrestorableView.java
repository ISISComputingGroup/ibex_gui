package uk.ac.stfc.isis.ibex.ui;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * An unrestorable view, used as a placeholder if restoring a view fails.
 */
public class UnrestorableView {

    /**
     * View ID.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.UnrestorableView";
    
    /**
     * Create the controls for the part.
     * 
     * @param parent parent panel to add controls to
     */
    @PostConstruct
    public void createPartControl(Composite parent) {
    	Label text = new Label(parent, SWT.NONE);
    	text.setText("This view could not be restored from a save file.");		
    }
}
