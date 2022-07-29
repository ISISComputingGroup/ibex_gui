
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

package uk.ac.stfc.isis.ibex.configserver.configuration;

/**
 * Holds the details of an available PV.
 *
 */
public class AvailablePV {
	private String name;
	private String description;
	
	/**
	 * 
	 * @return PV name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set this PV's name.
	 * 
	 * @param name new PV name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return PV description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Set this PV's description.
	 * 
	 * @param description new PV description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
