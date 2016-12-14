
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

package uk.ac.stfc.isis.ibex.ui.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;

import uk.ac.stfc.isis.ibex.ui.banner.views.BannerView;
import uk.ac.stfc.isis.ibex.ui.blocks.views.BlocksView;
import uk.ac.stfc.isis.ibex.ui.dashboard.views.DashboardView;
import uk.ac.stfc.isis.ibex.ui.perspectives.switcher.PerspectiveSwitcherView;

/**
 * The base perspective that all IBEX perspectives should inherit from.
 */
@SuppressWarnings("checkstyle:magicnumber")
public abstract class BasePerspective implements IPerspectiveFactory, IsisPerspective {
	
    /**
     * The ID for this perspective.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.perspectives.base";
	
	@Override
    public void createInitialLayout(IPageLayout layout) {					
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		
		layout.addStandaloneView(DashboardView.ID, false, IPageLayout.TOP, 0.4f, IPageLayout.ID_EDITOR_AREA);
		layout.addStandaloneView(BannerView.ID, false, IPageLayout.BOTTOM, 0.15f, DashboardView.ID);
		layout.addStandaloneView(BlocksView.ID, false, IPageLayout.RIGHT, 0.3f, DashboardView.ID);
		layout.addStandaloneView(PerspectiveSwitcherView.ID, false, IPageLayout.LEFT, 0.15f, IPageLayout.ID_EDITOR_AREA);
	}
	
    private String removeAmp(String in) {
        return in.replaceAll("&", "");
    }

	@Override
	public int compareTo(IsisPerspective other) {
        return removeAmp(name()).compareTo(removeAmp(other.name()));
	}
	
	protected void lockView(IPageLayout layout, String viewID) {
		IViewLayout view = layout.getViewLayout(viewID);
		if (view != null) {
			view.setMoveable(false);
			view.setCloseable(true);
		}
	}
	
	@Override
	public boolean isVisibleDefault() {
		return true;
	}
}
