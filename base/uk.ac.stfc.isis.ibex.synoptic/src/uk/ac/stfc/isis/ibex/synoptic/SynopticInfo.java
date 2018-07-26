
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

package uk.ac.stfc.isis.ibex.synoptic;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.synoptic.internal.Variables;

/**
 * A class that contains basic information on a synoptic.
 */
@SuppressWarnings({ "checkstyle:membername" })
public class SynopticInfo {

	private final String name;
	private final String pv;
    private final boolean is_default;
	
    /**
     * Default constructor, creates an empty synoptic information class.
     */
    public SynopticInfo() {
        this.name = Variables.NONE_SYNOPTIC_NAME;
        this.pv = Variables.NONE_SYNOPTIC_PV;
        this.is_default = false;
    }

    /**
     * Constructor to create a synoptic info based on basic parameters.
     * @param name The user friendly name of the synoptic.
     * @param pv The pv that the synoptic can be read/written to.
     * @param isDefault Whether the synoptic is the default one.
     */
    public SynopticInfo(String name, String pv, boolean isDefault) {
		this.name = name;
		this.pv = pv;
        this.is_default = isDefault;
	}
	
    /**
     * Get the user friendly name for the synoptic.
     * @return The name of the synoptic
     */
	public String name() {
		return name;
	}
	
	/**
	 * Get the PV for accessing the synoptic.
	 * @return The PV to access the synoptic.
	 */
	public String pv() {
		return pv;
	}
	
	/**
	 * Gets whether the synoptic is the default.
	 * @return True if the synoptic is the default.
	 */
    public boolean isDefault() {
        return is_default;
    }

    /**
     * Returns a list of names of synoptics.
     * @param infos The synoptics to get the names from.
     * @return A list of synoptic names.
     */
	public static Collection<String> names(Collection<SynopticInfo> infos) {
		if (infos == null) {
			return Collections.emptyList();
		}
		
		return infos.stream().map(s -> s.name()).collect(Collectors.toList());		
	}
	
	/**
	 * Find a synoptic based on its name.
	 * @param available A list of the synoptics to search.
	 * @param name The name to look for.
	 * @return The synoptic that matches the name, null if none found.
	 */
	public static SynopticInfo search(Collection<SynopticInfo> available, String name) {
		return available.stream()
				.filter(s -> s.name().equals(name)).findFirst().orElse(null);
	}
}
