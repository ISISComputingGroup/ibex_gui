
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An enum representing the different time channel calculation methods.
 */
public enum CalculationMethod {
    /**
     * Specify parameters in the GUI.
     */
    SPECIFY_PARAMETERS("Specify Parameters"),
    /**
     * Get parameters from a TCB file.
     */
    USE_TCB_FILE("Use TCB File");
	
	private String text;

	private static final List<String> ALLTOSTRING;
	static {
		ALLTOSTRING = new ArrayList<>();
		for (CalculationMethod method : CalculationMethod.values()) {
			ALLTOSTRING.add(method.toString());
		}
	}
	
	/**
	 * Standard enum constructor.
	 * 
	 * @param text the text of the enum value.
	 */
	CalculationMethod(String text) {
		this.text = text;
	}
	 
	/**
	 * @return a list of all enum values as strings.
	 */
	public static List<String> allToString() {
		return Collections.unmodifiableList(ALLTOSTRING);
	}
	
	@Override
	public String toString() {
		return text;
	}
}
