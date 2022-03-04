package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.swt.widgets.Composite;

/**
 * Reset PV connection button, located below reset layout button in perspective selection menu.
 *
 */
public class ResetPvConnectionButton extends Button {

    private static final String RESET_PV_CONNECTION_URI = "platform:/plugin/uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher/icons/resetpv.png";
	
    /**
     * Constructor.
     * 
     * @param parent parent composite
     * @param model button view model
     */
	public ResetPvConnectionButton(Composite parent, ButtonViewModel model) {
		super(parent, RESET_PV_CONNECTION_URI, "Reset PV connection", model);
	}

}
