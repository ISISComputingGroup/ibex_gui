
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
import java.util.Collection;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.managermode.ManagerModeModel;
import uk.ac.stfc.isis.ibex.managermode.ManagerModePvNotConnectedException;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.DeleteItemsDialogHelper;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MultipleConfigsSelectionDialog;

/**
 * Handles the selection of the delete configurations menu item.
 */
public class DeleteConfigsHandler extends DisablingConfigHandler<Collection<String>> {
		
    /**
     * The constructor.
     */
	public DeleteConfigsHandler() {
		super(SERVER.deleteConfigs());
	}
	
	/**
	 * Open the delete configs dialogue.
	 *
	 * @param shell the shell
	 */
	@Override
	public void safeExecute(Shell shell) {
	    Map<String, Boolean> configNamesWithFlags = SERVER.configNamesWithFlags();
        MultipleConfigsSelectionDialog dialog = new MultipleConfigsSelectionDialog(shell, "Delete Configurations",
                SERVER.configsInfo().getValue(), configNamesWithFlags, false, false);
		if (dialog.open() == Window.OK) {
	        DeleteItemsDialogHelper helper = new DeleteItemsDialogHelper();
	    	if (helper.deleteItemsConfirmDialog(dialog.selectedConfigs(), "Configurations")) {
			    try {		        
			        configService.write(dialog.selectedConfigs());
		            boolean noError = true;
			    	if (!ManagerModeModel.getInstance().isInManagerMode()) {
			    		for (String item: dialog.selectedConfigs()) {
			    			if (configNamesWithFlags.get(item)) {
			    				noError = false;
			    				displayErrorDialog(shell);
			    				break;
			    			}
			    		}
			    	}
					// Delete selected configs from recently loaded config lists if in Manager mode 
					if (noError) {	
						for (String item : dialog.selectedConfigs()) {
							Configurations.getInstance().removeNameFromRecentlyLoadedConfigList(item);
						}
					}
						
			    } catch (ManagerModePvNotConnectedException | IOException e) {
					MessageDialog error = new MessageDialog(shell, "Error", null, e.getMessage(),
							MessageDialog.ERROR, new String[] {"OK"}, 0);
					error.open();
				}
	    	}
		}
	}
	
	/**
	 * opens and error dialog if there is an error
	 * @param shell the shell
	 */
	private void displayErrorDialog(Shell shell) {
	    MessageBox errorMessage = new MessageBox(shell, SWT.ICON_ERROR);
	    errorMessage = new MessageBox(shell, SWT.ICON_ERROR);
        errorMessage.setMessage("Cannot delete the selected configurations, make sure protected "
                + "configurations are not selected or try again in Manager mode to delete it");
        errorMessage.setText("ERROR");
        errorMessage.open();
	    
	}
}
