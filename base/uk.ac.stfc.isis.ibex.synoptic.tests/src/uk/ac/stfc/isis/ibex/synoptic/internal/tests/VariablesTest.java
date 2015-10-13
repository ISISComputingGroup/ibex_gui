
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

package uk.ac.stfc.isis.ibex.synoptic.internal.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.conversion.Convert;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;
import uk.ac.stfc.isis.ibex.instrument.pv.PVType;
import uk.ac.stfc.isis.ibex.json.JsonDeserialisingConverter;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.synoptic.internal.Variables;

/**
 * This class is responsible for testing instrument.Variables 
 *
 */
@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:methodname", "unchecked" })
public class VariablesTest {
	
	/**
	 * Items used throughout
	 */
	private Variables variables;
	private String synopticPV = "Synoptic PV";
	
	/**
	 * Code to generate the required components
	 */
	@Before
	public void set_up(){
		// Arrange
		ClosableCachingObservable mockCloseableCachingObservable = mock(ClosableCachingObservable.class);
		
		ClosableWritable mockClosableWritable = mock(ClosableWritable.class);
		
		Channels mockChannels = mock(Channels.class);
		when(mockChannels.getReader(any(ChannelType.class), any(String.class))).thenReturn(mockCloseableCachingObservable);
		when(mockChannels.getWriter(any(ChannelType.class), any(String.class))).thenReturn(mockClosableWritable);
		when(mockChannels.getReader(any(ChannelType.class), any(String.class), any(PVType.class))).thenReturn(mockCloseableCachingObservable);
		when(mockChannels.getWriter(any(ChannelType.class), any(String.class), any(PVType.class))).thenReturn(mockClosableWritable);
		
		variables = new Variables(mockChannels);
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#toSynopticInfo()}.
	 */
	@Test
	public final void variables_to_synoptic_info() {
		// Act
		Converter<String, Collection<SynopticInfo>> actual = variables.toSynopticInfo();

		// Assert
		assertNotNull(actual);
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#getSynopticDescription(java.lang.String)}.
	 */
	@Test
	public final void get_synoptic_description() {
		// Act
		Object actual = variables.getSynopticDescription(synopticPV );
		
		// Assert
		assertNotNull(actual);
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#defaultReader(java.lang.String)}.
	 */
	@Test
	public final void get_default_reader_address() {
		// Act
		InitialiseOnSubscribeObservable<String> actual = variables.defaultReader(synopticPV);
		
		// Assert
		assertNotNull(actual);
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#defaultReader(java.lang.String, uk.ac.stfc.isis.ibex.instrument.pv.PVType)}.
	 */
	@Test
	public final void get_default_reader_address_and_local() {
		// Act
		InitialiseOnSubscribeObservable<String> actual = variables.defaultReader(synopticPV, PVType.LOCAL_PV);
		
		// Assert
		assertNotNull(actual);
	}
	
	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#defaultReader(java.lang.String, uk.ac.stfc.isis.ibex.instrument.pv.PVType)}.
	 */
	@Test
	public final void get_default_reader_address_and_remote() {
		// Act
		InitialiseOnSubscribeObservable<String> actual = variables.defaultReader(synopticPV, PVType.REMOTE_PV);
		
		// Assert
		assertNotNull(actual);
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#defaultReaderWithoutUnits(java.lang.String)}.
	 */
	@Test
	public final void get_default_reader_without_units_address() {
		// Act
		InitialiseOnSubscribeObservable<String> actual = variables.defaultReaderWithoutUnits(synopticPV);
		
		// Assert
		assertNotNull(actual);
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#defaultReaderWithoutUnits(java.lang.String, uk.ac.stfc.isis.ibex.instrument.pv.PVType)}.
	 */
	@Test
	public final void get_default_reader_without_units_address_and_local() {
		// Act
		InitialiseOnSubscribeObservable<String> actual = variables.defaultReaderWithoutUnits(synopticPV, PVType.LOCAL_PV);
		
		// Assert
		assertNotNull(actual);
	}
	
	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#defaultReaderWithoutUnits(java.lang.String, uk.ac.stfc.isis.ibex.instrument.pv.PVType)}.
	 */
	@Test
	public final void get_default_reader_without_units_address_and_remote() {
		// Act
		InitialiseOnSubscribeObservable<String> actual = variables.defaultReaderWithoutUnits(synopticPV, PVType.REMOTE_PV);
		
		// Assert
		assertNotNull(actual);
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#defaultWritable(java.lang.String)}.
	 */
	@Test
	public final void get_default_writable_address() {
		// Act
		Writable<String> actual = variables.defaultWritable(synopticPV);
		
		// Assert
		assertNotNull(actual);
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#defaultWritable(java.lang.String, uk.ac.stfc.isis.ibex.instrument.pv.PVType)}.
	 */
	@Test
	public final void get_default_writable_address_and_local() {
		// Act
		Writable<String> actual = variables.defaultWritable(synopticPV, PVType.LOCAL_PV);
		
		// Assert
		assertNotNull(actual);
	}
	
	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#defaultWritable(java.lang.String, uk.ac.stfc.isis.ibex.instrument.pv.PVType)}.
	 */
	@Test
	public final void get_default_writable_address_and_remote() {
		// Act
		Writable<String> actual = variables.defaultWritable(synopticPV, PVType.REMOTE_PV);
		
		// Assert
		assertNotNull(actual);
	}

}
