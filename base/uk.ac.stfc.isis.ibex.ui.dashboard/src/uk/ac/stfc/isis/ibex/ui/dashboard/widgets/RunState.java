
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

package uk.ac.stfc.isis.ibex.ui.dashboard.widgets;

import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * An enum for describing the colour associated with each run state.
 */
public enum RunState {
    /** Processing. **/
    PROCESSING(RunStateColour.YELLOW),
    /** Running. **/
    RUNNING(RunStateColour.LIGHT_GREEN),
    /** Setup. **/
    SETUP(RunStateColour.LIGHT_BLUE),
    /** Paused. **/
    PAUSED(RunStateColour.RED),
    /** Waiting. **/
    WAITING(RunStateColour.GOLDEN_ROD),
    /** Vetoing. **/
    VETOING(RunStateColour.GOLDEN_ROD),
    /** Ending. **/
    ENDING(RunStateColour.BLUE),
    /** Pausing. **/
    PAUSING(RunStateColour.DARK_RED),
    /** Beginning. **/
    BEGINNING(RunStateColour.GREEN),
    /** Aborting. **/
    ABORTING(RunStateColour.BLUE),
    /** Resuming. **/
    RESUMING(RunStateColour.GREEN),
    /** Updating. **/
    UPDATING(RunStateColour.YELLOW),
    /** Storing. **/
    STORING(RunStateColour.YELLOW),
    /** Saving. **/
    SAVING(RunStateColour.YELLOW),
    /** Unknown. **/
    UNKNOWN(RunStateColour.YELLOW);
	
	private final String name;
	private final Color color;

    /**
     * Constructor.
     * 
     * @param name the name for the colour
     * @param color the colour
     */
	RunState(String name, Color color) {
		this.name = name;
		this.color = color;
	}
	
    /**
     * Constructor.
     * 
     * @param color the colour
     */
	RunState(Color color) {
		this.name = this.toString();
		this.color = color;
	}
	
    /**
     * Constructor that uses RGB values to define the colour.
     * 
     * @param r red component
     * @param g green component
     * @param b blue component
     */
	RunState(int r, int g, int b) {
		this(SWTResourceManager.getColor(r, g, b));
	}
	
    /**
     * Get the name.
     * 
     * @return the name
     */
	public String getName() {
		return name;
	}
	
    /**
     * Get the colour.
     * 
     * @return the colour
     */
	public Color color() {
		return color;
	}
}
