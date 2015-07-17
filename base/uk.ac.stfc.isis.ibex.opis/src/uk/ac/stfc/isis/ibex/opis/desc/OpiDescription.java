
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

package uk.ac.stfc.isis.ibex.opis.desc;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A class for holding the OPI description.
 *
 */
@XmlRootElement(name = "opi")
@XmlAccessorType(XmlAccessType.FIELD)
public class OpiDescription {
	private String path;
	private String description;
	
	@XmlElementWrapper(name = "macros")
	@XmlElement(name = "macro", type = MacroInfo.class)
	private List<MacroInfo> macros = new ArrayList<>();
	
	public String getPath() {
		return path;
	}

	public String getDescription() {
		return description;
	}

	public List<MacroInfo> getMacros() {
		return macros;
	}
	
	/**
	 * The XML serialisation requires a default constructor.
	 */
	public OpiDescription() { }
	
	/**
	 * This constructor is purely for unit testing.
	 * 
	 * @param path the relative OPI path
	 * @param description the description of the OPI
	 * @param macros the macros for the OPI
	 */
	public OpiDescription(String path, String description, List<MacroInfo> macros) {
		this.path = path;
		this.description = description;
		this.macros = macros;
	}

}
