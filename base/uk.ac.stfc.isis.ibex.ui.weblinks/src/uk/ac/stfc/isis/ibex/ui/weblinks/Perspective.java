
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

package uk.ac.stfc.isis.ibex.ui.weblinks;


import javax.annotation.PostConstruct;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IPageLayout;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.ui.perspectives.BasePerspective;

/**
 * The perspective that shows weblinks for IBEX.
 * 
 * Registers the perspective to be displayed in the list (see plugin.xml file
 * for this package).
 */
public class Perspective extends BasePerspective {

    /**
     * The perspective ID.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.weblinks.perspective";

    /**
     * {@inheritDoc}
     */
	@PostConstruct
	public void createInitialLayout(IPageLayout layout) {
		super.createInitialLayout(layout);
        layout.addStandaloneView(WebLinksOpiTargetView.ID, false, IPageLayout.RIGHT, IPageLayout.DEFAULT_VIEW_RATIO,
        		IPageLayout.ID_EDITOR_AREA);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String id() {
		return ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String name() {
        return "&Web Links";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image image() {
		return ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.weblinks", "icons/external1_32x24.png");
		// Icon made by Dave Gandy http://www.flaticon.com/authors/dave-gandy
		// from Flaticon http://www.flaticon.com
		// is licensed by Creative Commons BY 3.0 http://creativecommons.org/licenses/by/3.0/
	}
}