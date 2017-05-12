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

package uk.ac.stfc.isis.ibex.configserver.configuration;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Defines a Macro and its value for an IOC.
 * 
 * The available Macros list for each IOC comes from the BlockServer.
 * 
 */
public class Macro extends ModelObject {
	private String name;
    private String value = "";
	private String description;
	private String pattern;

	/**
	 * GSON requires the default constructor to create the macro properly from
	 * JSON.
	 * 
	 * If the default constructor is not present then GSON uses reflection to
	 * create the object. This mean the parent class's constructor is NOT called.
	 * GSON does not care if this is private.
	 */
	private Macro() {
	}

	public Macro(Macro other) {
		this(other.getName(), other.getValue(), other.getDescription(), other
				.getPattern());
	}

	public Macro(String name, String value, String description, String pattern) {
		this.name = name;
		this.value = value;
		this.description = description;
		this.pattern = pattern;
	}

	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	public String getName() {
		return name;
	}

	public void setValue(String value) {
		firePropertyChange("value", this.value, this.value = value);
	}

	public String getValue() {
		return value;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}

	@Override
	public String toString() {
		return name + "=" + value;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Macro)) {
			return false;
		}

		if (obj == this) {
			return true;
		}

		Macro other = (Macro) obj;
		return name.equals(other.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
