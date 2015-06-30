package uk.ac.stfc.isis.ibex.instrument.channels;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Channels;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableWritable;

/**
 * A channel for a number which will be formatted based on the PREC field of the underlying PV
 *
 */
public class NumberWithPrecisionChannel implements ChannelType<Number> {

	@Override
	public ClosableCachingObservable<Number> reader(String address) {
		return Channels.Numbers.readerWithPrecision(address);
	}

	@Override
	public ClosableWritable<Number> writer(String address) {
		throw new UnsupportedOperationException();
	}

}
