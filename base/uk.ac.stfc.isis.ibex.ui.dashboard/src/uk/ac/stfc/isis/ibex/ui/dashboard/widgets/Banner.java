
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

package uk.ac.stfc.isis.ibex.ui.dashboard.widgets;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.dashboard.DashboardPv;
import uk.ac.stfc.isis.ibex.ui.dashboard.models.BannerModel;

/**
 * The view for the instrument dashboard banner showing the instrument state.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class Banner extends Composite {

	private final Label bannerText;
	private final Composite details;
	private final DataBindingContext bindingContext = new DataBindingContext();
	
	private static final List<DashboardPv> DASHBOARD_PVS = List.of(
			DashboardPv.BANNER_LEFT, DashboardPv.BANNER_MIDDLE, DashboardPv.BANNER_RIGHT);
	
	/**
	 * The constructor. 
	 * 
	 * @param parent The parent composite
	 * @param style The SWT style
	 * @param model The model holding data to display in the view
	 * @param titleFont The font for the banner title
	 * @param textFont The font for the banner text
	 * @param simulationModeFont The font for the simulation mode indicator
	 */
	public Banner(Composite parent, int style, BannerModel model, Font titleFont, Font textFont, Font simulationModeFont) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		this.setLayout(gridLayout);
		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		this.setBackgroundMode(SWT.INHERIT_FORCE);
		
		bannerText = new Label(this, SWT.WRAP | SWT.CENTER);
		bannerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		bannerText.setFont(titleFont);
		bannerText.setText("INSTRUMENT is UNKNOWN");
		
		details = new Composite(this, SWT.NONE);
		details.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		details.setLayout(new GridLayout(3, true));
		details.setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		for (DashboardPv pv : DASHBOARD_PVS) {
			createBannerLabel(details, textFont, pv, model);
		}
		
		if (model != null) {
			bind(model);
		}
	}
	
	private void createBannerLabel(Composite parent, Font textFont, DashboardPv pv, BannerModel model) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		container.setLayout(new GridLayout(2, true));
		container.setBackgroundMode(SWT.INHERIT_FORCE);
		
		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		label.setFont(textFont);
		label.setText("");
		
		Label value = new Label(container, SWT.NONE);
		value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		value.setFont(textFont);
		value.setText("");
		
		bindingContext.bindValue(WidgetProperties.background().observe(container), 
				BeanProperties.value("value").observe(model.background()));
		bindingContext.bindValue(WidgetProperties.text().observe(label), 
				BeanProperties.value("value").observe(model.dashboardLabel(pv)));
		bindingContext.bindValue(WidgetProperties.text().observe(value), 
				BeanProperties.value("value").observe(model.dashboardValue(pv)));
	}
	
    private void bind(BannerModel model) {
		bindingContext.bindValue(WidgetProperties.text().observe(bannerText), BeanProperties.value("value").observe(model.bannerText()));
		bindingContext.bindValue(WidgetProperties.background().observe(this), BeanProperties.value("value").observe(model.background()));
	}
}
