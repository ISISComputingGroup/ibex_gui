
/**
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2022 Science & Technology Facilities Council.
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

import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButtonBuilder;

/**
 * A panel in the edit block dialog editing the block's logging settings.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class BlockGroupPanel extends Composite {
    private Button btnEnabled;
    private Combo cmboGroup;

    /**
     * Standard constructor.
     * 
     * @param parent The parent composite.
     * @param style The SWT style.
     * @param viewModel The viewModel for the block settings.
     */
    public BlockGroupPanel(Composite parent, int style, final BlockGroupViewModel viewModel) {
        super(parent, style);

        setLayout(new FillLayout(SWT.HORIZONTAL));

        Group grpGroupSettings = new Group(this, SWT.NONE);
        grpGroupSettings.setText("Add to Group");
        grpGroupSettings.setLayout(new GridLayout(5, false));

        Label lblGroup = new Label(grpGroupSettings, SWT.NONE);
        lblGroup.setAlignment(SWT.RIGHT);
        lblGroup.setText("Group:");
        GridData gdLblType = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gdLblType.widthHint = 50;
        lblGroup.setLayoutData(gdLblType);

        cmboGroup = new Combo(grpGroupSettings, SWT.READ_ONLY);
        cmboGroup.setItems(viewModel.getGroups());
        cmboGroup.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        
        btnEnabled = new IBEXButtonBuilder(grpGroupSettings, SWT.CHECK)
        		.customLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1))
        		.text("Add to Group")
        		.build();

        setModel(viewModel);
    }
    
    /**
     * Sets the view model and observers. 
     * @param viewModel The group settings view model. 
     */
    public void setModel(BlockGroupViewModel viewModel) {
        DataBindingContext bindingContext = new DataBindingContext();

        bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnEnabled),
                BeanProperties.value("enabled").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(cmboGroup),
                BeanProperties.value("enabled").observe(viewModel));


        bindingContext.bindValue(WidgetProperties.comboSelection().observe(cmboGroup),
                BeanProperties.value("comboText").observe(viewModel));

    }
}
