
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2016 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE public LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse public License v1.0 for more details.
*
* You should have received a copy of the Eclipse public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

package uk.ac.stfc.isis.ibex.dae.dataacquisition;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.dae.xml.DoubleNode;
import uk.ac.stfc.isis.ibex.dae.xml.EnumNode;
import uk.ac.stfc.isis.ibex.dae.xml.IntegerNode;
import uk.ac.stfc.isis.ibex.dae.xml.StringNode;
import uk.ac.stfc.isis.ibex.dae.xml.XmlFile;
import uk.ac.stfc.isis.ibex.dae.xml.XmlNode;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Gets the DAE settings from XML.
 */
public class XmlBackedDaeSettings extends DaeSettings {
    private static final Logger LOG = IsisLog.getLogger(XmlBackedDaeSettings.class);

    private static final String XPATH_TO_DAETIMINGSOURCE = "/Cluster/EW[Name='DAETimingSource']/Val";
	private static final String XPATH_TO_SMPVETO = "/Cluster/EW[Name='SMP (Chopper) Veto']/Val";
	private static final String XPATH_TO_VETO0 = "/Cluster/EW[Name='Veto 0']/Val";
	private static final String XPATH_TO_VETO1 = "/Cluster/EW[Name='Veto 1']/Val";
	private static final String XPATH_TO_VETO2 = "/Cluster/EW[Name='Veto 2']/Val";
	private static final String XPATH_TO_VETO3 = "/Cluster/EW[Name='Veto 3']/Val";	
	private static final String XPATH_TO_TS2PULSEVETO = "/Cluster/EW[Name=' TS2 Pulse Veto']/Val";
	private static final String XPATH_TO_ISIS50HZVETO = "/Cluster/EW[Name=' ISIS 50Hz Veto']/Val";	
	private static final String XPATH_TO_FERMICHOPPERVETO = "/Cluster/EW[Name=' Fermi Chopper Veto']/Val";
	private static final String XPATH_TO_MUONCERENKOVPULSE = "/Cluster/EW[Name='Muon Cerenkov Pulse']/Val";
	private static final String XPATH_TO_MUONMSMODE = "/Cluster/EW[Name='Muon MS Mode']/Val";	
	
    private IntegerNode monitorSpectrum = new IntegerNode("/Cluster/I32[Name='Monitor Spectrum']/Val");
    private DoubleNode from = new DoubleNode("/Cluster/DBL[Name='from']/Val");
    private DoubleNode to = new DoubleNode("/Cluster/DBL[Name='to']/Val");
    private StringNode wiringTable = new StringNode("/Cluster/String[Name='Wiring Table']/Val");
    private StringNode detectorTable = new StringNode("/Cluster/String[Name='Detector Table']/Val");
    private StringNode spectraTable = new StringNode("/Cluster/String[Name='Spectra Table']/Val");
    private EnumNode<DaeTimingSource> daeTimingSource = new EnumNode<>(XPATH_TO_DAETIMINGSOURCE, DaeTimingSource.class);

    private EnumNode<BinaryChoice> smpVeto = new EnumNode<>(XPATH_TO_SMPVETO, BinaryChoice.class);
    private EnumNode<BinaryChoice> veto0 = new EnumNode<>(XPATH_TO_VETO0, BinaryChoice.class);
    private EnumNode<BinaryChoice> veto1 = new EnumNode<>(XPATH_TO_VETO1, BinaryChoice.class);
    private EnumNode<BinaryChoice> veto2 = new EnumNode<>(XPATH_TO_VETO2, BinaryChoice.class);
    private EnumNode<BinaryChoice> veto3 = new EnumNode<>(XPATH_TO_VETO3, BinaryChoice.class);
    private EnumNode<BinaryChoice> ts2PulseVeto = new EnumNode<>(XPATH_TO_TS2PULSEVETO, BinaryChoice.class);
    private EnumNode<BinaryChoice> isis50HzVeto = new EnumNode<>(XPATH_TO_ISIS50HZVETO, BinaryChoice.class);
    private EnumNode<BinaryChoice> fermiChopperVeto = new EnumNode<>(XPATH_TO_FERMICHOPPERVETO, BinaryChoice.class);

    private DoubleNode fcDelay = new DoubleNode("/Cluster/DBL[Name='FC Delay']/Val");
    private DoubleNode fcWidth = new DoubleNode("/Cluster/DBL[Name='FC Width']/Val");

    private EnumNode<MuonCerenkovPulse> muonCerenkovPulse =
            new EnumNode<>(XPATH_TO_MUONCERENKOVPULSE, MuonCerenkovPulse.class);
    private EnumNode<BinaryState> muonMsMode = new EnumNode<>(XPATH_TO_MUONMSMODE, BinaryState.class);
	
    private final XmlFile xmlFile;
    private final List<XmlNode<?>> nodes = new ArrayList<>();

    /**
     * Constructor. Creates the xml structure that backs the DaeSettings.
     */
    public XmlBackedDaeSettings() {
        nodes.add(monitorSpectrum);
        nodes.add(from);
        nodes.add(to);
        nodes.add(wiringTable);
        nodes.add(detectorTable);
        nodes.add(spectraTable);
        nodes.add(daeTimingSource);
        nodes.add(smpVeto);
        nodes.add(veto0);
        nodes.add(veto1);
        nodes.add(veto2);
        nodes.add(veto3);
        nodes.add(ts2PulseVeto);
        nodes.add(isis50HzVeto);
        nodes.add(fermiChopperVeto);
        nodes.add(fcDelay);
        nodes.add(fcWidth);
        nodes.add(muonCerenkovPulse);
        nodes.add(muonMsMode);

        xmlFile = new XmlFile(nodes);
    }

    /**
     * Updates the setting based on some xml.
     * 
     * @param xml
     *            the xml to update the settings with
     */
	public void setXml(String xml) {
        xmlFile.setXml(xml);
        initialiseFromXml();
	}
	
    /**
     * Gets the xml from file.
     * 
     * @return the xml
     */
	public String xml() {
		System.out.println(xmlFile.toString());
        return xmlFile.toString();
	} 
	
	@Override
	public void setMonitorSpectrum(int value) {
		super.setMonitorSpectrum(value);
        monitorSpectrum.setValue(value);
	}
	
	@Override
	public void setFrom(double value) {
		super.setFrom(value);
        from.setValue(value);
	}

	@Override
	public void setTo(double value) {
		super.setTo(value);
        to.setValue(value);
	}

	@Override
	public void setNewWiringTable(String value) {
		super.setNewWiringTable(value);
        wiringTable.setValue(value);
	}
		
	@Override
	public void setNewDetectorTable(String value) {
		super.setNewDetectorTable(value);
        detectorTable.setValue(value);
	}
	
	@Override
	public void setNewSpectraTable(String value) {
		System.out.println("New spectra table: " + value);
		super.setNewSpectraTable(value);
        spectraTable.setValue(value);
	}
	
	@Override
	public void setTimingSource(DaeTimingSource value) {
		super.setTimingSource(value);
        daeTimingSource.setValue(value);
	}
	
	@Override
	public void setSmpVeto(BinaryChoice value) {
		super.setSmpVeto(value);
        smpVeto.setValue(value);
	}

	@Override
	public void setVeto0(BinaryChoice value) {
		super.setVeto0(value);
        veto0.setValue(value);
	}

	@Override
	public void setVeto1(BinaryChoice value) {
		super.setVeto1(value);
        veto1.setValue(value);
	}
	
	@Override
	public void setVeto2(BinaryChoice value) {
		super.setVeto2(value);
        veto2.setValue(value);
	}
	
	@Override
	public void setVeto3(BinaryChoice value) {
		super.setVeto3(value);
        veto3.setValue(value);
	}
	
	@Override
	public void setTs2PulseVeto(BinaryChoice value) {
		super.setTs2PulseVeto(value);
        ts2PulseVeto.setValue(value);
	}
	
	@Override
	public void setIsis50HzVeto(BinaryChoice value) {
		super.setIsis50HzVeto(value);
        isis50HzVeto.setValue(value);
	}
	
	@Override
	public void setFermiChopperVeto(BinaryChoice value) {
		super.setFermiChopperVeto(value);
        fermiChopperVeto.setValue(value);
	}
	
	@Override
	public void setFcDelay(double value) {
		super.setFcDelay(value);
        fcDelay.setValue(value);
	}
	
	@Override
	public void setFcWidth(double value) {
		super.setFcWidth(value);
        fcWidth.setValue(value);
	}
	
	@Override
	public void setMuonCerenkovPulse(MuonCerenkovPulse value) {
		super.setMuonCerenkovPulse(value);
        muonCerenkovPulse.setValue(value);
	}
	
	@Override
	public void setMuonMsMode(BinaryState value) {
		super.setMuonMsMode(value);
        muonMsMode.setValue(value);
	}
	
	private void initialiseFromXml() {
		
		System.out.println("INITIALIZING FROM XML");
		
        for (XmlNode<?> node : nodes) {
            if (node == null || node.value() == null) {
                LOG.info("Error, Dae Settings were not initialised correctly from the XML.");
                return;
            }
        }

        setMonitorSpectrum(monitorSpectrum.value());
        setFrom(from.value());
        setTo(to.value());
        
        setWiringTable(wiringTable.value());
        setDetectorTable(detectorTable.value());
        setSpectraTable(spectraTable.value());
		
        setNewWiringTable(wiringTable.value());
        setNewDetectorTable(detectorTable.value());
        setNewSpectraTable(spectraTable.value());
		
        setTimingSource(daeTimingSource.value());

        setSmpVeto(smpVeto.value());
        setVeto0(veto0.value());
        setVeto1(veto1.value());
        setVeto2(veto2.value());
        setVeto3(veto3.value());
        setTs2PulseVeto(ts2PulseVeto.value());
        setIsis50HzVeto(isis50HzVeto.value());
        setFermiChopperVeto(fermiChopperVeto.value());
		
        setFcDelay(fcDelay.value());
        setFcWidth(fcWidth.value());
		
        setMuonCerenkovPulse(muonCerenkovPulse.value());
        setMuonMsMode(muonMsMode.value());
	}
}
