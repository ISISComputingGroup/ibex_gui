
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

package uk.ac.stfc.isis.ibex.ui.logplotter;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.ui.perspectives.BasePerspective;

public class Perspective extends BasePerspective {

    public static final String ID = "uk.ac.stfc.isis.ibex.ui.logplotter.perspective"; //$NON-NLS-1$
	
	@Override
	public String id() {
		return ID;
	}

	@Override
	public String name() {
        return "Log Plotter";
	}

	@Override
	public Image image() {
        return ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.logplotter", "icons/stocks2_32x24.png");
		// Icon made by OCHA http://www.flaticon.com/authors/ocha
		// from Flaticon http://www.flaticon.com
		// is licensed by Creative Commons BY 3.0 http://creativecommons.org/licenses/by/3.0/
	}
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		super.createInitialLayout(layout);
		
		lockView(layout, "org.csstudio.trends.databrowser.waveformview.WaveformView");
		
		layout.addStandaloneView(EmptyLogPlotterView.ID, false, IPageLayout.RIGHT, 0.1f, "uk.ac.stfc.isis.ibex.ui.perspectives.PerspectiveSwitcher");
		
		
		//This part listener will be called to open the empty view when all graphs are closed
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(new IPartListener() {
			
			@Override
			public void partOpened(IWorkbenchPart part) {
				checkEditorEmpty(part);
			}
			
			@Override
			public void partDeactivated(IWorkbenchPart part) {
			}
			
			@Override
			public void partClosed(IWorkbenchPart part) {
				checkEditorEmpty(part);
			}
			
			@Override
			public void partBroughtToTop(IWorkbenchPart part) {
			}
			
			@Override
			public void partActivated(IWorkbenchPart part) {
			}
			
			private void checkEditorEmpty(IWorkbenchPart part) {
				if (part.getClass().equals(EmptyLogPlotterView.class)) {
					return;
				}
				
				IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				if (activePage == null) {
					return;
				}
				
				if (!activePage.getPerspective().getId().equals(ID)) {
					return;
				}
				
				if (activePage.getEditorReferences().length > 0) {
					IViewPart emptyView = activePage.findView(EmptyLogPlotterView.ID);
					activePage.setEditorAreaVisible(true);		
					activePage.hideView(emptyView);
				} else {
					try {
						activePage.showView(EmptyLogPlotterView.ID);
						activePage.setEditorAreaVisible(false);
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
