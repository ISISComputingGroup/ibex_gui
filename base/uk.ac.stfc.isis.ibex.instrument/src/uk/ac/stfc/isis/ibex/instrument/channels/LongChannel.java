package uk.ac.stfc.isis.ibex.instrument.channels;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Channels;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableWritable;

public class LongChannel implements ChannelType<Long> {

	@Override
	public ClosableCachingObservable<Long> reader(String address) {
		return Channels.Longs.reader(address);
	}

	@Override
	public ClosableWritable<Long> writer(String address) {
		return Channels.Longs.writer(address);
	}
}
