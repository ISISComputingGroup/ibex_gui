
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

package uk.ac.stfc.isis.ibex.ui.runcontrol.commands;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlActivator;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;
import uk.ac.stfc.isis.ibex.ui.runcontrol.dialogs.EditRunControlDialog;

/**
 * Opens the edit run control dialog box.
 */
public class RunControlHandler {

	private static final String TITLE = "Run-Control Settings";
	private static final ConfigServer CONFIGSERVER = Configurations.getInstance().server();
	
	/**
	 * Opens the edit run control dialog box.
	 * @param shell The shell to open the dialog box from.
	 */
	@Execute
	public void execute(Shell shell) {
		RunControlServer rcServer = RunControlActivator.getInstance().getServer();
		EditRunControlDialog dialog = new EditRunControlDialog(shell, TITLE, CONFIGSERVER, rcServer);
		dialog.open();
	}
	
	/**
	 * Can only be executed if the blockserver is running.
	 * @return True if we can read the current configuration.
	 */
	@CanExecute
	public boolean canExecute() {
		return CONFIGSERVER.currentConfig().isConnected();
	}
}
