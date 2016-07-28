
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

package uk.ac.stfc.isis.ibex.ui.mainmenu.instrument.custom;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * A view for the definition of a new custom instrument info.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class CustomInstrumentPanel extends Composite {

    private Text txtPVPrefix;

    /**
     * Creates an instance of the panel for configuring a new custom instrument
     * info.
     * 
     * @param parent the parent composite
     * @param style the style of the panel
     * @param viewModel the view model for the custom instrument
     */
    public CustomInstrumentPanel(Composite parent, int style, CustomIntrumentViewModel viewModel) {
        super(parent, style);

        setLayout(new FillLayout(SWT.HORIZONTAL));

        Group grpPVPrefix = new Group(this, SWT.NONE);
        grpPVPrefix.setText("Instrument PV Prefix");
        grpPVPrefix.setLayout(new GridLayout(2, false));

        createWarningLabel(grpPVPrefix, viewModel.getInstrumentName());
        createPvPrefixLabel(grpPVPrefix);
        createPvPrefixTextBox(grpPVPrefix);

        bindModel(viewModel);
    }

    private void createWarningLabel(Composite parent, String instrumentName) {
        String warningMsg =
                "Please configure a PV prefix for the unknown instrument \"" + instrumentName
                        + "\"\n(e.g. \"IN:LARMOR:\". Do not forget the trailing colon!)";
        Label lblWarning = new Label(parent, SWT.WRAP | SWT.LEFT);
        lblWarning.setLayoutData(new GridData(SWT.HORIZONTAL, SWT.CENTER, true, false, 2, 1));
        lblWarning.setText(warningMsg);
    }

    private void createPvPrefixLabel(Composite parent) {
        Label lblPVPrefix = new Label(parent, SWT.NONE);
        lblPVPrefix.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblPVPrefix.setText("PV Prefix:");
    }

    private void createPvPrefixTextBox(Composite parent) {
        txtPVPrefix = new Text(parent, SWT.BORDER);
        GridData gdTxtPVPrefix = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gdTxtPVPrefix.widthHint = 250;
        txtPVPrefix.setLayoutData(gdTxtPVPrefix);
    }

    private void bindModel(CustomIntrumentViewModel viewModel) {
        DataBindingContext bindingContext = new DataBindingContext();

        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtPVPrefix),
                BeanProperties.value("pvPrefix").observe(viewModel));
    }
}
