package uk.ac.stfc.isis.ibex.instrument.pv;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import uk.ac.stfc.isis.ibex.epics.observing.BaseCachingObservable;

public class PVAddressBook {
	
	private String prefix;
	private final Map<String, PVAddress> addresses = new ConcurrentHashMap<>();
	
	public PVAddressBook(String prefix) {
		this.prefix = prefix;
	}

	public BaseCachingObservable<String> resolvePV(String suffix) {
		if (addresses.containsKey(suffix)) {
			return addresses.get(suffix);
		}
		
		PVAddress address = new PVAddress(prefix, suffix);
		addresses.put(suffix, address);
		return address;
	}
	
	public void setPrefix(String newPrefix) {
		this.prefix = newPrefix;
		for (PVAddress address : addresses.values()) {
			address.setPrefix(newPrefix);
		}			
	}
}
