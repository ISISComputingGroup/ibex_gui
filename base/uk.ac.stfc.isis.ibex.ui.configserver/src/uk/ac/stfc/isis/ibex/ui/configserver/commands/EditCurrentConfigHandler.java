
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

import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.menus.UIElement;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.ConfigHelper;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.EditConfigHelper;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.ViewConfigHelper;

/**
 * The handler class for editing the current config.
 */
public class EditCurrentConfigHandler extends ConfigHandler<Configuration> implements IElementUpdater {

    private static final String EDIT_MENU_TEXT = "Edit Current Configuration...";
    private static final String READ_ONLY_TEXT = "View Current Configuration...";
    
    private static final String ID = "uk.ac.stfc.isis.ibex.ui.configserver.commands.EditCurrentConfig";
    
    private Boolean canWrite = false;

	/**
	 * This is an inner anonymous class will disable the menu item if the current config is not available.
	 */
    private Observer<Configuration> currentConfigObserver = new BaseObserver<Configuration>() {
		
		@Override
		public void onConnectionStatus(boolean isConnected) {
			updateDisabled(isConnected);
		}

		@Override
        public void onValue(Configuration value) {

        }

		@Override
		public void onError(Exception e) {
			updateDisabled(false);
		}
		
		private void updateDisabled(boolean isConnected) {
			if (!(canWrite)) {
				setBaseEnabled(isConnected);
			}
		}
	};
	
    /**
     * Create the handler for opening the editor via the menu.
     */
	public EditCurrentConfigHandler() {
		super(SERVER.setCurrentConfig());
		SERVER.currentConfig().addObserver(currentConfigObserver);
	}

    /**
     * {@inheritDoc}
     */
	@Override
    public void safeExecute(ExecutionEvent event) {
        ConfigHelper helper = canWrite ? new EditConfigHelper(shell(), SERVER) : new ViewConfigHelper(shell());
        helper.createDialogCurrent();
	}


	@SuppressWarnings("rawtypes")
	@Override
	public void updateElement(UIElement element, Map parameters) {
		// Added so as to update text for viewing/editing configs
		if (canWrite) {
			element.setText(EDIT_MENU_TEXT);
			setBaseEnabled(true);
		} else {
			element.setText(READ_ONLY_TEXT);
			setBaseEnabled(SERVER.currentConfig().isConnected());
		}
	}

	@Override
	public void canWriteChanged(final boolean canWrite) {
		// Update the element
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
                ICommandService commandService = (ICommandService) activeWindow().getService(ICommandService.class);
				if (commandService != null) {
					setCanWrite(canWrite);
					commandService.refreshElements(ID, null);
				}
			}
		});
	}
	
    /**
     * Handles the setting of "can write".
     * 
     * @param canWrite True for can write
     */
	protected void setCanWrite(Boolean canWrite) {
		if (this.canWrite != canWrite) {
			this.canWrite = canWrite;
		}
	}
}
