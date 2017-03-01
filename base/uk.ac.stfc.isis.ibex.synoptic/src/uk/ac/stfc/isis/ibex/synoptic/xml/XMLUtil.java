
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

package uk.ac.stfc.isis.ibex.synoptic.xml;

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

import com.google.common.base.Strings;

/**
 * Static utility class that deals with decoding/encoding XML into classes.
 */
public final class XMLUtil {
    
    private XMLUtil() {
    }

	/**
     * Converts an input XML into the given type.
     * 
     * @param <T>
     *            the type to parse the XML into
     * @param xml
     *            the XML to convert
     * @param clazz
     *            the type to parse the XML into
     * @return the xml data converted into the specified type
     * @throws JAXBException
     *             XML Exception thrown if the conversion failed
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> T fromXml(String xml, Class<T> clazz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        return (T) unmarshaller.unmarshal(new StringReader(xml));
	}
	
	/**
     * Converts an object into an XML representation. The XML is checked against
     * the supplied schema if the schema is not blank or empty.
     * 
     * @param <T>
     *            the type than needs to be converted into XML
     * @param toConvert
     *            the object to convert into XML
     * @param clazz
     *            the type to parse the XML into
     * @param rawSchema
     *            the schema to check against
     * @return the XML that the object has been converted into
     * @throws JAXBException
     *             XML Exception for if the conversion to xml failed
     * @throws SAXException
     *             XML Exception for if the xml doesn't conform to the schema
     */
    public static synchronized <T> String toXml(T toConvert, Class<T> clazz, String rawSchema)
            throws JAXBException, SAXException {
        Schema schema = null; // Null means no validation
        
        if (!Strings.isNullOrEmpty(rawSchema)) {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schema = sf.newSchema(new StreamSource(new StringReader(rawSchema)));
        }
        
        JAXBContext context = JAXBContext.newInstance(clazz);
        Marshaller marshaller = context.createMarshaller();
        StringWriter writer = new StringWriter();
        marshaller.setSchema(schema);
        marshaller.marshal(toConvert, writer);
		
		return writer.toString();		
	}
}
