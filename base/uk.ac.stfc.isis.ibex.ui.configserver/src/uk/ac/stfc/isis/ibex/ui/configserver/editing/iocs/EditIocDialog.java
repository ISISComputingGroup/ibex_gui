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
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;

/**
 *
 */
public class EditIocDialog extends TitleAreaDialog {
    EditableConfiguration config;
    boolean isBlank;

    Button btnNav;
    Button btnOk;
    Composite content;
    StackLayout stack;
    AddIocPanel addIocPanel;
    EditIocPanel editIocPanel;
    Composite nextNav;

    private static final Display DISPLAY = Display.getCurrent();

    SelectionListener navListener = new SelectionListener() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            updateStack(nextNav);
            if (nextNav.equals(addIocPanel)) {
                nextNav = editIocPanel;
                btnNav.setText("Next");
                btnNav.setFocus();
                btnOk.setEnabled(false);
            } else {
                nextNav = addIocPanel;
                btnNav.setText("Previous");
                btnOk.setEnabled(true);
                btnOk.setFocus();
            }
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            //
        }
    };

    /**
     * @param parent
     * @param style
     */
    public EditIocDialog(Shell parent, EditableConfiguration config, boolean isBlank) {
        super(parent);
        this.isBlank = isBlank;
        this.config = config;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // TODO put it on a stack
        btnNav = createButton(parent, IDialogConstants.NO_ID, "Next", false);
        btnOk = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

//        btnNav.setEnabled(false);
        btnNav.addSelectionListener(navListener);
        btnOk.setEnabled(false);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        // TODO conditional
        content = new Composite(parent, SWT.NONE);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        stack = new StackLayout();
        content.setLayout(stack);

        addIocPanel = new AddIocPanel(content, config, SWT.NONE);
        addIocPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        editIocPanel = new EditIocPanel(content, config, SWT.NONE);
        editIocPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        stack.topControl = addIocPanel;
        nextNav = editIocPanel;

        content.layout();

        this.setTitle("Add IOC");
        return addIocPanel;
    }

    private void updateStack(final Control top) {
        DISPLAY.asyncExec(new Runnable() {
            @Override
            public void run() {
                stack.topControl = top;
                content.layout();
            }
        });
    }
}
