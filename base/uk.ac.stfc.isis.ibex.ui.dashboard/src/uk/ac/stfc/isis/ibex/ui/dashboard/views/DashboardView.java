
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISizeProvider;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.dashboard.Dashboard;
import uk.ac.stfc.isis.ibex.ui.dashboard.models.BannerModel;
import uk.ac.stfc.isis.ibex.ui.dashboard.models.MonitorPanelModel;
import uk.ac.stfc.isis.ibex.ui.dashboard.models.TimePanelModel;
import uk.ac.stfc.isis.ibex.ui.dashboard.models.TitlePanelModel;
import uk.ac.stfc.isis.ibex.ui.dashboard.widgets.Banner;
import uk.ac.stfc.isis.ibex.ui.dashboard.widgets.MonitorPanel;
import uk.ac.stfc.isis.ibex.ui.dashboard.widgets.TimePanel;
import uk.ac.stfc.isis.ibex.ui.dashboard.widgets.TitlePanel;

/**
 * The dashboard view.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class DashboardView extends ViewPart implements ISizeProvider {
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.dashboard.views.DashboardView"; //$NON-NLS-1$
			
    private static final int FIXED_WIDTH = 600;
    private static final int FIXED_HEIGHT = 250;
	
	private final Font bannerTitleFont = SWTResourceManager.getFont("Arial", 24, SWT.BOLD);
	private final Font bannerFont = SWTResourceManager.getFont("Arial", 14, SWT.NORMAL);
	private final Font textFont = SWTResourceManager.getFont("Arial", 12, SWT.NORMAL);
	private final Font simulationModeFont = SWTResourceManager.getFont("Arial", 20, SWT.BOLD);
	
	private final Dashboard dashboard = Dashboard.getInstance();
	
	private final BannerModel bannerModel = new BannerModel(dashboard.observables());
	
	private final TitlePanelModel titleModel = 
			new TitlePanelModel(dashboard.observables().dae.title, dashboard.observables().users);
	
	private final MonitorPanelModel monitorsModel = new MonitorPanelModel(dashboard.observables());
	private final TimePanelModel timesModel = new TimePanelModel(dashboard.observables());

	@Override
    public void createPartControl(Composite parent) {
        GridLayout glParent = new GridLayout(3, false);
		glParent.marginHeight = 0;
		glParent.marginWidth = 0;
		glParent.horizontalSpacing = 1;
		glParent.verticalSpacing = 0;
		parent.setLayout(glParent);
		Banner banner = new Banner(parent, SWT.NONE, bannerModel, bannerTitleFont, bannerFont, simulationModeFont);
        banner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		
		Label separator1 = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        separator1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		TitlePanel title = new TitlePanel(parent, SWT.NONE, titleModel, textFont);
        title.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label separator2 = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        separator2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		MonitorPanel monitors = new MonitorPanel(parent, SWT.NONE, monitorsModel, textFont);
        GridData monitorsLayoutGridData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        monitorsLayoutGridData.widthHint = 325;
        monitors.setLayoutData(monitorsLayoutGridData);
		
		Label separator3 = new Label(parent, SWT.SEPARATOR | SWT.VERTICAL);
		separator3.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		
		TimePanel times = new TimePanel(parent, SWT.NONE, textFont, timesModel);
        GridData timesGridData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        timesGridData.widthHint = FIXED_WIDTH - monitorsLayoutGridData.widthHint;
        times.setLayoutData(timesGridData);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		bannerModel.close();
		titleModel.close();
		monitorsModel.close();
		timesModel.close();
	}

	@Override
	public int getSizeFlags(boolean width) {
		return SWT.MIN | SWT.MAX;
	}

	@Override
	public int computePreferredSize(boolean width, int availableParallel,
			int availablePerpendicular, int preferredResult) {
		return  width ? FIXED_WIDTH : FIXED_HEIGHT;
	}

	@Override
	public void setFocus() {		
	}
}
