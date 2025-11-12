/**
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2025 Science & Technology Facilities Council.
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
package uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers;

import java.io.IOException;
import java.util.Optional;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.EditConfigDialog;

/**
 * A helper class to open block editing and configuration editing dialog boxes.
 *
 */
public class AddBlockToConfigHelper extends EditConfigHelper {

	/**
	 * Constructor for the helper class.
	 * 
	 * @param shell
	 * @param server
	 */
	public AddBlockToConfigHelper(Shell shell, ConfigServer server) {
		super(shell, server);

	}

	/**
	 * Create the dialogs to edit the block and edit the configuration.
	 * 
	 * @param config              Configuration selected by the user to add the
	 *                            block to
	 * @param isCurrent           If configuration is the current configuration
	 * @param editBlockFirst      Editing the block first before adding to
	 *                            configuration
	 * @param openEditBlockDialog Open edit block dialog
	 * @param pvName              PV that is added as block
	 * @throws IOException
	 */
	public void openDialogs(EditableConfiguration config, boolean isCurrent, boolean editBlockFirst,
			boolean openEditBlockDialog, String pvName) throws IOException {
		config.setIsComponent(false);
		final String configName = getConfigDisplayName(config, isCurrent);
		String subTitle = "Editing the " + configName + " configuration";
		if (Optional.ofNullable(config.getGlobalmacros()).map(l -> l.isEmpty()).orElse(true)) {
			subTitle += "\n\nNote: There also are global macros defined. See the Global Macros tab.\\n\\nThey over-ride IOC level macro";			
		}
		EditConfigDialog dialog = new EditConfigDialog(shell, title, subTitle, config, false, configurationViewModels,
				editBlockFirst, openEditBlockDialog, pvName); // Creating dialog to edit configuration
		dialog.addNew(config);  // Open edit block dialog before editing the configuration
		if (dialog.open() == Window.OK) {  // Edit configuration dialog

			server.saveAs().write(config.asConfiguration()); // Saving the block to the configuration

		}
	}

}
