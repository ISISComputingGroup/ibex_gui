
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.devicescreens.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;

/**
 * Utility class used to parse from and write to XML.
 */
public final class XMLUtil {

    private static JAXBContext context;
    private static Unmarshaller unmarshaller;
    private static Marshaller marshaller;

    private static final SchemaFactory SF = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    private static Schema schema;

    /**
     * Default constructor.
     */
    private XMLUtil() {
    }

    /**
     * Parses an XML into a device screens description object.
     *
     * @param <T> the type to unmarshal to
     * @param xml the device screens XML received from the BlockServer
     * @return the data converted into a device screens description
     * @throws JAXBException XML Exception
     * @throws SAXException XML Exception
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> T fromXml(String xml) throws JAXBException, SAXException {
        if (context == null) {
            initialise();
        }
        return (T) unmarshaller.unmarshal(new StringReader(xml));
    }

    /**
     * Converts the device screens description into the XML expected by the
     * BlockServer.
     *
     * @param <T> the type to marshal to
     * @param deviceScreensDescription the input device screens description
     * @return the XML for the device screens
     * @throws JAXBException XML Exception
     * @throws SAXException XML Exception
     */
    public static <T> String toXml(T deviceScreensDescription) throws JAXBException, SAXException {
        if (context == null) {
            initialise();
        }

        StringWriter writer = new StringWriter();
        marshaller.marshal(deviceScreensDescription, writer);

        return writer.toString();
    }

    /**
     * Sets the schema.
     * 
     * @param rawSchema the XML schema for the device screens as supplied by the
     *            BlockServer
     * @throws SAXException XML Exception
     * @throws JAXBException XML Exception
     */
    public static void setSchema(String rawSchema) throws SAXException, JAXBException {
        if (context == null) {
            initialise();
        }

        schema = SF.newSchema(new StreamSource(new StringReader(rawSchema)));
        marshaller.setSchema(schema);
        unmarshaller.setSchema(schema);
    }

    private static void initialise() throws JAXBException, SAXException {
        try {
            context = JAXBContext.newInstance(DeviceScreensDescription.class);
            marshaller = context.createMarshaller();
            unmarshaller = context.createUnmarshaller();
        } catch (Exception e) {
            context = null;
            throw e;
        }
    }
}
