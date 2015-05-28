package uk.ac.stfc.isis.ibex.instrument;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableWritable;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

public interface Channels {
	<T> ClosableCachingObservable<T> getReader(ChannelType<T> channelType, String addressSuffix);
	
	<T> ClosableWritable<T> getWriter(ChannelType<T> channelType, String addressSuffix);
}
