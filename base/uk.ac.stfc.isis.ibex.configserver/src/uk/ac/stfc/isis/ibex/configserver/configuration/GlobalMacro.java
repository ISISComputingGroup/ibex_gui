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

package uk.ac.stfc.isis.ibex.configserver.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Represents a global macro.	
 * 
 * Contains the IOC name, or none, and a list of associated macros
 * 
 */
public class GlobalMacro extends ModelObject  implements Comparable<GlobalMacro> {

	private final String name;	
	
	private Map<String, String> macros;
	
    /**
     * Create a Global Macro with a given name.
     * 
     * @param name The IOC name (or global)
     */
	public GlobalMacro(String name) {
		this.name = name;
	}

    /**
     * Create a copy of a Global Macro list for an IOC (or global).
     * 
     * @param globalMacro The Global Macro to copy
     */
	public GlobalMacro(GlobalMacro globalMacro) {
		this.name = globalMacro.getName();
		//this.macros = new ArrayList<>(globalMacro.getMacros());
		this.macros = new HashMap<String, String>(globalMacro.getMacros());
	}

    /**
     * @return The IOC name
     */
    public String getName() {
		return name;
	}
		
    /**
     * @return A collection of  macros
     */
	public Map<String, String> getMacros() {
		return Optional.ofNullable(macros).orElseGet(HashMap::new);
	}

	/**
	 * Compares this GlobalMacro to another based on name. Used for sorting.
	 */
	@Override
	public int compareTo(GlobalMacro other) {
		if (this.name != null && other.name != null) {
			return this.name.compareTo(other.name);
		}
		return 0;
	}

	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GlobalMacro other = (GlobalMacro) obj;
        if (this.name == null && other.name == null) {
            return true;
        }
        if (this.name == null || other.name == null) {
            return false;
        }
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }
    
    @Override
	public String toString() {
		return "GlobalMacro [name=" + name + ", macros=" + macros + "]";
	}
}
