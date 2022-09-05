
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.instrument.list.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.internal.LocalHostInstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.list.InstrumentListUtils;

@SuppressWarnings("checkstyle:methodname")
public class InstrumentListUtilsTest {
    
    private InstrumentInfo instrument1;
    private InstrumentInfo instrument2;
    private InstrumentInfo nullNameInstrument;
    private Logger mockLogger;

    @Before
    public void setUp() {

        instrument1 = mock(InstrumentInfo.class);
        when(instrument1.name()).thenReturn("instrument1");

        instrument2 = mock(InstrumentInfo.class);
        when(instrument2.name()).thenReturn("instrument2");

        nullNameInstrument = mock(InstrumentInfo.class);
        when(nullNameInstrument.name()).thenReturn(null);

        mockLogger = mock(Logger.class);
    }

    @Test
    public void filter_valid_instruments_returns_empty_list_if_pv_value_null() {

        // Act
        Collection<InstrumentInfo> instruments = doFiltering(null);

        // Assert
        assertNotNull(instruments);
        assertTrue(instruments.isEmpty());
    }

    @Test
    public void filter_valid_instruments_logs_warning_message_if_pv_value_null() {
        // Arrange
        verify(mockLogger, never()).warn(anyString());

        // Act
        doFiltering(null);

        // Assert
        verify(mockLogger, times(1)).warn(contains("Error while parsing"));
        verify(mockLogger, times(1)).warn(contains("no instrument"));
    }

    @Test
    public void filter_valid_instruments_returns_pv_instruments_when_all_valid() {
        // Arrange
        Collection<InstrumentInfo> expected = new ArrayList<>();
        expected.add(instrument1);
        expected.add(instrument2);

        // Assert
        assertEquals(expected, doFiltering(expected));
    }

    @Test
    public void filter_valid_instruments_logs_info_message_when_all_valid() {
        // Arrange
        Collection<InstrumentInfo> expected = new ArrayList<>();
        expected.add(instrument1);
        expected.add(instrument2);

        verify(mockLogger, never()).info(anyString());

        // Act
        doFiltering(expected);

        // Assert
        verify(mockLogger, times(1)).info(contains("read successfully"));
    }

    @Test
    public void filter_valid_instruments_does_not_return_invalid_items() {
        // Arrange
        Collection<InstrumentInfo> input = new ArrayList<>();
        input.add(instrument1);
        input.add(nullNameInstrument);
        input.add(instrument2);

        Collection<InstrumentInfo> expected = new ArrayList<>();
        expected.add(instrument1);
        expected.add(instrument2);

        // Assert
        assertEquals(expected, doFiltering(input));
    }

    @Test
    public void filter_valid_instruments_logs_warning_when_invalid_items() {
        // Arrange
        Collection<InstrumentInfo> input = new ArrayList<>();
        input.add(instrument1);
        input.add(nullNameInstrument);
        input.add(instrument2);

        Collection<InstrumentInfo> expected = new ArrayList<>();
        expected.add(instrument1);
        expected.add(instrument2);

        verify(mockLogger, never()).warn(anyString());

        // Act
        doFiltering(input);

        // Assert
        verify(mockLogger, times(1)).warn(contains("Error while parsing"));
        verify(mockLogger, times(1)).warn(contains("one or more instruments"));
    }

    @Test
    public void GIVEN_no_instruments_WHEN_combine_with_localhost_THEN_list_contains_only_localhost() {
        // Arrange
        Collection<InstrumentInfo> value = new ArrayList<>();
        InstrumentInfo localhost = new InstrumentInfo("", "", "", Collections.emptyList());
        Collection<InstrumentInfo> expetedList = new ArrayList<>();
        expetedList.add(localhost);

        // Act
        Collection<InstrumentInfo> result =
                InstrumentListUtils.combineInstrumentsAndLocalHost(value, localhost);

        // Assert
        assertEquals(expetedList, result);

    }

    @Test
    public void
            GIVEN_instruments_no_host_match_WHEN_combine_with_localhost_THEN_list_contains_localhost_and_all_instruments() {
        // Arrange
        InstrumentInfo instA = new InstrumentInfo("a", "", "no match", Collections.emptyList());
        InstrumentInfo instB = new InstrumentInfo("b", "", "no match", Collections.emptyList());
        Collection<InstrumentInfo> value = new ArrayList<>();
        value.add(instA);
        value.add(instB);

        InstrumentInfo localhost = new LocalHostInstrumentInfo("machineName");

        Collection<InstrumentInfo> expetedList = new ArrayList<>();
        expetedList.add(localhost);
        expetedList.add(instA);
        expetedList.add(instB);

        // Act
        Collection<InstrumentInfo> result =
                InstrumentListUtils.combineInstrumentsAndLocalHost(value, localhost);

        // Assert
        assertEquals(expetedList, result);

    }

    @Test
    public void
            GIVEN_instruments_host_match_WHEN_combine_with_localhost_THEN_list_contains_localhost_wih_new_name_and_all_instruments_except_match() {
        // Arrange
        String machineName = "machineName";
        InstrumentInfo instA = new InstrumentInfo("a", "", machineName, Collections.emptyList());
        InstrumentInfo instB = new InstrumentInfo("b", "", "no match", Collections.emptyList());
        Collection<InstrumentInfo> value = new ArrayList<>();
        value.add(instA);
        value.add(instB);

        InstrumentInfo localhost = new LocalHostInstrumentInfo(machineName);

        // Act
        Collection<InstrumentInfo> result = InstrumentListUtils.combineInstrumentsAndLocalHost(value, localhost);

        // Assert
        assertEquals(2, result.size());
        InstrumentInfo[] results = result.toArray(new InstrumentInfo[0]);
        assertEquals(instA.name(), results[0].name());
        assertEquals("localhost", results[0].hostName());
        assertEquals(instB, results[1]);

    }

    public void
            GIVEN_instruments_WHEN_combine_with_localhost_THEN_list_is_alphebetic() {
        // Arrange
        InstrumentInfo instA = new InstrumentInfo("a", "", "no match", Collections.emptyList());
        InstrumentInfo instB = new InstrumentInfo("b", "", "no match", Collections.emptyList());
        InstrumentInfo instC = new InstrumentInfo("c", "", "no match", Collections.emptyList());

        Collection<InstrumentInfo> value = new ArrayList<>();
        value.add(instC);
        value.add(instA);
        value.add(instB);


        InstrumentInfo localhost = new LocalHostInstrumentInfo("machineName");

        Collection<InstrumentInfo> expetedList = new ArrayList<>();
        expetedList.add(localhost);
        expetedList.add(instA);
        expetedList.add(instB);
        expetedList.add(instC);

        // Act
        Collection<InstrumentInfo> result = InstrumentListUtils.combineInstrumentsAndLocalHost(value, localhost);

        // Assert
        assertEquals(expetedList, result);

    }

    @Test
    public void GIVEN_allowed_group_specified_THEN_instruments_in_other_groups_not_available() {
    	// Arrange
        Collection<InstrumentInfo> instList = new ArrayList<>();
        
        var instrument1 = new InstrumentInfo("one", "one", "one", List.of("GROUP1"));
        var instrument2 = new InstrumentInfo("two", "two", "two", List.of("GROUP2"));
        
        instList.add(instrument1);
        instList.add(instrument2);
    	
    	Collection<InstrumentInfo> expected = new ArrayList<>();
    	expected.add(instrument1);
    	
    	// Assert
    	assertEquals(expected, InstrumentListUtils.filterByAllowedGroup(instList, Optional.of(List.of("GROUP1"))));
    }
    
    @Test
    public void GIVEN_multiple_allowed_groups_specified_THEN_instruments_in_other_groups_not_available() {
    	// Arrange
        Collection<InstrumentInfo> instList = new ArrayList<>();
        
        var instrument1 = new InstrumentInfo("one", "one", "one", List.of("GROUP1"));
        var instrument2 = new InstrumentInfo("two", "two", "two", List.of("GROUP2"));
        var instrument3 = new InstrumentInfo("three", "three", "three", List.of("GROUP3"));
        
        instList.add(instrument1);
        instList.add(instrument2);
        instList.add(instrument3);
    	
    	Collection<InstrumentInfo> expected = new ArrayList<>();
    	expected.add(instrument1);
    	expected.add(instrument2);
    	
    	// Assert
    	assertEquals(expected, InstrumentListUtils.filterByAllowedGroup(instList, Optional.of(List.of("GROUP1", "GROUP2"))));
    }
    
    @Test
    public void GIVEN_allowed_groups_specified_and_instruments_in_multiple_groups_THEN_instruments_in_other_groups_not_available() {
    	// Arrange
        Collection<InstrumentInfo> instList = new ArrayList<>();
        
        var instrument1 = new InstrumentInfo("one", "one", "one", List.of("GROUP1", "GROUP2"));
        var instrument2 = new InstrumentInfo("two", "two", "two", List.of("GROUP2", "GROUP1"));
        var instrument3 = new InstrumentInfo("three", "three", "three", List.of("GROUP3"));
        
        instList.add(instrument1);
        instList.add(instrument2);
        instList.add(instrument3);
    	
    	Collection<InstrumentInfo> expected = new ArrayList<>();
    	expected.add(instrument1);
    	expected.add(instrument2);
    	
    	// Assert
    	assertEquals(expected, InstrumentListUtils.filterByAllowedGroup(instList, Optional.of(List.of("GROUP1"))));
    }
    
    @Test
    public void GIVEN_absent_instrument_allowlist_THEN_all_instruments_shown( ) {
    	// Arrange
        Collection<InstrumentInfo> instList = new ArrayList<>();
        instList.add(instrument1);
        instList.add(instrument2);
    	
    	// Assert
    	assertEquals(instList, doFiltering(instList, Optional.empty()));
    }
    
    
    private Collection<InstrumentInfo> doFiltering(Collection<InstrumentInfo> input) {
        return InstrumentListUtils.filterValidInstruments(input, mockLogger);
    }
    
    private Collection<InstrumentInfo> doFiltering(Collection<InstrumentInfo> input, Optional<ArrayList<String>> localInstList){
    	return InstrumentListUtils.filterValidInstruments(input, mockLogger);
    }
}

