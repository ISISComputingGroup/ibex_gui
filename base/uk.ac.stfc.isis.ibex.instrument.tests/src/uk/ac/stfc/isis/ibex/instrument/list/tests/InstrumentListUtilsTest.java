
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

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.Observable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.list.InstrumentListUtils;

@SuppressWarnings("checkstyle:methodname")
public class InstrumentListUtilsTest {
    
    private Observable<Collection<InstrumentInfo>> mockObservable;
    private InstrumentInfo instrument1;
    private InstrumentInfo instrument2;
    private InstrumentInfo nullNameInstrument;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        mockObservable = mock(Observable.class);

        instrument1 = mock(InstrumentInfo.class);
        when(instrument1.name()).thenReturn("instrument1");

        instrument2 = mock(InstrumentInfo.class);
        when(instrument2.name()).thenReturn("instrument2");

        nullNameInstrument = mock(InstrumentInfo.class);
        when(nullNameInstrument.name()).thenReturn(null);
    }
    
    @Test
    public void filter_valid_instruments_returns_empty_list_if_pv_not_connected() {
        // Arrange
        when(mockObservable.isConnected()).thenReturn(false);

        // Act
        Collection<InstrumentInfo> instruments = InstrumentListUtils.filterValidInstruments(mockObservable);

        // Assert
        assertNotNull(instruments);
        assertTrue(instruments.isEmpty());
    }

    @Test
    public void filter_valid_instruments_returns_empty_list_if_pv_value_null() {
        // Arrange
        when(mockObservable.isConnected()).thenReturn(true);
        when(mockObservable.getValue()).thenReturn(null);

        // Act
        Collection<InstrumentInfo> instruments = InstrumentListUtils.filterValidInstruments(mockObservable);

        // Assert
        assertNotNull(instruments);
        assertTrue(instruments.isEmpty());
    }

    @Test
    public void filter_valid_instruments_returns_pv_instruments_when_all_valid() {
        // Arrange
        Collection<InstrumentInfo> expected = new ArrayList<>();
        expected.add(instrument1);
        expected.add(instrument2);
        int expectedSize = 2;

        when(mockObservable.isConnected()).thenReturn(true);
        when(mockObservable.getValue()).thenReturn(expected);

        // Act
        Collection<InstrumentInfo> instruments = InstrumentListUtils.filterValidInstruments(mockObservable);

        // Assert
        assertNotNull(instruments);
        assertEquals(expectedSize, instruments.size());
        assertEquals(expected, instruments);
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
        int expectedSize = 2;

        when(mockObservable.isConnected()).thenReturn(true);
        when(mockObservable.getValue()).thenReturn(input);

        // Act
        Collection<InstrumentInfo> instruments = InstrumentListUtils.filterValidInstruments(mockObservable);

        // Assert
        assertNotNull(instruments);
        assertEquals(expectedSize, instruments.size());
        assertEquals(expected, instruments);
    }
}

