
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
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
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

    private Collection<InstrumentInfo> doFiltering(Collection<InstrumentInfo> input) {
        return InstrumentListUtils.filterValidInstruments(input, mockLogger);
    }
}

