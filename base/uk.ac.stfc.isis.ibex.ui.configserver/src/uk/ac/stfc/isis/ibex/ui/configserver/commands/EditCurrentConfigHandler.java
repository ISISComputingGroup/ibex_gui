
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

import java.util.List;

import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.ui.configserver.BundleConstants;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.ConfigHelper;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.EditConfigHelper;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.ViewConfigHelper;

/**
 * The handler class for editing the current config. 
 * 
 * It sets the menu labels, and opens the dialogue for editing or viewing a configuration.
 */
public class EditCurrentConfigHandler extends ConfigHandler<Configuration> {

    private static final String EDIT_MENU_TEXT = "Edit Current Configuration...";
    private static final String READ_ONLY_TEXT = "View Current Configuration...";
    private static final String MENU_MNEMONIC = "C";

    private boolean canWrite;

	/**
	 * This is an inner anonymous class will disable the menu item if the current config is not available.
	 */
    private Observer<Configuration> currentConfigObserver = new BaseObserver<Configuration>() {
		
		@Override
		public void onConnectionStatus(boolean isConnected) {
			canViewOrEditConfig(isConnected);
		}

		@Override
		public void onError(Exception e) {
			canViewOrEditConfig(false);
		}
		
		private void canViewOrEditConfig(boolean isConnected) {
			setCanExecute(isConnected);
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
	 * Execute the handler to open the edit/view config dialogue.
	 *
	 * @param shell the shell to user
	 */
	@Override
    public void safeExecute(Shell shell) {
        ConfigHelper helper;
        if (canWrite)  {
        	helper = new EditConfigHelper(shell, SERVER);
        } else {
        	helper = new ViewConfigHelper(shell);
        }
        helper.createDialogCurrent();
	}

	/**
	 * Generate the menu item as the menu is about to be shown.
	 * 
	 * It must be dynamic because the menu has a different label depending on the state of the config pv.
	 * @param items menu items to add to
	 */
	@AboutToShow
	public void aboutToShow(List<MMenuElement> items) {
		String menuText;
		if (canWrite) {
			menuText = EDIT_MENU_TEXT;
		} else {
			menuText = READ_ONLY_TEXT;
		}
				
		MDirectMenuItem dynamicItem = MMenuFactory.INSTANCE.createDirectMenuItem();
		
	    dynamicItem.setLabel(menuText);
	    dynamicItem.setMnemonics(MENU_MNEMONIC);
	    dynamicItem.setContributorURI(BundleConstants.getPlatformPlugin()); // plugin in which this menu item exists
	    dynamicItem.setContributionURI(BundleConstants.getClassURI(EditCurrentConfigHandler.class));    
	    items.add(dynamicItem);	
	}

	@Override
	public void canWriteChanged(final boolean canWrite) {
		this.canWrite = canWrite;
	}
}
