
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

package uk.ac.stfc.isis.ibex.epics.conversion;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.XMLConstants;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.jaxb.JAXB;

/**
 * Static utility class that deals with decoding/encoding XML into classes.
 */
public final class XMLUtil {

	private XMLUtil() {
    }

    /**
     * Converts an input XML reader into the given type.
     * 
     * @param <T>
     *            the type to parse the XML into
     * @param xml
     *            the XML to convert
     * @param clazz
     *            the type to parse the XML into
     * @return the xml data converted into the specified type
     * @throws IOException
     *             XML Exception thrown if the conversion failed
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> T fromXml(Reader xml, Class<T> clazz) throws IOException {
    	try {
    		return (T) JAXB.getUnmarshaller(clazz).unmarshal(xml);
    	} catch (JAXBException e) {
    		throw new IOException(e.getMessage(), e);
    	}
    }

    /**
     * Converts an input XML string into the given type.
     * 
     * @param <T>
     *            the type to parse the XML into
     * @param xml
     *            the XML to convert
     * @param clazz
     *            the type to parse the XML into
     * @return the xml data converted into the specified type
     * @throws IOException
     *             XML Exception thrown if the conversion failed
     */
    public static synchronized <T> T fromXml(String xml, Class<T> clazz) throws IOException {
        return fromXml(new StringReader(xml), clazz);
	}

    /**
     * Converts an object into an XML representation without checking it against a schema.
     * 
     * @param <T>
     *            the type than needs to be converted into XML
     * @param toConvert
     *            the object to convert into XML
     * @param clazz
     *            the type to parse the XML into
     * @return the XML that the object has been converted into
     * @throws IOException
     *             XML Exception for if the conversion to xml failed
     */
    public static synchronized <T> String toXml(T toConvert, Class<T> clazz)
            throws IOException {
        
        return XMLUtil.toXml(toConvert, clazz, null);
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
     * @throws IOException
     *             XML Exception for if the conversion to xml failed
     */
    public static synchronized <T> String toXml(T toConvert, Class<T> clazz, String rawSchema)
            throws IOException {
        Schema schema = null; // Null means no validation
        
        if (!Strings.isNullOrEmpty(rawSchema)) {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            try {
            	schema = sf.newSchema(new StreamSource(new StringReader(rawSchema)));
            } catch (SAXException e) {
                throw new IOException(e.getMessage(), e);
            }
        }
        
        Marshaller marshaller = JAXB.getMarshaller(clazz);
        StringWriter writer = new StringWriter();
        marshaller.setSchema(schema);
        try {
        	marshaller.marshal(toConvert, writer);
        } catch (JAXBException e) {
        	throw new IOException(e.getMessage(), e);
        }
		
		return writer.toString();		
	}
}
