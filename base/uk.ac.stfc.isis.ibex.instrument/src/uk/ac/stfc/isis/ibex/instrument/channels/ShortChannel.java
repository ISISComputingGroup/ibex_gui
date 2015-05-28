package uk.ac.stfc.isis.ibex.instrument.channels;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Channels;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableWritable;

public class ShortChannel implements ChannelType<Short> {

	@Override
	public ClosableCachingObservable<Short> reader(String address) {
		return Channels.Shorts.reader(address);
	}

	@Override
	public ClosableWritable<Short> writer(String address) {
		throw new UnsupportedOperationException();
	}
}
