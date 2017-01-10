
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

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.ui.devicescreens.models.DeviceScreensDescriptionViewModel;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * The main dialog for editing the device screens.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class ConfigureDeviceScreensDialog extends TitleAreaDialog implements MessageDisplayer {

    /** The initial dialog size. */
    private static final Point INITIAL_SIZE = new Point(650, 750);

    /** The dialog title. */
    private static final String TITLE = "Configure Device Screens";

    /** The OPI list. */
    private Collection<String> availableOPIs;

    /** The view model. */
    private DeviceScreensDescriptionViewModel viewModel;

    /** Persistence of a device screen */
    private Collection<String> persistedOptions;

    /**
     * The constructor.
     * 
     * @param parentShell the parent
     * @param availableOPIs the names of the OPIs
     * @param description the current screens description
     */
    public ConfigureDeviceScreensDialog(Shell parentShell, Collection<String> availableOPIs,
            DeviceScreensDescription description) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.DIALOG_TRIM | SWT.RESIZE);
        this.availableOPIs = availableOPIs;
        this.viewModel =
                new DeviceScreensDescriptionViewModel(description, this, Opi.getDefault().descriptionsProvider());
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        ConfigureDeviceScreensPanel editor =
                new ConfigureDeviceScreensPanel(parent, SWT.NONE, availableOPIs, viewModel, persistedOptions);
        editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        setTitle(TITLE);

        return editor;
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(TITLE);
    }

    @Override
    protected Point getInitialSize() {
        return INITIAL_SIZE;
    }

    /**
     * Gets the new device description for saving.
     * 
     * @return the new device description
     */
    public DeviceScreensDescription getDeviceDescription() {
        return viewModel.getDeviceDescription();
    }

    /**
     * Enable or disable the OK button.
     * 
     * @param value true equals enabled
     */
    private void setOKEnabled(boolean value) {
        Button okButton = getButton(IDialogConstants.OK_ID);
        if (okButton != null) {
            okButton.setEnabled(value);
        }
    }

    /**
     * Display the error message.
     * 
     * @param source where it came from
     * @param message what it is
     */
    @Override
    public void setErrorMessage(String source, String message) {
        super.setErrorMessage(message);
        if (message != null && message.length() > 0) {
            setOKEnabled(false);
        } else {
            setOKEnabled(true);
        }
    }
}
