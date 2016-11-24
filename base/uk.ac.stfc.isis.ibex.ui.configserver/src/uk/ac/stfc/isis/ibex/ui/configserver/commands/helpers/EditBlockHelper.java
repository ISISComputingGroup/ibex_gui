
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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationServerUI;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.EditBlockDialog;

/**
 * A helper class to open a stand-alone block editing dialog.
 */
public class EditBlockHelper extends ConfigHelper {

    /**
     * The configuration server used to save modifications to the configuration.
     */
    private ConfigServer server;

    /**
     * Constructor for the helper class.
     * 
     * @param shell The shell in which to display dialog boxes
     * @param server The config server used to save the changed configuration
     */
    public EditBlockHelper(Shell shell, ConfigServer server) {
        this.server = server;
        this.shell = shell;
        this.configurationViewModels = ConfigurationServerUI.getDefault().configurationViewModels();
    }

    /**
     * Opens the dialog.
     * 
     * @param subTitle the window sub-title
     * @param config the configuration to edit
     * @param blockname the block name
     * @param isCurrent whether it is the current configuration
     */
    @Override
    protected void openDialog(String subTitle, EditableConfiguration config, String blockname, boolean isCurrent) {
        // We only edit stand alone blocks as part of the current config
        assert (isCurrent);
        openDialog(config, blockname);
    }

    /**
     * @param config the configuration to edit
     * @param blockname the block name
     */
    private void openDialog(EditableConfiguration config, String blockname) {
        EditableBlock thisEditableBlock = null;
        for (EditableBlock block : config.getEditableBlocks()) {
            if (block.getName() == blockname) {
                thisEditableBlock = block;
                break;
            }
        }
        if (thisEditableBlock == null) {
            MessageDialog.openError(shell, "Error",
                    "Unable to find editable block with name " + blockname + " in configuration " + config.getName());
        } else {
            EditBlockDialog dialog = new EditBlockDialog(shell, thisEditableBlock, config);
            if (dialog.open() == Window.OK) {
                server.setCurrentConfig().write(config.asConfiguration());
            }
        }
    }
}
