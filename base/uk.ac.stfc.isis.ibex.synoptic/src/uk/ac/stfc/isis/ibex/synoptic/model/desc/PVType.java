
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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * ENUM of types and an associated display name for linking to PVs for the synoptic
 * BLOCK is commented as a block lookup is required, but am starting with the PVs
 *
 */
@XmlType(name = "pvtype")
@XmlEnum(String.class)
public enum PVType {
	LOCAL_PV ("Local PV"),
	REMOTE_PV ("Remote PV");
	
	private String displayName;
	
	private PVType(String displayName) {
		this.displayName = displayName;
	}
	
	public String display() {
		return displayName;
	}
	
	@Override
	public String toString () {
		return displayName;
	}
}
