
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2015
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
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

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableObservable;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannelWithoutUnits;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.synoptic.internal.Variables;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;

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
    private static final String NAMES = "NAMES";
    private static final String SCHEMA = "SCHEMA";
    private static final String GET_SYNOPTIC = ":GET";

    private Variables variables;
    private WritableFactory closingWritableFactory;
    private WritableFactory switchingWritableFactory;
    private ObservableFactory closingObservableFactory;
    private ObservableFactory switchingObservableFactory;

    private Writable defaultWritable = mock(Writable.class);
    private Writable mockWritable;
    private SwitchableObservable defaultSwitchableObservable = mock(SwitchableObservable.class);
    private SwitchableObservable mockSwitchableObservable;

    /**
     * Code to generate the required components
     */
    @Before
    public void set_up() {
        // Arrange
//
//        ForwardingWritable mockClosableWritable = mock(ForwardingWritable.class);

        mockWritable = mock(Writable.class);
        mockSwitchableObservable = mock(SwitchableObservable.class);
//
        closingWritableFactory = mock(WritableFactory.class);
        when(closingWritableFactory.getSwitchableWritable(any(ChannelType.class), any(String.class)))
                .thenReturn(defaultWritable);

        switchingWritableFactory = mock(WritableFactory.class);
        when(switchingWritableFactory.getSwitchableWritable(any(ChannelType.class), any(String.class)))
                .thenReturn(defaultWritable);
//        when(switchingWritableFactory.getSwitchableWritable(any(ChannelType.class), any(String.class)))
//                .thenReturn(mockClosableWritable);
//
        closingObservableFactory = mock(ObservableFactory.class);
        when(closingObservableFactory.getSwitchableObservable(any(ChannelType.class), anyString()))
                .thenReturn(defaultSwitchableObservable);
//        
        switchingObservableFactory = mock(ObservableFactory.class);
        when(switchingObservableFactory.getSwitchableObservable(any(ChannelType.class), any(String.class)))
                .thenReturn(defaultSwitchableObservable);
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
        assertSame(expectedResult, variables.synopticSetter);
        assertNotEquals(defaultWritable, variables.synopticSetter);
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

    @Test
    public void available_is_initialised_as_collection_of_synoptic_info_for_available_synoptics() {
        // Arrange
        String input =
                "[{\"is_default\": true, \"pv\": \"__BLANK__\", \"name\": \"-- NONE --\"}, {\"is_default\": false, \"pv\": \"CHOPPER\", \"name\": \"chopper\"}]";
        String expectedName1 = "-- NONE --";
        String expectedName2 = "chopper";
        String expectedPV1 = "__BLANK__";
        String expectedPV2 = "CHOPPER";
        when(mockSwitchableObservable.getValue()).thenReturn(input);
        when(mockSwitchableObservable.lastError()).thenReturn(null);
        when(mockSwitchableObservable.isConnected()).thenReturn(true);

        when(switchingObservableFactory.getSwitchableObservable(any(ChannelType.class),
                eq(pvPrefix + SYNOPTIC_ADDRESS + NAMES))).thenReturn(mockSwitchableObservable);

        // Act
        variables = createVariables();
        
        // Assert
        ForwardingObservable<Collection<SynopticInfo>> available = variables.available;
        Collection<SynopticInfo> result = available.getValue();
        ArrayList<SynopticInfo> resultAsList = new ArrayList<>(result);
        assertEquals(2, resultAsList.size());
        assertEquals(expectedName1, resultAsList.get(0).name());
        assertEquals(expectedName2, resultAsList.get(1).name());
        assertEquals(expectedPV1, resultAsList.get(0).pv());
        assertEquals(expectedPV2, resultAsList.get(1).pv());
        assertTrue(resultAsList.get(0).isDefault());
        assertFalse(resultAsList.get(1).isDefault());
    }

    @Test
    public void synopticsSchema_is_initialised_pointing_at_correct_pv() {
        // Arrange
        when(switchingObservableFactory.getSwitchableObservable(any(ChannelType.class),
                eq(pvPrefix + SYNOPTIC_ADDRESS + SCHEMA))).thenReturn(mockSwitchableObservable);

        // Act
        variables = createVariables();

        // Assert
        assertSame(mockSwitchableObservable, variables.synopticSchema);
        assertNotEquals(defaultSwitchableObservable, variables.synopticSchema);
    }

    @Test
    public void getSynopticDescription_reads_from_specified_pv() {
        // Arrange
        String expectedName = "Test";
        String value = "<?xml version=\"1.0\" ?>\n" + "<instrument xmlns=\"http://www.isis.stfc.ac.uk//instrument\">"
                + " <name>" + expectedName + "</name>" + "</instrument>";
        when(mockSwitchableObservable.getValue()).thenReturn(value);
        when(mockSwitchableObservable.lastError()).thenReturn(null);
        when(mockSwitchableObservable.isConnected()).thenReturn(true);

        String synopticPV = "TEST";
        when(closingObservableFactory.getSwitchableObservable(any(ChannelType.class),
                eq(pvPrefix + SYNOPTIC_ADDRESS + synopticPV + GET_SYNOPTIC))).thenReturn(mockSwitchableObservable);
        variables = createVariables();

        // Act
        ForwardingObservable<SynopticDescription> result = variables.getSynopticDescription(synopticPV);

        // Assert
        verify(mockSwitchableObservable, atLeast(1)).getValue();
        assertEquals(expectedName, result.getValue().name());
    }

    @Test
    public void defaultReaderRemote_returns_default_observable_from_specified_pv() {
        // Arrange
        String address = "Test";
        when(closingObservableFactory.getSwitchableObservable(any(DefaultChannel.class), eq(address)))
                .thenReturn(mockSwitchableObservable);
        variables = createVariables();

        // Act
        ForwardingObservable result = variables.defaultReaderRemote(address);
        
        // Assert
        assertSame(mockSwitchableObservable, result);
        assertNotEquals(defaultSwitchableObservable, result);
    }

    @Test
    public void defaultReaderRemoteWithoutUnits_returns_default_observable_without_units_from_specified_pv() {
        // Arrange
        String address = "Test";
        when(closingObservableFactory.getSwitchableObservable(any(DefaultChannelWithoutUnits.class), eq(address)))
                .thenReturn(mockSwitchableObservable);
        variables = createVariables();

        // Act
        ForwardingObservable result = variables.defaultReaderRemoteWithoutUnits(address);

        // Assert
        assertSame(mockSwitchableObservable, result);
        assertNotEquals(defaultSwitchableObservable, result);
    }

    @Test
    public void defaultWritableRemote_returns_default_writable_from_specified_pv() {
        // Arrange
        String address = "Test";
        when(closingWritableFactory.getSwitchableWritable(any(StringChannel.class), eq(address)))
                .thenReturn(mockWritable);
        variables = createVariables();

        // Act
        Writable result = variables.defaultWritableRemote(address);

        // Assert
        assertSame(mockWritable, result);
        assertNotEquals(defaultWritable, result);
    }
}
