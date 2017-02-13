
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
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MultipleConfigsSelectionDialog;

/**
 * Handles the selection of the delete components menu item.
 */
public class DeleteComponentsHandler extends DisablingConfigHandler<Collection<String>> {
    
    private Map<String, Collection<String>> dependencies = new HashMap<>();
	
    /**
     * The constructor.
     */
	public DeleteComponentsHandler() {
		super(SERVER.deleteComponents());
        createObservers(SERVER.componentsInfo().getValue());
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
        MultipleConfigsSelectionDialog dialog = new MultipleConfigsSelectionDialog(shell(), "Delete Components",
                SERVER.componentsInfo().getValue(), true, false);
		if (dialog.open() == Window.OK) {
            Collection<String> toDelete = dialog.selectedConfigs();
            Map<String, Collection<String>> selected = filterDependencies(toDelete);
            System.out.println(selected);
            if (selected.size() > 0) {
                // TODO pop up dialog
                System.out.println("nope: " + selected);
            } else {
                // configService.write(toDelete);
                System.out.println("deleting: " + toDelete);
            }
		}
		
		return null;
	}

    /**
     * Creates observers for each component's dependencies PV and stores values
     * in a local hashmap.
     * 
     * @param components
     *            The available components.
     */
    private void createObservers(Collection<ConfigInfo> components) {
        Collection<String> names = ConfigInfo.names(components);
        for (final String name : names) {
            String pv = getCompPV(name);
            if (pv == null) {
                continue; // TODO throw exception
            }
            SERVER.dependencies(pv).addObserver(new BaseObserver<Collection<String>>() {
                
                @Override
                public void onValue(java.util.Collection<String> value) {
                    dependencies.put(name, value);
                };
            });
        }
    }

    /**
     * Finds the dynamic blockserver PV of a given component based on its name.
     * 
     * @param name
     *            The name of the component.
     * @return The PV associated to the component.
     */
    private String getCompPV(String name) {
        for (ConfigInfo comp : SERVER.componentsInfo().getValue()) {
            if (comp.name().equals(name)) {
                return comp.pv();
            }
        }
        return null;
    }

    /**
     * Filter to return only non-empty dependencies of selected components.
     * 
     * @param selected
     *            The list of selected components to delete.
     * @return A map containing dependencies for selected components only.
     */
    private Map<String, Collection<String>> filterDependencies(Collection<String> selected) {
        Map<String, Collection<String>> result = new HashMap<String, Collection<String>>();
        for (String key : dependencies.keySet()) {
            Collection<String> value = dependencies.get(key);
            if (selected.contains(key) && (value.size() > 0)) {
                    result.put(key, dependencies.get(key));
            }
        }
        return result;
    }
}
