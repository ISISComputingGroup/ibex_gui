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

package uk.ac.stfc.isis.ibex.scriptgenerator;

import java.util.Objects;

/**
 * Data type for an action parameter.
 * 
 */
public class ActionParameter {

	private String name;
	
	/**
	 * This class holds the name about an action parameter.
	 * @param name The name of the action parameter (column header)
	 */
	public ActionParameter(String name) {
		this.name = name;
	}

	/**
	 * @return The name of this parameter.
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * Check to see if the ActionParameter's attributes are equal to provided object
	 * @param The object to compare against
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof ActionParameter)) return false;	
		
		ActionParameter actionParameter = (ActionParameter) o;
		return Objects.equals(this.getName(), actionParameter.getName());
		
	}
	
	/**
	 * Calculate the hash of the ActionParameter.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.getName());
	}

}
