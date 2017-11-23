
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

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.XMLBackedPeriodSettings;
import uk.ac.stfc.isis.ibex.dae.xml.EnumNode;
import uk.ac.stfc.isis.ibex.dae.xml.IntegerNode;
import uk.ac.stfc.isis.ibex.dae.xml.XmlFile;
import uk.ac.stfc.isis.ibex.dae.xml.XmlNode;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Gets the DAE update settings from XML.
 */
public class XMLBackedUpdateSettings extends UpdateSettings {
    private static final Logger LOG = IsisLog.getLogger(XMLBackedPeriodSettings.class);

    private EnumNode<AutosaveUnit> autosaveUnits =
            new EnumNode<>("/Cluster/EW[Name='Units']/Val", AutosaveUnit.class);
    private IntegerNode autosaveFrequency = new IntegerNode("/Cluster/U32[Name=' Frequency']/Val");

    private final XmlFile xmlFile;
    private final List<XmlNode<?>> nodes = new ArrayList<>();

    /**
     * Constructor. Creates the xml structure that backs the UpdateSettings.
     */
    public XMLBackedUpdateSettings() {
        nodes.add(autosaveFrequency);
        nodes.add(autosaveUnits);

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
        return xmlFile.toString();
	}	
	
	@Override
	public void setAutosaveFrequency(int value) {
		super.setAutosaveFrequency(value);
        autosaveFrequency.setValue(value);
	}
	
	@Override
	public void setAutosaveUnits(AutosaveUnit value) {
		super.setAutosaveUnits(value);
        autosaveUnits.setValue(value);
	}
	
    private void initialiseFromXml() {
        for (XmlNode<?> node : nodes) {
            if (node == null || node.value() == null) {
                LOG.info("Error, Update Settings were not initialised correctly from the XML.");
                return;
            }
        }
	    
        super.setAutosaveFrequency(autosaveFrequency.value());
        super.setAutosaveUnits(autosaveUnits.value());
	}	
}
