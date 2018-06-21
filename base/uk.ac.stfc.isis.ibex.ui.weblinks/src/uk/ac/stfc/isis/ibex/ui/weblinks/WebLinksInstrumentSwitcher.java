package uk.ac.stfc.isis.ibex.ui.weblinks;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiverAdapter;
import uk.ac.stfc.isis.ibex.ui.PerspectiveReopener;

/**
 * Class responsible for switching the weblinks perspective to a new instrument.
 */
public class WebLinksInstrumentSwitcher extends InstrumentInfoReceiverAdapter {
	
	private static final PerspectiveReopener REOPENER = new PerspectiveReopener("uk.ac.stfc.isis.ibex.client.e4.product.perspective.weblinks");
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public void setInstrument(InstrumentInfo instrument) {
		REOPENER.reopenPerspective();
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void preSetInstrument(InstrumentInfo instrument) {
        REOPENER.closePerspective();
    }
}
