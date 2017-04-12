
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.dae.experimentsetup.periods;

import java.util.ArrayList;
import java.util.Collections;
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
 * Gets the DAE period settings from XML.
 */
public class XMLBackedPeriodSettings extends PeriodSettings {

    private static final Logger LOG = IsisLog.getLogger(XMLBackedPeriodSettings.class);
		
	private static final int NUMBER_OF_PERIODS = 8;
	
	private final XmlFile xmlFile;
	private final List<XmlNode<?>> nodes = new ArrayList<>();
	
	private final EnumNode<PeriodSetupSource> setupSource = new EnumNode<>("/Cluster/EW[Name='Period Setup Source']/Val", PeriodSetupSource.class);
	private final StringNode periodFile = new StringNode("/Cluster/String[Name='Period File']/Val");
	private final EnumNode<PeriodControlType> periodType = new EnumNode<>("/Cluster/EW[Name='Period Type']/Val", PeriodControlType.class);
	private final IntegerNode softwarePeriods = new IntegerNode("/Cluster/I32[Name='Number Of Software Periods']/Val");
	private final DoubleNode hardwarePeriods = new DoubleNode("/Cluster/DBL[Name='Hardware Period Sequences']/Val");
	private final DoubleNode outputDelay = new DoubleNode("/Cluster/DBL[Name='Output Delay (us)']/Val");
	
	private final ArrayList<XmlBackedPeriod> periods = new ArrayList<>();
	
	/**
	 * Constructor.
	 */
	public XMLBackedPeriodSettings() {
		nodes.add(setupSource);
		nodes.add(periodFile);
		nodes.add(periodType);
		nodes.add(softwarePeriods);
		nodes.add(hardwarePeriods);
		nodes.add(outputDelay);
		
		for (int i = 1; i <= NUMBER_OF_PERIODS; i++) {
			XmlBackedPeriod period = new XmlBackedPeriod(i);
			nodes.addAll(period.nodes());
			periods.add(period);
		}
		
		xmlFile = new XmlFile(nodes);
	}
	
	/**
	 * Sets the xml.
	 * @param xml the xml to set
	 */
	public void setXml(String xml) {
		xmlFile.setXml(xml);
		initialiseFromXml();
	}

	/**
	 * Gets the xml from file.
	 * @return the xml
	 */
	public String xml() {
		return xmlFile.toString();
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSetupSource(PeriodSetupSource value) {
		super.setSetupSource(value);
		setupSource.setValue(value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNewPeriodFile(String value) {
		super.setNewPeriodFile(value);
		periodFile.setValue(value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPeriodType(PeriodControlType value) {
	    if (value == null) {
	        LOG.info("Error, attempted to set a null PeriodControlType.");
	        return;
	    }
		super.setPeriodType(value);
		periodType.setValue(value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSoftwarePeriods(int value) {
		super.setSoftwarePeriods(value);
		softwarePeriods.setValue(value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHardwarePeriods(double value) {
		super.setHardwarePeriods(value);
		hardwarePeriods.setValue(value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOutputDelay(double value) {
		super.setOutputDelay(value);
		outputDelay.setValue(value);
	}
	
	private void initialiseFromXml() {
        if (setupSource == null || periodFile == null || periodType == null || softwarePeriods == null
                || hardwarePeriods == null || outputDelay == null) {
            LOG.info("Error, Period Settings were not initialised correctly from the XML.");
            return;
        }

		super.setSetupSource(setupSource.value());
		super.setPeriodFile(periodFile.value());
		super.setPeriodType(periodType.value());
		super.setSoftwarePeriods(softwarePeriods.value());
		super.setHardwarePeriods(hardwarePeriods.value());
		super.setOutputDelay(outputDelay.value());
		
		for (XmlBackedPeriod period : periods) {
			period.initialiseFromNodes();
		}
		
		super.getPeriods().clear();
		super.getPeriods().addAll(periods);
		firePropertyChange("periods", Collections.<Period>emptyList(), super.getPeriods());
	}
}
