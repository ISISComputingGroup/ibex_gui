package uk.ac.stfc.isis.ibex.instrument.channels;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableWritable;

public interface ChannelType<T> {
	
	ClosableCachingObservable<T> reader(String address);
	
	ClosableWritable<T> writer(String address);
}
