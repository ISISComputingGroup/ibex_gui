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
package uk.ac.stfc.isis.ibex.ui.mainmenu.managermode;

import javax.security.auth.login.FailedLoginException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.managermode.ManagerModeModel;

/**
 *
 */
@SuppressWarnings("checkstyle:magicnumber")
public class EnterManagerModeDialog extends TitleAreaDialog {

    private static final String WINDOW_TITLE = "Manager mode";
    private static final String AREA_TITLE = "Enter manager mode";

    private Composite upperDialogArea;

    private final ManagerModeModel model;

    private Text passwordEntryField;

    /**
     * Constructor.
     * 
     * @param parentShell
     *            the parent shell
     * @param model
     *            the view model
     */
    protected EnterManagerModeDialog(Shell parentShell, ManagerModeModel model) {
        super(parentShell);
        upperDialogArea = (Composite) super.createDialogArea(parentShell);
        upperDialogArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        this.model = model;

    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(WINDOW_TITLE);

        shell.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));

    }

    @Override
    protected Point getInitialSize() {
        return new Point(400, 200);
    }

    @Override
    protected void okPressed() {

        try{
            model.login(passwordEntryField.getText());
            super.okPressed();
        } catch (FailedLoginException ex) {
            displayError(this.getShell(), ex.getMessage());
        }

    }

    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle(AREA_TITLE);

        Composite container = (Composite) super.createDialogArea(parent);
        Group group = new Group(container, SWT.NONE);
        group.setLayout(new GridLayout(2, true));

        Label passwordEntryLabel = new Label(group, SWT.LEFT);
        passwordEntryLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        passwordEntryLabel.setText("Please enter the manager password:");

        passwordEntryField = new Text(group, SWT.PASSWORD | SWT.BORDER);
        passwordEntryField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        return container;
    }

    private static void displayError(Shell shell, String message) {
        MessageDialog error = new MessageDialog(shell, "Error", null,
                message, MessageDialog.ERROR, new String[] { "OK" }, 0);
        error.open();
    }

}
