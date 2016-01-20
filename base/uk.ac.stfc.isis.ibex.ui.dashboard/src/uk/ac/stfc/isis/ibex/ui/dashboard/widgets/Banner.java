
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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.ui.dashboard.models.BannerModel;

@SuppressWarnings("checkstyle:magicnumber")
public class Banner extends Composite {

	private final Label bannerText;
	private final Composite details;
	private final Label lblRun;
	private final Label runNumber;
	private final Label lblShutter;
	private final Label shutter;
	
	public Banner(Composite parent, int style, BannerModel model, Font titleFont, Font textFont) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		
		bannerText = new Label(this, SWT.WRAP | SWT.CENTER);
		bannerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		bannerText.setFont(titleFont);
		bannerText.setText("INSTRUMENT   is   RUNNING");
		
		details = new Composite(this, SWT.NONE);
		details.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		details.setLayout(new GridLayout(4, false));
		details.setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		lblRun = new Label(details, SWT.NONE);
		lblRun.setFont(textFont);
		lblRun.setText("Run:");
		
		runNumber = new Label(details, SWT.NONE);
		runNumber.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		runNumber.setFont(textFont);
		runNumber.setText("0000001");
		
		lblShutter = new Label(details, SWT.NONE);
		lblShutter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		lblShutter.setFont(textFont);
		lblShutter.setText("Shutter:");
		
		shutter = new Label(details, SWT.NONE);
		GridData gdShutter = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdShutter.widthHint = 100;
		gdShutter.minimumWidth = 100;
		shutter.setLayoutData(gdShutter);
		shutter.setFont(textFont);
		shutter.setText("UNKNOWN");
		
		if (model != null) {
			bind(model);
		}
	}
	
	public void bind(BannerModel model) {		
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(bannerText), BeanProperties.value("value").observe(model.bannerText()));
		bindingContext.bindValue(WidgetProperties.background().observe(this), BeanProperties.value("value").observe(model.background()));
		bindingContext.bindValue(WidgetProperties.text().observe(runNumber), BeanProperties.value("value").observe(model.runNumber()));
		bindingContext.bindValue(WidgetProperties.text().observe(shutter), BeanProperties.value("value").observe(model.shutter()));
	}
}
