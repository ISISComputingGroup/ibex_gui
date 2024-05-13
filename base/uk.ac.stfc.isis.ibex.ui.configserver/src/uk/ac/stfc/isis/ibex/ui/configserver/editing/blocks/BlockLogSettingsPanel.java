
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButton;


/**
 * A panel in the edit block dialog editing the block's logging settings.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class BlockLogSettingsPanel extends Composite {
    private Button btnEnabled;
    private Combo cmboType;
    private Label lblSettings;
    private Text txtSettings;

    /**
     * Standard constructor.
     * 
     * @param parent The parent composite.
     * @param style The SWT style.
     * @param viewModel The viewModel for the block settings.
     */
    public BlockLogSettingsPanel(Composite parent, int style, final BlockLogSettingsViewModel viewModel) {
        super(parent, style);

        setLayout(new FillLayout(SWT.HORIZONTAL));

        Group grpLogSettings = new Group(this, SWT.NONE);
        grpLogSettings.setText("Logging Settings");
        grpLogSettings.setLayout(new GridLayout(5, false));

        Label lblType = new Label(grpLogSettings, SWT.NONE);
        lblType.setAlignment(SWT.RIGHT);
        lblType.setText("Mode:");
        GridData gdLblType = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gdLblType.widthHint = 50;
        lblType.setLayoutData(gdLblType);

        cmboType = new Combo(grpLogSettings, SWT.READ_ONLY);
        cmboType.add(BlockLogSettingsViewModel.PERIODIC_STRING);
        cmboType.add(BlockLogSettingsViewModel.MONITOR_STRING);
        cmboType.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        cmboType.setToolTipText(BlockLogSettingsViewModel.COMBO_TOOLTIP);

        lblSettings = new Label(grpLogSettings, SWT.NONE);
        lblSettings.setAlignment(SWT.RIGHT);
        GridData gdLblSettings = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gdLblSettings.widthHint = 80;
        lblSettings.setLayoutData(gdLblSettings);
        
        txtSettings = new Text(grpLogSettings, SWT.BORDER);
        GridData gdTxtSettings = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gdTxtSettings.widthHint = 145;
        txtSettings.setLayoutData(gdTxtSettings);
        
        btnEnabled = new IBEXButton(grpLogSettings, SWT.CHECK)
        		.layoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1))
        		.text("Enabled")
        		.get();
        
        setModel(viewModel);
    }
    
    /**
     * Sets the view model and observers for the view. 
     * @param viewModel The logging settings view model. 
     */
    public void setModel(BlockLogSettingsViewModel viewModel) {
        DataBindingContext bindingContext = new DataBindingContext();

        bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnEnabled),
                BeanProperties.value("enabled").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(cmboType),
                BeanProperties.value("enabled").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(txtSettings),
                BeanProperties.value("enabled").observe(viewModel));

        bindingContext.bindValue(WidgetProperties.comboSelection().observe(cmboType),
                BeanProperties.value("comboText").observe(viewModel));

        bindingContext.bindValue(WidgetProperties.text().observe(lblSettings),
                BeanProperties.value("labelText").observe(viewModel));

        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtSettings),
                BeanProperties.value("textBoxText").observe(viewModel));

    }
}
