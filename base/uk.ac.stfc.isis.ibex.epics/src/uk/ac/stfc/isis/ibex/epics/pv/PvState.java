
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2019 Science & Technology Facilities Council.
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
package uk.ac.stfc.isis.ibex.epics.pv;

/**
 * Used to reflect alarm & connection status of a block in the GUI.
 */
public enum PvState {
    /**
     * The default block state.
     */
    DEFAULT("No alarm"),
    /**
     * The block is in a minor alarm state.
     */
    MINOR_ALARM("Minor alarm"),
    /**
     * The block is in a major alarm state.
     */
    MAJOR_ALARM("Major alarm"),
    /**
     * The block is in an invalid alarm state (e.g. the pv is accessible but the device is disconnected).
     */
    INVALID("Invalid"),
    /**
     * The block is disconnected (i.e. the PV is not accessible).
     */
    DISCONNECTED("Disconnected");
	
	private final String userFriendlyName;
    
    PvState(String userFriendlyName) {
    	this.userFriendlyName = userFriendlyName;
    }
    
    /**
     * Gets a name for this state suitable for displaying on a user interface.
     * @return the name
     */
    public String getUserFriendlyName() {
    	return userFriendlyName;
    }
}
