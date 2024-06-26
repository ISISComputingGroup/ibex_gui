
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2023
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any kind,
 * either expressed or implied, including but not limited to the implied
 * warranties of merchantability and/or fitness for a particular purpose.
 */
package uk.ac.stfc.isis.ibex.ui.moxas.views;

import java.util.List;
import java.util.ArrayList;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;

/**
 * Class to hold information about the mapping between physical moxa ports and
 * COM numbers.
 *
 */
public class MoxaModelObject extends ModelObject implements Comparable<MoxaModelObject> {

	private final String physport;
	private final String comport;
	private final List<Ioc> iocs;
	private final String status;
	private final String additionalInfo;

	/**
	 * Instantiates a new moxa mapping pair.
	 *
	 * @param physport physical moxa port number for a mapping.
	 * @param comport  COM port for a mapping.
	 * @param iocs List of IOCs using the COM port.
	 * @param status the Operation Status information
	 * @param additionalInfo Any additional information available for the port
	 */
	public MoxaModelObject(String physport, String comport, List<Ioc> iocs, String status, String additionalInfo) {
		this.physport = physport;
		this.comport = comport;
		this.iocs = new ArrayList<Ioc>(iocs);
		this.additionalInfo = additionalInfo;
		this.status = status;
	}

	/**
	 * Gets physical moxa port number for a mapping.
	 *
	 * @return the port number
	 */

	public String getPhysPort() {
		return physport;
	}

	/**
	 * Gets the COM port for a mapping.
	 *
	 * @return COM port string
	 */
	public String getComPort() {
		return comport;
	}
	
	/**
	 * @return IOCs that use the COM port
	 */
	public List<Ioc> getIocs() {
		return new ArrayList<Ioc>(iocs);
	}

	/**
	 * @return status of the port
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 
	 * @return any additional information
	 */
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(MoxaModelObject moxaModelObject) {
		return physport.compareTo(moxaModelObject.getPhysPort());
	}
}
