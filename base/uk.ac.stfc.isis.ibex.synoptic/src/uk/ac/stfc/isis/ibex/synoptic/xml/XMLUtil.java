
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.synoptic.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;

public final class XMLUtil {
	
	private static final SchemaFactory SF = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    private static final URL SCHEMA_LOCATION = XMLUtil.class.getResource("/uk/ac/stfc/isis/ibex/synoptic/xml/instrument.xsd");
    private static Schema schema; 

    private static JAXBContext context;
    private static Unmarshaller unmarshaller;
    private static Marshaller marshaller;
    
    private XMLUtil() { }
    
	public static SynopticDescription fromXml(String xml) throws JAXBException, SAXException {
		if (context == null) {
			initialise();
		}
	         
		return (SynopticDescription) unmarshaller.unmarshal(new StringReader(xml));
	}
	
	public static String toXml(SynopticDescription instrument) throws JAXBException, SAXException {
		if (context == null) {
			initialise();
		}
	    
		StringWriter writer = new StringWriter();
		marshaller.marshal(instrument, writer);
		
		return writer.toString();		
	}
	
	private static void initialise() throws JAXBException, SAXException {
		try {
			context = JAXBContext.newInstance(SynopticDescription.class);
			schema = SF.newSchema(SCHEMA_LOCATION);
			
			marshaller = context.createMarshaller();
			marshaller.setSchema(schema);
			
			unmarshaller = context.createUnmarshaller();	
			unmarshaller.setSchema(schema);
		} catch (Exception e) {
			context = null;
			throw e;
		}
	}
}
