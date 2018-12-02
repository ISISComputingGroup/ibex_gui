
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.logging.log4j.Logger;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.xml.sax.SAXException;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * Static utility class that deals with decoding/encoding XML into classes.
 */
public final class XMLUtil {
	
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
     * @param clazz the clazz on which to base this JAXB context.
     * @return the JAXBContext
     * @throws JAXBException if an error was thrown by JAXB or the provided class did not
     * pass the classloader check.
     */
    private static <T> JAXBContext getJaxbContext(Class<T> clazz) throws JAXBException {
    	// Nuclear approach - depending on where this gets called from
    	// we need different classloaders to be used. Try them all until
    	// one doesn't error. This is hacky but it's the best way I've found
    	// to get around JAXB's limitations so far.
    	List<ClassLoader> classloaders = List.of(
    			clazz.getClassLoader(),
    			XMLUtil.class.getClassLoader(),
    			Thread.currentThread().getContextClassLoader()
    	);
    	
    	Map<String, Throwable> errors = new HashMap<>();
    	
    	for (ClassLoader classloader : classloaders) {
    		try {
        		return JAXBContextFactory.createContext(new Class[] {clazz}, Collections.emptyMap(), classloader);
        	} catch (Exception | NoClassDefFoundError e) {
        		errors.put(String.format("used classloader %s", classloader), e);
        	}
    	}
    	
    	for (var entry : errors.entrySet()) {
    		LoggerUtils.logErrorWithStackTrace(LOG, 
    				String.format("%s, got exception: %s", entry.getKey(), entry.getValue().getMessage()), entry.getValue());
    		
    		if (entry.getValue().getCause() != null) {
    			LoggerUtils.logErrorWithStackTrace(LOG, "Caused by: " + entry.getValue().getCause().getMessage(), entry.getValue().getCause());
    		}
    	}
    	
    	throw new JAXBException("Failed to get a JAXBContext using any classloader");
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
