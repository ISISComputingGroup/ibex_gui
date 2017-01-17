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
package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;

/**
 *
 */
public class EditIocDialog extends TitleAreaDialog {
    AddIocPanel addIocPanel;
    EditableConfiguration config;
    boolean isBlank;

    Composite addNavButtonBar;
    Composite editNavButtonBar;

    Button btnNav;
    Button btnOk;

    /**
     * @param parent
     * @param style
     */
    public EditIocDialog(Shell parent, EditableConfiguration config, boolean isBlank) {
        super(parent);
        this.isBlank = isBlank;
        this.config = config;
        super.createButtonsForButtonBar(parent);
        getButton(IDialogConstants.OK_ID).setEnabled(false);
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        addNavButtonBar = new Composite(parent, SWT.NONE);
        addNavButtonBar.setLayout(new GridLayout());

        btnNav = createButton(addNavButtonBar, IDialogConstants.NO_ID, "Next", false);
        btnOk = createButton(addNavButtonBar, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
        createButton(addNavButtonBar, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

        btnNav.setEnabled(false);
        btnOk.setEnabled(false);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle("Add IOC");

        addIocPanel = new AddIocPanel(parent, config, SWT.NONE);
        addIocPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        return addIocPanel;
    }
}
