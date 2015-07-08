
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

package uk.ac.stfc.isis.ibex.dae.dataacquisition;

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

public class XmlBackedDaeSettings extends DaeSettings {

    private final XPath xpath = XPathFactory.newInstance().newXPath();
	private Document doc;

	private static final String XPATH_TO_MONITORSPECTRUM_VALUE = "/Cluster/I32[Name='Monitor Spectrum']/Val";
	private static final String XPATH_TO_FROM_VALUE = "/Cluster/DBL[Name='from']/Val";
	private static final String XPATH_TO_TO_VALUE = "/Cluster/DBL[Name='to']/Val";
	private static final String XPATH_TO_WIRINGTABLE_VALUE = "/Cluster/String[Name='Wiring Table']/Val";
	private static final String XPATH_TO_DETECTORTABLE_VALUE = "/Cluster/String[Name='Detector Table']/Val";
	private static final String XPATH_TO_SPECTRATABLE_VALUE = "/Cluster/String[Name='Spectra Table']/Val";
	private static final String XPATH_TO_DAETIMINGSOURCE_VALUE = "/Cluster/EW[Name='DAETimingSource']/Val";
	private static final String XPATH_TO_SMPVETO = "/Cluster/EW[Name='SMP (Chopper) Veto']/Val";
	private static final String XPATH_TO_VETO0 = "/Cluster/EW[Name='Veto 0']/Val";
	private static final String XPATH_TO_VETO1 = "/Cluster/EW[Name='Veto 1']/Val";
	private static final String XPATH_TO_VETO2 = "/Cluster/EW[Name='Veto 2']/Val";
	private static final String XPATH_TO_VETO3 = "/Cluster/EW[Name='Veto 3']/Val";	
	private static final String XPATH_TO_TS2PULSEVETO = "/Cluster/EW[Name=' TS2 Pulse Veto']/Val";
	private static final String XPATH_TO_ISIS50HZVETO = "/Cluster/EW[Name=' ISIS 50Hz Veto']/Val";	
	private static final String XPATH_TO_FERMICHOPPERVETO = "/Cluster/EW[Name=' Fermi Chopper Veto']/Val";
	private static final String XPATH_TO_FCDELAY = "/Cluster/DBL[Name='FC Delay']/Val";
	private static final String XPATH_TO_FCWIDTH = "/Cluster/DBL[Name='FC Width']/Val";
	private static final String XPATH_TO_MUONCERENKOVPULSE = "/Cluster/EW[Name='Muon Cerenkov Pulse']/Val";
	private static final String XPATH_TO_MUONMSMODE = "/Cluster/EW[Name='Muon MS Mode']/Val";	
	
	private Node monitorSpectrum;
	private Node from;
	private Node to;
	private Node wiringTable;
	private Node detectorTable;
	private Node spectraTable;
	private Node daeTimingSource;
	private Node smpVeto;
	private Node veto0;
	private Node veto1;
	private Node veto2;
	private Node veto3;
	private Node ts2PulseVeto;
	private Node isis50HzVeto;
	private Node fermiChopperVeto;
	private Node fcDelay;
	private Node fcWidth;
	private Node muonCerenkovPulse;
	private Node muonMsMode;
	
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
	public void setMonitorSpectrum(int value) {
		super.setMonitorSpectrum(value);
		setInt(monitorSpectrum, value);
	}
	
	@Override
	public void setFrom(double value) {
		super.setFrom(value);
		setDouble(from, value);
	}

	@Override
	public void setTo(double value) {
		super.setTo(value);
		setDouble(to, value);
	}

	@Override
	public void setWiringTable(String value) {
		super.setWiringTable(value);
		
		if (doc == null) {
			return;
		}
		wiringTable.setTextContent(value);
	}
		
	@Override
	public void setDetectorTable(String value) {
		super.setDetectorTable(value);

		if (doc == null) {
			return;
		}
		detectorTable.setTextContent(value);
	}
	
	@Override
	public void setSpectraTable(String value) {
		super.setSpectraTable(value);
		
		if (doc == null) {
			return;
		}
		spectraTable.setTextContent(value);
	}
	
	@Override
	public void setTimingSource(DaeTimingSource value) {
		super.setTimingSource(value);

		if (doc == null) {
			return;
		}
		setInt(daeTimingSource, value.ordinal());
	}
	
	@Override
	public void setSmpVeto(BinaryChoice value) {
		super.setSmpVeto(value);
		
		if (doc == null) {
			return;
		}
		setInt(smpVeto, value.ordinal());
	}

	@Override
	public void setVeto0(BinaryChoice value) {
		super.setVeto0(value);
		
		if (doc == null) {
			return;
		}
		setInt(veto0, value.ordinal());
	}

	@Override
	public void setVeto1(BinaryChoice value) {
		super.setVeto1(value);
		
		if (doc == null) {
			return;
		}
		setInt(veto1, value.ordinal());
	}
	
	@Override
	public void setVeto2(BinaryChoice value) {
		super.setVeto2(value);

		if (doc == null) {
			return;
		}
		setInt(veto2, value.ordinal());
	}
	
	@Override
	public void setVeto3(BinaryChoice value) {
		super.setVeto3(value);

		if (doc == null) {
			return;
		}
		setInt(veto3, value.ordinal());
	}
	
	@Override
	public void setTs2PulseVeto(BinaryChoice value) {
		super.setTs2PulseVeto(value);
		
		if (doc == null) {
			return;
		}
		setInt(ts2PulseVeto, value.ordinal());
	}
	
	@Override
	public void setIsis50HzVeto(BinaryChoice value) {
		super.setIsis50HzVeto(value);
		
		if (doc == null) {
			return;
		}
		setInt(isis50HzVeto, value.ordinal());
	}
	
	@Override
	public void setFermiChopperVeto(BinaryChoice value) {
		super.setFermiChopperVeto(value);
		
		if (doc == null) {
			return;
		}
		setInt(fermiChopperVeto, value.ordinal());
	}
	
	@Override
	public void setFcDelay(double value) {
		super.setFcDelay(value);
		
		if (doc == null) {
			return;
		}
		setDouble(fcDelay, value);
	}
	
	@Override
	public void setFcWidth(double value) {
		super.setFcWidth(value);
		
		if (doc == null) {
			return;
		}
		setDouble(fcWidth, value);
	}
	
	@Override
	public void setMuonCerenkovPulse(MuonCerenkovPulse value) {
		super.setMuonCerenkovPulse(value);
		
		if (doc == null) {
			return;
		}
		setInt(muonCerenkovPulse, value.ordinal());
	}
	
	@Override
	public void setMuonMsMode(BinaryState value) {
		super.setMuonMsMode(value);
		
		if (doc == null) {
			return;
		}
		setInt(muonMsMode, value.ordinal());
	}
	
	private static BinaryChoice parseBinaryChoice(String value) {
		int index = Integer.parseInt(value);
		return BinaryChoice.values()[index];
	}
	
	private static void setDouble(Node node, double value) {
		node.setTextContent(String.format("%f", value));
	}

	private static void setInt(Node node, int value) {
		node.setTextContent(String.format("%d", value));
	}
	
	private void updateNodes() throws XPathExpressionException {
		monitorSpectrum = getNode(XPATH_TO_MONITORSPECTRUM_VALUE);
		from = getNode(XPATH_TO_FROM_VALUE);
		to = getNode(XPATH_TO_TO_VALUE);
		wiringTable = getNode(XPATH_TO_WIRINGTABLE_VALUE);
		detectorTable = getNode(XPATH_TO_DETECTORTABLE_VALUE);
		spectraTable = getNode(XPATH_TO_SPECTRATABLE_VALUE);
		daeTimingSource = getNode(XPATH_TO_DAETIMINGSOURCE_VALUE);
		smpVeto = getNode(XPATH_TO_SMPVETO);
		veto0 = getNode(XPATH_TO_VETO0);
		veto1 = getNode(XPATH_TO_VETO1);
		veto2 = getNode(XPATH_TO_VETO2);
		veto3 = getNode(XPATH_TO_VETO3);
		ts2PulseVeto = getNode(XPATH_TO_TS2PULSEVETO);
		isis50HzVeto = getNode(XPATH_TO_ISIS50HZVETO);
		fermiChopperVeto = getNode(XPATH_TO_FERMICHOPPERVETO);
		fcDelay = getNode(XPATH_TO_FCDELAY);
		fcWidth = getNode(XPATH_TO_FCWIDTH);
		muonCerenkovPulse = getNode(XPATH_TO_MUONCERENKOVPULSE);
		muonMsMode = getNode(XPATH_TO_MUONMSMODE);
	}
	
	
	private void buildDocument(String xml) throws ParserConfigurationException, SAXException, IOException {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();    
	    InputSource source = new InputSource(new StringReader(xml));
	    
	    doc = builder.parse(source);
	}
	
	private void initialiseFromXml() {
		super.setMonitorSpectrum(Integer.parseInt(monitorSpectrum.getTextContent()));
		super.setFrom(Double.parseDouble(from.getTextContent()));
		super.setTo(Double.parseDouble(to.getTextContent()));
		
		super.setWiringTable(wiringTable.getTextContent());	
		super.setDetectorTable(detectorTable.getTextContent());
		super.setSpectraTable(spectraTable.getTextContent());
		
		int index = Integer.parseInt(daeTimingSource.getTextContent());
		super.setTimingSource(DaeTimingSource.values()[index]);

		super.setSmpVeto(parseBinaryChoice(smpVeto.getTextContent()));
		super.setVeto0(parseBinaryChoice(veto0.getTextContent()));
		super.setVeto1(parseBinaryChoice(veto1.getTextContent()));
		super.setVeto2(parseBinaryChoice(veto2.getTextContent()));
		super.setVeto3(parseBinaryChoice(veto3.getTextContent()));
		super.setTs2PulseVeto(parseBinaryChoice(ts2PulseVeto.getTextContent()));
		super.setIsis50HzVeto(parseBinaryChoice(isis50HzVeto.getTextContent()));
		super.setFermiChopperVeto(parseBinaryChoice(fermiChopperVeto.getTextContent()));
		
		super.setFcDelay(Double.parseDouble(fcDelay.getTextContent()));
		super.setFcWidth(Double.parseDouble(fcWidth.getTextContent()));
		
		index = Integer.parseInt(muonCerenkovPulse.getTextContent());
		super.setMuonCerenkovPulse(MuonCerenkovPulse.values()[index]);
		
		index = Integer.parseInt(muonMsMode.getTextContent());
		super.setMuonMsMode(BinaryState.values()[index]);
	}	
	
	private Node getNode(String expression) throws XPathExpressionException {
		NodeList nodes = (NodeList) xpath.compile(expression).evaluate(doc, XPathConstants.NODESET);	
		return nodes.item(0);
	}
}
