
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

package uk.ac.stfc.isis.ibex.instrument.internal;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

/**
 * Some basic info that defines a custom instrument. User can specify a pvPrefix and hostname. <br>
 * If no hostname is specified it is assumed to be the same as the instrument name.
 */
public class CustomInstrumentInfo extends InstrumentInfo {

	/**
	 * Constructor that sets the hostname as the same as the instrument name.
	 * @param name The instrument name
	 * @param pvPrefix The PV prefix for the instrument
	 */
    public CustomInstrumentInfo(String name, String pvPrefix) {
    	this(name, pvPrefix, name);
    }
    
	/**
	 * Constructor that sets a different name, pv prefix and hostname for an instrument.
	 * @param name The instrument name
	 * @param pvPrefix The PV prefix for the instrument
	 * @param hostName The hostname of the instrument PC
	 */
    public CustomInstrumentInfo(String name, String pvPrefix, String hostName) {
        super(name, pvPrefix, hostName);
        
        checkPreconditions(pvPrefix);
    }

    private void checkPreconditions(String pvPrefix) {
        if (pvPrefix == null) {
            throw new RuntimeException("The PV prefix must not be null");
        }
        if (pvPrefix.isEmpty()) {
            throw new IllegalArgumentException("The PV prefix must not be empty");
        }
    }
}
