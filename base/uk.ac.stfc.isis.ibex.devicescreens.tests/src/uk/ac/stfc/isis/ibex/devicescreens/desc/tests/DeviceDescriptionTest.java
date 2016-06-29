
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.devicescreens.desc.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;

/**
 * This class tests DeviceDescription.
 */
public class DeviceDescriptionTest {

    private DeviceDescription deviceDescription;

    @Before
    public void set_up() {
        // Arrange
        deviceDescription = new DeviceDescription();
    }

    @Test
    public void GIVEN_new_device_description_THEN_key_name_and_type_are_null() {
        // Assert
        assertNull(deviceDescription.getName());
        assertNull(deviceDescription.getKey());
        assertNull(deviceDescription.getType());
    }

    @Test
    public void WHEN_key_is_set_THEN_get_key_is_correct() {
        // Arrange
        String expected = "a key";

        // Act
        deviceDescription.setKey(expected);

        // Assert
        assertEquals(expected, deviceDescription.getKey());
    }

    @Test
    public void WHEN_name_is_set_THEN_get_name_is_correct() {
        // Arrange
        String expected = "a name";

        // Act
        deviceDescription.setName(expected);

        // Assert
        assertEquals(expected, deviceDescription.getName());
    }

    @Test
    public void WHEN_type_is_set_THEN_get_type_is_correct() {
        // Arrange
        String expected = "a type";

        // Act
        deviceDescription.setType(expected);

        // Assert
        assertEquals(expected, deviceDescription.getType());
    }

    @Test
    public void GIVEN_new_device_description_THEN_properties_is_not_null() {
        // Assert
        assertNotNull(deviceDescription.getProperties());
    }

    @Test
    public void GIVEN_new_device_description_THEN_properties_is_empty() {
        // Assert
        assertTrue(deviceDescription.getProperties().isEmpty());
    }
}
