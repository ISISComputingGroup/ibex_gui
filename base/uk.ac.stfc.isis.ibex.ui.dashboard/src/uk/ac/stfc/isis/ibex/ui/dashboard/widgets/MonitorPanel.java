
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.dashboard.models.MonitorPanelModel;

public class MonitorPanel extends Composite {

	private final StyledText goodRawFrames;
	private final StyledText monitorCounts;
	private final StyledText currentTotal;
	
	public MonitorPanel(Composite parent, int style, MonitorPanelModel model, Font font) {
		super(parent, style);
		setEnabled(false);
		setLayout(new GridLayout(2, false));
		
		Label lblGoodRaw = new Label(this, SWT.NONE);
		lblGoodRaw.setFont(font);
        lblGoodRaw.setText("Good / Raw Frames:");
		
		goodRawFrames = new StyledText(this, SWT.READ_ONLY | SWT.SINGLE);
		goodRawFrames.setEnabled(false);
		goodRawFrames.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		goodRawFrames.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		goodRawFrames.setFont(font);
        goodRawFrames.setText("xxxxxxxx / xxxxxxxx");
		
		Label lblCurrentTotal = new Label(this, SWT.NONE);
		lblCurrentTotal.setFont(font);
		lblCurrentTotal.setText("Current / Total:");
		
		currentTotal = new StyledText(this, SWT.READ_ONLY | SWT.SINGLE);
		currentTotal.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		currentTotal.setEditable(false);
		currentTotal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		currentTotal.setFont(font);
		currentTotal.setText("d.dd / d.dd");
		
		Label lblMonitorCounts = new Label(this, SWT.NONE);
		lblMonitorCounts.setFont(font);
		lblMonitorCounts.setText("Monitor Counts:");
		
		monitorCounts = new StyledText(this, SWT.READ_ONLY | SWT.SINGLE);
		monitorCounts.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		monitorCounts.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		monitorCounts.setFont(font);
		monitorCounts.setText("x count");
		
		if (model != null) {
			bind(model);
		}
	}

	private void bind(MonitorPanelModel model) {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(goodRawFrames), BeanProperties.value("value").observe(model.goodOverRawFrames()));
		bindingContext.bindValue(WidgetProperties.text().observe(currentTotal), BeanProperties.value("value").observe(model.currentOverTotal()));
		bindingContext.bindValue(WidgetProperties.text().observe(monitorCounts), BeanProperties.value("value").observe(model.monitorCounts()));
	}
}
