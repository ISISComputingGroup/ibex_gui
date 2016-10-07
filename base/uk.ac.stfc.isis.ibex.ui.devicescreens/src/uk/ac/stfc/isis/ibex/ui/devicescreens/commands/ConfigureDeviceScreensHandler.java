
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
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.ui.devicescreens.dialogs.ConfigureDeviceScreensDialog;

/**
 * 
 */
public class ConfigureDeviceScreensHandler extends AbstractHandler {

    final Writable<DeviceScreensDescription> writeable = DeviceScreens.getInstance().getDevicesSetter();

    /**
     * This is an inner anonymous class inherited from SameTypeWriter with added
     * functionality for disabling the command if the underlying PV cannot be
     * written to.
     */
    protected final SameTypeWriter<DeviceScreensDescription> configService =
            new SameTypeWriter<DeviceScreensDescription>() {
                @Override
                public void onCanWriteChanged(boolean canWrite) {
                    setBaseEnabled(canWrite);
                };
            };

    /**
     * Constructor.
     * 
     * @param destination where to write the data to
     */
    public ConfigureDeviceScreensHandler() {
        configService.writeTo(writeable);
        writeable.subscribe(configService);
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
                new ConfigureDeviceScreensDialog(shell(), Opi.getDefault().descriptionsProvider().getOpiList(), load());

        if (dialog.open() == Window.OK) {
            DeviceScreensDescription desc = dialog.getDeviceDescription();
            writeable.write(desc);
        }

        return null;
    }

    private DeviceScreensDescription load() {
        UpdatedValue<DeviceScreensDescription> instrumentDescription =
                new UpdatedObservableAdapter<>(DeviceScreens.getInstance().getDevices());
        if (Awaited.returnedValue(instrumentDescription, 1)) {
            return instrumentDescription.getValue();
        }
        return null;
    }

}
