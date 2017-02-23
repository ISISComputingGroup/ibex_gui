
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

import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;

/**
 * Singleton class that deals with decoding/encoding XML into classes.
 */
public final class XMLUtil {
	
	private static final SchemaFactory SF = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    private static Schema schema; 

    private static JAXBContext context;
    private static Unmarshaller unmarshaller;
    private static Marshaller marshaller;
    
    private XMLUtil() { }
    
	/**
     * Converts an input XML into a synoptic description.
     * 
     * @param <T> the type to parse the XML into
     * @param xml the synoptic XML received from the BlockServer
     * @return the synoptic data converted into an instrument description
     * @throws JAXBException XML Exception
     * @throws SAXException XML Exception
     */
    public static synchronized <T> T fromXml(String xml) throws JAXBException, SAXException {
		if (context == null) {
			initialise();
		}
	         
        return (T) unmarshaller.unmarshal(new StringReader(xml));
	}
	
	/**
     * Converts the instrument description into the synoptic XML expected by the
     * BlockServer.
     * 
     * @param <T>
     *            the type than needs to be converted into XML
     * @param instrument
     *            the instrument description
     * @return the XML for the synoptic
     * @throws JAXBException
     *             XML Exception
     * @throws SAXException
     *             XML Exception
     */
    public static synchronized <T> String toXml(T instrument) throws JAXBException, SAXException {
		if (context == null) {
			initialise();
		}
	    
		StringWriter writer = new StringWriter();
		marshaller.marshal(instrument, writer);
		
		return writer.toString();		
	}
	
	/**
     * Sets the XML schema.
     * 
     * @param rawSchema the XML schema for the synoptic as supplied by the
     *            BlockServer
     */
    public static synchronized void setSchema(String rawSchema) {
		if (context == null) {
			initialise();
		}
		
        System.out.println("Schema is: " + rawSchema);

        if (Strings.isNullOrEmpty(rawSchema)) {
            clearSchema();
        } else {
            try {
                schema = SF.newSchema(new StreamSource(new StringReader(rawSchema)));
                marshaller.setSchema(schema);
                unmarshaller.setSchema(schema);
            } catch (SAXException e) {
                clearSchema();
            }
        }
	}

    private static void clearSchema() {
        // Add warning here
        marshaller.setSchema(null);
        unmarshaller.setSchema(null);
    }
	
    private static void initialise() {
		try {
			context = JAXBContext.newInstance(SynopticDescription.class);
			marshaller = context.createMarshaller();
			unmarshaller = context.createUnmarshaller();	
		} catch (Exception e) {
			context = null;
		}
	}
}
