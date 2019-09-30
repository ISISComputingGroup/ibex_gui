
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

package uk.ac.stfc.isis.ibex.experimentdetails;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * A parameter with a name, units, and a value.
 */
public class Parameter extends ModelObject {

	private String name;
	private String units;
	private String value;

	/**
	 * @return the name of the parameter
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the parameter.
	 * 
	 * @param name the name to set
	 */
	protected void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}
	
	/**
     * @return the units of the parameter
     */
	public String getUnits() {
		return units;
	}
	
	/**
	 * Sets the units of the parameter.
	 * 
	 * @param units the units to set
	 */
	protected void setUnits(String units) {
		firePropertyChange("units", this.units, this.units = units);
	}
	
	/**
     * @return the value of the parameter
     */
	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the value of the parameter.
	 * 
	 * @param value the value to set.
	 */
	public void setValue(String value) {
		firePropertyChange("value", this.value, this.value = value);
	}
}
