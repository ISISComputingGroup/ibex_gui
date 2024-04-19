
/**
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButtonBuilder;

/**
 * A panel in the edit block dialog for the block's run-control settings.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class BlockRunControlPanel extends Composite {
    private Text lowLimit;
    private Text highLimit;
    private Button btnEnabled;
	private Button btnSuspendIfInvalid;

    /**
     * Standard constructor.
     * 
     * @param parent The parent composite.
     * @param style The SWT style.
     * @param viewModel The viewModel for the block run-control.
     */
    public BlockRunControlPanel(Composite parent, int style, BlockRunControlViewModel viewModel) {
        super(parent, style);

        setLayout(new FillLayout(SWT.HORIZONTAL));

        Group grpRuncontrolSettings = new Group(this, SWT.NONE);
        grpRuncontrolSettings.setText("Default Run-Control Settings");
        grpRuncontrolSettings.setLayout(new GridLayout(4, false));

        Label lblLowLimit = new Label(grpRuncontrolSettings, SWT.NONE);
        lblLowLimit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblLowLimit.setText("Low Limit:");

        lowLimit = new Text(grpRuncontrolSettings, SWT.BORDER);
        lowLimit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblHighLimit = new Label(grpRuncontrolSettings, SWT.NONE);
        lblHighLimit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblHighLimit.setText("High Limit:");

        highLimit = new Text(grpRuncontrolSettings, SWT.BORDER);
        highLimit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        btnEnabled = new IBEXButtonBuilder(grpRuncontrolSettings, SWT.CHECK)
        		.customLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1))
        		.text("Enabled")
        		.build();
        
        btnSuspendIfInvalid = new IBEXButtonBuilder(grpRuncontrolSettings, SWT.CHECK)
        		.customLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1))
        		.text("Suspend data collection if invalid")
        		.build();
        
        setModel(viewModel);
    }
	
    /**
     * Sets the view model and observers for run control settings.
     * @param viewModel The view model for run control settings. 
     */
	private void setModel(BlockRunControlViewModel viewModel) {
		DataBindingContext bindingContext = new DataBindingContext();
		
        bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnEnabled),
                BeanProperties.value(BlockRunControlViewModel.ENABLED_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnSuspendIfInvalid),
                BeanProperties.value(BlockRunControlViewModel.SUSPEND_ON_INVALID_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(lowLimit),
                BeanProperties.value(BlockRunControlViewModel.ENABLED_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(highLimit),
                BeanProperties.value(BlockRunControlViewModel.ENABLED_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnSuspendIfInvalid),
                BeanProperties.value(BlockRunControlViewModel.ENABLED_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(lowLimit),
                BeanProperties.value(BlockRunControlViewModel.LOW_LIMIT_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(highLimit),
                BeanProperties.value(BlockRunControlViewModel.HIGH_LIMIT_BINDING_NAME).observe(viewModel)); 
        
	}
}
