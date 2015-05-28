package uk.ac.stfc.isis.ibex.instrument.channels;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Channels;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableWritable;

public class EnumChannel<E extends Enum<E>> implements ChannelType<E> {

	private final Class<E> enumType;
	
	public EnumChannel(Class<E> enumType) {
		this.enumType = enumType;
	}
	
	@Override
	public ClosableCachingObservable<E> reader(String address) {
		return Channels.Enums.reader(address, enumType);
	}

	@Override
	public ClosableWritable<E> writer(String address) {
		throw new UnsupportedOperationException();
	}

}
