
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
 * A Helper class to open config editing dialog boxes
 */
public class EditConfigHelper {
    private static final String TITLE = "Edit Configuration";
    private static final String SUB_TITLE_CURRENT = "Editing the current configuration";

    private Shell shell;
    private ConfigServer SERVER;
    private ConfigurationViewModels configurationViewModels;

    public EditConfigHelper(Shell shell, ConfigServer server) {
        this.shell = shell;
        this.SERVER = server;
        this.configurationViewModels = ConfigurationServerUI.getDefault().configurationViewModels();
    }

    public void createDialogCurrent(String blockname) {
        configurationViewModels.setModelAsCurrentConfig();
        UpdatedValue<EditableConfiguration> config = configurationViewModels.getConfigModel();

        if (Awaited.returnedValue(config, 1)) {
            openDialog(SUB_TITLE_CURRENT, config.getValue(), configurationViewModels, blockname, true);
        }
    }

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
                SERVER.saveAsComponent().write(dialog.getComponent());
            } else {
                if (isCurrent) {
                    SERVER.setCurrentConfig().write(dialog.getConfig());
                    SERVER.save().write(dialog.getConfig().name());
                } else {
                    SERVER.saveAs().write(dialog.getConfig());
                }
            }
        }

    }
}
