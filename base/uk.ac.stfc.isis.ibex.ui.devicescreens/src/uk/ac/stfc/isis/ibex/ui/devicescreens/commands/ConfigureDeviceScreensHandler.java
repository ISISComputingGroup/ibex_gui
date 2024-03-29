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
package uk.ac.stfc.isis.ibex.ui.devicescreens.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.devicescreens.DeviceScreens;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.epics.writing.OnCanWriteChangeListener;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.ui.devicescreens.dialogs.ConfigureDeviceScreensDialog;

/**
 * The handler for launching the configure screens dialog.
 */
public class ConfigureDeviceScreensHandler extends AbstractHandler {

    /** The writable for the setting the devices screens. */
    private final Writable<DeviceScreensDescription> writeable = DeviceScreens.getInstance().getDevicesSetter();
    private final OnCanWriteChangeListener canWriteListener = canWrite -> setBaseEnabled(canWrite);
            
    /**
     * Constructor.
     */
    public ConfigureDeviceScreensHandler() {
        writeable.addOnCanWriteChangeListener(canWriteListener);
    }

    /**
     * Helper method for getting the GUI shell for the active window.
     * 
     * @return the current active shell
     */
    protected Shell shell() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {

        ConfigureDeviceScreensDialog dialog =
                new ConfigureDeviceScreensDialog(shell());

        if (dialog.open() == Window.OK) {
            dialog.save();
        }

        return null;
    }

}
