
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

package uk.ac.stfc.isis.ibex.ui.blocks.presentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * The interface for a class that displays the history of a PV in some way.
 */
public interface PVHistoryPresenter {
	
	/**
	 * Gets the names of all of the plots, mapped to lists of axis names.
	 * @return A hash map of plot names and lists of axis names
	 */
	HashMap<String, ArrayList<String>> getBrowserTitles();
	
	/**
	 * Creates a new display and plots the PV history on it.
	 * @param pvAddress The PV to plot the history of.
	 * @param display The user-friendly name for the plot and the axis
	 */
	void newDisplay(String pvAddress, String display);

    /**
     * Adds a PV to a pre-existing display.
     * @param pvAddress The PV to plot the history of.
     * @param display The user-friendly name for the plot and the axis to add.
     * @param presenterName The name of the display to add the PV to.
     * @param axisName The name of the axis to add the PV to, if empty creates a new axis
     */
    void addToDisplay(String pvAddress, String display, String presenterName, Optional<String> axisName);

}
