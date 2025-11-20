/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2025 Science & Technology Facilities Council.
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
package uk.ac.stfc.isis.ibex.ui.configserver.editing.macros;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * View model for the global macro, just a container for the global macro values.
 */
public class GlobalMacroViewModel extends ModelObject {

	private final String iocName;
	private final String macroName;
	private final String macroValue;

	private static final String IOC_SEPARATOR = "__";
	private static final String ALL_IOCS = "All IOCs";
	
	/**
	 * Constructor. Sets the use default based on provided macro.
	 * 
	 * @param iocName The IOC Name.
	 * @param macroName The Macro Name.
	 * @param macroValue The Macro Value.
	 */
	public GlobalMacroViewModel(String iocName, String macroName, String macroValue) {
		this.iocName = !IOC_SEPARATOR.equals(iocName) ? iocName : ALL_IOCS;
		this.macroName = macroName;
		this.macroValue = macroValue;
	}

	/**
	 * @return the iocName
	 */
	public String getIocName() {
		return iocName;
	}

	/**
	 * @return the macroName
	 */
	public String getMacroName() {
		return macroName;
	}

	/**
	 * @return the macroValue
	 */
	public String getMacroValue() {
		return macroValue;
	}
}
