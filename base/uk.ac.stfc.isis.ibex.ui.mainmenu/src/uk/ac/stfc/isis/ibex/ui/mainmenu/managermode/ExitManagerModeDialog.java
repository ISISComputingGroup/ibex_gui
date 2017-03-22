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

import uk.ac.stfc.isis.ibex.managermode.ManagerModeModel;

/**
 *
 */
@SuppressWarnings("checkstyle:magicnumber")
public class ExitManagerModeDialog extends TitleAreaDialog {

    private static final String WINDOW_TITLE = "Manager mode";
    private static final String AREA_TITLE = "Exit manager mode";

    private Composite upperDialogArea;

    private final ManagerModeModel model;

    /**
     * Constructor.
     * 
     * @param parentShell
     *            the parent shell
     * @param model
     *            the view model
     */
    protected ExitManagerModeDialog(Shell parentShell, ManagerModeModel model) {
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
        return new Point(300, 200);
    }

    @Override
    protected void okPressed() {
        model.logout();
        super.okPressed();
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle(AREA_TITLE);

        Composite container = (Composite) super.createDialogArea(parent);

        Group group = new Group(container, SWT.NONE);
        group.setLayout(new GridLayout(1, true));

        Label label1 = new Label(group, SWT.FILL);
        label1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        label1.setText("Are you sure you wish to exit manager mode?");

        Label label2 = new Label(group, SWT.FILL);
        label2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        label2.setText("Press OK to confirm or cancel to cancel.");

        return container;
    }

}
