
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

package uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers;

import java.util.concurrent.TimeoutException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;

/**
 * An interface for a class that is able to open a dialog giving information on the current configuration.
 */
public abstract class ConfigHelper {
    protected String title;

    protected ConfigurationViewModels configurationViewModels;
    protected Shell shell;
    protected static final int MAX_SECONDS_TO_WAIT = 10;

    /**
     * Create a dialog box for editing a config other than the current one.
     *
     * @param configName
     *            The name of the config we wish to edit
     * @param editBlockFirst
     *            Whether to present the blocks tab first
     * @throws TimeoutException
     *             Thrown if the config cannot be obtained in a reasonable time.
     */
    public void createDialog(String configName, boolean editBlockFirst) throws TimeoutException {
        EditableConfiguration config = configurationViewModels.getConfig(configName);
		openDialog(config, false, editBlockFirst);
		config.close();
    }

    /**
     * Create a dialog box for editing the current config.
     *
     * @param editBlockFirst
     *            Whether the first operation we want to do is edit a block
     */
    public void createDialogCurrent(boolean editBlockFirst) {
        try {
            EditableConfiguration currentConfig = configurationViewModels.getCurrentConfig();
			openDialog(currentConfig, true, editBlockFirst);
			currentConfig.close();
        } catch (TimeoutException err) {
            MessageDialog.openError(shell, "Error", "There is no current configuration, so it can not be edited.");
        }
    }

    /**
     * Create a dialog box for editing the current config.
     */
    public void createDialogCurrent() {
        createDialogCurrent(false);
    }

    /**
     * Opens a view/edit configuration dialog for the specified configuration.
     *
     * @param config
     *            The configuration to open
     * @param isCurrent
     *            Is this the current configuration
     * @param editBlockFirst
     *            Should the dialog open the blocks tab first
     */
    protected abstract void openDialog(EditableConfiguration config, boolean isCurrent,
            boolean editBlockFirst);

    /**
     * Get the display name for this configuration
     *
     * This will return the configuration name or current if it is the current configuration.
     * @param config
     * 			The configuration to get the name from
     * @param isCurrent
     * 			Is this the current configuration
     * @return the display name for this configuration
     */
    public String getConfigDisplayName(EditableConfiguration config, boolean isCurrent) {
    	return (isCurrent) ? "current" : config.getName();
    }
}
