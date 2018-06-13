package uk.ac.stfc.isis.ibex.ui.weblinks;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiverAdapter;
import uk.ac.stfc.isis.ibex.ui.PerspectiveReopener;

public class WebLinksInstrumentSwitcher extends InstrumentInfoReceiverAdapter {
	
	private static final PerspectiveReopener REOPENER = new PerspectiveReopener(Perspective.ID);
	
	@Override
    public void setInstrument(InstrumentInfo instrument) {
		REOPENER.reopenPerspective();
    }

    @Override
    public void preSetInstrument(InstrumentInfo instrument) {
        REOPENER.closePerspective();
    }
}
