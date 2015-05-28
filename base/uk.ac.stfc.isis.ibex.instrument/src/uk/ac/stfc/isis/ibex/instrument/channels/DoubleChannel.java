package uk.ac.stfc.isis.ibex.instrument.channels;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Channels;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableWritable;

public class DoubleChannel implements ChannelType<Double> {

	@Override
	public ClosableCachingObservable<Double> reader(String address) {
		return Channels.Doubles.reader(address);
	}

	@Override
	public ClosableWritable<Double> writer(String address) {
		return Channels.Doubles.writer(address);
	}
}
