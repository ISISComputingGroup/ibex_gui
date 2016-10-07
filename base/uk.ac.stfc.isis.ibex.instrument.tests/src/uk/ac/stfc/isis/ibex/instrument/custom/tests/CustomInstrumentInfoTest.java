
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

package uk.ac.stfc.isis.ibex.instrument.custom.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.instrument.custom.CustomInstrumentInfo;

@SuppressWarnings("checkstyle:methodname")
public class CustomInstrumentInfoTest {

    @Test
    public final void custom_instrument_info_initialised_correctly() {
        // Arrange
        String name = "myInstrument";
        String pvPrefix = "myPrefix";

        // Act
        CustomInstrumentInfo instrument = new CustomInstrumentInfo(name, pvPrefix);

        // Assert
        assertEquals(name, instrument.name());
        assertEquals(pvPrefix, instrument.pvPrefix());
    }

    @Test
    public final void host_name_equals_name() {
        // Arrange
        String name = "myInstrument";
        String pvPrefix = "myPrefix";
        String expectedHostName = name;

        // Act
        CustomInstrumentInfo instrument = new CustomInstrumentInfo(name, pvPrefix);
        
        // Assert
        assertEquals(expectedHostName, instrument.hostName());
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

    @Test(expected = IllegalArgumentException.class)
    public final void empty_pv_prefix_throws() {
        // Arrange
        String name = "aName";
        String emptyPVPrefix = "";

        // Act
        CustomInstrumentInfo instrument = new CustomInstrumentInfo(name, emptyPVPrefix);
    }
}
