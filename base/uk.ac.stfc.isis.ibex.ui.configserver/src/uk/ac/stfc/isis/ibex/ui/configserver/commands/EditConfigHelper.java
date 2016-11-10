
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
package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationServerUI;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.EditConfigDialog;

/**
 * A helper class to open config editing dialog boxes.
 */
public class EditConfigHelper {
    private static final String TITLE = "Edit Configuration";
    private static final String SUB_TITLE_CURRENT = "Editing the current configuration";

    private Shell shell;
    private ConfigServer server;
    private ConfigurationViewModels configurationViewModels;

    /**
     * Constructor for the helper class.
     * 
     * @param shell The shell in which to display dialog boxes
     * @param server The ConfigServer to save configurations to
     */
    public EditConfigHelper(Shell shell, ConfigServer server) {
        this.shell = shell;
        this.server = server;
        this.configurationViewModels = ConfigurationServerUI.getDefault().configurationViewModels();
    }

    /**
     * Create a dialog box for editing the current config.
     * 
     * @param blockname The blockname we wish to edit, if null or empty we will
     *            edit the whole config.
     */
    public void createDialogCurrent(String blockname) {
        configurationViewModels.setModelAsCurrentConfig();
        UpdatedValue<EditableConfiguration> config = configurationViewModels.getConfigModel();

        if (Awaited.returnedValue(config, 1)) {
            openDialog(SUB_TITLE_CURRENT, config.getValue(), configurationViewModels, blockname, true);
        } else {
            MessageDialog.openError(shell, "Error", "There is no current configuration, so it can not be edited.");
        }
    }

    /**
     * Create a dialog box for editing a config other than the current one.
     * 
     * @param configName The name of the config we wish to edit
     */
    public void createDialog(String configName) {
        String subTitle = "Editing " + configName;

        configurationViewModels.setModelAsConfig(configName);
        UpdatedValue<EditableConfiguration> config = configurationViewModels.getConfigModel();

        if (Awaited.returnedValue(config, 1)) {
            openDialog(subTitle, config.getValue(), configurationViewModels, null, false);
        }
    }

    private void openDialog(String subTitle, EditableConfiguration config,
            ConfigurationViewModels configurationViewModels,
            String blockname, boolean isCurrent) {
        EditConfigDialog dialog = new EditConfigDialog(shell, TITLE, SUB_TITLE_CURRENT, config, false, false, blockname,
                configurationViewModels);
        if (dialog.open() == Window.OK) {
            if (dialog.doAsComponent()) {
                server.saveAsComponent().write(dialog.getComponent());
            } else {
                if (isCurrent) {
                    server.setCurrentConfig().write(dialog.getConfig());
                } else {
                    server.saveAs().write(dialog.getConfig());
                }
            }
        }

    }
}
