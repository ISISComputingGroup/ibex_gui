
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

package uk.ac.stfc.isis.ibex.dae.experimentsetup.periods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DAE period types.
 */
public enum PeriodType {
	/**
	 * Unused period.
	 */
    UNUSED("Unused"),
    /**
     * DAQ period.
     */
    DAQ("DAQ"),
    /**
     * Dwell period.
     */
    DWELL("Dwell");

	private String text;

	private static final List<String> ALLTOSTRING;
	static {
		ALLTOSTRING = new ArrayList<>();
		for (PeriodType method : PeriodType.values()) {
			ALLTOSTRING.add(method.toString());
		}
	}
	
	/**
	 * Instantiates a new period type.
	 *
	 * @param text the text
	 */
	PeriodType(String text) {
		this.text = text;
	}
	 
	/**
	 * Gets all values as a string.
	 *
	 * @return the list
	 */
	public static List<String> allToString() {
		return Collections.unmodifiableList(ALLTOSTRING);
	}
	
	@Override
	public String toString() {
		return text;
	}
}
