
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

public enum RunState {

	PROCESSING (RunStateColour.YELLOW),
	RUNNING (RunStateColour.LIGHT_GREEN),
	SETUP (RunStateColour.LIGHT_BLUE),
	PAUSED (RunStateColour.RED),
	WAITING (RunStateColour.GOLDEN_ROD),
	VETOING (RunStateColour.GOLDEN_ROD),	
	ENDING (RunStateColour.BLUE),
	
	PAUSING (RunStateColour.DARK_RED),
	BEGINNING (RunStateColour.GREEN),
	ABORTING (RunStateColour.BLUE),
	RESUMING (RunStateColour.GREEN),
	
	UPDATING (RunStateColour.YELLOW),
	STORING (RunStateColour.YELLOW),	
	SAVING (RunStateColour.YELLOW),
	
	UNKNOWN (RunStateColour.YELLOW);
	
	private final String name;
	private final Color color;

	private RunState(String name, Color color) {
		this.name = name;
		this.color = color;
	}
	
	private RunState(Color color) {
		this.name = this.toString();
		this.color = color;
	}
	
	private RunState(int r, int g, int b) {
		this(SWTResourceManager.getColor(r, g, b));
	}
	
	public String getName() {
		return name;
	}
	
	public Color color() {
		return color;
	}
}
