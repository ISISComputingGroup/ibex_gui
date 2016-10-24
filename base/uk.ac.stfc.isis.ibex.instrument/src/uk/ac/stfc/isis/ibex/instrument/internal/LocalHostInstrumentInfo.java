
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

package uk.ac.stfc.isis.ibex.instrument.internal;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

/**
 * Instrument Information for the instrument on the local host.
 */
public class LocalHostInstrumentInfo extends InstrumentInfo {

    /**
     * Instantiates a new local host instrument info; allowing the machine name
     * to be set.
     *
     * @param machineName the machine name
     */
    public LocalHostInstrumentInfo(String machineName) {
        super(machineName, constructPvPrefix(machineName), "localhost");
    }

    /**
     * Instantiates a new local host instrument info; machine name defaults to
     * current machine name.
     */
	public LocalHostInstrumentInfo() {
        this(MachineName.get());
	}

    /**
     * Construct pv prefix.
     *
     * @param machineName the machine name
     * @return the pvs prefix
     */
    private static String constructPvPrefix(String machineName) {
        PVPrefix pvPrefix = new PVPrefix(machineName);
        return pvPrefix.toString();
	}

    /**
     * Valid local instrument regex.
     *
     * @return the string
     */
    public static String validLocalInstrumentRegex() {
        return "localhost";
    }

    /**
     * @return
     */
    @Override
    public boolean hasValidHostName() {
        return true;
    }
}
