
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

import java.util.HashMap;
import java.util.Map;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Holds the descriptions.
 *
 */
@XmlRootElement(name = "descriptions")
@XmlAccessorType(XmlAccessType.FIELD)
public class Descriptions {
	
	@XmlElementWrapper(name = "opis")
	Map<String, OpiDescription> opis = new HashMap<>();

	/**
	 * Gets the OPI descriptions.
	 * 
	 * @return opis the OPI descriptions
	 */
	public Map<String, OpiDescription> getOpis() {
		return opis;
	}
	
	/**
	 * This method is purely used for unit-testing.
	 * 
	 * @param opis the OPI descriptions
	 */
	public void setOpis(Map<String, OpiDescription> opis) {
		this.opis = opis;
	}
}
