package uk.ac.stfc.isis.ibex.ui;

import javax.annotation.PostConstruct;

import org.eclipse.swt.widgets.Composite;

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
