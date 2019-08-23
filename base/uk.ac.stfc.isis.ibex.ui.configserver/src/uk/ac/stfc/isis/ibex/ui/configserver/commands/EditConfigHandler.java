
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import java.util.concurrent.TimeoutException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.ConfigHelper;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.EditConfigHelper;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.ViewConfigHelper;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.ConfigSelectionDialog;

/**
 * The handler class for editing configurations.
 *
 * It sets the menu labels, and opens the dialogue for editing or viewing the
 * configurations.
 */
public class EditConfigHandler extends EditConfigurationsHandler {

    private static final String TYPE_STRING = "Configuration";

    /**
     * Create the handler for opening the editor via the menu.
     */
    public EditConfigHandler() {
        super(SERVER.saveAs());
        SERVER.currentConfig().subscribe(configObserver);
    }

    /**
     * Execute the handler to open the given edit/view configuration dialogue.
     *
     * @param shell
     *            the shell to user
     */
    @Override
    public void safeExecute(Shell shell) throws TimeoutException {
        ConfigHelper helper;
        String titleText;
        if (canWrite) {
            helper = new EditConfigHelper(shell, SERVER);
            titleText = editTitle;
        } else {
            helper = new ViewConfigHelper(shell);
            titleText = viewTitle;
        }

        ConfigSelectionDialog selectionDialog =
                new ConfigSelectionDialog(shell, titleText, SERVER.configsInfo().getValue(), false, true);
        if (selectionDialog.open() == Window.OK) {
            String configName = selectionDialog.selectedConfig();
            if (configName.equals(SERVER.currentConfig().getValue().name()) && canWrite) {
                if (editCurrentConfigConfirmDialog(configName, shell)) {
                    helper.createDialogCurrent();
                }
            } else {
                helper.createDialog(configName, false);
            }
        }
    }

    /**
     * Create a dialog to confirm whether the user wants to edit a current configuration.
     *
     * @param configName the name of the configuration
     * @param shell the shell to user
     * @return the response from the user
     */
    private boolean editCurrentConfigConfirmDialog(String configName, Shell shell) {
        return MessageDialog.openQuestion(shell, "Confirm Edit Current Configuration",
                configName + " is the current configuration, are you sure you want to edit it?");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTypeString() {
        return TYPE_STRING;
    }
}
