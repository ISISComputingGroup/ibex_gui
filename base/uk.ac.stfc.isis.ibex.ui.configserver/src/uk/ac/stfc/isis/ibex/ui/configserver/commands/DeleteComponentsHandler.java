
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2017 Science & Technology Facilities Council.
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

import java.util.Collection;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.ui.configserver.DeleteComponentsViewModel;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.DeleteComponentsDialog;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MultipleConfigsSelectionDialog;

/**
 * Handles the selection of the delete components menu item.
 */
public class DeleteComponentsHandler extends DisablingConfigHandler<Collection<String>> {
    
    private DeleteComponentsViewModel viewModel;

    /**
     * Instantiates the class and adds relevant observers.
     */
	public DeleteComponentsHandler() {
		super(SERVER.deleteComponents());
	}

    @Override
    public void safeExecute(ExecutionEvent event) {
        viewModel = new DeleteComponentsViewModel(SERVER.getDependenciesModel().getDependencies());
        MultipleConfigsSelectionDialog dialog = new DeleteComponentsDialog(shell(), 
                SERVER.componentsInfo().getValue(), viewModel.getDependencies().keySet());
        
        if (dialog.open() == Window.OK) {
            Collection<String> toDelete = dialog.selectedConfigs();
            Map<String, Collection<String>> selectedDependencies = viewModel.filterSelected(toDelete);

            if (selectedDependencies.isEmpty()) {
                configService.uncheckedWrite(toDelete);
            } else {
                displayWarning(selectedDependencies);
                safeExecute(event); // Re-open selection dialog.
            }
        }
    }
    
    private void displayWarning(Map<String, Collection<String>> selectedDependencies) {
        String message = viewModel.buildWarning(selectedDependencies);
        new MessageDialog(shell(), "Component in Use", null, message, MessageDialog.WARNING, new String[] {"Ok"}, 0)
                .open();
    }



}
