
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

public class PVPrefix {

	private static final String INSTRUMENT_FORMAT = "IN:%s:";
	private static final String USER_FORMAT = "%s:%s:";
	public static final String NDX = "NDX";
    public static final String ND = "ND";
	
	private final String prefix;
	
	public PVPrefix(String machineName, String userName) {
		prefix = isInstrument(machineName) 
 ? instrumentPrefix(machineName) : userPrefix(machineName, userName);
	}
	
	public String get() {
		return prefix;
	}
	
	private boolean isInstrument(String machineName) {
		return machineName.startsWith(NDX);
	}

	private String userPrefix(String machineName, String userName) {
        return String.format(USER_FORMAT, machineName, userName).toUpperCase();
	}

	private String instrumentPrefix(String machineName) {
        return String.format(INSTRUMENT_FORMAT, instrumentName(machineName)).toUpperCase();
	}

	// Strip off the NDX prefix
	private String instrumentName(String machineName) {
		return machineName.substring(NDX.length()).toUpperCase();
	}
}
