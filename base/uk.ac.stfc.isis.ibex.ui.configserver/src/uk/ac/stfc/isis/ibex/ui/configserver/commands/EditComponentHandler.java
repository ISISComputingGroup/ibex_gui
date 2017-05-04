
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.configserver.configuration.ComponentInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.DuplicateChecker;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationServerUI;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.ConfigSelectionDialog;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.EditConfigDialog;

/**
 * Handles the selection of the edit component menu item.
 */
public class EditComponentHandler extends DisablingConfigHandler<Configuration> {
	private static final String TITLE = "Edit Component";
	
    /**
     * The constructor.
     */
	public EditComponentHandler() {
		super(SERVER.saveAsComponent());
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        ConfigSelectionDialog selectionDialog =
                new ConfigSelectionDialog(shell(), TITLE, SERVER.componentsInfo().getValue(), true, true);
		if (selectionDialog.open() == Window.OK) {
			String componentName = selectionDialog.selectedConfig();
			edit(componentName);
		}
		
		return null;
	}
		
	private void edit(String componentName) {
		String subTitle = "Editing " + componentName; 
		
        ConfigurationViewModels configurationViewModels = ConfigurationServerUI.getDefault().configurationViewModels();
        configurationViewModels.setModelAsComponent(componentName);
        UpdatedValue<EditableConfiguration> config = configurationViewModels.getConfigModel();

		if (Awaited.<EditableConfiguration>returnedValue(config, 1)) {
            openDialog(subTitle, config.getValue(), configurationViewModels);
		}
	}

    private void openDialog(String subTitle, EditableConfiguration config,
            ConfigurationViewModels configurationViewModels) {
        config.setIsComponent(true);
        EditConfigDialog editDialog =
                new EditConfigDialog(shell(), TITLE, subTitle, config, false, configurationViewModels);
        if (editDialog.open() == Window.OK) {
            Map<String, Set<String>> conflicts = conflictsWithCurrent(config);
            if (conflicts.isEmpty()) {
                try {
                    configService.write(editDialog.getComponent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                new MessageDialog(shell(), "Conflicts with current configuration", null, buildWarning(conflicts),
                        MessageDialog.WARNING, new String[] {"Ok"}, 0).open();
                openDialog(subTitle, config, configurationViewModels);
            }
        }
	}

    private Map<String, Set<String>> conflictsWithCurrent(EditableConfiguration editingComp) {
        Map<String, Set<String>> conflicts = new HashMap<String, Set<String>>();
        if (compInCurrent(editingComp)) {
            DuplicateChecker duplicateChecker = new DuplicateChecker();
            duplicateChecker.setBase(SERVER.currentConfig().getValue());
            conflicts = duplicateChecker.checkOnEdit(editingComp.asConfiguration());
        }
        return conflicts;

    }

    private boolean compInCurrent(EditableConfiguration compToSave) {
        for (ComponentInfo comp : SERVER.currentConfig().getValue().getComponents()) {
            if (comp.getName().equals(compToSave.getName())) {
                return true;
            }
        }
        return false;
    }

    private String buildWarning(Map<String, Set<String>> conflicts) {
        boolean multi = (conflicts.size() > 1);
        StringBuilder sb = new StringBuilder();
        sb.append("Cannot save this component as it is used in the current configuration and would result in duplicate blocks. "
                        + "Conflicts detected for the following block" + (multi ? "s" : "") + ":\n\n");

        for (String block : conflicts.keySet()) {
            sb.append("Block \"" + block + "\" contained in:\n");
            Set<String> sources = conflicts.get(block);
            for (String source : sources) {
                sb.append("\u2022 " + source + "\n");
            }
            sb.append("\n");
        }
        sb.append("Please rename or remove the duplicate block" + (multi ? "s" : "") + " before saving this component.");
        return sb.toString();
    }
}
