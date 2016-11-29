
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

import uk.ac.stfc.isis.ibex.instrument.internal.PVPrefixFactory;

/**
 * Some basic info that defines an instrument. Instruments hostnames will always
 * start with NDX and contain only alphanumeric characters or underscores.
 */
public class InstrumentInfo {

	private final String name;
    private String pvPrefix;
    private final String hostName;

	/**
     * Constructor for creating any general instrument. CONSTRUCTORS NOT CALLED
     * FOR INSTRUMENTS IN INSTLIST AS JSON DESERIALISED. THIS INCLUDES INLINE
     * CONSTRUCTOR.
     * 
     * @param name The user friendly name of the instrument
     * @param pvPrefix The PV prefix used to connect to the instrument
     * @param hostName The host name of the machine that the instrument is
     *            running on
     */
	public InstrumentInfo(String name, String pvPrefix, String hostName) {
		this.name = name;
		this.hostName = hostName;
		this.pvPrefix = pvPrefix;
        assert (hasValidHostName());
	}

	/**
	 * @return The user friendly name of the instrument.
	 */
	public String name() {
		return name;
	}
	
	/**
	 * @return The PV prefix of the instrument. Returns IN:name: if no pvPrefix has been set.
	 */
	public String pvPrefix() {
        if (pvPrefix == null) {
            pvPrefix = new PVPrefixFactory().fromInstrumentName(name);
        }

        return pvPrefix;
	}
	
	/**
     * @return The hostname of the machine that the instrument is running on.
     *         Returns NDX + name if no host name has been set. This is the
     *         default and will work on most instruments from the instrument
     *         list, usually the instrument list will explicitly set the
     *         hostname.
     */
    public String hostName() {
    	return hostName == null ? PVPrefixFactory.DEFAULT_HOSTNAME_PREFIX + name : hostName;
	}

    /**
     * @return Regex describing a valid default instrument hostname.
     */
    private static String validInstrumentRegex() {
        return "[_a-zA-Z0-9]+";
    }

    /**
     * A helper method to check the instrument host name against its valid regex.
     * @return True if the host name is valid.
     */
    public boolean hasValidHostName() {
        return hostName().matches(validInstrumentRegex());
    }

    /**
     * @return String representation of instrument info
     */
    @Override
    public String toString() {
        return String.format("Instrument %s (%s, %s)", this.name, this.hostName(), this.pvPrefix);
    }
}
