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

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.managermode.ManagerModeModel;
import uk.ac.stfc.isis.ibex.managermode.ManagerModePvNotConnectedException;

/**
 * Handler for manager mode being selected in the main menu.
 */
public class ManagerModeHandler {

    /**
     * Execution event for the manager mode dialog being opened.
     * 
     * @return null
     */
    @Execute
    public Object execute() {

        displayDialog();

        return null;
    }

    /**
     * Display the dialog.
     * 
     * @return selected instrument; null for do not switch instrument
     */
    private void displayDialog() {

        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        ManagerModeModel model = ManagerModeModel.getInstance();

        boolean managerMode;
        try {
            managerMode = model.isInManagerMode();
        } catch (ManagerModePvNotConnectedException exception) {
            displayError(shell, exception.getMessage());
            return;
        }

        if (managerMode) {
            (new ExitManagerModeDialog(shell, model)).open();
        } else {
            (new EnterManagerModeDialog(shell, model)).open();
        }

    }

    private static void displayError(Shell shell, String message) {
        MessageDialog error = new MessageDialog(shell, "Error", null,
                message, MessageDialog.ERROR, new String[] {"OK"}, 0);
        error.open();
    }

}
