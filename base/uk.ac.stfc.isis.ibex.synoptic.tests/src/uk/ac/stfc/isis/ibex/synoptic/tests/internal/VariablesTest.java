
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
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

import java.io.IOException;
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
 * This class is responsible for testing instrument.Variables.
 *
 */
@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:methodname", "unchecked" })
public class VariablesTest {

    /**
     * Items used throughout
     */
    private final String pvPrefix = "PVPrefix";
    private static final String SYNOPTIC_ADDRESS = "CS:SYNOPTICS:";
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
     * Code to generate the required components.
     */
    @Before
    public void set_up() {
        // Arrange
        mockWritable = mock(Writable.class);
        mockSwitchableObservable = mock(SwitchableObservable.class);

        closingWritableFactory = mock(WritableFactory.class);
        when(closingWritableFactory.getSwitchableWritable((ChannelType<String>) any(ChannelType.class), any(String.class)))
                .thenReturn(defaultWritable);

        switchingWritableFactory = mock(WritableFactory.class);
        when(switchingWritableFactory.getSwitchableWritable((ChannelType<String>) any(ChannelType.class), any(String.class)))
                .thenReturn(defaultWritable);

        closingObservableFactory = mock(ObservableFactory.class);
        when(closingObservableFactory.getSwitchableObservable((ChannelType<String>) any(ChannelType.class), anyString()))
                .thenReturn(defaultSwitchableObservable);

        switchingObservableFactory = mock(ObservableFactory.class);
        when(switchingObservableFactory.getSwitchableObservable((ChannelType<String>) any(ChannelType.class), any(String.class)))
                .thenReturn(defaultSwitchableObservable);
    }

    private Variables createVariables() {
        return new Variables(closingWritableFactory, switchingWritableFactory, closingObservableFactory,
                switchingObservableFactory, pvPrefix);
    }

    @Test
    public void WHEN_variables_is_initalised_THEN_synopticSetter_points_at_correct_pv() {
        // Arrange
        Writable expectedResult = mock(Writable.class);
        when(switchingWritableFactory.getSwitchableWritable((ChannelType<String>) any(ChannelType.class),
                eq(pvPrefix + SYNOPTIC_ADDRESS + SET_DETAILS))).thenReturn(expectedResult);

        // Act
        variables = createVariables();

        // Assert
        assertSame(expectedResult, variables.synopticSetter);
        assertNotEquals(defaultWritable, variables.synopticSetter);
    }

    @Test
    public void
            WHEN_variables_is_initialised_THEN_synopticsDeleter_is_a_writable_converting_string_collection_to_json_string_on_correct_pv() throws IOException {
        // Arrange
        Writable expectedDestination = mock(Writable.class);
        when(switchingWritableFactory.getSwitchableWritable((ChannelType<String>) any(ChannelType.class),
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
    public void WHEN_variables_is_initalised_THEN_available_is_a_collection_of_synoptic_info_for_available_synoptics() {
        // Arrange
        String expectedName0 = "-- NONE --";
        String expectedName1 = "chopper";
        String expectedPV0 = "__BLANK__";
        String expectedPV1 = "CHOPPER";
        boolean expectedIsDefault0 = true;
        boolean expectedIsDefault1 = false;

        String input = "[{\"is_default\": " + expectedIsDefault0
                + ", \"pv\": \"" + expectedPV0
                + "\", \"name\": \"" + expectedName0
                + "\"}, {\"is_default\": " + expectedIsDefault1 + ", \"pv\": \"" + expectedPV1
                + "\", \"name\": \"" + expectedName1 + "\"}]";

        when(mockSwitchableObservable.getValue()).thenReturn(input);
        when(mockSwitchableObservable.currentError()).thenReturn(null);
        when(mockSwitchableObservable.isConnected()).thenReturn(true);

        when(switchingObservableFactory.getSwitchableObservable((ChannelType<String>) any(ChannelType.class),
                eq(pvPrefix + SYNOPTIC_ADDRESS + NAMES))).thenReturn(mockSwitchableObservable);

        // Act
        variables = createVariables();
        
        // Assert
        ForwardingObservable<Collection<SynopticInfo>> available = variables.available;
        Collection<SynopticInfo> result = available.getValue();
        ArrayList<SynopticInfo> resultAsList = new ArrayList<>(result);
        assertEquals(2, resultAsList.size());
        assertEquals(expectedName0, resultAsList.get(0).name());
        assertEquals(expectedName1, resultAsList.get(1).name());
        assertEquals(expectedPV0, resultAsList.get(0).pv());
        assertEquals(expectedPV1, resultAsList.get(1).pv());
        assertTrue(resultAsList.get(0).isDefault());
        assertFalse(resultAsList.get(1).isDefault());
    }

    @Test
    public void WHEN_variables_is_initialised_THEN_synopticsSchema_points_at_correct_pv() {
        // Arrange
        when(switchingObservableFactory.getSwitchableObservable((ChannelType<String>) any(ChannelType.class),
                eq(pvPrefix + SYNOPTIC_ADDRESS + SCHEMA))).thenReturn(mockSwitchableObservable);

        // Act
        variables = createVariables();

        // Assert
        assertSame(mockSwitchableObservable, variables.synopticSchema);
        assertNotEquals(defaultSwitchableObservable, variables.synopticSchema);
    }

    @Test
    public void GIVEN_new_variables_WHEN_getting_synoptic_description_THEN_description_is_read_from_specified_pv() {
        // Arrange
        String expectedName = "Test";
        String value = "<?xml version=\"1.0\" ?>\n" + "<instrument xmlns=\"http://www.isis.stfc.ac.uk//instrument\">"
                + " <name>" + expectedName + "</name>" + "</instrument>";
        when(mockSwitchableObservable.getValue()).thenReturn(value);
        when(mockSwitchableObservable.currentError()).thenReturn(null);
        when(mockSwitchableObservable.isConnected()).thenReturn(true);

        String synopticPV = "TEST";
        when(closingObservableFactory.getSwitchableObservable((ChannelType<String>) any(ChannelType.class),
                eq(pvPrefix + SYNOPTIC_ADDRESS + synopticPV + GET_SYNOPTIC))).thenReturn(mockSwitchableObservable);
        variables = createVariables();

        // Act
        ForwardingObservable<SynopticDescription> result = variables.getSynopticDescription(synopticPV);

        // Assert
        verify(mockSwitchableObservable, atLeast(1)).getValue();
        assertEquals(expectedName, result.getValue().name());
    }

    @Test
    public void
            GIVEN_new_variables_WHEN_getting_defaultReaderRemote_THEN_returns_default_observable_from_specified_pv() {
        // Arrange
        String address = "Test";
        when(closingObservableFactory.getSwitchableObservable(any(DefaultChannel.class), eq(address)))
                .thenReturn(mockSwitchableObservable);
        variables = createVariables();

        // Act
        ForwardingObservable<String> result = variables.defaultReaderRemote(address);
        
        // Assert
        assertSame(mockSwitchableObservable, result);
        assertNotEquals(defaultSwitchableObservable, result);
    }

    @Test
    public void
            GIVEN_new_variables_WHEN_getting_defaultReaderRemoteWithoutUnits_THEN_returns_default_observable_without_units_from_specified_pv() {
        // Arrange
        String address = "Test";
        when(closingObservableFactory.getSwitchableObservable(any(DefaultChannelWithoutUnits.class), eq(address)))
                .thenReturn(mockSwitchableObservable);
        variables = createVariables();

        // Act
        ForwardingObservable<String> result = variables.defaultReaderRemoteWithoutUnits(address);

        // Assert
        assertSame(mockSwitchableObservable, result);
        assertNotEquals(defaultSwitchableObservable, result);
    }

    @Test
    public void
            GIVEN_new_variables_WHEN_getting_defaultWritableRemote_THEN_returns_default_writable_from_specified_pv() {
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
