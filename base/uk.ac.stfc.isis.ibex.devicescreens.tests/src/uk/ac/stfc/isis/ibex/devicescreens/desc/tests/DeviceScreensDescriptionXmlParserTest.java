
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
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescriptionXmlParser;
import uk.ac.stfc.isis.ibex.devicescreens.desc.PropertyDescription;
import uk.ac.stfc.isis.ibex.devicescreens.tests.xmldata.DeviceScreensXmlProvider;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;

@SuppressWarnings("checkstyle:methodname")
public class DeviceScreensDescriptionXmlParserTest {

    private static String deviceName = "Eurotherm 2";
    private static String deviceKey = "Eurotherm";
    private static String deviceType = "OPI";
    private static String propertyKey = "EURO";
    private static String propertyValue = "EUROTHERM1";

    DeviceScreensDescriptionXmlParser parser;
    
    @Before
    public void set_up() {
        parser = new DeviceScreensDescriptionXmlParser();
    }

    @Test
    public void GIVEN_valid_xml_WHEN_xml_is_converted_THEN_returned_device_screens_description_is_correct() {
        try {
            // Act
            String inputXml =
                    DeviceScreensXmlProvider.getXML(deviceName, deviceKey, deviceType, propertyKey, propertyValue);
            DeviceScreensDescription description = parser.convert(inputXml);

            // Assert
            assertEquals(1, description.getDevices().size());

            DeviceDescription device = description.getDevices().get(0);
            assertEquals(deviceName, device.getName());
            assertEquals(deviceKey, device.getKey());
            assertEquals(deviceType, device.getType());
            assertEquals(1, device.getProperties().size());

            PropertyDescription property = device.getProperties().get(0);
            assertEquals(propertyKey, property.getKey());
            assertEquals(propertyValue, property.getValue());

        } catch (ConversionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void GIVEN_invalid_xml_WHEN_xml_is_converted_THEN_parser_throws() {
        // Arrange
        String invalidXml = "invalid";
        
        try {
            parser.convert(invalidXml);
            fail("ConversionException not thrown");
        } catch (ConversionException e) {
            assertNotNull(e.getMessage());
        }
        
    }
}

