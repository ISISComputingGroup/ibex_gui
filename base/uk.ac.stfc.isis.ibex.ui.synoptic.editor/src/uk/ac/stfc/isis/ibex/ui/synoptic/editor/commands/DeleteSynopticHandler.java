
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs.MultipleSynopticsSelectionDialog;

/**
 * Handler class for deleting synoptics.
 */
public class DeleteSynopticHandler extends SynopticEditorHandler {

    private static final String TITLE = "Delete Synoptics";

    /**
     * Constructor that adds a listener to disable the handler if the
     * destination is disabled.
     */
    public DeleteSynopticHandler() {
        synopticService.subscribe(SYNOPTIC.delete());
        SYNOPTIC.delete().subscribe(synopticService);
    }

    private boolean deleteConfigSynopticConfirmDialog(Collection<String> inUseSynoptics,
            Collection<String> configsUsingSynoptics) {
        return MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Confirm Delete Synoptics",
                "The following synoptics, " + inUseSynoptics + ", are respectively used in the configurations: "
                        + configsUsingSynoptics + ". Are you sure you want to delete them?");
    }

    /**
     * Runs dialog and tries to delete selected synoptic.
     *
     * @param shell the shell to execute this action on.
     *
     * @throws ExecutionException thrown when it can't find/write to PV.
     *
     * @return null.
     */
    @Execute
    public Object execute(Shell shell) throws ExecutionException {

        MultipleSynopticsSelectionDialog dialog = new MultipleSynopticsSelectionDialog(shell, TITLE, SYNOPTIC.availableEditableSynoptics());

		if (dialog.open() == Window.OK) {
            ConfigServer server = Configurations.getInstance().server();
            Collection<ConfigInfo> existingConfigs = server.configsInfo().getValue();
            Collection<String> configsUsingSynoptic = new ArrayList<String>();
            Collection<String> inUseSynoptics = new ArrayList<String>();
            for (String selectedSynoptic : dialog.selectedSynoptics()) {
                for (ConfigInfo existingConfig : existingConfigs) {
                    String existingConfigSynoptic = existingConfig.synoptic();
                    if (existingConfigSynoptic.equals(selectedSynoptic)) {
                        configsUsingSynoptic.add(existingConfig.name());
                        inUseSynoptics.add(selectedSynoptic);
                    }
                }
            }
            if (!configsUsingSynoptic.isEmpty()) {
                if (deleteConfigSynopticConfirmDialog(inUseSynoptics, configsUsingSynoptic)) {
			try {
                SYNOPTIC.delete().write(dialog.selectedSynoptics());
            } catch (IOException e) {
                throw new ExecutionException("Failed to write to PV", e);
            }
		}
            } else {
                try {
                    synopticService.write(dialog.selectedSynoptics());
                } catch (IOException e) {
                    throw new ExecutionException("Failed to write to PV", e);
                }
            }

        }
		return null;
	}
}
