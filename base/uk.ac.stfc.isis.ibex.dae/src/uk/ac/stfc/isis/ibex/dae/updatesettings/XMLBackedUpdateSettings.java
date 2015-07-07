
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

package uk.ac.stfc.isis.ibex.dae.updatesettings;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLBackedUpdateSettings extends UpdateSettings {
	private static final String XPATH_TO_AUTOSAVE_FREQ = "/Cluster/U32[Name=' Frequency']/Val";
	private static final String XPATH_TO_AUTOSAVE_UNITS = "/Cluster/EW[Name='Units']/Val";	
	
	private Node autosaveUnits;
	private Node autosaveFrequency;

	private final XPath xpath = XPathFactory.newInstance().newXPath();
	private Document doc;
		
	public void setXml(String xml) {
		try {
			buildDocument(xml);
			updateNodes();
			initialiseFromXml();
		} catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	public String xml() {
		if (doc == null) {
			return "";
		}
		
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String output = writer.getBuffer().toString();
			
			return output;
		} catch (TransformerException e) {
			return "";
		}
	}	
	
	@Override
	public void setAutosaveFrequency(int value) {
		super.setAutosaveFrequency(value);
		
		if (doc == null) { 
			return; 
		}
		setInt(autosaveFrequency, value);
	}
	
	@Override
	public void setAutosaveUnits(AutosaveUnit value) {
		super.setAutosaveUnits(value);
		
		if (doc == null) { 
			return; 
		}
		setInt(autosaveUnits, value.ordinal());
	}
	
	private static void setInt(Node node, int value) {
		node.setTextContent(String.format("%d", value));
	}
	
	private void buildDocument(String xml) throws ParserConfigurationException, SAXException, IOException {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();    
	    InputSource source = new InputSource(new StringReader(xml));
	    
	    doc = builder.parse(source);
	}
	
	private void initialiseFromXml() {
		super.setAutosaveFrequency(Integer.parseInt(autosaveFrequency.getTextContent()));
		
		int index = Integer.parseInt(autosaveUnits.getTextContent());
		super.setAutosaveUnits(AutosaveUnit.values()[index]);
	}	
	
	private void updateNodes() throws XPathExpressionException {
		autosaveFrequency = getNode(XPATH_TO_AUTOSAVE_FREQ);
		autosaveUnits = getNode(XPATH_TO_AUTOSAVE_UNITS);
	}
	
	private Node getNode(String expression) throws XPathExpressionException {
		NodeList nodes = (NodeList) xpath.compile(expression).evaluate(doc, XPathConstants.NODESET);	
		return nodes.item(0);
	}
}
