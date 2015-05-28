package uk.ac.stfc.isis.ibex.instrument.internal;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

public class LocalHostInstrumentInfo extends InstrumentInfo {

	public LocalHostInstrumentInfo() {
		super(MachineName.get());
	}

	@Override
	public String pvPrefix() {
		return new DefaultSettings().pvPrefix();
	}
	
	@Override
	public String hostName() {
		return "localhost";
	}
}
