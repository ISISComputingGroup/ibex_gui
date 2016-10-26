
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

import com.google.common.collect.ImmutableList;

/**
 * The Class PVPrefix representing the prefix for the instrument.
 */
public class PVPrefix {

    /**
     * Length after which to use a CRC.
     */
    private static final int LENGTH_AT_WHICH_TO_USES_CRC8 = 8;

    /**
     * Length of the string to use when added a CRC to the PV PREFIX.
     */
    private static final int LENGTH_OF_MACHINE_NAME_WITH_CRC = LENGTH_AT_WHICH_TO_USES_CRC8 - 2;

    /** instruments are prefixed with IN and do not contain NDX/NDE. */
	private static final String INSTRUMENT_FORMAT = "IN:%s:";
	
    /** test machine start TE and do contain NDW. */
    private static final String USER_FORMAT = "TE:%s:";
    
	public static final String NDX = "NDX";
    
    /** List of possible prefixes for instruments on the machine name. */
    public static final ImmutableList<String> INSTRUMENT_MACHINE_NAME_PREFIXES = ImmutableList.of("NDX", "NDE");
	
	private final String prefix;
	
    /**
     * Constructor.
     * 
     * @param prefix the pv prefix
     */
    public PVPrefix(String prefix) {
        this.prefix = prefix;

	}
    
    /**
     * Create a PVPrefix from a machine name. Machines starting with an
     * instrument prefix are in the TE domain. Blank or null machines return
     * TE::.
     *
     * @param machineName the machine name
     * @return the PV prefix
     */
    public static PVPrefix fromMachineName(String machineName) {
        String prefix = PVPrefix.calcPrefix(machineName).toUpperCase();
        return new PVPrefix(prefix);
    }

    /**
     * Create a PVPrefix from an instrument name, this is an IRIS instrument so
     * the prefix will start IN.
     * 
     * @param instrumentName the instrument name
     * @return pv prefix for the instrument name
     */
    public static PVPrefix fromInstrumentName(String instrumentName) {
        String instrument = getInstrumentName(instrumentName, "");
        String prefix = String.format(INSTRUMENT_FORMAT, instrument);
        return new PVPrefix(prefix);
    }

    /**
     * Calculate the prefix based on the machine name.
     * 
     * @param machineName machine name
     */
    private static String calcPrefix(String machineName) {
        for (String prefix : INSTRUMENT_MACHINE_NAME_PREFIXES) {
            if (machineName != null && machineName.startsWith(prefix)) {
                String instrument = getInstrumentName(machineName, prefix);
                return String.format(INSTRUMENT_FORMAT, instrument);
            }

        }
        String instrument = getInstrumentName(machineName, "");
        return String.format(USER_FORMAT, instrument);
    }

	
    /**
     * Generate the instrument name from the machine name using a CRC8 if
     * needed. Empty of null machine names are returned as blank.
     * 
     * @param machineName the machine name
     * @param prefixToRemove the prefix to remove from the machine name
     * @return
     */
    private static String getInstrumentName(String machineName, String prefixToRemove) {
        if (machineName == null || machineName.isEmpty()) {
            return "";
        }
        String cleanMachineName = machineName.substring(prefixToRemove.length());
        if (cleanMachineName.length() > LENGTH_AT_WHICH_TO_USES_CRC8) {
            String crc8 = CRC8.fromString(cleanMachineName).toString();
            return cleanMachineName.substring(0, LENGTH_OF_MACHINE_NAME_WITH_CRC) + crc8;
        }
        return cleanMachineName;
    }

    /**
     * PvPrefix to a string.
     * 
     * @return PV prefix
     */
    @Override
    public String toString() {
        return prefix;
    }

}
