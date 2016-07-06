
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

package uk.ac.stfc.isis.ibex.targets;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Typically identifies a target for a view to display. A target is given a name
 * and a series of properties that can be used to customise the behaviour of the
 * view
 */
public abstract class Target {

    /**
     * The name used to identify the target.
     */
	private final String name;

    /**
     * A list of properties used to customise the action of the target.
     */
	private final Map<String, String> properties = new LinkedHashMap<>();
	
    /**
     * 
     * @param name - The name of the target
     */
	public Target(String name) {
		this.name = name;
	}
	
    /**
     * @return The target name
     */
	public String name() {
		return name;
	}
	
    /**
     * 
     * @return The name of the target
     */
	@Override
	public String toString() {
		return name;
	}

    /**
     * 
     * @param key - Property key
     * @param value - Property value
     */
	public void addProperty(String key, String value) {
		properties.put(key, value);
	}

    /**
     * 
     * @return The target properties
     */
	public Map<String, String> properties() {
		return new LinkedHashMap<>(properties);
	}
}
