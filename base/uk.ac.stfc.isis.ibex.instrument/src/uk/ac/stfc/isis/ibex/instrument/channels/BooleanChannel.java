package uk.ac.stfc.isis.ibex.instrument.channels;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Channels;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableWritable;

public class BooleanChannel implements ChannelType<Boolean> {

	@Override
	public ClosableCachingObservable<Boolean> reader(String address) {
		return Channels.Booleans.reader(address);
	}

	@Override
	public ClosableWritable<Boolean> writer(String address) {
		throw new UnsupportedOperationException();
	}
}
