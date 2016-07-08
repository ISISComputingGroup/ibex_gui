
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.PropertyDescription;
import uk.ac.stfc.isis.ibex.devicescreens.tests.xmldata.DeviceScreensXmlProvider;
import uk.ac.stfc.isis.ibex.devicescreens.xml.XMLUtil;

/**
 * This class tests the loading and parsing of an xml by the XMLUtil class.
 */
@SuppressWarnings("checkstyle:methodname")
public class XmlUtilTest {

    private static String deviceName = "Eurotherm 2";
    private static String deviceKey = "Eurotherm";
    private static String deviceType = "OPI";
    private static String propertyKey = "EURO";
    private static String propertyValue = "EUROTHERM1";

    private DeviceScreensDescription singleDeviceScreensDescription;
    private DeviceScreensDescription multipleDeviceScreensDescription;

    private static final String xmlTextMultipleDescription =
            DeviceScreensXmlProvider.HEADER + "<device>"
            + "<name>Eurotherm 2</name>" + "<key>Eurotherm</key>"
            + "<type>OPI</type>" + "<properties>" + "<property>" + "<key>EURO</key>" + "<value>EUROTHERM1</value>"
            + "</property>" + "</properties>" + "</device>" + "<device>" + "<name>Julabo 1</name>" + "<key>Julabo</key>"
            + "<type>OPI</type>" + "<properties>" + "<property>" + "<key>JULABO</key>" + "<value>JULABO1</value>"
            + "</property>" + "<property>" + "<key>MACRO</key>" + "<value>VALUE</value>" + "</property>"
                    + "</properties>" + "</device>" + DeviceScreensXmlProvider.ENDING;

    @Before
    public void set_up() throws Exception {
        // Arrange
        String singleXml =
                DeviceScreensXmlProvider.getXML(deviceName, deviceKey, deviceType, propertyKey, propertyValue);
        singleDeviceScreensDescription = XMLUtil.fromXml(singleXml);
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
            GIVEN_device_description_parsed_from_xml_WHEN_it_is_written_to_xml_THEN_output_xml_is_equal_to_input_xml()
                    throws JAXBException, SAXException {
        // Act
        String outputXml = XMLUtil.toXml(multipleDeviceScreensDescription);

        // Assert
        assertEquals(xmlTextMultipleDescription, outputXml);
    }

    @Test
    public void GIVEN_valid_device_description_WHEN_it_is_written_to_xml_using_schema_THEN_output_xml_is_correct()
            throws IOException {
        // Arrange
        String expectedXml =
                DeviceScreensXmlProvider.getXML(deviceName, deviceKey, deviceType, propertyKey, propertyValue);
        PropertyDescription propertyDescription = new PropertyDescription(propertyKey, propertyValue);

        DeviceDescription deviceDescription = new DeviceDescription();
        deviceDescription.setName(deviceName);
        deviceDescription.setType(deviceType);
        deviceDescription.setKey(deviceKey);
        deviceDescription.addProperty(propertyDescription);

        DeviceScreensDescription deviceScreensDescription = new DeviceScreensDescription();
        deviceScreensDescription.addDevice(deviceDescription);
        
        String schemaFilePath = "/uk/ac/stfc/isis/ibex/devicescreens/xml/tests/screens_schema.xml";
        String schema = loadFile(schemaFilePath);

        // Act
        try {
            XMLUtil.setSchema(schema);
            String outputXml = XMLUtil.toXml(deviceScreensDescription);
            assertEquals(expectedXml, outputXml);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
    }

    private DeviceDescription firstDeviceDescription() {
        return singleDeviceScreensDescription.getDevices().get(0);
    }

    private DeviceDescription secondDeviceDescription() {
        return multipleDeviceScreensDescription.getDevices().get(1);
    }

    protected URL fileLocation(String filePath) throws MalformedURLException {
        return getClass().getResource(filePath);
    }

    protected String loadFile(String filePath) throws IOException {
        BufferedReader in = null;
        InputStream inputStream = null;
        URL url = fileLocation(filePath);
        try {
            inputStream = url.openConnection().getInputStream();
            in = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }

            String fileContent = builder.toString();
            return fileContent;
        } finally {
            if (in != null) {
                in.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }

    }

}
