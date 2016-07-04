
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

import org.junit.Test;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreenDescriptionToXmlConverter;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.PropertyDescription;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;

@SuppressWarnings("checkstyle:methodname")
public class DeviceScreensDescriptionToXmlConverterTest {

    @Test
    public void GIVEN_device_screens_description_WHEN_it_is_converted_to_xml_THEN_result_xml_is_correct() {
        // Arrange
        String name = "Device name";
        String key = "Device key";
        String type = "Device type";
        String propertyKey = "Property key";
        String propertyValue = "Property value";
        String expectedXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                        + "<devices xmlns=\"http://epics.isis.rl.ac.uk/schema/screens/1.0/\">"
                        + "<device>" + "<name>" + name + "</name>" + "<key>" + key + "</key>" + "<type>" + type
                        + "</type>" + "<properties>" + "<property>" + "<key>" + propertyKey + "</key>"
                        + "<value>" + propertyValue + "</value>"
                        + "</property>" + "</properties>" + "</device>" + "</devices>";

        PropertyDescription propertyDescription = new PropertyDescription(propertyKey, propertyValue);
        DeviceDescription deviceDescription = new DeviceDescription();
        deviceDescription.setName(name);
        deviceDescription.setType(type);
        deviceDescription.setKey(key);
        deviceDescription.addProperty(propertyDescription);
        DeviceScreensDescription deviceScreensDescription = new DeviceScreensDescription();
        deviceScreensDescription.addDevice(deviceDescription);

        DeviceScreenDescriptionToXmlConverter converter = new DeviceScreenDescriptionToXmlConverter();

        try {
            // Act
            String resultXml = converter.convert(deviceScreensDescription);

            // Assert
            assertEquals(expectedXml, resultXml);
        
        } catch (ConversionException e) {
            fail(e.getMessage());
        }

    }

}
