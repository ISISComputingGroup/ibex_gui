
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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationServerUI;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.EditConfigDialog;

/**
 * Handles the selection of the new configuration menu item.
 */
public class NewConfigHandler extends DisablingConfigHandler<Configuration> {

    private static final String TITLE = "New Configuration";
	private static final String SUB_TITLE = "Editing a new configuration";

    /**
     * The constructor.
     */
	public NewConfigHandler() {
		super(SERVER.saveAs());
	}

	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        ConfigurationViewModels configurationViewModels = ConfigurationServerUI.getDefault().configurationViewModels();
        configurationViewModels.setModelAsBlankConfig();
        UpdatedValue<EditableConfiguration> config = configurationViewModels.getConfigModel();

		if (Awaited.returnedValue(config, 1)) {
            openDialog(config.getValue(), configurationViewModels);
		}
		
		return null;
	}
	
    private void openDialog(EditableConfiguration config, ConfigurationViewModels configurationViewModels) {
        config.setIsComponent(false);
        EditConfigDialog editDialog =
                new EditConfigDialog(shell(), TITLE, SUB_TITLE, config, true, configurationViewModels, null);
        if (editDialog.open() == Window.OK) {
            if (editDialog.doAsComponent()) {
                SERVER.saveAsComponent().uncheckedWrite(editDialog.getComponent());
            } else {
                SERVER.saveAs().uncheckedWrite(editDialog.getConfig());
            }
        }
	}
}
