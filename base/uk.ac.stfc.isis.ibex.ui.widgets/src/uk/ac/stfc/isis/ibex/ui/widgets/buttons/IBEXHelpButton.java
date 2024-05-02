/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2024
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */


package uk.ac.stfc.isis.ibex.ui.widgets.buttons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;

/**
 * A help icon button that opens browser links.
 *
 */
public class IBEXHelpButton extends IBEXButton {

	/**
	 * 
	 * @param parent
	 * @param link
	 * @param tooltip
	 */
	public IBEXHelpButton(Composite parent, String link, String tooltip) {
		super(parent, SWT.PUSH);
		
		String description = String.format("Open user manual link in browser for help with '%s': \n%s", tooltip, link);
		Image image = ResourceManager.getPluginImage(SYMBOLIC_PATH, "/icons/helpIcon.png");
		
		this.layoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1))
		.link(link)
		.tooltip(description)
		.image(image);
	}
}
