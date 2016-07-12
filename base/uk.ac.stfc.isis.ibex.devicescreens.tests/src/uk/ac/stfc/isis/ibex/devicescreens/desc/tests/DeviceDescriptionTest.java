
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
import uk.ac.stfc.isis.ibex.devicescreens.desc.PropertyDescription;
import uk.ac.stfc.isis.ibex.targets.OpiTarget;

/**
 * This class tests DeviceDescription.
 */
@SuppressWarnings("checkstyle:methodname")
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

    @Test
    public void GIVEN_new_device_description_THEN_properties_can_be_added() {
        // Arrange
        PropertyDescription property1 = new PropertyDescription("key1", "value1");
        PropertyDescription property2 = new PropertyDescription("key2", "value2");

        // Act
        deviceDescription.addProperty(property1);
        deviceDescription.addProperty(property2);

        // Assert
        assertTrue(deviceDescription.getProperties().contains(property1));
        assertTrue(deviceDescription.getProperties().contains(property2));
    }

    @Test
    public void GIVEN_key_name_and_no_properties_WHEN_get_opi_target_THEN_target_has_details() {
        // Arrange
        String expected_key = "a key";
        String expected_name = "name";
        deviceDescription.setKey(expected_key);
        deviceDescription.setName(expected_name);

        // Act
        OpiTarget result = deviceDescription.getOPITarget();

        // Assert
        assertEquals(result.opiName(), deviceDescription.getKey());
        assertEquals(result.name(), deviceDescription.getName());
        assertEquals("Number of propeties", result.properties().size(), 0);
    }

    @Test
    public void GIVEN_properties_WHEN_get_opi_target_THEN_target_has_given_properties() {
        // Arrange
        String key1 = "key1";
        String value1 = "value1";
        PropertyDescription property1 = new PropertyDescription(key1, value1);
        String key2 = "key2";
        String value2 = "value2";
        PropertyDescription property2 = new PropertyDescription(key2, value2);
        deviceDescription.addProperty(property1);
        deviceDescription.addProperty(property2);

        // Act
        OpiTarget result = deviceDescription.getOPITarget();

        // Assert
        assertEquals("Number of propeties", result.properties().size(), 2);
        assertEquals("First key value", result.properties().get(key1), value1);
        assertEquals("First key value", result.properties().get(key2), value2);
    }

}
