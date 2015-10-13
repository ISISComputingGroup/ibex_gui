
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

package uk.ac.stfc.isis.ibex.instrument.pv.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.BaseCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableWritable;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;
import uk.ac.stfc.isis.ibex.instrument.pv.PVAddressBook;
import uk.ac.stfc.isis.ibex.instrument.pv.PVChannels;
import uk.ac.stfc.isis.ibex.instrument.pv.PVType;

/**
 * This class is responsible for testing the PV Channels class 
 *
 */
@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:methodname", "unchecked" })
public class PVChannelsTest {

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.instrument.pv.PVChannels#getReader(uk.ac.stfc.isis.ibex.instrument.channels.ChannelType, java.lang.String)}.
	 */
	@Test
	public final void get_reader_string_only() {
		// Arrange
		String address = "Address";
		String channelValue = "Channel";
		// Mock a subscription
		Subscription sub = mock(Subscription.class);
		// Mock the observable for the pv values
		BaseCachingObservable<String> mockPvValue = mock(BaseCachingObservable.class);
		when(mockPvValue.getValue()).thenReturn(address);
		when(mockPvValue.addObserver(any(Observer.class))).thenReturn(sub);
		// Mock the address book to use the mock pv value
		PVAddressBook mockAddresses = mock(PVAddressBook.class);
		when(mockAddresses.resolvePV(address)).thenReturn(mockPvValue);
		// Mock an observable
		ClosableCachingObservable<String> mockChannelValue = mock(ClosableCachingObservable.class);
		when(mockChannelValue.getValue()).thenReturn(channelValue);
		// Mock a channel
		ChannelType<String> mockChannelType = mock(ChannelType.class);
		when(mockChannelType.reader(address)).thenReturn(mockChannelValue);
		// Generate the class under test		
		PVChannels pvChannels = new PVChannels(mockAddresses);
		// Act
		ClosableCachingObservable<String> reader = pvChannels.getReader(mockChannelType, address);
		// Assert
		assertEquals(channelValue, reader.getValue());
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.instrument.pv.PVChannels#getReader(uk.ac.stfc.isis.ibex.instrument.channels.ChannelType, java.lang.String, uk.ac.stfc.isis.ibex.instrument.pv.PVType)}.
	 */
	@Test
	public final void get_reader_string_and_local_type() {
		// Arrange
		String address = "Address";
		String channelValue = "Channel";
		PVType pvType = PVType.LOCAL_PV;
		// Mock a subscription
		Subscription sub = mock(Subscription.class);
		// Mock the observable for the pv values
		BaseCachingObservable<String> mockPvValue = mock(BaseCachingObservable.class);
		when(mockPvValue.getValue()).thenReturn(address);
		when(mockPvValue.addObserver(any(Observer.class))).thenReturn(sub);
		// Mock the address book to use the mock pv value
		PVAddressBook mockAddresses = mock(PVAddressBook.class);
		when(mockAddresses.resolvePV(address, pvType)).thenReturn(mockPvValue);
		// Mock an observable
		ClosableCachingObservable<String> mockChannelValue = mock(ClosableCachingObservable.class);
		when(mockChannelValue.getValue()).thenReturn(channelValue);
		// Mock a channel
		ChannelType<String> mockChannelType = mock(ChannelType.class);
		when(mockChannelType.reader(address)).thenReturn(mockChannelValue);
		// Generate the class under test		
		PVChannels pvChannels = new PVChannels(mockAddresses);
		// Act
		ClosableCachingObservable<String> reader = pvChannels.getReader(mockChannelType, address, pvType);
		// Assert
		assertEquals(channelValue, reader.getValue());
	}
	
	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.instrument.pv.PVChannels#getReader(uk.ac.stfc.isis.ibex.instrument.channels.ChannelType, java.lang.String, uk.ac.stfc.isis.ibex.instrument.pv.PVType)}.
	 */
	@Test
	public final void get_reader_string_and_remote_type() {
		// Arrange
		String address = "Address";
		String channelValue = "Channel";
		PVType pvType = PVType.REMOTE_PV;
		// Mock a subscription
		Subscription sub = mock(Subscription.class);
		// Mock the observable for the pv values
		BaseCachingObservable<String> mockPvValue = mock(BaseCachingObservable.class);
		when(mockPvValue.getValue()).thenReturn(address);
		when(mockPvValue.addObserver(any(Observer.class))).thenReturn(sub);
		// Mock the address book to use the mock pv value
		PVAddressBook mockAddresses = mock(PVAddressBook.class);
		when(mockAddresses.resolvePV(address, pvType)).thenReturn(mockPvValue);
		// Mock an observable
		ClosableCachingObservable<String> mockChannelValue = mock(ClosableCachingObservable.class);
		when(mockChannelValue.getValue()).thenReturn(channelValue);
		// Mock a channel
		ChannelType<String> mockChannelType = mock(ChannelType.class);
		when(mockChannelType.reader(address)).thenReturn(mockChannelValue);
		// Generate the class under test		
		PVChannels pvChannels = new PVChannels(mockAddresses);
		// Act
		ClosableCachingObservable<String> reader = pvChannels.getReader(mockChannelType, address, pvType);
		// Assert
		assertEquals(channelValue, reader.getValue());
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.instrument.pv.PVChannels#getWriter(uk.ac.stfc.isis.ibex.instrument.channels.ChannelType, java.lang.String)}.
	 */
	@Test
	public final void get_writer_string_only() {
		// Arrange
		String address = "Address";
		Boolean returnWrite = true;
		// Mock a subscription
		Subscription sub = mock(Subscription.class);
		// Mock the observable for the pv values
		BaseCachingObservable<String> mockPvValue = mock(BaseCachingObservable.class);
		when(mockPvValue.getValue()).thenReturn(address);
		when(mockPvValue.addObserver(any(Observer.class))).thenReturn(sub);
		// Mock the address book to use the mock pv value
		PVAddressBook mockAddresses = mock(PVAddressBook.class);
		when(mockAddresses.resolvePV(address)).thenReturn(mockPvValue);
		// Mock an observable
		ClosableWritable<String> mockChannelValue = mock(ClosableWritable.class);
		when(mockChannelValue.canWrite()).thenReturn(returnWrite);
		// Mock a channel
		ChannelType<String> mockChannelType = mock(ChannelType.class);
		when(mockChannelType.writer(address)).thenReturn(mockChannelValue);
		// Generate the class under test		
		PVChannels pvChannels = new PVChannels(mockAddresses);
		// Act
		ClosableWritable<String> writer = pvChannels.getWriter(mockChannelType, address);
		// Assert
		assertEquals(returnWrite, writer.canWrite());
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.instrument.pv.PVChannels#getWriter(uk.ac.stfc.isis.ibex.instrument.channels.ChannelType, java.lang.String, uk.ac.stfc.isis.ibex.instrument.pv.PVType)}.
	 */
	@Test
	public final void get_writer_string_and_local_type() {
		// Arrange
		String address = "Address";
		Boolean returnWrite = true;
		PVType pvType = PVType.LOCAL_PV;
		// Mock a subscription
		Subscription sub = mock(Subscription.class);
		// Mock the observable for the pv values
		BaseCachingObservable<String> mockPvValue = mock(BaseCachingObservable.class);
		when(mockPvValue.getValue()).thenReturn(address);
		when(mockPvValue.addObserver(any(Observer.class))).thenReturn(sub);
		// Mock the address book to use the mock pv value
		PVAddressBook mockAddresses = mock(PVAddressBook.class);
		when(mockAddresses.resolvePV(address, pvType)).thenReturn(mockPvValue);
		// Mock an observable
		ClosableWritable<String> mockChannelValue = mock(ClosableWritable.class);
		when(mockChannelValue.canWrite()).thenReturn(returnWrite);
		// Mock a channel
		ChannelType<String> mockChannelType = mock(ChannelType.class);
		when(mockChannelType.writer(address)).thenReturn(mockChannelValue);
		// Generate the class under test		
		PVChannels pvChannels = new PVChannels(mockAddresses);
		// Act
		ClosableWritable<String> writer = pvChannels.getWriter(mockChannelType, address, pvType);
		// Assert
		assertEquals(returnWrite, writer.canWrite());
	}
	
	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.instrument.pv.PVChannels#getWriter(uk.ac.stfc.isis.ibex.instrument.channels.ChannelType, java.lang.String, uk.ac.stfc.isis.ibex.instrument.pv.PVType)}.
	 */
	@Test
	public final void get_writer_string_and_remote_type() {
		// Arrange
		String address = "Address";
		Boolean returnWrite = true;
		PVType pvType = PVType.REMOTE_PV;
		// Mock a subscription
		Subscription sub = mock(Subscription.class);
		// Mock the observable for the pv values
		BaseCachingObservable<String> mockPvValue = mock(BaseCachingObservable.class);
		when(mockPvValue.getValue()).thenReturn(address);
		when(mockPvValue.addObserver(any(Observer.class))).thenReturn(sub);
		// Mock the address book to use the mock pv value
		PVAddressBook mockAddresses = mock(PVAddressBook.class);
		when(mockAddresses.resolvePV(address, pvType)).thenReturn(mockPvValue);
		// Mock an observable
		ClosableWritable<String> mockChannelValue = mock(ClosableWritable.class);
		when(mockChannelValue.canWrite()).thenReturn(returnWrite);
		// Mock a channel
		ChannelType<String> mockChannelType = mock(ChannelType.class);
		when(mockChannelType.writer(address)).thenReturn(mockChannelValue);
		// Generate the class under test		
		PVChannels pvChannels = new PVChannels(mockAddresses);
		// Act
		ClosableWritable<String> writer = pvChannels.getWriter(mockChannelType, address, pvType);
		// Assert
		assertEquals(returnWrite, writer.canWrite());
	}

}
