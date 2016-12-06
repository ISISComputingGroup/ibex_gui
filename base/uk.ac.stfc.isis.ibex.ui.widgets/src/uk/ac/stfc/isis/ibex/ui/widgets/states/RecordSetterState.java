
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

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.ui.widgets.states;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * Enumeration the holds the different possible states of the RecordSetterModel. For
 * each state, a highlighting colour, icon, and text description is provided.
 */
public enum RecordSetterState {
	/** The record is not connected to its backing data-store; no updates possible. */
    DISCONNECTED(255, 0, 0, // DARK RED
			ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.widgets", "icons/warning_red.png"),
			"The record is DISCONNECTED"),
	
	/** The record is connected but the current value is not a valid record value.. */
    INVALID(240, 150, 150, // LIGHT RED
			ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.widgets", "icons/thumb_down.png"),
			"The value entered is INVALID for this record type"),
	
	/** The record is connected and the current value is a valid record value. */
    VALID(150, 240, 150, // LIGHT GREEN
			ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.widgets", "icons/thumb_up.png"),
			"The value entered is VALID for this record type"),
	
	/** The record is connected and the current value is the last one sent to the record. */
    CURRENT(150, 240, 150, // LIGHT GREEN
			ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.widgets", "icons/check.png"),
			"The value entered was SENT to the record");
	
	private final Color color;
	private final Image image;
	private final String message;
	
	RecordSetterState(int r, int g, int b, Image image, String message) {
		this.color = SWTResourceManager.getColor(r, g, b);
		this.image = image;
		this.message = message;
	}

	public Color getColor() {
		return color;
	}
	
	public Image getImage() {
		return image;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean isConnected() {
		return this != DISCONNECTED;
	}
}