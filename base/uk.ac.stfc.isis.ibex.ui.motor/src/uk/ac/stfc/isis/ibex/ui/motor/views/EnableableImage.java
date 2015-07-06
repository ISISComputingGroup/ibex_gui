
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

package uk.ac.stfc.isis.ibex.ui.motor.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class EnableableImage {

	private final Image enabled;
	private final Image disabled;
	
	public EnableableImage(Display display, Image enabled) {
		this.enabled = enabled;
		disabled = new Image(display, enabled, SWT.IMAGE_DISABLE);
	}
	
	public Image enabled() {
		return enabled;
	}
	
	public Image disabled() {
		return disabled;
	}
	
	public Image isEnabled(boolean enable) {
		return enable ? enabled : disabled;
	}
	
	public void dispose() {
		disabled.dispose();
	}
}
