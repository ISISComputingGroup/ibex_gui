
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

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.ConfigHelper;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.EditComponentHelper;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.ViewComponentHelper;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.ConfigSelectionDialog;

/**
 * The handler class for editing components.
 * 
 * It sets the menu labels, and opens the dialogue for editing or viewing the
 * components.
 */
public class EditComponentHandler extends EditConfigurationsHandler {

    private static final String TYPE_STRING = "Component";

    /**
     * Create the handler for opening the editor via the menu.
     */
    public EditComponentHandler() {
        super(SERVER.saveAsComponent());
        SERVER.currentConfig().addObserver(configObserver);
    }

    /**
     * Execute the handler to open the given edit/view component dialogue.
     *
     * @param shell
     *            the shell to user
     */
    @Override
    public void safeExecute(Shell shell) {
        ConfigHelper helper;
        String titleText;
        if (canWrite) {
            helper = new EditComponentHelper(shell, SERVER);
            titleText = editTitle;
        } else {
            helper = new ViewComponentHelper(shell);
            titleText = viewTitle;
        }

        ConfigSelectionDialog selectionDialog =
                new ConfigSelectionDialog(shell, titleText, SERVER.componentsInfo().getValue(), true, true);
        if (selectionDialog.open() == Window.OK) {
            String configName = selectionDialog.selectedConfig();
            helper.createDialog(configName, false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTypeString() {
        return TYPE_STRING;
    }
}
