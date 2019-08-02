
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

import uk.ac.stfc.isis.ibex.ui.dashboard.models.TimePanelModel;

/**
 * The time panel on the dashboard.
 */
public class TimePanel extends Composite {
    /** The control for showing the time. */
	private final StyledText instrumentTime; 

    /** The run time label. */
	private final Label runTime;

    /** The period label. */
	private final Label period;
	
    /**
     * Constructor.
     * 
     * @param parent the parent control
     * @param style the SWT style
     * @param font the font for the text
     * @param model the view model
     */
	public TimePanel(Composite parent, int style, Font font, TimePanelModel model) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(2, false));
		
		Label lblInstTime = new Label(this, SWT.NONE);
		lblInstTime.setFont(font);
		lblInstTime.setText("Inst. Time:");
		
		instrumentTime = new StyledText(this, SWT.FULL_SELECTION | SWT.READ_ONLY);
		instrumentTime.setEnabled(false);
		instrumentTime.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		instrumentTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		instrumentTime.setFont(font);
        instrumentTime.setText("99/99/9999 99:99:99");
		
		Label lblRunTime = new Label(this, SWT.NONE);
		lblRunTime.setFont(font);
		lblRunTime.setText("Run Time:");
		
		runTime = new Label(this, SWT.NONE);
		runTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		runTime.setFont(font);
        runTime.setText("999 hours 99 mins 99 s");
		
		Label lblPeriod = new Label(this, SWT.NONE);
		lblPeriod.setFont(font);
		lblPeriod.setText("Period:");
		
		period = new Label(this, SWT.NONE);
		period.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		period.setFont(font);
        period.setText("99999 / 99999");
		
        if (model != null) {
            bind(model);
        }
	}

    /**
     * Sets up the data binding.
     * 
     * @param model the view model
     */
	private void bind(TimePanelModel model) {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(instrumentTime), BeanProperties.value("value").observe(model.instrumentTime()));
		bindingContext.bindValue(WidgetProperties.text().observe(runTime), BeanProperties.value("value").observe(model.runTime()));
		bindingContext.bindValue(WidgetProperties.text().observe(period), BeanProperties.value("value").observe(model.period()));
	}
}
