
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

// A component that may be included in a config
package uk.ac.stfc.isis.ibex.configserver.configuration;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * General information for a component inside a configuration.
 * 
 * Note: The values from this class are populated from the BlockServer JSON via
 * reflection. Therefore variable names must reflect those expected from the
 * JSON.
 */
public class ComponentInfo extends ModelObject implements Comparable<ComponentInfo> {
	
	private final String name;
	private String description = "";
	private String pv = "";
	
    /**
     * Constructor for testing.
     * 
     * @param name
     *            The component name.
     */
	public ComponentInfo(String name) {
		this.name = name;
	}
	
    /**
     * Constructor to initialise using another ComponentInfo-object.
     * 
     * @param other
     *            The component to copy
     */
	public ComponentInfo(ComponentInfo other) {
		this.name = other.name;
		this.description = other.description;
		this.pv = other.pv();
	}

    /**
     * Constructor to initialise using a full configuration.
     * 
     * @param other
     *            The configuration to copy.
     */
    public ComponentInfo(Configuration other) {
        this.name = other.name();
        this.description = other.description();
        this.pv = other.pv();
    }

    /**
     * @return The name of the component.
     */
	public String getName() {
		return name;
	}

    /**
     * @return The description of the component.
     */
	public String description() {
		return description;
	}

    /**
     * @return The source PV of the component.
     */
	public String pv() {
		return pv;
	}
	
	@Override
	public int compareTo(ComponentInfo other) {
		return name.compareTo(other.name);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ComponentInfo)) {
			return false;
		}
		
		if (obj == this) {
			return true;
		}
		
		ComponentInfo other = (ComponentInfo) obj;
		return name.equals(other.name);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
