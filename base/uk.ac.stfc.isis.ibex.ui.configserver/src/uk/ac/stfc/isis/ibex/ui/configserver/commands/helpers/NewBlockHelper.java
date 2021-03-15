
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

import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.editing.BlockFactory;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationServerUI;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.EditBlockDialog;

/**
 * A helper class to open the dialog for adding a block.
 */
public class NewBlockHelper {

    /** The view models for existing configurations. */
    protected ConfigurationViewModels configurationViewModels;
    /** The parent shell to load the dialog in. */
    protected Shell shell;
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
    public NewBlockHelper(Shell shell, ConfigServer server) {
        this.server = server;
        this.shell = shell;
        this.configurationViewModels = ConfigurationServerUI.getDefault().configurationViewModels();
    }

    /**
     * Create the dialog for creating the new block.
     * 
     * @param pvAddress The PV to create the block for
     */
    public void createDialog(String pvAddress) {
        try {
            EditableConfiguration config = configurationViewModels.getCurrentConfig();
            BlockFactory blockFactory = new BlockFactory(config);
            EditableBlock added = blockFactory.createNewBlock(Optional.of(pvAddress));
            Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    
            EditBlockDialog dialog = new EditBlockDialog(shell, added, config);
            if (dialog.open() == Window.OK) {
                server.setCurrentConfig().uncheckedWrite(config.asConfiguration());
            }
        } catch (Exception ex) {
            Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
            MessageDialog.openError(shell,
                    "Error", ex.getMessage());
        }
    }
}
