package uk.ac.stfc.isis.ibex.ui;

import javax.annotation.PostConstruct;

import org.eclipse.swt.widgets.Composite;

/**
 * A completely empty view, used as a placeholder if restoring a view fails.
 */
public class EmptyView {

    /**
     * View ID.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.EmptyView";
    
    /**
     * Create the controls for the part.
     * 
     * @param parent parent panel to add controls to
     */
    @PostConstruct
    public void createPartControl(Composite parent) {
    }
}
