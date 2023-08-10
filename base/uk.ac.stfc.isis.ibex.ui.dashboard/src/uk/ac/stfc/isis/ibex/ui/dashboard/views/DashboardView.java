
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

package uk.ac.stfc.isis.ibex.ui.dashboard.views;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.dashboard.Dashboard;
import uk.ac.stfc.isis.ibex.ui.dashboard.models.BannerModel;
import uk.ac.stfc.isis.ibex.ui.dashboard.models.TitlePanelModel;
import uk.ac.stfc.isis.ibex.ui.dashboard.widgets.Banner;
import uk.ac.stfc.isis.ibex.ui.dashboard.widgets.DashboardTable;
import uk.ac.stfc.isis.ibex.ui.dashboard.widgets.TitlePanel;

/**
 * The dashboard view.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class DashboardView {
	/**
	 * The ID of this view.
	 */
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.dashboard.views.DashboardView"; //$NON-NLS-1$
			
    private static final int FIXED_WIDTH = 580;
    private static final int FIXED_HEIGHT = 225;
	
	private final Font bannerTitleFont = SWTResourceManager.getFont("Arial", 24, SWT.BOLD);
	private final Font bannerFont = SWTResourceManager.getFont("Arial", 14, SWT.NORMAL);
	private final Font textFont = SWTResourceManager.getFont("Arial", 11, SWT.NORMAL);
	private final Font simulationModeFont = SWTResourceManager.getFont("Arial", 20, SWT.BOLD);
	
	private final Dashboard dashboard = Dashboard.getInstance();
	
	private final BannerModel bannerModel = new BannerModel(dashboard.observables());
	
	private final TitlePanelModel titleModel = 
			new TitlePanelModel(dashboard.observables().title, dashboard.observables().users, dashboard.observables().displayTitle, dashboard.observables().displayTitleSetter);

	/**
	 * Create the dashboard view.
	 * 
	 * @param parent the parent composite
	 */
	@PostConstruct
    public void createPartControl(Composite parent) {
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		Composite dashboardControl = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(dashboardControl);
        scrolledComposite.setMinSize(new Point(FIXED_WIDTH, FIXED_HEIGHT));
		
        GridLayout dashboardLayout = new GridLayout(1, false);
		dashboardLayout.marginHeight = 0;
		dashboardLayout.marginWidth = 0;
		dashboardLayout.horizontalSpacing = 1;
		dashboardLayout.verticalSpacing = 0;
		dashboardControl.setLayout(dashboardLayout);
		
		new Banner(dashboardControl, SWT.NONE, bannerModel, bannerTitleFont, bannerFont, simulationModeFont);
		makeSeparator(dashboardControl);
		new TitlePanel(dashboardControl, SWT.NONE, titleModel, textFont);
		makeSeparator(dashboardControl);
        new DashboardTable(dashboardControl, bannerModel, textFont);
	}
	
	private void makeSeparator(Composite container) {
		Label separator = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
        separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	}
	
	/**
	 * Called before eclipse disposes of this view.
	 */
	@PreDestroy
	public void dispose() {
		bannerModel.close();
		titleModel.close();
	}
}
