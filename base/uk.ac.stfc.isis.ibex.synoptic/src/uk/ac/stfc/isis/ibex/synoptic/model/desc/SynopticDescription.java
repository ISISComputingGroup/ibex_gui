
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

@XmlRootElement(name = "instrument")
@XmlAccessorType(XmlAccessType.FIELD)
public class SynopticDescription {
	private String name;
	
	private String showbeam;
	
	@XmlElementWrapper(name = "components")
	@XmlElement(name = "component", type = ComponentDescription.class)
	private ArrayList<ComponentDescription> components = new ArrayList<>();
	
	public String name() {
		return name;
	}
	
	public boolean showBeam() {
		return Boolean.valueOf(showbeam);
	}
	
	public void setShowBeam(boolean showBeam) {
		if (showBeam) {
			showbeam = "TRUE";
		} else {
			showbeam = "FALSE";
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<ComponentDescription> components() {
		return components;
	}
	
	public void addComponent(ComponentDescription component) {
		components.add(component);
	}
	
	public void addComponent(ComponentDescription component, int index) {
		components.add(index, component);
	}
	
	public void removeComponent(ComponentDescription component) {
		components.remove(component);
	}
	
	public void processChildComponents() {
		for (ComponentDescription cd: components) {
			cd.setParent(null);
			cd.processChildComponents();
		}
	}
	
	public String getXmlDescription() throws JAXBException, SAXException {
		return XMLUtil.toXml(this);
	}
}
