
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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Holds the descriptive information about the macro.
 *
 */
@XmlRootElement(name = "macro")
@XmlAccessorType(XmlAccessType.FIELD)
public class MacroInfo {
	private String name;
	private String description;
	
	@XmlElement(name = "default")
	private String defaultValue;
	
	/**
	 * Gets the name.
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the description.
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the default value.
	 * @return default
	 */
	public String getDefault() {
		return defaultValue;
	}

	/**
	 * The XML serialisation requires a default constructor.
	 */
	public MacroInfo() { }
	
	/**
	 * This constructor is purely for unit testing.
	 * 
	 * @param name the macro name
	 * @param description a description of what it does
	 */
	public MacroInfo(String name, String description) {
		this.name = name;
		this.description = description;
		this.defaultValue = "";
	}
	
	
}
