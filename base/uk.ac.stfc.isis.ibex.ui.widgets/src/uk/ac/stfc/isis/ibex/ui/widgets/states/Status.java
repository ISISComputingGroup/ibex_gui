
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

package uk.ac.stfc.isis.ibex.ui.widgets.states;

import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

public enum Status {
	ON (0, 255, 0, "ON"),	// Green
	OFF (255, 0, 0, "OFF"),	// Red
	UNKNOWN (255, 127, 0, "UNKNOWN"); // Orange
	
	private final Color color;
	private final String text;
	
	private Status(int r, int g, int b, String text) {
		this.color = SWTResourceManager.getColor(r, g, b);
		this.text = text;
	}

	public Color getColor() {
		return color;
	}
	
	public String getText() {
		return text;
	}
}
