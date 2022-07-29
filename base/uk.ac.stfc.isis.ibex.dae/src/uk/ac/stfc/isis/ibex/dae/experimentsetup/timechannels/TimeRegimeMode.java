
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

package uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels;

import java.util.HashMap;

/**
 * An enum representing the time regime modes.
 */
public enum TimeRegimeMode {
    
    /**
     * Blank mode.
     */
    BLANK("Blank"),
    
    /**
     * Time regime mode of "dT = C".
     */
    DT("dT = C"),
    
    /**
     * Time regime mode of "dT/T = C".
     */
    DTDIVT("dT/T = C"),
    
    /**
     * Time regime mode of "dT/T**2 = C".
     */
    DTDIVT2("dT/T**2 = C"),
    
    /**
     * Time regime mode of "Shifted".
     */
    SHIFTED("Shifted");
		
	private String mode;
	
	private static HashMap<String, TimeRegimeMode> lookup;
	static {
		lookup = new HashMap<>();
		for (TimeRegimeMode mode : TimeRegimeMode.values()) {
			lookup.put(mode.toString(), mode);
		}
	}
	
	/**
	 * Instantiates a new time regime mode.
	 *
	 * @param mode the mode
	 */
	TimeRegimeMode(String mode) {
		this.mode = mode;
	}
	
	@Override
	public String toString() {
		return mode;
	}
}
