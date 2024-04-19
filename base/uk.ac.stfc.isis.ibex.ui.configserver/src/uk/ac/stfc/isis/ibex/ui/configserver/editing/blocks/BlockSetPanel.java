
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * A panel in the edit block dialog editing the block's logging settings.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class BlockSetPanel extends Composite {
    private Button btnEnabled;
    private Text textSet;

    /**
     * Standard constructor.
     * 
     * @param parent The parent composite.
     * @param style The SWT style.
     * @param blockSetViewModel The viewModel for the block settings.
     */
    public BlockSetPanel(Composite parent, int style, final BlockSetViewModel blockSetViewModel) {
        super(parent, style);

        setLayout(new FillLayout(SWT.HORIZONTAL));

        Group grpBlockSet = new Group(this, SWT.NONE);
        grpBlockSet.setText("Set block on config change");
        grpBlockSet.setLayout(new GridLayout(5, false));

        Label lblGroup = new Label(grpBlockSet, SWT.NONE);
        lblGroup.setAlignment(SWT.RIGHT);
        lblGroup.setText("Set block to:");
        GridData gdLblType = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gdLblType.widthHint = 85;
        lblGroup.setLayoutData(gdLblType);

        textSet = new Text(grpBlockSet, SWT.BORDER);
        GridData textGrid = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        textGrid.widthHint = 110;
        textSet.setLayoutData(textGrid);

        btnEnabled = new Button(grpBlockSet, SWT.CHECK);
        btnEnabled.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        btnEnabled.setText("Set block on config change.");

        setModel(blockSetViewModel);
    }
    
    /**
     * Sets the view model and observers. 
     * @param viewModel The group settings view model. 
     */
    public void setModel(BlockSetViewModel viewModel) {
        DataBindingContext bindingContext = new DataBindingContext();

        bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnEnabled),
                BeanProperties.value("enabled").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(textSet),
                BeanProperties.value("enabled").observe(viewModel));


        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(textSet),
                BeanProperties.value("textBoxText").observe(viewModel));

    }
}
