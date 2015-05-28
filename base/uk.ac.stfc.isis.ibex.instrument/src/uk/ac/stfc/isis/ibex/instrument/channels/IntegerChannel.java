package uk.ac.stfc.isis.ibex.instrument.channels;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Channels;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableWritable;

public class IntegerChannel implements ChannelType<Integer> {

	@Override
	public ClosableCachingObservable<Integer> reader(String address) {
		return Channels.Integers.reader(address);
	}

	@Override
	public ClosableWritable<Integer> writer(String address) {
		throw new UnsupportedOperationException();
	}
}
