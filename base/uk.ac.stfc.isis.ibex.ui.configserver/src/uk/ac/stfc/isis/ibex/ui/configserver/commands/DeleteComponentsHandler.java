
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MultipleConfigsSelectionDialog;

/**
 * Handles the selection of the delete components menu item.
 */
public class DeleteComponentsHandler extends DisablingConfigHandler<Collection<String>> {
	
    /**
     * The constructor.
     */
	public DeleteComponentsHandler() {
		super(SERVER.deleteComponents());
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
        MultipleConfigsSelectionDialog dialog = new MultipleConfigsSelectionDialog(shell(), "Delete Components",
                SERVER.componentsInfo().getValue(), true, false);
		if (dialog.open() == Window.OK) {
            Collection<String> toDelete = dialog.selectedConfigs();
            Map<String, Collection<String>> dependencies = getDependencies(toDelete);
            if (dependencies.size() == 0) {
                configService.write(toDelete);
            } else {
                // TODO create dialog showing conflicts
            }
		}
		
		return null;
	}

    private Map<String, Collection<String>> getDependencies(Collection<String> names) {
        Map<String, Collection<String>> result = new HashMap<String, Collection<String>>();
        for (String name : names) {
            String pv = getCompPV(name);
            Collection<String> dependencies = SERVER.dependencies(pv).getValue();
            result.put(name, dependencies);
        }
        return result;
    }

    private String getCompPV(String name) {
        for (ConfigInfo comp : SERVER.componentsInfo().getValue()) {
            if (comp.name().equals(name)) {
                return comp.pv();
            }
        }
        return "";
    }
}
