package uk.ac.stfc.isis.ibex.instrument;

/**
 * This class provides default no operation behaviour for @see InstrumentInfoReceiver.
 */
public abstract class InstrumentInfoReceiverAdapter implements InstrumentInfoReceiver {

	@Override
	public void setInstrument(InstrumentInfo instrument) {
		/* Default NO OP */
	}

	@Override
	public void preSetInstrument(InstrumentInfo instrument) {
		/* Default NO OP */
	}

	@Override
	public void postSetInstrument(InstrumentInfo instrument) {
		/* Default NO OP */
	}
}
