
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

package uk.ac.stfc.isis.ibex.ui.journalviewer;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.ui.perspectives.BasePerspective;

/**
 * The perspective that displays information about historical runs.
 */
public class Perspective extends BasePerspective {
	
	private final static String PERSPECTIVE_ID = "uk.ac.stfc.isis.ibex.ui.journalviewer.Perspective";
	private final static String PERSPECTIVE_NAME = "&Journal Viewer";
	
	@Override
	public String id() {
        return PERSPECTIVE_ID;
	}

	@Override
	public String name() {
        return PERSPECTIVE_NAME;
	}

	@Override
	public Image image() {
		return ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.journalviewer", "icons/journal_viewer_32x24.png");
	}
}
