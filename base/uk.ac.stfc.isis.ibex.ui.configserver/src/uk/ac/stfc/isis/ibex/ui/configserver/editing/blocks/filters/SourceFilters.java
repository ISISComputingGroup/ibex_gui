
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters;

/**
 * The filter values allowed based on the source of PVs.
 */
public enum SourceFilters {
	ALL ("All IOCs"),
	ACTIVE ("Active IOCs"),
	ASSOCIATED ("Config IOCs");
	
	private String displayName;
    private static SourceFilters lastSelection;
	
    /**
     * Associate the strings to display.
     * 
     * @param displayName - the string to display
     */
	SourceFilters(String displayName) {
		this.displayName = displayName;
	}
	
	@Override
	public String toString() {
		return displayName;
	}

    /**
     * Return either the default or the last value that was selected.
     * 
     * @return - the filter selection to use
     */
    public static SourceFilters lastValue() {
        if (lastSelection == null) {
            // Provide a default value
            return SourceFilters.ACTIVE;
        }
        return lastSelection;
    }

    /**
     * Allow for the filter selection value to be updated.
     * 
     * @param pvFilter - the filter that was selected most recently
     */
    public static void setSelectedValue(SourceFilters pvFilter) {
        lastSelection = pvFilter;
    }
}