package uk.ac.stfc.isis.ibex.instrument.channels;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Channels;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableWritable;

public class FloatArrayChannel implements ChannelType<float[]> {

	@Override
	public ClosableCachingObservable<float[]> reader(String address) {
		return Channels.Floats.reader(address);
	}

	@Override
	public ClosableWritable<float[]> writer(String address) {
		throw new UnsupportedOperationException();
	}
}
