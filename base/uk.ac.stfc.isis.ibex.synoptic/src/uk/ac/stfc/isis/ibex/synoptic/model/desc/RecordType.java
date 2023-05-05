
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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * The IO mode of a PV in the synoptic view.
 */
@XmlRootElement(name = "recordtype")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecordType {

	private IO io;
	
	/**
	 * Gets the IO mode, read or write, of a PV in a synoptic.
	 * @return the IO mode, read or write, of a PV in a synoptic
	 */
	public IO io() {
		return io;
	}
	
	/**
	 * Set the IO mode of the PV in a synoptic.
	 * @param io io mode to set
	 */
	public void setIO(IO io) {
		this.io = io;
	}
}
