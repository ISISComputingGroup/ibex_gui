
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.instrument.custom;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.internal.PVPrefix;

/**
 * Some basic info that defines a custom instrument. Instrument hostname is the
 * same as the instrument name. User must specify a pvPrefix.
 */
public class CustomInstrumentInfo extends InstrumentInfo {

    private String pvPrefix;

    public CustomInstrumentInfo(String name, String pvPrefix) {
        super(name);

        checkPreconditions(pvPrefix);
        this.pvPrefix = pvPrefix;
    }

    @Override
    public String pvPrefix() {
        return pvPrefix;
    }

    @Override
    public String hostName() {
        return name();
    }

    public static String validCustomInstrumentRegex() {
        return PVPrefix.NDW + "[_a-zA-Z0-9]+";
    }

    @Override
    public boolean hasValidHostName() {
        return hostName().matches(validCustomInstrumentRegex());
    }

    private void checkPreconditions(String pvPrefix) {
        if (pvPrefix.isEmpty()) {
            String msg = "The PV prefix must not be empty";
            throw new IllegalArgumentException(msg);
        }
    }
}
