
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

package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.BlockDuplicateChecker;
import uk.ac.stfc.isis.ibex.configserver.editing.IocDuplicateChecker;
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
	
	/**
	 * Show the load configuration dialogue and if it is conflict free load it into the instrument.
	 * 
	 * @param shell the shell
	 */
	@Override
	public void safeExecute(Shell shell) {
        updateObservers();
        
        ConfigSelectionDialog dialog =
                new ConfigSelectionDialog(shell, "Load Configuration", SERVER.configsInfo().getValue(), 
                        SERVER.configNamesWithFlags(), false, false);
		if (dialog.open() == Window.OK) {
            String config = dialog.selectedConfig();
            
            Map<String, Set<String>> blockConflicts = getItemConflicts(new BlockDuplicateChecker(), configs.get(config));
            Map<String, Set<String>> iocConflicts = getItemConflicts(new IocDuplicateChecker(), configs.get(config));
            
            if (blockConflicts.isEmpty() && iocConflicts.isEmpty()) {
                configService.uncheckedWrite(config);
                Configurations.getInstance().addNameToRecentlyLoadedConfigList(config);
            } else {
                new MessageDialog(shell, "Conflicts in selected configuration", null,
                        DisplayConfiguration.buildWarning(blockConflicts, iocConflicts, "load", "configuration"),
                        MessageDialog.WARNING, new String[] {"Ok"}, 0).open();
                execute(shell);
            }
		}
	}

    private void updateObservers() {
        for (String name : SERVER.configNames()) {
            if (!configs.containsKey(name)) {
                ForwardingObservable<Configuration> configObs = SERVER.config(name);
                configObs.subscribe(new BaseObserver<Configuration>() {
                    @Override
                    public void onValue(Configuration value) {
                        configs.put(value.getName(), value);
                    }
                });
            }
        }
    }
}
