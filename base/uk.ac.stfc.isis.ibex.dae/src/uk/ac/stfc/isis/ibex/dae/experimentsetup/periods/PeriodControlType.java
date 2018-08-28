
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
 * An enum representing the period control type.
 */
public enum PeriodControlType {
    /**
     * The period is controlled via a PC.
     */
    SOFTWARE("Software: PC controlled"),
    /**
     * The period is controlled by a DAE internal control.
     */
    HARDWARE_DAE("Hardware: DAE internal control"),
    /**
     * The period is controlled by an external hardware signal.
     */
    HARDWARE_EXTERNAL("Hardware: External signal control");

	private String text;

	private static final List<String> ALLTOSTRING;
	static {
		ALLTOSTRING = new ArrayList<>();
		for (PeriodControlType method : PeriodControlType.values()) {
			ALLTOSTRING.add(method.toString());
		}
	}
	
	/**
	 * The constructor for the period control type.
	 * 
	 * @param text
	 *             The type.
	 */
	PeriodControlType(String text) {
		this.text = text;
	}
	
	/**
	 * Returns a string representation of each possible period type.
	 * @return
	 *         A string representation of each possible period type.
	 */
	public static List<String> allToString() {
		return Collections.unmodifiableList(ALLTOSTRING);
	}
	
	@Override
	public String toString() {
		return text;
	}
}
