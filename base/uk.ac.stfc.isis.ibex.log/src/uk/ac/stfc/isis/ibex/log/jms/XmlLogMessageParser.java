
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

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.log.jms;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.stfc.isis.ibex.log.message.LogMessage;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Converts the XML representation of an IOC log message
 * into an instance of LogMessage.
 */
public class XmlLogMessageParser {
	private static final Logger LOG = IsisLog.getLogger(XmlLogMessageParser.class);
	
	public LogMessage xmlToLogMessage(String xml) {
		LogMessage message = new LogMessage();
		
		Node root;
		try {
			root = getRootElement(xml);
		} catch (SAXException | IOException | ParserConfigurationException ex) {
			LOG.error("Error parsing IOC log message XML root element: " + ex.getMessage());
			return message;
		}
		
		NodeList messageProperties = root.getChildNodes();
		
		for (int i = 0; i < messageProperties.getLength(); ++i) {
			String tag = messageProperties.item(i).getNodeName();
			String value = messageProperties.item(i).getTextContent();

			// Get the enum value for the log message filed from the XML tag name
			try {
				LogMessageFields field = LogMessageFields.getFieldByTagName(tag);
				message.setProperty(field, value);
			} catch (Exception ex) {
				LOG.error(ex.getMessage());
			}
		}

		return message;
	}
	
	private Node getRootElement(String xml) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();

		InputSource inputSource = new InputSource(new StringReader(xml));
		
		Document newDoc = builder.parse(inputSource);

		return newDoc.getDocumentElement();
	}
}
