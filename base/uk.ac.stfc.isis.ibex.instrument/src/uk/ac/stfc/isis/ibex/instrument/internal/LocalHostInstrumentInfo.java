
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

import java.util.ArrayList;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

/**
 * Instrument Information for the instrument on the local host.
 */
public class LocalHostInstrumentInfo extends InstrumentInfo {

    private static final PVPrefixFactory PV_PREFIX_FACTORY = new PVPrefixFactory();

    /**
     * Instantiates a new local host instrument info; allowing the machine name
     * to be set.
     *
     * @param machineName the machine name
     */
    public LocalHostInstrumentInfo(String machineName) {
        super(machineName, PV_PREFIX_FACTORY.fromMachineName(machineName), "localhost", new ArrayList<>());
    }

    /**
     * Instantiates a new local host instrument info; machine name defaults to
     * current machine name.
     */
	public LocalHostInstrumentInfo() {
        this(MachineName.get());
	}

    /**
     * 
     * @return True because host name is always valid for a local instrument
     */
    @Override
    public boolean hasValidHostName() {
        return true;
    }
}
