
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

package uk.ac.stfc.isis.ibex.scriptgenerator.settings;

/**
 * Sizes that can be used in the SANS combo box within the Aperture settings.
 */
public enum ApertureSans {

    /**
     * Large aperture.
     */
	LARGE("Large = ?, ? mm"),
	
	/**
	 * Medium aperture.
	 */
	MEDIUM("Medium = 20, 14 mm"),
	
	/**
	 * Small aperture.
	 */
	SMALL("Small = ?, ? mm");
	
	private String name;
	
	/**
	 * Instantiates a new aperture sans.
	 *
	 * @param displayName the display name
	 */
	ApertureSans(String displayName) {
		this.name = displayName;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
