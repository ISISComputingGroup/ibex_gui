
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

package uk.ac.stfc.isis.ibex.synoptic.model.desc;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.xml.sax.SAXException;

import uk.ac.stfc.isis.ibex.synoptic.xml.XMLUtil;

/**
 * This class is converted to XML, to be stored on the local file system and read 
 * by the blockserver.
 * 
 * Note any changes here will require corresponding changes to 
 * EPICS/schema/configurations/synoptic.xsd.
 * 
 */
@XmlRootElement(name = "instrument")
@XmlAccessorType(XmlAccessType.FIELD)
public class SynopticDescription implements SynopticParentDescription {
	private String name;
	private String showbeam;
	
	@XmlElementWrapper(name = "components")
	@XmlElement(name = "component", type = ComponentDescription.class)
	private ArrayList<ComponentDescription> components = new ArrayList<>();
	
    /**
     * Gets an empty synoptic.
     * 
     * @return an empty description
     */
    public static SynopticDescription getEmptySynopticDescription() {
        SynopticDescription toReturn = new SynopticDescription();
        toReturn.setShowBeam(false);
        return toReturn;
    }

    /**
     * Gets the synoptic name.
     * 
     * @return the name
     */
	public String name() {
		return name;
	}
	
    /**
     * Set the synoptic's name.
     * 
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Determines whether the beam is shown on the synoptic.
     * 
     * @return true means show it
     */
	public boolean showBeam() {
		if (showbeam == null) {
			return true;
		}
		
		return Boolean.valueOf(showbeam);
	}
	
    /**
     * Sets whether to show the beam on the synoptic.
     * 
     * @param showBeam true means show it
     */
	public void setShowBeam(boolean showBeam) {
		if (showBeam) {
			showbeam = "TRUE";
		} else {
			showbeam = "FALSE";
		}
	}
	
	@Override
    public List<ComponentDescription> components() {
		return components;
	}
	
	@Override
    public void addComponent(ComponentDescription component) {
		components.add(component);
	}
	
	@Override
    public void addComponent(ComponentDescription component, int index) {
		components.add(index, component);
	}
	
	@Override
    public void removeComponent(ComponentDescription component) {
		components.remove(component);
	}
	
    /**
     * Processes the child components. Sets the parent for the components to
     * this.
     */
	public void processChildComponents() {
		for (ComponentDescription cd: components) {
			cd.setParent(null);
			cd.processChildComponents();
		}
	}
	
    /**
     * Gets the description as XML.
     * 
     * @return an XML string
     * @throws JAXBException standard XML parsing error
     * @throws SAXException standard XML parsing error
     */
	public String getXmlDescription() throws JAXBException, SAXException {
		return XMLUtil.toXml(this);
	}

    /**
     * Provide a list of the component names, but not children.
     * 
     * @return A list of the component names
     */
    public List<String> getComponentNameList() {
        ArrayList<String> nameList = new ArrayList<>();

        for (ComponentDescription cd : components) {
            nameList.add(cd.name());
        }

        return nameList;
    }

    /**
     * Provide a list of the component names, including children.
     * 
     * @return A list of the component names
     */
    public List<String> getComponentNameListWithChildren() {
        ArrayList<String> nameList = new ArrayList<>();

        for (ComponentDescription cd : components) {
            nameList.add(cd.name());
            getChildNames(cd, nameList);
        }

        return nameList;
    }

    private List<String> getChildNames(ComponentDescription component, List<String> nameList) {

        for (ComponentDescription cd : component.components()) {
            nameList.add(cd.name());
            getChildNames(cd, nameList);
        }

        return nameList;
    }
}
