
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2019 Science & Technology Facilities Council.
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

/**
 *
 */
package uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.configuration.ComponentInfo;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.BlockDuplicateChecker;
import uk.ac.stfc.isis.ibex.configserver.editing.DuplicateChecker;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.IocDuplicateChecker;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationServerUI;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.EditConfigDialog;

/**
 * A helper class to open component editing dialog boxes.
 */
public class EditComponentHelper extends ConfigHelper {
	
	/**
	 * Configuration server.
	 */
	protected ConfigServer server;
	private static final String[] OK = {"OK"};

	/**
	 * The title of the dialog
	 */
	private static final String TITLE = "Edit Component";

	/**
	 * Constructor for the helper class.
	 *
	 * @param shell  The shell in which to display dialog boxes
	 * @param server The ConfigServer to save configurations to
	 */
	public EditComponentHelper(Shell shell, ConfigServer server) {
		this.shell = shell;
		this.server = server;
		this.configurationViewModels = ConfigurationServerUI.getDefault().configurationViewModels();
	}

	@Override
	public void openDialog(EditableConfiguration component, boolean isCurrent, boolean editBlockFirst) {
		openDialogComponent(component, editBlockFirst);
	}

	private void openDialogComponent(EditableConfiguration component, boolean editBlockFirst) {
		component.setIsComponent(true);
		final String subTitle = "Editing the " + getConfigDisplayName(component, false) + " component";
		EditConfigDialog editDialog = new EditConfigDialog(shell, TITLE, subTitle, component, false,
				configurationViewModels, editBlockFirst);
		if (editDialog.open() == Window.OK) {
			Map<String, Set<String>> blockConflicts = itemConflictsWithCurrent(new BlockDuplicateChecker(), component);
			Map<String, Set<String>> iocConflicts = itemConflictsWithCurrent(new IocDuplicateChecker(), component);

			if (blockConflicts.isEmpty() && iocConflicts.isEmpty()) {
				try {
					server.saveAsComponent().write(editDialog.getComponent());
				} catch (IOException e) {
					openErrorSavingDialog(e);
				}
			} else {
				openConflictsDialog(
						DisplayConfiguration.buildWarning(blockConflicts, iocConflicts, "save", "component"));
				openDialog(component, false, editBlockFirst);
			}
		}
	}

	private void openErrorSavingDialog(IOException e) {
		String title = "Error saving component";
		String description = "Unable to save component: " + e.getMessage();
		new MessageDialog(shell, title, null, description, MessageDialog.ERROR, OK, 0).open();
	}

	private void openConflictsDialog(String warning) {

		new MessageDialog(shell, "Conflicts with current configuration", null, warning, MessageDialog.WARNING, OK, 0)
				.open();
	}

	@Override
	public void createDialog(String componentName, boolean editBlockFirst) throws TimeoutException {
		openDialog(configurationViewModels.getComponent(componentName), false, editBlockFirst);
	}

	private Map<String, Set<String>> itemConflictsWithCurrent(DuplicateChecker<?> checker,
			EditableConfiguration editingComp) {
		Map<String, Set<String>> conflicts = new HashMap<String, Set<String>>();
		if (compInCurrent(editingComp)) {
			checker.setBase(server.currentConfig().getValue());
			conflicts = checker.checkItemsOnEdit(editingComp.asConfiguration());
		}
		return conflicts;
	}

	private boolean compInCurrent(EditableConfiguration compToSave) {
		for (ComponentInfo comp : server.currentConfig().getValue().getComponents()) {
			if (comp.getName().equals(compToSave.getName())) {
				return true;
			}
		}
		return false;
	}
}
