package uk.ac.stfc.isis.ibex.instrument;

import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.instrument.internal.PVPrefix;

public class InstrumentInfo {

	private final String name;
	
	public InstrumentInfo(String name) {
		this.name = name;
	}

	public String name() {
		return name;
	}
	
	public String pvPrefix() {
		return PVAddress.startWith("IN").append(name).toString() + PVAddress.COLON;
	}
	
	public String hostName() {	
		return PVPrefix.NDX + name;
	}
}
