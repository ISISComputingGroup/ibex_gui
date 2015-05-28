package uk.ac.stfc.isis.ibex.instrument.internal;

import uk.ac.stfc.isis.ibex.instrument.Settings;

public class DefaultSettings implements Settings {

	private final String pvPrefix;
	
	public DefaultSettings() {
		new MachineName();
		pvPrefix = new PVPrefix(MachineName.get(), UserName.get()).get();	
	}

	@Override
	public String pvPrefix() {
		return pvPrefix;
	}
}
