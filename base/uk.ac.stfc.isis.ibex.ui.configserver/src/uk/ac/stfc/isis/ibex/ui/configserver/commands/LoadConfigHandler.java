
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.DuplicateChecker;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.ConfigSelectionDialog;

/**
 * Handler for loading configurations.
 */
public class LoadConfigHandler extends DisablingConfigHandler<String> {

    private Map<String, Configuration> configs;
			
    /**
     * Instantiates the handler object and adds observers on the values of all
     * configurations available.
     */
	public LoadConfigHandler() {
		super(SERVER.load());
        configs = new HashMap<String, Configuration>();
	}	
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
        updateObservers();
        ConfigSelectionDialog dialog =
                new ConfigSelectionDialog(shell(), "Load Configuration", SERVER.configsInfo().getValue(), false, false);
		if (dialog.open() == Window.OK) {
            String config = dialog.selectedConfig();
            Map<String, Set<String>> conflicts = getConflicts(config);
            if (conflicts.isEmpty()) {
                configService.uncheckedWrite(config);
            } else {
                new MessageDialog(shell(), "Conflicts in selected configuration", null, buildWarning(conflicts),
                        MessageDialog.WARNING, new String[] {"Ok"}, 0).open();
                execute(event);
            }
		}
		return null;
	}

    private void updateObservers() {
        for (String name : SERVER.configNames()) {
            if (!configs.containsKey(name)) {
                ForwardingObservable<Configuration> configObs = SERVER.config(name);
                configObs.addObserver(new BaseObserver<Configuration>() {
                    @Override
                    public void onValue(Configuration value) {
                        configs.put(value.getName(), value);
                    }
                });
            }
        }
    }

    private Map<String, Set<String>> getConflicts(String name) {
        Configuration config = configs.get(name);
        DuplicateChecker duplicateChecker = new DuplicateChecker();
        duplicateChecker.setBase(config);
        return duplicateChecker.checkOnLoad();
    }

    private String buildWarning(Map<String, Set<String>> conflicts) {
        boolean multi = (conflicts.size() > 1);
        StringBuilder sb = new StringBuilder();
        sb.append("Cannot load the selected configuration as it and its components contains duplicate blocks. "
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
                "Please rename or remove the duplicate block" + (multi ? "s" : "")
                        + " before loading this configuration.");
        return sb.toString();
    }
}
