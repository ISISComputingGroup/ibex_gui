
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

import java.util.concurrent.TimeoutException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationServerUI;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;

/**
 * A helper class to open a stand-alone block editing dialog.
 */
public class EditBlockHelper {

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
    public EditBlockHelper(Shell shell, ConfigServer server) {
        this.server = server;
        this.shell = shell;
        this.configurationViewModels = ConfigurationServerUI.getDefault().configurationViewModels();
    }

    /**
     * @param config
     *            the configuration to edit
     * @param isCurrent
     *            is this the current configuration
     * @param isComponent
     *            is this a component
     * @throws TimeoutException
     */
    private void createConfigurationDialog(String config, Boolean isCurrent, Boolean isComponent) throws TimeoutException {
        // Pick a helper class who'll open the edit dialog
        ConfigHelper helper;
        if (isComponent) {
            helper = new EditComponentHelper(shell, server);
        } else {
            helper = new EditConfigHelper(shell, server);
        }

        // Open the appropriate edit dialog
        if (isCurrent) {
            helper.createDialogCurrent(true);
        } else {
            helper.createDialog(config, true);
        }
    }

    /**
     * Attempts to get a configuration or component to edit for the named block,
     * starting with the current configuration.
     *
     * @param blockName
     *            The name of the block to look for
     * @return The result of the request with details of the configuration and
     *         any errors
     */
    private EditBlockRequestResult findBlockHostConfiguration(String blockName) throws TimeoutException {
        // Get the current configuration so we can assess who the block the
        // belongs to.

        EditBlockRequestResult result = new EditBlockRequestResult();

        // Decide what to open
        try {
        	EditableConfiguration config = configurationViewModels.getCurrentConfig();
            Block block = config.getBlockByName(blockName);
            if (block == null) {
                result.setError("Cannot find block in current configuration or its components.");
            } else if (block.inComponent()) {
            	try {
            		result.setConfig(configurationViewModels.getComponent(block.getComponent()).getName(), true);
                } catch (TimeoutException err) {
                    result.setError("Cannot edit component containing block.");
                }
            } else {
                result.setConfig(config.getName(), false);
            }
        } catch (TimeoutException e) {
            result.setError("There is no current configuration, so it can not be edited.");
        }
        return result;
    }

    /**
     * Create a dialog box for editing the block.
     *
     * @param blockName
     *            The name of the block to edit
     * @throws TimeoutException
     *             Thrown if the host config cannot be obtained in a reasonable
     *             time.
     */
    public void createDialog(String blockName) throws TimeoutException {

        EditBlockRequestResult result = findBlockHostConfiguration(blockName);

        // Open the dialog or create an error as appropriate
        if (result.hasError()) {
            MessageDialog.openError(shell, "Error", result.getError());
        } else if (result.hasConfig()) {
            // If the result has a component it is not the current configuration
            // and vice versa.
            createConfigurationDialog(result.getConfig(), !result.isComponent(), result.isComponent());
        }
    }
}
