
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

import uk.ac.stfc.isis.ibex.devicescreens.desc.PropertyDescription;

/**
 * This class tests PropertyDescription.
 */
public class PropertyDescriptionTest {

    private PropertyDescription propertyDescription;

    @Before
    public void set_up() {
        // Arrange
        propertyDescription = new PropertyDescription();
    }

    @Test
    public void GIVEN_new_property_description_THEN_the_key_is_null() {
        // Assert
        assertNull(propertyDescription.getKey());
    }

    @Test
    public void GIVEN_new_property_description_THEN_the_value_is_null() {
        // Assert
        assertNull(propertyDescription.getValue());
    }

    @Test
    public void WHEN_key_is_set_THEN_get_key_is_correct() {
        // Arrange
        String expectedKey = "a key";

        // Act
        propertyDescription.setKey(expectedKey);

        // Assert
        assertEquals(expectedKey, propertyDescription.getKey());
    }

    @Test
    public void WHEN_value_is_set_THEN_get_value_is_correct() {
        // Arrange
        String expectedValue = "a value";

        // Act
        propertyDescription.setValue(expectedValue);

        // Assert
        assertEquals(expectedValue, propertyDescription.getValue());
    }

}
