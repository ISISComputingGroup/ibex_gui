
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

package uk.ac.stfc.isis.ibex.instrument;

import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.instrument.internal.PVPrefix;

/**
 * Some basic info that defines an instrument. Instruments hostnames will always
 * start with NDX and contain only alphanumeric characters or underscores.
 */
public class InstrumentInfo {

	private final String name;
	
	public InstrumentInfo(String name) {
		this.name = name;
        assert(hostName().matches(validInstrumentRegex()));
	}

	public String name() {
		return name;
	}
	
	public String pvPrefix() {
		return PVAddress.startWith("IN").append(name).toString() + PVAddress.COLON;
	}
	
    public String hostName() {
        return PVPrefix.NDX + name;
	}

    public static String validInstrumentRegex() {
        return PVPrefix.NDX + "[_a-zA-Z0-9]+";
    }

    public boolean hasValidHostName() {
        return hostName().matches(validInstrumentRegex());
    }
}
