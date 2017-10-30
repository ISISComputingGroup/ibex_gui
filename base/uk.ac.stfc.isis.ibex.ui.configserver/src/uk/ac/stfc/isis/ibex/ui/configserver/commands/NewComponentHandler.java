
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationServerUI;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.EditConfigDialog;

/**
 * Handles the selection of the new component menu item.
 */
public class NewComponentHandler extends DisablingConfigHandler<Configuration> {

    private static final String TITLE = "New Component";
	private static final String SUB_TITLE = "Editing a new component";

    /**
     * The constructor.
     */
	public NewComponentHandler() {
		super(SERVER.saveAs());
	}
	
	/**
	 * Open a new component dialogue.
	 * 
	 * @param shell shell to use
	 */
	@Override
	public void safeExecute(Shell shell) {
        ConfigurationViewModels configurationViewModels = ConfigurationServerUI.getDefault().configurationViewModels();
        configurationViewModels.setModelAsBlankConfig();
        UpdatedValue<EditableConfiguration> config = configurationViewModels.getConfigModel();

		if (Awaited.returnedValue(config, 1)) {
            openDialog(shell, config.getValue(), configurationViewModels);
		}
	}
	
    private void openDialog(Shell shell, EditableConfiguration config, ConfigurationViewModels configurationViewModels) {
        config.setIsComponent(true);
        EditConfigDialog editDialog =
                new EditConfigDialog(shell, TITLE, SUB_TITLE, config, true, configurationViewModels, false);
        if (editDialog.open() == Window.OK) {
            if (editDialog.doAsComponent()) {
                SERVER.saveAsComponent().uncheckedWrite(editDialog.getComponent());
            } else {
                SERVER.saveAs().uncheckedWrite(editDialog.getConfig());
            }
        }
	}
}
