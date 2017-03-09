
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

package uk.ac.stfc.isis.ibex.ui.devicescreens.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.ui.devicescreens.models.EditDeviceScreensDescriptionViewModel;

/**
 * The widget for setting the screen name and OPI.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class TargetNameWidget extends Composite {

    /** The view model. */
    private EditDeviceScreensDescriptionViewModel viewModel;

    /** The OPI list. */
    private List<String> availableOPIs;

    /**
     * The constructor.
     * 
     * @param parent the parent composite
     * @param viewModel the view model
     */
    public TargetNameWidget(Composite parent, EditDeviceScreensDescriptionViewModel viewModel) {
		super(parent, SWT.NONE);
		
        this.viewModel = viewModel;
		
        this.availableOPIs = new ArrayList<>(viewModel.getAvailableOPIs());
        // Insert a blank option for the OPIs
        this.availableOPIs.add(0, "");
		
		createControls(this);
    }

    /**
     * Create the controls.
     * 
     * @param parent the parent composite
     */
    private void createControls(Composite parent) {
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        setLayout(gridLayout);

        ComboViewer cmboOpiName = new ComboViewer(parent, SWT.READ_ONLY);
        Combo combo = cmboOpiName.getCombo();
        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        cmboOpiName.setContentProvider(new ArrayContentProvider());
        cmboOpiName.setInput(availableOPIs);
        cmboOpiName.getCombo().select(-1);

        DataBindingContext bindingContext = new DataBindingContext();

        bindingContext.bindValue(WidgetProperties.selection().observe(cmboOpiName.getCombo()),
                BeanProperties.value("currentKey").observe(viewModel));

        bindingContext.bindValue(WidgetProperties.enabled().observe(cmboOpiName.getCombo()),
                BeanProperties.value("enabled").observe(viewModel));
    }

}
