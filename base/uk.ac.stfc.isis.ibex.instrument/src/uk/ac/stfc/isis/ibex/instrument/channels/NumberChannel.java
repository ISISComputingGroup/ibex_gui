package uk.ac.stfc.isis.ibex.instrument.channels;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Channels;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableWritable;

public class NumberChannel implements ChannelType<Number> {

	@Override
	public ClosableCachingObservable<Number> reader(String address) {
		return Channels.Numbers.reader(address);
	}

	@Override
	public ClosableWritable<Number> writer(String address) {
		throw new UnsupportedOperationException();
	}

}
