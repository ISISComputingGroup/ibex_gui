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

import java.util.List;
import java.util.Objects;

import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ActionParameter;

/**
 * Data type for an action parameter.
 * 
 */
public class JavaActionParameter implements ActionParameter {

	private final String name;
	
	private final String defaultValue;
	
	private final boolean copyPreviousRow;
	
	private final boolean isEnum;
	
	private final List<String> enumValues;
	
	/**
	 * This class holds the name about an action parameter.
	 * @param name The name of the action parameter (column header)
	 * @param defaultValue The value this parameter should have as a default
	 * @param copyPreviousRow Whether or not the parameter should copy the previous row
	 */
	public JavaActionParameter(String name, String defaultValue, boolean copyPreviousRow, boolean isEnum, List<String> enumValues) {
		this.name = name;
		this.defaultValue = defaultValue;
		this.copyPreviousRow = copyPreviousRow;
		this.isEnum = isEnum;
		this.enumValues = enumValues;
	}

	/**
	 * @return The name of this parameter.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return The default value for this parameter.
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	
	/**
	 * @return Whether or not the parameter is set to copy the previous action's value. 
	 */
	public boolean getCopyPreviousRow() { 
		return copyPreviousRow;
	}
	
	public boolean getIsEnum() {
		return isEnum;
	}
	
	public List<String> getEnumValues() {
		return enumValues;
	}

	/**
	 * Check to see if the ActionParameter's attributes are equal to provided object.
	 * @param other The object to compare against
	 */
	@Override
	public boolean equals(Object other) {
		if (other == this) {
		    return true;
		}
		if (!(other instanceof JavaActionParameter)) {
		    return false;	
		}
	
		JavaActionParameter actionParameter = (JavaActionParameter) other;
		return Objects.equals(this.getName(), actionParameter.getName()) 
			&& Objects.equals(this.getDefaultValue(), actionParameter.getDefaultValue())
			&& Objects.deepEquals(this.getCopyPreviousRow(), actionParameter.getCopyPreviousRow())
			&& Objects.deepEquals(this.getIsEnum(), actionParameter.getIsEnum())
			&& Objects.deepEquals(this.getEnumValues(), actionParameter.getEnumValues());
	}
	
	/**
	 * Calculate the hash of the ActionParameter.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.getName() + this.getDefaultValue());
	}

}
