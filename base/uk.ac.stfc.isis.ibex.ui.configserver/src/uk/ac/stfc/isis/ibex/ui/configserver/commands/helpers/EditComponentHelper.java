
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.configuration.ComponentInfo;
import uk.ac.stfc.isis.ibex.configserver.editing.DuplicateChecker;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationServerUI;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.EditConfigDialog;

/**
 * A helper class to open component editing dialog boxes.
 */
public class EditComponentHelper extends ConfigHelper {

    private ConfigServer server;

    /**
     * The title of the dialog
     */
    private static final String TITLE = "Edit Component";

    /**
     * Constructor for the helper class.
     * 
     * @param shell
     *            The shell in which to display dialog boxes
     * @param server
     *            The ConfigServer to save configurations to
     */
    public EditComponentHelper(Shell shell, ConfigServer server) {
        this.shell = shell;
        this.server = server;
        this.configurationViewModels = ConfigurationServerUI.getDefault().configurationViewModels();
    }

    @Override
    protected void openDialog(String subTitle, EditableConfiguration component, boolean isCurrent,
            boolean editBlockFirst) {
        openDialog(subTitle, component, editBlockFirst);
    }

    private void openDialog(String subTitle, EditableConfiguration component, boolean editBlockFirst) {
        component.setIsComponent(true);
        EditConfigDialog editDialog =
                new EditConfigDialog(shell, TITLE, subTitle, component, false,
                        configurationViewModels, editBlockFirst);
        if (editDialog.open() == Window.OK) {
            Map<String, Set<String>> conflicts = conflictsWithCurrent(component);
            if (conflicts.isEmpty()) {
                try {
                    server.saveAsComponent().write(editDialog.getComponent());
                } catch (IOException e) {
                    openErrorSavingDialog(e);
                }
            } else {
                openConflictsDialog(buildWarning(conflicts));
                openDialog(subTitle, component, false, editBlockFirst);
            }
        }
    }

    private void openErrorSavingDialog(IOException e) {
        String title = "Error saving component";
        String description = "Unable to save component: " + e.getMessage();
        String[] button = new String[] { "OK" };
        new MessageDialog(shell, title, null, description, MessageDialog.ERROR, button, 0).open();
    }

    private void openConflictsDialog(String warning) {
        new MessageDialog(shell, "Conflicts with current configuration", null, warning,
                MessageDialog.WARNING, new String[] { "Ok" }, 0).open();
    }

    @Override
    public void createDialog(String componentName, boolean editBlockFirst) {
        String subTitle = "Editing component " + componentName;

        configurationViewModels.setModelAsComponent(componentName);
        UpdatedValue<EditableConfiguration> component = configurationViewModels.getConfigModel();

        if (Awaited.returnedValue(component, 1)) {
            openDialog(subTitle, component.getValue(), false, editBlockFirst);
        }
    }

    private Map<String, Set<String>> conflictsWithCurrent(EditableConfiguration editingComp) {
        Map<String, Set<String>> conflicts = new HashMap<String, Set<String>>();
        if (compInCurrent(editingComp)) {
            DuplicateChecker duplicateChecker = new DuplicateChecker();
            duplicateChecker.setBase(server.currentConfig().getValue());
            conflicts = duplicateChecker.checkOnEdit(editingComp.asConfiguration());
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

    private String buildWarning(Map<String, Set<String>> conflicts) {
        boolean multi = (conflicts.size() > 1);
        StringBuilder sb = new StringBuilder();
        sb.append(
                "Cannot save this component as it is used in the current configuration and would result in duplicate blocks. "
                        + "Conflicts detected for the following block" + (multi ? "s" : "") + ":\n\n");

        for (String block : conflicts.keySet()) {
            sb.append("Block \"" + block + "\" contained in:\n");
            Set<String> sources = conflicts.get(block);
            for (String source : sources) {
                sb.append("\u2022 " + source + "\n");
            }
            sb.append("\n");
        }
        sb.append(
                "Please rename or remove the duplicate block" + (multi ? "s" : "") + " before saving this component.");
        return sb.toString();
    }
}
