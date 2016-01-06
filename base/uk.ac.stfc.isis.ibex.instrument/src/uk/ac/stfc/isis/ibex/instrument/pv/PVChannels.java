
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

package uk.ac.stfc.isis.ibex.instrument.pv;

import uk.ac.stfc.isis.ibex.epics.observing.BaseCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ClosingSwitchableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.SimpleObserver;
import uk.ac.stfc.isis.ibex.epics.writing.BaseWritable;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWritable;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

public class PVChannels implements Channels {
	
	private final PVAddressBook addresses;

	public PVChannels(PVAddressBook addresses) {
		this.addresses = addresses;
	}
	
	@Override
	public <T> ClosableCachingObservable<T> getReader(final ChannelType<T> channelType, String addressSuffix) {		
	    final BaseCachingObservable<String> address = addresses.resolvePV(addressSuffix);
		final ClosingSwitchableObservable<T> channel = 
				new ClosingSwitchableObservable<>(channelType.reader(address.getValue()));
		
		// Update the source pv when the prefix changes
		address.addObserver(new SimpleObserver<String>() {			
			@Override
			public void onValue(String address) {
				channel.switchTo(channelType.reader(address));
			}
		});
		
		return channel;
	}
	
	@Override
	public <T> ClosableCachingObservable<T> getReader(final ChannelType<T> channelType, String addressSuffix, PVType pvType) {	
	    final BaseCachingObservable<String> address = addresses.resolvePV(addressSuffix, pvType);
		final ClosingSwitchableObservable<T> channel = 
				new ClosingSwitchableObservable<>(channelType.reader(address.getValue()));
		
		// Update the source pv when the prefix changes
		address.addObserver(new SimpleObserver<String>() {			
			@Override
			public void onValue(String address) {
				channel.switchTo(channelType.reader(address));
			}
		});
		
		return channel;
	}

	@Override
    public <T> BaseWritable<T> getWriter(final ChannelType<T> channelType, String addressSuffix) {
	    final BaseCachingObservable<String> address = addresses.resolvePV(addressSuffix);
		final SameTypeWritable<T> channel = new SameTypeWritable<>(channelType.writer(address.getValue()));
		
		// Update the source pv when the prefix changes
		address.addObserver(new SimpleObserver<String>() {			
			@Override
			public void onValue(String address) {
				channel.setWritable(channelType.writer(address));
			}
		});
		
		return channel;	}	
	
	@Override
    public <T> BaseWritable<T> getWriter(final ChannelType<T> channelType, String addressSuffix, PVType pvType) {
	    final BaseCachingObservable<String> address = addresses.resolvePV(addressSuffix, pvType);
		final SameTypeWritable<T> channel = new SameTypeWritable<>(channelType.writer(address.getValue()));
		
		// Update the source pv when the prefix changes
		address.addObserver(new SimpleObserver<String>() {			
			@Override
			public void onValue(String address) {
				channel.setWritable(channelType.writer(address));
			}
		});
		
		return channel;	}
}
