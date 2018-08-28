
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
 * A class to represent a set of PVs.
 *
 */
public class PVSet extends ModelObject {
	private String name;
	private boolean enabled;
	
	/**
	 * A copy constructor for class that represents a set of PVs.
	 * 
	 * @param other
	 *             The PV set to be copied.
	 */
	public PVSet(PVSet other) {
		this.name = other.name;
		this.enabled = other.enabled;
	}
	/**
	 * A constructor for class that represents a set of PVs.
	 * 
	 * @param name
	 *             The PV set name.
	 *             
	 * @param enabled
	 *             True if the PV set is enabled.
	 */
	public PVSet(String name, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
	}

	/**
	 * Returns the PV set name.
	 * 
	 * @return
	 *         The PV set name.
	 */
	public String getName() {
		return name;
	}
	
	/**
     * Returns true if the PV set is enabled.
     * 
     * @return
     *         True if the PV set is enabled.
     */
	public boolean getEnabled() {
		return enabled;
	}
	
	/**
     * Sets whether or not the PV set is enabled.
     * 
     * @param enabled
     *                  True if the PV set is to be enabled.
     */
	public void setEnabled(boolean enabled) {
		firePropertyChange("enabled", this.enabled, this.enabled = enabled);
	}
}
