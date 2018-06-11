
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


import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.opis.OPIViewCreationException;
//import uk.ac.stfc.isis.ibex.ui.logplotter.EmptyLogPlotterView;
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

	@Override
	public void createInitialLayout(IPageLayout layout) {
		super.createInitialLayout(layout);
        final float viewRatio = 0.1f;
        layout.addStandaloneView(WebLinksOpiTargetView.ID, false, IPageLayout.RIGHT, viewRatio,
        		IPageLayout.ID_EDITOR_AREA);
        
        //openOpi();
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
//				try {
//					Thread.sleep(10000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
		        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		        WebLinksOpiTargetView view = (WebLinksOpiTargetView) activePage.findView(WebLinksOpiTargetView.ID);
		        try {
					view.initialiseOPI();
				} catch (OPIViewCreationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
	
	private void openOpi() {
		try {
			new WebLinksOpiTargetView().initialiseOPI();
		} catch (OPIViewCreationException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String id() {
		return ID;
	}

	@Override
	public String name() {
        return "&Web Links";
	}
	
	@Override
	public Image image() {
		return ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.weblinks", "icons/external1_32x24.png");
		// Icon made by Dave Gandy http://www.flaticon.com/authors/dave-gandy
		// from Flaticon http://www.flaticon.com
		// is licensed by Creative Commons BY 3.0 http://creativecommons.org/licenses/by/3.0/
	}
}