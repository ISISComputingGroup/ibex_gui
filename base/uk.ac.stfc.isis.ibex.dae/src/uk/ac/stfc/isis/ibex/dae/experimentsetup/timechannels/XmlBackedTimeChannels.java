
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

package uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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

public class XmlBackedTimeChannels extends TimeChannels {
    private final XPath xpath = XPathFactory.newInstance().newXPath();
	private Document doc;
	
	private static final int ROWS_PER_MODEL = 5;
	private static final int NUMBER_OF_MODELS = 6;
		
	private static final String XPATH_TO_DBL = "/Cluster/DBL";
	private static final String XPATH_TO_U16 = "/Cluster/U16";
	
	private static final String XPATH_TO_TIMECHANNEL_FILE_VALUE = "/Cluster/String[Name='Time Channel File']/Val";
	private static final String XPATH_TO_TIMEUNIT_VALUE = "/Cluster/U16[Name='Time Unit']/Val";
	private static final String XPATH_TO_CALCMETHOD_VALUE = "/Cluster/U16[Name='Calculation Method']/Val";

	private Node timeChannelFile;
	private Node timeUnit;
	private Node calculationMethod;
	
	public void setXml(String xml) {
		try {
			buildDocument(xml);
			updateNodes();
			updateTimeRegimes();
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
	public void setNewTimeChannelFile(String value) {
		super.setNewTimeChannelFile(value);
		
		if (doc == null) { 
			return; 
		}
		timeChannelFile.setTextContent(value);
	}
	
	@Override
	public void setTimeUnit(TimeUnit value) {
		super.setTimeUnit(value);
		
		if (doc == null) { 
			return; 
		}
		setInt(timeUnit, value.ordinal());
	}
	
	@Override
	public void setCalculationMethod(CalculationMethod value) {
		super.setCalculationMethod(value);
		
		if (doc == null) { 
			return; 
		}
		setInt(calculationMethod, value.ordinal());
	}
	
	private void initialiseFromXml() {
		super.setTimeChannelFile(timeChannelFile.getTextContent());
		
		int index = Integer.parseInt(timeUnit.getTextContent());
		super.setTimeUnit(TimeUnit.values()[index]);
		
		index = Integer.parseInt(calculationMethod.getTextContent());
		super.setCalculationMethod(CalculationMethod.values()[index]);
	}
	
	private void updateNodes() throws XPathExpressionException {
		timeChannelFile = getNode(XPATH_TO_TIMECHANNEL_FILE_VALUE);
		timeUnit = getNode(XPATH_TO_TIMEUNIT_VALUE);
		calculationMethod = getNode(XPATH_TO_CALCMETHOD_VALUE);
	}

	private void updateTimeRegimes() throws XPathExpressionException {
		ArrayList<TimeRegime> timeRegimes = new ArrayList<>();
		
		for (int modelCount = 1; modelCount <= NUMBER_OF_MODELS; modelCount++) {
			List<TimeRegimeRow> rows = new ArrayList<>();
			for (int rowCount = 1; rowCount <= ROWS_PER_MODEL; rowCount++) {		
				Node from = getNode(xPathFrom(modelCount, rowCount));
				Node to = getNode(xPathTo(modelCount, rowCount));
				Node step = getNode(xPathStep(modelCount, rowCount));
				Node mode = getNode(xPathMode(modelCount, rowCount));
	
				rows.add(new XmlNodeBackedTimeRegimeRow(from, to, step, mode));
			}
						
			timeRegimes.add(new TimeRegime(rows));
		}
		
		super.setTimeRegimes(timeRegimes);
	}
		
	private static String xPathFrom(int model, int row) {
		return XPATH_TO_DBL + String.format("[Name='TR%d From %d']/Val", model, row);
	}

	private static String xPathTo(int model, int row) {
		return XPATH_TO_DBL + String.format("[Name='TR%d To %d']/Val", model, row);
	}

	private static String xPathStep(int model, int row) {
		return XPATH_TO_DBL + String.format("[Name='TR%d Steps %d']/Val", model, row);
	}

	private static String xPathMode(int model, int row) {
		return XPATH_TO_U16 + String.format("[Name='TR%d In Mode %d']/Val", model, row);
	}
	
	private static void setInt(Node node, int value) {
		node.setTextContent(String.format("%d", value));
	}
	
	private Node getNode(String expression) throws XPathExpressionException {
		NodeList nodes = (NodeList) xpath.compile(expression).evaluate(doc, XPathConstants.NODESET);	
		return nodes.item(0);
	}
	
	private void buildDocument(String xml) throws ParserConfigurationException, SAXException, IOException {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();    
	    InputSource source = new InputSource(new StringReader(xml));
	    
	    doc = builder.parse(source);
	}
}
