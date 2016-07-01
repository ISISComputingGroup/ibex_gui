
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
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;

/**
 * This class tests DeviceScreensDescription.
 */
@SuppressWarnings("checkstyle:methodname")
public class DeviceScreensDescriptionTest {

    DeviceScreensDescription description;

    @Before
    public void set_up() {
        // Arrange
        description = new DeviceScreensDescription();
    }

    @Test
    public void GIVEN_new_device_screens_description_THEN_devices_is_not_null() {
        // Assert
        assertNotNull(description.getDevices());
    }

    @Test
    public void GIVEN_new_device_screens_description_THEN_devices_is_empty() {
        // Assert
        assertTrue(description.getDevices().isEmpty());
    }

    @Test
    public void GIVEN_new_device_screens_description_THEN_devices_can_be_added() {
        // Arrange
        DeviceDescription device1 = new DeviceDescription();
        DeviceDescription device2 = new DeviceDescription();

        // Act
        description.addDevice(device1);
        description.addDevice(device2);

        // Assert
        assertTrue(description.getDevices().contains(device1));
        assertTrue(description.getDevices().contains(device2));
    }
}
