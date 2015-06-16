package uk.ac.stfc.isis.ibex.instrument.channels;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Channels;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableWritable;

public class NumberWithUnitsChannel implements ChannelType<String> {

	@Override
	public ClosableCachingObservable<String> reader(String address) {
		return Channels.Numbers.readerWithUnits(address);
	}

	@Override
	public ClosableWritable<String> writer(String address) {
		throw new UnsupportedOperationException();
	}

}
