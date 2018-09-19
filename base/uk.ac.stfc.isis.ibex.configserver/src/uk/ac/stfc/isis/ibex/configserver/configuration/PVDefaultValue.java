
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

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * A class to give a default value to a PV.
 *
 */
public class PVDefaultValue extends ModelObject {
	private String name;
	private String value;
	
	/**
	 * A copy constructor for a class to give a default value to a PV.
	 *
	 * @param other
	 *             The PV default value to be copied.
	 */
	public PVDefaultValue(PVDefaultValue other) {
		this.name = other.name;
		this.value = other.value;
	}
	
	/**
     * A constructor for a class to give a default value to a PV.
     *
     * @param name
     *             The name of the PV.
     * @param value
     *             The default value for the PV.
     */
	public PVDefaultValue(String name, String value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * Returns the PV's name.
	 * 
	 * @return
	 *         The PV's name.
	 */
	public String getName() {
		return name;
	}
	
	/**
     * Sets the PV's name.
     * 
     * @param name
     *              The PV's name.
     */
	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	/**
     * Returns the PV's default value..
     * 
     * @return
     *         The PV's default value..
     */
	public String getValue() {
		return value;
	}
	
	/**
     * Sets the PV's default value.
     * 
     * @param value
     *              The PV's default value.
     */
	public void setValue(String value) {
		firePropertyChange("value", this.value, this.value = value);
	}
}
