
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

@SuppressWarnings("checkstyle:methodname")
public class InstrumentInfoTest {

    @Test
    public final void GIVEN_only_name_set_WHEN_instrument_info_constructed_THEN_instrument_info_name_matches_input() {
        // Arrange
        final String name = "myInstrument";

        // Act

        // Assert
        assertEquals(name, new InstrumentInfo(name, null, null).name());
    }

    @Test
    public final void
            WHEN_instrument_info_constructed_with_just_name_THEN_pv_prefix_is_IN_plus_colon_plus_name_plus_colon() {
        // Arrange
        final String name = "my_inst";

        // Act

        // Assert
        assertEquals("IN:" + name + ":", new InstrumentInfo(name, null, null).pvPrefix());
    }

    @Test
    public final void WHEN_instrument_info_constructed_with_just_name_THEN_host_name_is_NDX_plus_name() {
        // Arrange
        final String name = "myInstrument";

        // Act

        // Assert
        assertEquals("NDX" + name, new InstrumentInfo(name, null, null).hostName());
    }

    @Test
    public final void
            GIVEN_name_pv_prefix_and_valid_host_name_WHEN_instrument_is_constructed_with_all_metadata_THEN_name_matches_input_name() {
        // Arrange
        final String name = "myInstrument";
        final String hostName = "NDXTEST";
        final String pvPrefix = "myPV";

        // Act

        // Assert
        assertEquals(name, new InstrumentInfo(name, pvPrefix, hostName).name());
    }

    @Test
    public final void
            GIVEN_name_pv_prefix_and_valid_host_name_WHEN_instrument_is_constructed_with_all_metadata_THEN_pv_prefix_matches_input_pv_prefix() {
        // Arrange
        final String name = "myInstrument";
        final String hostName = "NDXTEST";
        final String pvPrefix = "myPV";

        // Act

        // Assert
        assertEquals(pvPrefix, new InstrumentInfo(name, pvPrefix, hostName).pvPrefix());
    }

    @Test
    public final void
            GIVEN_name_pv_prefix_and_valid_host_name_WHEN_instrument_is_constructed_with_all_metadata_THEN_host_name_matches_input_host_name() {
        // Arrange
        final String name = "myInstrument";
        final String hostName = "NDXTEST";
        final String pvPrefix = "myPV";

        // Act

        // Assert
        assertEquals(hostName, new InstrumentInfo(name, pvPrefix, hostName).hostName());
    }

    @Test
    public final void
            GIVEN_null_host_name_WHEN_instrument_is_constructed_THEN_host_name_is_name_plus_NDX() {
        // Arrange
        final String name = "myInstrument";
        final String hostName = null;
        final String pvPrefix = "myPV";

        // Act

        // Assert
        assertEquals("NDX" + name, new InstrumentInfo(name, pvPrefix, hostName).hostName());
    }

    @Test
    public final void
            GIVEN_null_inputs_WHEN_instrument_is_constructed_THEN_host_name_is_NDXnull() {
        // Arrange

        // Act

        // Assert
        assertEquals("NDXnull", new InstrumentInfo(null, null, null).hostName());
    }

    @Test
    public final void
            GIVEN_null_inputs_WHEN_instrument_is_constructed_THEN_name_is_null() {
        // Arrange

        // Act

        // Assert
        assertEquals(null, new InstrumentInfo(null, null, null).name());
    }

    @Test
    public final void
            GIVEN_null_inputs_WHEN_instrument_is_constructed_THEN_pv_prefix_is_NDXnull_colon_null_colon() {
        // Arrange

        // Act

        // Assert
        assertEquals("IN::", new InstrumentInfo(null, null, null).pvPrefix());
    }

    @Test
    public final void GIVEN_empty_host_name_WHEN_instrument_is_constructed_THEN_host_name_is_invalid() {
        // Arrange

        // Act

        // Assert
        assertFalse(new InstrumentInfo(null, null, "").hasValidHostName());
    }

    @Test
    public final void
            GIVEN_null_host_name_but_non_null_instrument_name_WHEN_instrument_is_constructed_THEN_host_name_is_valid() {
        // Arrange

        // Act

        // Assert
        assertTrue(new InstrumentInfo("my_name_is", null, null).hasValidHostName());
    }

    @Test
    public final void GIVEN_instrument_name_starting_NDX_WHEN_instrument_is_constructed_THEN_host_name_is_valid() {
        // Arrange

        // Act

        // Assert
        assertTrue(new InstrumentInfo(null, null, "NDX_is_a_valid_host_name").hasValidHostName());
    }
}
