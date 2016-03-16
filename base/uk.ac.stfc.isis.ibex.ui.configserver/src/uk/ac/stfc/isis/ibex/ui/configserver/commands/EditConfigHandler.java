
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
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.ConfigSelectionDialog;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.EditConfigDialog;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.groups.GroupEditorViewModel;

public class EditConfigHandler extends ConfigHandler<Configuration> {

	private static final String TITLE = "Edit Configuration";

	public EditConfigHandler() {
		super(SERVER.saveAs());
	}

		
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
		ConfigSelectionDialog selectionDialog = new ConfigSelectionDialog(shell(), TITLE, SERVER.configsInfo().getValue(), false);
		if (selectionDialog.open() == Window.OK) {
			String configName = selectionDialog.selectedConfig();
			edit(configName);
		}
		
		return null;
	}
	
	private void edit(String configName) {
		String subTitle = "Editing " + configName;
		
        GroupEditorViewModel groupEditorViewModel = ConfigurationServerUI.getDefault().groupEditorViewModel();
        groupEditorViewModel.setModelAsConfig(configName);
        UpdatedValue<EditableConfiguration> config = groupEditorViewModel.getConfigModel();
		
		if (Awaited.returnedValue(config, 1)) {
            openDialog(subTitle, config.getValue(), groupEditorViewModel);
		}
	}
	
    private void openDialog(String subTitle, EditableConfiguration config, GroupEditorViewModel groupEditorViewModel) {
        EditConfigDialog editDialog = new EditConfigDialog(shell(), TITLE, subTitle, config, false, false,
                groupEditorViewModel);
        if (editDialog.open() == Window.OK) {
            if (editDialog.doAsComponent()) {
                SERVER.saveAsComponent().write(editDialog.getComponent());
            } else {
                SERVER.saveAs().write(editDialog.getConfig());
            }
        }
	}
}
