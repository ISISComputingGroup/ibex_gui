
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableObservable;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;
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
    private final String pvPrefix = "PVPrefix";
    private final String synopticPV = "Synoptic PV";
    private static final String SYNOPTIC_ADDRESS = "CS:BLOCKSERVER:SYNOPTICS:";
    private static final String SET_DETAILS = "SET_DETAILS";

    private Variables variables;
    private WritableFactory closingWritableFactory;
    private WritableFactory switchingWritableFactory;
    private ObservableFactory closingObservableFactory;
    private ObservableFactory switchingObservableFactory;
	
	/**
	 * Code to generate the required components
	 */
	@Before
	public void set_up() {
		// Arrange
        SwitchableObservable mockSwitchableObservable = mock(SwitchableObservable.class);
//
//        ForwardingWritable mockClosableWritable = mock(ForwardingWritable.class);

        Writable mockWritable = mock(Writable.class);
//
        closingWritableFactory = mock(WritableFactory.class);
//        when(closingWritableFactory.getSwitchableWritable(any(ChannelType.class), any(String.class)))
//                .thenReturn(mockClosableWritable);
//        
        switchingWritableFactory = mock(WritableFactory.class);
        when(switchingWritableFactory.getSwitchableWritable(any(ChannelType.class), any(String.class)))
                .thenReturn(mockWritable);
//        when(switchingWritableFactory.getSwitchableWritable(any(ChannelType.class), any(String.class)))
//                .thenReturn(mockClosableWritable);
//
        closingObservableFactory = mock(ObservableFactory.class);
//        when(closingObservableFactory.getSwitchableObservable(any(ChannelType.class), any(String.class)))
//                .thenReturn(switchableObservable);
//        
        switchingObservableFactory = mock(ObservableFactory.class);
        when(switchingObservableFactory.getSwitchableObservable(any(ChannelType.class), any(String.class)))
                .thenReturn(mockSwitchableObservable);
	}

    private Variables createVariables() {
        return new Variables(closingWritableFactory, switchingWritableFactory, closingObservableFactory,
                switchingObservableFactory, pvPrefix);
	}
	
	@Test
    public void renameMe() {
        // Arrange
        Writable expectedResult = mock(Writable.class);
        when(switchingWritableFactory.getSwitchableWritable(any(ChannelType.class),
                eq(pvPrefix + SYNOPTIC_ADDRESS + SET_DETAILS))).thenReturn(expectedResult);

        // Act
        variables = createVariables();

        // Assert
        assertEquals(expectedResult, variables.setSynoptic);
	}

//	/**
//	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#toSynopticInfo()}.
//	 */
//	@Test
//	public final void variables_to_synoptic_info() {
//		// Act
//		Converter<String, Collection<SynopticInfo>> actual = variables.toSynopticInfo();
//
//		// Assert
//		assertNotNull(actual);
//	}
//
//	/**
//	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#getSynopticDescription(java.lang.String)}.
//	 */
//	@Test
//	public final void get_synoptic_description() {
//		// Act
//		Object actual = variables.getSynopticDescription(synopticPV);
//		
//		// Assert
//		assertNotNull(actual);
//	}
//
//	/**
//	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#defaultReader(java.lang.String)}.
//	 */
//	@Test
//	public final void get_default_reader_address() {
//		// Act
//        ForwardingObservable<String> actual = variables.defaultReaderRemote(synopticPV);
//		
//		// Assert
//		assertNotNull(actual);
//	}
//
//	/**
//	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#defaultReader(java.lang.String, uk.ac.stfc.isis.ibex.instrument.pv.PVType)}.
//	 */
//	@Test
//	public final void get_default_reader_address_and_local() {
//		// Act
//        ForwardingObservable<String> actual = variables.defaultReaderRemote(synopticPV);
//		
//		// Assert
//		assertNotNull(actual);
//	}
//	
//	/**
//	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#defaultReader(java.lang.String, uk.ac.stfc.isis.ibex.instrument.pv.PVType)}.
//	 */
//	@Test
//	public final void get_default_reader_address_and_remote() {
//		// Act
//        ForwardingObservable<String> actual = variables.defaultReaderRemote(synopticPV);
//		
//		// Assert
//		assertNotNull(actual);
//	}
//
//	/**
//	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#defaultReaderWithoutUnits(java.lang.String)}.
//	 */
//	@Test
//	public final void get_default_reader_without_units_address() {
//		// Act
//        ForwardingObservable<String> actual = variables.defaultReaderRemoteWithoutUnits(synopticPV);
//		
//		// Assert
//		assertNotNull(actual);
//	}
//
//	/**
//	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#defaultWritable(java.lang.String)}.
//	 */
//	@Test
//	public final void get_default_writable_address() {
//		// Act
//        Writable<String> actual = variables.defaultWritableRemote(synopticPV);
//		
//		// Assert
//		assertNotNull(actual);
//	}
//
//	/**
//	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#defaultWritable(java.lang.String, uk.ac.stfc.isis.ibex.instrument.pv.PVType)}.
//	 */
//	@Test
//	public final void get_default_writable_address_and_local() {
//		// Act
//        Writable<String> actual = variables.defaultWritableRemote(synopticPV);
//		
//		// Assert
//		assertNotNull(actual);
//	}
//	
//	/**
//	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.Variables#defaultWritable(java.lang.String, uk.ac.stfc.isis.ibex.instrument.pv.PVType)}.
//	 */
//	@Test
//	public final void get_default_writable_address_and_remote() {
//		// Act
//        Writable<String> actual = variables.defaultWritableRemote(synopticPV);
//		
//		// Assert
//		assertNotNull(actual);
//	}

}
