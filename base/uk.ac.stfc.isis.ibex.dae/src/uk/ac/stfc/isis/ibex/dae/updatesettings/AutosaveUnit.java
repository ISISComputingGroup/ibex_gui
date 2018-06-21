
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

package uk.ac.stfc.isis.ibex.dae.updatesettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.stfc.isis.ibex.ui.Utils;

/**
 * Enum containing the possible auto save units for the DAE.
 */
public enum AutosaveUnit {
    /**
     * Save on every so many frames.
     */
    FRAMES("Frames"),
    /**
     * Save on every so many events.
     */
    EVENTS("Events"),
    /**
     * Save on every so many dashboard polls.
     */
    DASHBOARD_POLLS("Dashboard polls"),
    /**
     * Save on every so many micro-amps.
     */
    MICROAMPS(Utils.MU + "A");
	
	private String text;

	private static final List<String> ALLTOSTRING;
	static {
		ALLTOSTRING = new ArrayList<>();
		for (AutosaveUnit method : AutosaveUnit.values()) {
			ALLTOSTRING.add(method.toString());
		}
	}
	
	/**
	 * Assigns the enum value to the text variable.
	 *  
	 * @param text enum value
	 */
	AutosaveUnit(String text) {
		this.text = text;
	}
	
	/**
	 * Create a list containing all the auto save units.
	 * 
	 * @return a list of each auto save unit.
	 */
	public static List<String> allToString() {
		return Collections.unmodifiableList(ALLTOSTRING);
	}
	
	@Override
	public String toString() {
		return text;
	}
}
