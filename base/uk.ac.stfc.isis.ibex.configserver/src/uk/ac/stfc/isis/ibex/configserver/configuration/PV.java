
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
 *  A class to represent PVs.
 *
 */
public class PV extends ModelObject {
	
	private String address;
	private String type;
	private String description;
	private String iocName;
	
	/**
	 * The constructor to represent PVs.
	 * 
	 * @param address
	 *                 The PV address.
	 * @param type
	 *                 The PV type.
	 * @param description
	 *                 The description of the PV.
	 * @param iocName
	 *                 The name of the IOC to which the PV is associated.
	 */
	public PV(String address, String type, String description, String iocName) {
		this.address = address;
		this.type = type;
		this.description = description;
		this.iocName = iocName;
	}

	/**
     * Returns the PV address.
     * 
     * @return 
     *          The PV address.
     */
	public String getAddress() {
		return address;
	}
	
	/**
     * Sets the PV address.
     * 
     * @param address
     *                 The PV address.
     */
	public void setAddress(String address) {
		firePropertyChange("address", this.address, this.address = address);
	}
	
	/**
     * Returns the PV type.
     * 
     * @return 
     *          The PV type.
     */
	public String type() {
		return type;
	}

	/**
     * Returns the PV description.
     * 
     * @return 
     *          The PV description.
     */
	public String description() {
		return description;
	}

	/**
     * Returns the name of the IOC to which the PV is associated.
     * 
     * @return 
     *          The name of the IOC to which the PV is associated.
     */
	public String iocName() {
		return iocName;
	}
	
	@Override
	public String toString() {
		return address;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PV)) {
			return false;
		}
		
		if (obj == this) {
			return true;
		}
		
		PV other = (PV) obj;
		return address.equals(other.address);
	}
	
	@Override
	public int hashCode() {
		return address.hashCode();
	}
}
