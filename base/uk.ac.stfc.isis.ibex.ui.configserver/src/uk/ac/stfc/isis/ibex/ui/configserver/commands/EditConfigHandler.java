
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

import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationServerUI;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.ConfigSelectionDialog;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.EditConfigDialog;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;

public class EditConfigHandler extends ConfigHandler<Configuration> {

	private static final String TITLE = "Edit Configuration";

	public EditConfigHandler() {
		super(SERVER.saveAs());
	}


	@Execute
	public Object execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell activeShell) {		
		ConfigSelectionDialog selectionDialog = new ConfigSelectionDialog(activeShell, TITLE, SERVER.configsInfo().getValue(), false);
		if (selectionDialog.open() == Window.OK) {
			String configName = selectionDialog.selectedConfig();
			edit(configName, activeShell);
		}
		
		return null;
	}
	
	private void edit(String configName, Shell activeShell) {
		String subTitle = "Editing " + configName;
		
        ConfigurationViewModels configurationViewModels = ConfigurationServerUI.getDefault().configurationViewModels();
        configurationViewModels.setModelAsConfig(configName);
        UpdatedValue<EditableConfiguration> config = configurationViewModels.getConfigModel();
		
		if (Awaited.returnedValue(config, 1)) {
            openDialog(subTitle, config.getValue(), configurationViewModels, activeShell);
		}
	}
	
    private void openDialog(String subTitle, EditableConfiguration config,
            ConfigurationViewModels configurationViewModels, Shell activeShell) {
        EditConfigDialog editDialog = new EditConfigDialog(activeShell, TITLE, subTitle, config, false, false,
                configurationViewModels);
        if (editDialog.open() == Window.OK) {
            if (editDialog.doAsComponent()) {
                SERVER.saveAsComponent().write(editDialog.getComponent());
            } else {
                SERVER.saveAs().write(editDialog.getConfig());
            }
        }
	}
}
