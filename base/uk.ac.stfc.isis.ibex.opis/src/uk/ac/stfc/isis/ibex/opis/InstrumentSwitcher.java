package uk.ac.stfc.isis.ibex.opis;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiverAdapter;

/**
 * This class refreshes all OPIs after an instrument switch, so that they pick up new macros.
 */
public class InstrumentSwitcher extends InstrumentInfoReceiverAdapter {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInstrument(InstrumentInfo instrument) {
//		OpiViewModel.refreshViews();
	}
}
