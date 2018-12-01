
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

import java.io.Reader;
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

import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Static utility class that deals with decoding/encoding XML into classes.
 */
public final class XMLUtil {
	
	private static final String JAXB_CONTEXT_CLASS_PATH = "javax/xml/bind/JAXBContext.class";
	
	private static final Logger LOG = IsisLog.getLogger(XMLUtil.class);
	
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
     * @throws JAXBException
     *             XML Exception thrown if the conversion failed
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> T fromXml(Reader xml, Class<T> clazz) throws JAXBException {
        JAXBContext context = getJaxbContext(clazz);
    	Unmarshaller unmarshaller = context.createUnmarshaller();
	    return (T) unmarshaller.unmarshal(xml);
    }
    
    /**
     * Gets a JAXBContext, given a class which JAXB should marshal/unmarshal XML to.
     * 
     * This method performs a check on the provided class' classloader, to check it is
     * loading javax.xml.bind from the right place. If this check isn't done the error
     * will just occur further down the call stack and be harder to diagnose.
     * 
     * @param clazz the clazz on which to base this JAXB context.
     * @return the JAXBContext
     * @throws JAXBException if an error was thrown by JAXB or the provided class did not
     * pass the classloader check.
     */
    private static <T> JAXBContext getJaxbContext(Class<T> clazz) throws JAXBException {
    	try {
    	    return JAXBContext.newInstance(clazz);
    	} catch (JAXBException e) {
    		// This is a really tricky bug - if you hit this condition, it's because the class calling
    		// this method has loaded javax.xml.bind from core java instead of getting it from the
    		// explicit dependency plugin. This will cause issues later on with more obscure error messages
    		// if you remove this condition.
    		//
    		// Steps to fix this issue are:
    		// - Find the offending class (reported below)
    		// - Find the plugin in which it is defined
    		// - In the plugin folder look in META-INF/MANIFEST.MF
    		// - Under the "dependencies" tab, check that you depend on "javax.xml.bind" explicitly.
    		// - If you still get issues, move javax.xml.bind above any other packages in the dependency list
    		ClassLoader classLoader = clazz.getClassLoader();
    		String javaxXmlPath = classLoader.getResource(JAXB_CONTEXT_CLASS_PATH).toString();
    		
    		LOG.error("Exception thrown when JAXBContext was being initialized.");
    		LOG.error(String.format("Called from: '%s', using classloader '%s'", clazz.getName(), classLoader));
    		LOG.error(String.format("Classloader '%s' loads class '%s' from '%s'", classLoader, JAXB_CONTEXT_CLASS_PATH, javaxXmlPath));
    		throw e;
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
     * @throws JAXBException
     *             XML Exception thrown if the conversion failed
     */
    public static synchronized <T> T fromXml(String xml, Class<T> clazz) throws JAXBException {
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
     * @throws JAXBException
     *             XML Exception for if the conversion to xml failed
     * @throws SAXException
     *             XML Exception for if the xml doesn't conform to the schema
     */
    public static synchronized <T> String toXml(T toConvert, Class<T> clazz)
            throws JAXBException {
        
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
     * @throws JAXBException
     *             XML Exception for if the conversion to xml failed
     * @throws SAXException
     *             XML Exception for if the xml doesn't conform to the schema
     */
    public static synchronized <T> String toXml(T toConvert, Class<T> clazz, String rawSchema)
            throws JAXBException {
        Schema schema = null; // Null means no validation
        
        if (!Strings.isNullOrEmpty(rawSchema)) {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            try {
            	schema = sf.newSchema(new StreamSource(new StringReader(rawSchema)));
            } catch (SAXException e) {
                throw new JAXBException(e.getMessage(), e);
            }
        }
        
        JAXBContext context = getJaxbContext(clazz);
        Marshaller marshaller = context.createMarshaller();
        StringWriter writer = new StringWriter();
        marshaller.setSchema(schema);
        marshaller.marshal(toConvert, writer);
		
		return writer.toString();		
	}
}
