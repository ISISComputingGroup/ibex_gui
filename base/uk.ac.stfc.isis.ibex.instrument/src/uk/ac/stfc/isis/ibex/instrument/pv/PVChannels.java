package uk.ac.stfc.isis.ibex.instrument.pv;

import uk.ac.stfc.isis.ibex.epics.observing.BaseCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.CachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ClosingSwitchableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ObserverAdapter;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableWritable;
import uk.ac.stfc.isis.ibex.epics.writing.ClosingWritable;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

public class PVChannels implements Channels {
	
	private final PVAddressBook addresses;

	public PVChannels(PVAddressBook addresses) {
		this.addresses = addresses;
	}
	
	@Override
	public <T> ClosableCachingObservable<T> getReader(final ChannelType<T> channelType, String addressSuffix) {	
		
	    final CachingObservable<String> address = addresses.resolvePV(addressSuffix);
		final ClosingSwitchableObservable<T> channel = 
				new ClosingSwitchableObservable<>(channelType.reader(address.value()));
		
		// Update the source pv when the prefix changes
		address.subscribe(new ObserverAdapter<String>() {			
			@Override
			public void onValue(String address) {
				channel.switchTo(channelType.reader(address));
			}
		});
		
		return channel;
	}

	@Override
	public <T> ClosableWritable<T> getWriter(final ChannelType<T> channelType, String addressSuffix) {
	    final BaseCachingObservable<String> address = addresses.resolvePV(addressSuffix);
		final ClosingWritable<T> channel = new ClosingWritable<>(channelType.writer(address.value()));
		
		// Update the source pv when the prefix changes
		address.subscribe(new ObserverAdapter<String>() {			
			@Override
			public void onValue(String address) {
				channel.setWritable(channelType.writer(address));
			}
		});
		
		return channel;	}	
}
