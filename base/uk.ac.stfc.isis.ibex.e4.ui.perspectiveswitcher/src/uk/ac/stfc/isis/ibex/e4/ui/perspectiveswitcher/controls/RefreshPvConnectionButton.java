package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.epics.switching.InstrumentSwitchers;
import uk.ac.stfc.isis.ibex.instrument.Instrument;

/**
 * Refresh PV connection button, located below reset layout button in perspective selection menu.
 *
 */
public class RefreshPvConnectionButton extends Button {

    private static final String REFRESH_PV_CONNECTION_URI = "platform:/plugin/uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher/icons/resetpv.png";
   
    
    /**
     * Constructor.
     * 
     * @param parent parent composite
     * @param model button view model
     */
	public RefreshPvConnectionButton(Composite parent, ButtonViewModel model) {
		super(parent, REFRESH_PV_CONNECTION_URI, "Refresh PVs: Refreshes connection to the PVs, restoring blocked connections", model);
	}
	
	/**
	 * Refreshes PV connections using existing mechanism of switching to current instrument.
	 */
	public void refreshPvConnections() {
		InstrumentSwitchers switcher = InstrumentSwitchers.getDefault();
		if (!switcher.switching) {
			switcher.setInstrument(Instrument.getInstance().currentInstrument());
		}
	}
}
