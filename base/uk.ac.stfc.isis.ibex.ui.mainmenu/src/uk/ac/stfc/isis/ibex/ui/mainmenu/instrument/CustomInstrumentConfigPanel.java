
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

package uk.ac.stfc.isis.ibex.ui.mainmenu.instrument;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.custom.CustomInstrumentInfo;

public class CustomInstrumentConfigPanel extends Composite {

    private String instrumentName;
    private Text txtPVPrefix;

    public CustomInstrumentConfigPanel(Composite parent, int style, String instrumentName) {
        super(parent, style);

        this.instrumentName = instrumentName;

        setLayout(new FillLayout(SWT.HORIZONTAL));

        Group grpPVPrefix = new Group(this, SWT.NONE);
        grpPVPrefix.setText("Instrument PV Prefix");
        grpPVPrefix.setLayout(new GridLayout(2, false));

        String warningMsg = "The instrument \"" + instrumentName
                + "\" is unknown.\nTo use this instrument please configure the correct PV prefix below.";
        Label lblWarning = new Label(grpPVPrefix, SWT.NONE);
        lblWarning.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        lblWarning.setText(warningMsg);

        Label lblPVPrefix = new Label(grpPVPrefix, SWT.NONE);
        lblPVPrefix.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblPVPrefix.setText("PV Prefix:");

        txtPVPrefix = new Text(grpPVPrefix, SWT.BORDER);
        GridData gdTxtPVPrefix = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdTxtPVPrefix.widthHint = 150;
        txtPVPrefix.setLayoutData(gdTxtPVPrefix);
    }

    public InstrumentInfo getSelected() {
        String t = txtPVPrefix.getText();
        return new CustomInstrumentInfo(instrumentName, txtPVPrefix.getText());
    }
}
