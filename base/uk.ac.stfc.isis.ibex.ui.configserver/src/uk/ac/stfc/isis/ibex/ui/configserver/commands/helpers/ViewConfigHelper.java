
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

import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationServerUI;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.ConfigDetailsDialog;

/**
 * A helper class to open configuration editing dialog boxes.
 */
public class ViewConfigHelper extends ConfigHelper {
    /**
     * Constructor for the helper class.
     * 
     * @param shell The shell in which to display dialog boxes
     */
    public ViewConfigHelper(Shell shell) {
        this.shell = shell;
        this.configurationViewModels = ConfigurationServerUI.getDefault().configurationViewModels();
        
        title = "View Configuration";
        currentSubTitle = "Viewing the current configuration";
    }

    /**
     * Opens the dialog.
     * 
     * @param subTitle
     *            the window sub-title
     * @param config
     *            the configuration to edit
     * @param isCurrent
     *            whether it is the current configuration
     * @param editBlockFirst
     *            Open the dialog with blocks tab open
     */
    @Override
    protected void openDialog(String subTitle, EditableConfiguration config, boolean isCurrent,
            boolean editBlockFirst) {
        config.setIsComponent(false);
        ConfigDetailsDialog dialog =
                new ConfigDetailsDialog(shell, title, currentSubTitle, config, false, configurationViewModels);
        dialog.open();
    }
}
