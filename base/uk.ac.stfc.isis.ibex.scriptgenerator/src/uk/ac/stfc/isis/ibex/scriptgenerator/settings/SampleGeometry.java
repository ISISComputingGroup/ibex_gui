
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
 * Options for what a sample can be contained in, as used in the Sample Geometry setting.
 */
public enum SampleGeometry {
    
    /**
     * The sample has disc geometry.
     */
	DISC("Disc"),
	
	/**
     * The sample has cylindrical geometry.
     */
	CYLINDRICAL("Cylindrical"),
	
	/**
     * The sample has flat plate geometry.
     */
	FLATPLATE("Flat Plate"),
	
	/**
     * The sample has single crystal geometry.
     */
	SINGLECRYSTAL("Single Crystal");
	
	private String name;
	
	/**
	 * Instantiates a new sample geometry.
	 *
	 * @param displayName the display name
	 */
	SampleGeometry(String displayName) {
		this.name = displayName;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
