package uk.ac.stfc.isis.ibex.configserver.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.pv.ChannelTypeLookup;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

public class ChannelResolver extends TransformingObservable<Collection<PV>, ChannelResolver> {
	
	private Map<String, PV> lookup = new HashMap<>();
	
	public ChannelResolver(ClosableCachingObservable<Collection<PV>> source) {
		setSource(source);
	}

	public ChannelType<String> get(String address) {	
		return ChannelTypeLookup.get(getType(address));
	}
	
	@Override
	protected ChannelResolver transform(Collection<PV> value) {
		lookup.clear();
		for (PV pv : value) {
			lookup.put(pv.getAddress(), pv);
		}
		
		return this;
	}
	
	private String getType(String address) {
		if (lookup.containsKey(address)) {
			return lookup.get(address).type();
		}
		
		return "";
	}
}
