
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.devicescreens.dialogs;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.ui.devicescreens.models.EditDeviceScreensDescriptionViewModel;
import uk.ac.stfc.isis.ibex.ui.devicescreens.models.TargetPropertiesViewModel;

/**
 * The main panel for the configure device screens dialog.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class ConfigureDeviceScreensTargetPanel extends Composite {
    /** The view model. */
    private EditDeviceScreensDescriptionViewModel viewModel;

    /** binding context. */
    private DataBindingContext bindingContext = new DataBindingContext();

    /**
     * The constructor.
     * 
     * @param parent the main composite
     * @param style the SWT style
     * @param viewModel the view model
     */
    public ConfigureDeviceScreensTargetPanel(Composite parent, int style, EditDeviceScreensDescriptionViewModel viewModel) {
        super(parent, style);
        this.viewModel = viewModel;

        setLayout(new GridLayout(1, false));

        createTargetGroup(this);
    }

    /**
     * Creates the target details part of the display.
     * 
     * @param mainComposite the parent composite
     */
    @SuppressWarnings("unused")
    private void createTargetGroup(Composite mainComposite) {
        Group grpDetails = new Group(mainComposite, SWT.NONE);
        grpDetails.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        grpDetails.setText("Target");
        grpDetails.setLayout(new GridLayout(1, false));

        Composite detailsComposite = new Composite(grpDetails, SWT.NONE);
        detailsComposite.setLayout(new GridLayout(2, false));
        detailsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

        Label lblName = new Label(detailsComposite, SWT.NONE);
        lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblName.setText("Name");

        Text txtName = new Text(detailsComposite, SWT.BORDER);
        txtName.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

        // Target of IOC
        Label lblTarget = new Label(detailsComposite, SWT.NONE);
        lblTarget.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblTarget.setText("Target");

        TargetNameWidget targetSelect = new TargetNameWidget(detailsComposite, viewModel);
        targetSelect.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        // Description
        Label lblDescription = new Label(detailsComposite, SWT.NONE);
        lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblDescription.setText("Description");

        Text txtDescription = new Text(detailsComposite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
        GridData gdDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gdDescription.heightHint = 70;
        txtDescription.setLayoutData(gdDescription);

        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtDescription),
                BeanProperties.value("description").observe(viewModel), null, null);

        Composite propertiesComposite = new Composite(detailsComposite, SWT.NONE);
        propertiesComposite.setLayout(new GridLayout(2, false));
        propertiesComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        TargetPropertiesViewModel propsViewModel = new TargetPropertiesViewModel(viewModel);
        TargetPropertiesWidget propertiesView = new TargetPropertiesWidget(propertiesComposite, propsViewModel);

        YesNoRadioButtons yesNoRadioButtons = new YesNoRadioButtons(detailsComposite, "Save this device screen",
                "Remove this device screen when IBEX is closed");

        bindingContext.bindValue(BeanProperties.value("selected").observe(yesNoRadioButtons),
                BeanProperties.value("persistence").observe(viewModel));
        
        bindingContext.bindValue(BeanProperties.value("enabled").observe(yesNoRadioButtons),
                BeanProperties.value("persistenceEnabled").observe(viewModel));

        bindingContext.bindValue(WidgetProperties.enabled().observe(txtName),
                BeanProperties.value("enabled").observe(viewModel));

        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtName),
                BeanProperties.value("name").observe(viewModel));

        bindingContext.bindValue(WidgetProperties.enabled().observe(grpDetails),
                BeanProperties.value("enabled").observe(viewModel));

    }

}
