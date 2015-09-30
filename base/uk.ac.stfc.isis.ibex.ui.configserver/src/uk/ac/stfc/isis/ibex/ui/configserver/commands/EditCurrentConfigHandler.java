
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
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.EditConfigDialog;

public class EditCurrentConfigHandler extends ConfigHandler<Configuration> {
	
    private static final String TITLE = "Edit Configuration";
	private static final String SUB_TITLE = "Editing the current configuration";
	
	private EditConfigDialog dialog;
	
	public EditCurrentConfigHandler() {
		super(SERVER.setCurrentConfig());
	}

	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
		UpdatedValue<EditableConfiguration> config = new UpdatedObservableAdapter<>(EDITING.currentConfig());
		
		if (Awaited.returnedValue(config, 1)) {
			openDialog(config.getValue());			
		}
				
		return null;
	}
	
	private void openDialog(EditableConfiguration config) {
		dialog = new EditConfigDialog(shell(), TITLE, SUB_TITLE, config, false, false);	
		if (dialog.open() == Window.OK) {
			if (dialog.doAsComponent()) {
				SERVER.saveAsComponent().write(dialog.getComponent());
			} else {
				SERVER.setCurrentConfig().write(dialog.getConfig());
				SERVER.save().write(dialog.getConfig().name());
			}
		}
	}
}
