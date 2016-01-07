
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

package uk.ac.stfc.isis.ibex.synoptic.tests.internal;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableInitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;
import uk.ac.stfc.isis.ibex.instrument.pv.PVType;
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
        SwitchableInitialiseOnSubscribeObservable switchableObservable = mock(
                SwitchableInitialiseOnSubscribeObservable.class);

        SameTypeWritable mockClosableWritable = mock(SameTypeWritable.class);

        WritableFactory closingWritableFactory = mock(WritableFactory.class);
        when(closingWritableFactory.getSwitchableWritable(any(ChannelType.class), any(String.class)))
                .thenReturn(mockClosableWritable);
        
        WritableFactory switchingWritableFactory = mock(WritableFactory.class);
        when(switchingWritableFactory.getSwitchableWritable(any(ChannelType.class), any(String.class)))
                .thenReturn(mockClosableWritable);

        ObservableFactory closingObservableFactory = mock(ObservableFactory.class);
        when(closingObservableFactory.getSwitchableObservable(any(ChannelType.class), any(String.class)))
                .thenReturn(switchableObservable);
        
        ObservableFactory switchingObservableFactory = mock(ObservableFactory.class);
        when(switchingObservableFactory.getSwitchableObservable(any(ChannelType.class), any(String.class)))
                .thenReturn(switchableObservable);

        variables = new Variables(closingWritableFactory, switchingWritableFactory, closingObservableFactory,
                switchingObservableFactory, "PVPrefix");
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
