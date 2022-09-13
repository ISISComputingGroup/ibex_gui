
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

package uk.ac.stfc.isis.ibex.configserver.internal;

import java.util.Collection;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.configserver.configuration.Group;

/**
 * Utility methods for displaying groups.
 */
public final class DisplayUtils {
	
	private static final String NONE = "none"; 
	private static final String OTHER = "Other";
	
    private DisplayUtils() {
    }

    /**
     * Gets the display name for a group.
     * @param name The name of the group
     * @return the display name of the group
     */
	public static String renameGroup(String name) {
		return name.toLowerCase().equals(NONE) ? OTHER : name;
	}
	
	/**
	 * Filters the "OTHER" group out from a collection of groups.
	 * @param <T> The collection type (e.g. Group or EditableGroup)
	 * @param groups The collection of groups to filter
	 * @return the filtered collection of groups
	 */
	public static <T extends Group> Collection<T> removeOtherGroup(Collection<T> groups) {
		return groups.stream()
				.filter(g -> !g.getName().equals(OTHER))
				.collect(Collectors.toList());
	}
}
