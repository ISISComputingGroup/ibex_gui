package uk.ac.stfc.isis.ibex.instrument.pv;

import uk.ac.stfc.isis.ibex.epics.observing.BaseCachingObservable;

public class PVAddress extends BaseCachingObservable<String> {
	private String prefix;
	private String suffix;
	
	public PVAddress(String prefix, String suffix) {
		this.suffix = suffix;
		setPrefix(prefix);
	}
	
	@Override
	public String toString() {
		return prefix + suffix;
	}
	
	public void setPrefix(String newPrefix) {
		prefix = newPrefix;
		setValue(this.toString());
	}
}
