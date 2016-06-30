
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
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescriptionParser;
import uk.ac.stfc.isis.ibex.devicescreens.desc.PropertyDescription;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;

/**
 * This class tests the DeviceScreensDescriptionParser.
 */
@SuppressWarnings("checkstyle:methodname")
public class DeviceScreensDescriptionParserTest {

    private String xmlText =
            "<?xml version=\"1.0\" ?>" + "<devices xmlns=\"http://epics.isis.rl.ac.uk/schema/screens/1.0/\">"
                    + "<device>" + "<name>Eurotherm 2</name>" + "<key>Eurotherm</key>" + "<type>OPI</type>"
                    + "<properties>" + "<property>" + "<key>EURO</key>" + "<value>EUROTHERM1</value>" + "</property>"
                    + "</properties>" + "</device>" + "</devices>";
    
    DeviceScreensDescriptionParser parser;
    
    @Before
    public void set_up() {
        parser = new DeviceScreensDescriptionParser();
    }

    @Test
    public void GIVEN_valid_xml_WHEN_xml_is_converted_THEN_returned_device_screens_description_is_correct() {
        try {
            // Act
            DeviceScreensDescription description = parser.convert(xmlText);

            // Assert
            assertEquals(1, description.getDevices().size());

            DeviceDescription device = description.getDevices().get(0);
            assertEquals("Eurotherm 2", device.getName());
            assertEquals("Eurotherm", device.getKey());
            assertEquals("OPI", device.getType());
            assertEquals(1, device.getProperties().size());

            PropertyDescription property = device.getProperties().get(0);
            assertEquals("EURO", property.getKey());
            assertEquals("EUROTHERM1", property.getValue());

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

