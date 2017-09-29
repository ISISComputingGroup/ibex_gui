
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
import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.EditComponentHelper;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.ConfigSelectionDialog;

/**
 * Handles the selection of the edit component menu item.
 */
public class EditComponentHandler extends DisablingConfigHandler<Configuration> {

    /**
     * The title of the dialog.
     */
    private static final String TITLE = "Edit Component";
	
    /**
     * The constructor.
     */
	public EditComponentHandler() {
		super(SERVER.saveAsComponent());
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
    public void safeExecute(ExecutionEvent event) {
        ConfigSelectionDialog selectionDialog =
                new ConfigSelectionDialog(shell(), TITLE, SERVER.componentsInfo().getValue(), true, true);
        if (selectionDialog.open() == Window.OK) {
            (new EditComponentHelper(shell(), SERVER)).createDialog(selectionDialog.selectedConfig(), false);
        }
    }
}
