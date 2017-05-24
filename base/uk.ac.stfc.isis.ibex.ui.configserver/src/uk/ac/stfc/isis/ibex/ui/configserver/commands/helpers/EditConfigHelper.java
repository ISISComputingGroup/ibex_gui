
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
package uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationServerUI;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.EditConfigDialog;

/**
 * A helper class to open config editing dialog boxes.
 */
public class EditConfigHelper extends ConfigHelper {

    private ConfigServer server;

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
        
        title = "Edit Configuration";
        currentSubTitle = "Editing the current configuration";
    }

    @Override
    protected void openDialog(String subTitle, EditableConfiguration config, boolean isCurrent,
            boolean editBlockFirst) {
        config.setIsComponent(false);
        EditConfigDialog dialog =
                new EditConfigDialog(shell, title, currentSubTitle, config, false, configurationViewModels,
                        editBlockFirst);
        if (dialog.open() == Window.OK) {
            if (dialog.doAsComponent()) {
                server.saveAsComponent().uncheckedWrite(dialog.getComponent());
            } else {
                if (isCurrent) {
                    server.setCurrentConfig().uncheckedWrite(dialog.getConfig());
                } else {
                    server.saveAs().uncheckedWrite(dialog.getConfig());
                }
            }
        }
    }
}
