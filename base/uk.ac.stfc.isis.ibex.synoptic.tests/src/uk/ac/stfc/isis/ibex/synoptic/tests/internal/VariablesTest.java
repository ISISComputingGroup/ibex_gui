
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

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;

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
    private static final String DELETE = "DELETE";

    private Variables variables;
    private WritableFactory closingWritableFactory;
    private WritableFactory switchingWritableFactory;
    private ObservableFactory closingObservableFactory;
    private ObservableFactory switchingObservableFactory;

    private Writable mockWritable = mock(Writable.class);
	
	/**
	 * Code to generate the required components
	 */
	@Before
	public void set_up() {
		// Arrange
        SwitchableObservable mockSwitchableObservable = mock(SwitchableObservable.class);
//
//        ForwardingWritable mockClosableWritable = mock(ForwardingWritable.class);


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
    public void synopticSetter_is_initialised_pointing_at_correct_pv() {
        // Arrange
        Writable expectedResult = mock(Writable.class);
        when(switchingWritableFactory.getSwitchableWritable(any(ChannelType.class),
                eq(pvPrefix + SYNOPTIC_ADDRESS + SET_DETAILS))).thenReturn(expectedResult);

        // Act
        variables = createVariables();

        // Assert
        assertEquals(expectedResult, variables.synopticSetter);
        assertNotEquals(mockWritable, variables.synopticSetter);
	}

    @Test
    public void
            synopticsDeleter_is_initialised_as_writable_converting_string_collection_to_json_string_to_correct_pv() {
        // Arrange
        Writable expectedDestination = mock(Writable.class);
        when(switchingWritableFactory.getSwitchableWritable(any(ChannelType.class),
                eq(pvPrefix + SYNOPTIC_ADDRESS + DELETE))).thenReturn(expectedDestination);

        Collection<String> inputValue = new ArrayList<String>();
        inputValue.add("name 1");
        inputValue.add("name 2");
        String convertedValue = "[\"name 1\",\"name 2\"]";

        // Act
        variables = createVariables();

        // Assert
        Writable deleter = variables.synopticsDeleter;
        verify(expectedDestination, never()).write(any());
        deleter.write(inputValue);
        verify(expectedDestination, times(1)).write(convertedValue);
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
