
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

public enum RecordLabelState {
    DISCONNECTED(255, 0, 0, ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.widgets", "icons/warning_red.png")), // Red
    CONNECTED(0, 0, 0, null); // Black
	
	private final Color color;
	private final Image image;
	
	RecordLabelState(int r, int g, int b, Image image) {
		this.color = SWTResourceManager.getColor(r, g, b);
		this.image = image;
	}

	public Color getColor() {
		return color;
	}
	
	public Image getImage() {
		return image;
	}
	
	public boolean isConnected() {
		return this == CONNECTED;
	}
}
