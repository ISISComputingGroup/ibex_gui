
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
package uk.ac.stfc.isis.ibex.devicescreens.xml.tests;

import static org.junit.Assert.*;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.PropertyDescription;
import uk.ac.stfc.isis.ibex.devicescreens.xml.XMLUtil;

/**
 * This class tests the loading and parsing of an xml by the XMLUtil class.
 */
@SuppressWarnings("checkstyle:methodname")
public class XmlUtilTest {

    private DeviceScreensDescription singleDeviceScreensDescription;
    private DeviceScreensDescription multipleDeviceScreensDescription;

    private String xmlTextSingleDescription = "<?xml version=\"1.0\" ?>" +
            "<devices xmlns=\"http://epics.isis.rl.ac.uk/schema/screens/1.0/\">" +
            "<device>" + "<name>Eurotherm 2</name>" + "<key>Eurotherm</key>" + "<type>OPI</type>" + "<properties>"
            + "<property>" + "<key>EURO</key>" + "<value>EUROTHERM1</value>" + "</property>" + "</properties>"
            + "</device>" +
            "</devices>";
    
    private String xmlTextMultipleDescriptionHeader =
            "<?xml version=\"1.0\" ?>" + "<devices xmlns=\"http://epics.isis.rl.ac.uk/schema/screens/1.0/\">";

    private String xmlTextMultipleDescriptionCore = "<device>" + "<name>Eurotherm 2</name>" + "<key>Eurotherm</key>"
            + "<type>OPI</type>" + "<properties>" + "<property>" + "<key>EURO</key>" + "<value>EUROTHERM1</value>"
            + "</property>" + "</properties>" + "</device>" + "<device>" + "<name>Julabo 1</name>" + "<key>Julabo</key>"
            + "<type>OPI</type>" + "<properties>" + "<property>" + "<key>JULABO</key>" + "<value>JULABO1</value>"
            + "</property>" + "<property>" + "<key>MACRO</key>" + "<value>VALUE</value>" + "</property>"
            + "</properties>" + "</device>";

    private String xmlEnding = "</devices>";

    private String xmlTextMultipleDescription;

    @Before
    public void set_up() throws Exception {
        // Arrange
        singleDeviceScreensDescription = XMLUtil.fromXml(xmlTextSingleDescription);

        xmlTextMultipleDescription = xmlTextMultipleDescriptionHeader + xmlTextMultipleDescriptionCore + xmlEnding;
        multipleDeviceScreensDescription = XMLUtil.fromXml(xmlTextMultipleDescription);
    }

    @Test
    public void GIVEN_xml_with_single_description_WHEN_xml_is_parsed_THEN_a_single_description_is_read() {
        // Assert
        assertEquals(1, singleDeviceScreensDescription.getDevices().size());
    }
    
    @Test
    public void GIVEN_xml_with_single_description_WHEN_xml_is_parsed_THEN_device_name_is_correct() {
        // Assert
        assertEquals("Eurotherm 2", firstDeviceDescription().getName());
    }

    @Test
    public void GIVEN_xml_with_single_description_WHEN_xml_is_parsed_THEN_key_is_correct() {
        // Assert
        assertEquals("Eurotherm", firstDeviceDescription().getKey());
    }

    @Test
    public void GIVEN_xml_with_single_description_WHEN_xml_is_parsed_THEN_type_is_correct() {
        // Assert
        assertEquals("OPI", firstDeviceDescription().getType());
    }

    @Test
    public void
            GIVEN_xml_with_single_description_and_single_property_WHEN_xml_is_parsed_THEN_a_single_property_is_read() {
        // Assert
        assertEquals(1, firstDeviceDescription().getProperties().size());
    }

    @Test
    public void
            GIVEN_xml_with_single_description_and_single_property_WHEN_xml_is_parsed_THEN_property_key_and_value_are_correct() {
        // Assert
        PropertyDescription propertyDescription = firstDeviceDescription().getProperties().get(0);
        assertEquals("EURO", propertyDescription.getKey());
        assertEquals("EUROTHERM1", propertyDescription.getValue());
    }

    @Test
    public void GIVEN_xml_with_multiple_description_WHEN_xml_is_parsed_THEN_number_of_descriptions_is_correct() {
        // Assert
        assertEquals(2, multipleDeviceScreensDescription.getDevices().size());
    }

    @Test
    public void GIVEN_xml_with_multiple_properties_WHEN_xml_is_parsed_THEN_number_of_properties_is_correct() {
        // Assert
        assertEquals(2, secondDeviceDescription().getProperties().size());
    }

    @Test
    public void
            GIVEN_device_description_parsefd_from_xml_WHEN_it_is_written_to_xml_THEN_output_xml_is_equal_to_input_xml()
                    throws JAXBException, SAXException {
        // Act
        String outputXml = XMLUtil.toXml(multipleDeviceScreensDescription);

        // Assert
        assertTrue(outputXml.contains(xmlTextMultipleDescriptionCore));
    }

    private DeviceDescription firstDeviceDescription() {
        return singleDeviceScreensDescription.getDevices().get(0);
    }

    private DeviceDescription secondDeviceDescription() {
        return multipleDeviceScreensDescription.getDevices().get(1);
    }

}
