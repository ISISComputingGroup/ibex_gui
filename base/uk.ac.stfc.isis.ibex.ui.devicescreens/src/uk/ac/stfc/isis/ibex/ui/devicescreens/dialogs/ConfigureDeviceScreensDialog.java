
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

import java.util.Collection;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.ui.devicescreens.models.DeviceScreensDescriptionViewModel;

/**
 * The main dialog for editing the device screens.
 */
public class ConfigureDeviceScreensDialog extends Dialog {

    private static final Point INITIAL_SIZE = new Point(650, 750);
    private final String title = "Configure Device Screens";
    private Collection<String> availableOPIs;
    private DeviceScreensDescriptionViewModel viewModel;

    /**
     * The constructor.
     * 
     * @param parentShell the parent
     * @param availableOPIs the names of the OPIs
     */
    public ConfigureDeviceScreensDialog(Shell parentShell, Collection<String> availableOPIs,
            DeviceScreensDescription description) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.DIALOG_TRIM | SWT.RESIZE);
        this.availableOPIs = availableOPIs;
        this.viewModel = new DeviceScreensDescriptionViewModel(description);

    }

    @Override
    protected Control createDialogArea(Composite parent) {
        ConfigureDeviceScreensPanel editor =
                new ConfigureDeviceScreensPanel(parent, SWT.NONE, availableOPIs, viewModel);
        editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        return editor;
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(title);
    }

    @Override
    protected Point getInitialSize() {
        return INITIAL_SIZE;
    }
}
