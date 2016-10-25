
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

package uk.ac.stfc.isis.ibex.instrumentinfo.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.custom.CustomInstrumentInfo;

@SuppressWarnings("checkstyle:methodname")
public class CustomInstrumentInfoTest {

    @Test
    public final void GIVEN_name_WHEN_custom_instrument_created_THEN_host_name_equals_name() {
        // Arrange
        String name = "myInstrument";

        // Act
        CustomInstrumentInfo instrument = new CustomInstrumentInfo(name, "pvPrefix");

        // Assert
        assertEquals(name, instrument.name());
        assertEquals(name, instrument.hostName());
    }

    @Test
    public final void GIVEN_pv_prefix_WHEN_custom_instrument_created_THEN_pv_prefix_matches_input() {
        // Arrange
        String pvPrefix = "prefix";

        // Act
        CustomInstrumentInfo instrument = new CustomInstrumentInfo("name", pvPrefix);

        // Assert
        assertEquals(pvPrefix, instrument.pvPrefix());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void GIVEN_empty_pv_prefix_WHEN_custom_instrument_created_THEN_illegal_argument_exception_thrown() {
        // Arrange

        // Act
        new CustomInstrumentInfo("name", "");

        // Assert
    }

    @Test(expected = RuntimeException.class)
    public final void GIVEN_null_pv_prefix_WHEN_custom_instrument_created_THEN_runtime_exception_thrown() {
        // Arrange

        // Act
        new CustomInstrumentInfo("name", null);

        // Assert
    }

    @Test
    public final void GIVEN_null_name_WHEN_custom_instrument_created_THEN_host_name_is_NDXnull() {
        // Arrange

        // Act

        // Assert
        assertEquals("NDXnull", new CustomInstrumentInfo(null, "pv_prefix").hostName());
    }

    @Test
    public final void GIVEN_null_name_WHEN_custom_instrument_created_THEN_name_is_null() {
        // Arrange

        // Act

        // Assert
        assertEquals(null, new CustomInstrumentInfo(null, "pv_prefix").name());
    }

    @Test
    public final void host_name_starting_with_ND_is_valid() {
        // Arrange
        String name = "ND_a_valid_name_123";
        String pvPrefix = "myPrefix";
        CustomInstrumentInfo instrument = new CustomInstrumentInfo(name, pvPrefix);

        // Act
        boolean valid = instrument.hasValidHostName();

        // Assert
        assertTrue(valid);
    }

    @Test
    public final void host_name_not_starting_with_ND_is_invalid() {
        // Arrange
        String name = "invalid_name";
        String pvPrefix = "prefix";
        CustomInstrumentInfo instrument = new CustomInstrumentInfo(name, pvPrefix);
        
        // Act
        boolean valid = instrument.hasValidHostName();

        // Assert
        assertFalse(valid);
    }

    @Test
    public final void GIVEN_host_name_starting_nd_WHEN_instrument_is_constructed_THEN_host_name_is_invalid() {
        // Arrange

        // Act

        // Assert
        assertFalse(new CustomInstrumentInfo(null, "pv_prefix", "nd_is_not_a_valid_host_name").hasValidHostName());
    }

    @Test
    public final void GIVEN_host_name_starting_ndx_WHEN_instrument_is_constructed_THEN_host_name_is_invalid() {
        // Arrange

        // Act

        // Assert
        assertFalse(new CustomInstrumentInfo(null, "pv_prefix", "ndx_is_not_a_valid_host_name").hasValidHostName());
    }

    @Test
    public final void GIVEN_host_name_starting_ND_WHEN_instrument_is_constructed_THEN_host_name_is_valid() {
        // Arrange

        // Act

        // Assert
        assertTrue(new CustomInstrumentInfo(null, "pv_prefix", "ND_is_not_a_valid_host_name").hasValidHostName());
    }

    @Test
    public final void GIVEN_host_name_starting_NDW_WHEN_instrument_is_constructed_THEN_host_name_is_valid() {
        // Arrange

        // Act

        // Assert
        assertTrue(new CustomInstrumentInfo(null, "pv_prefix", "NDW_is_not_a_valid_host_name").hasValidHostName());
    }

    @Test
    public final void GIVEN_empty_host_name_WHEN_instrument_is_constructed_THEN_host_name_is_invalid() {
        // Arrange

        // Act

        // Assert
        assertFalse(new InstrumentInfo(null, "pv_prefix", "").hasValidHostName());
    }

    @Test
    public final void
            GIVEN_null_host_name_but_non_null_instrument_name_WHEN_instrument_is_constructed_THEN_host_name_is_valid() {
        // Arrange

        // Act

        // Assert
        assertTrue(new CustomInstrumentInfo("my_name_is", "pv_prefix", null).hasValidHostName());
    }

    @Test
    public final void GIVEN_instrument_name_starting_NDX_WHEN_instrument_is_constructed_THEN_host_name_is_valid() {
        // Arrange

        // Act

        // Assert
        assertTrue(new CustomInstrumentInfo(null, "pv_prefix", "NDX_is_a_valid_host_name").hasValidHostName());
    }
}
