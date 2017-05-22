
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
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationServerUI;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.EditBlockDialog;

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
     * @param blockname
     *            the block name
     * @param isCurrent
     *            is this the current configuration
     * @param isComponent
     *            is this a component
     */
    private void openDialog(EditableConfiguration config, String blockname, Boolean isCurrent, Boolean isComponent) {
        // Can't be both the current configuration and a component
        assert !isCurrent || !isComponent;

        EditableBlock thisEditableBlock = null;
        for (EditableBlock block : config.getEditableBlocks()) {
            if (block.getName().equals(blockname)) {
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
                Configuration configToSave = config.asConfiguration();
                if (isCurrent) {
                    server.setCurrentConfig().uncheckedWrite(configToSave);
                } else if (isComponent) {
                    server.saveAsComponent().uncheckedWrite(configToSave);
                } else {
                    server.saveAs().uncheckedWrite(configToSave);
                }
            }
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
    private EditBlockRequestResult requestBlockEdit(String blockName) {
        // Get the current configuration so we can assess who the block the
        // belongs to
        configurationViewModels.setModelAsCurrentConfig();
        UpdatedValue<EditableConfiguration> config = configurationViewModels.getConfigModel();

        EditBlockRequestResult result = new EditBlockRequestResult();

        // Decide what to open
        if (Awaited.returnedValue(config, 1)) {
            Block block = config.getValue().getBlockByName(blockName);
            if (block == null) {
                result.setError("Cannot find block in current configuration or its components.");
            } else if (block.hasComponent()) {
                configurationViewModels.setModelAsComponent(block.getComponent());
                UpdatedValue<EditableConfiguration> editableComponent = configurationViewModels.getConfigModel();
                if (Awaited.returnedValue(editableComponent, 1)) {
                    result.setConfig(editableComponent.getValue(), true);
                } else {
                    result.setError("Cannot edit component containing block.");
                }
            } else {
                result.setConfig(config.getValue(), false);
            }
        } else {
            result.setError("There is no current configuration, so it can not be edited.");
        }
        return result;
    }

    /**
     * Create a dialog box for editing the block.
     * 
     * @param blockName The name of the block to edit
     */
    public void createDialog(String blockName) {
        
        EditBlockRequestResult result = requestBlockEdit(blockName);
        
        // Open the dialog or create an error as appropriate
        if (result.hasError()) {
            MessageDialog.openError(shell, "Error", result.getError());
        } else if (result.hasConfig()) {
            // If the reesult has a component it is not the current config and
            // vice versa.
            openDialog(result.getConfig(), blockName, !result.isComponent(), result.isComponent());
        }
    }
}
