
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2017 Science & Technology Facilities Council.
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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.DeleteComponentsDialog;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MultipleConfigsSelectionDialog;

/**
 * Handles the selection of the delete components menu item.
 */
public class DeleteComponentsHandler extends DisablingConfigHandler<Collection<String>> {
    
    private Map<String, ForwardingObservable<Collection<String>>> observerRegister = new HashMap<>();
    private Map<String, Collection<String>> dependencies = new HashMap<>();

    Observer<Collection<ConfigInfo>> componentAdapter = new BaseObserver<Collection<ConfigInfo>>() {

        @Override
        public void onValue(java.util.Collection<ConfigInfo> value) {
            updateObservers(value);
        };
    };

    /**
     * Instantiates the class and adds relevant observers.
     */
	public DeleteComponentsHandler() {
		super(SERVER.deleteComponents());
        SERVER.componentsInfo().addObserver(componentAdapter);
	}

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        MultipleConfigsSelectionDialog dialog =
                new DeleteComponentsDialog(shell(), SERVER.componentsInfo().getValue(), dependencies.keySet());
        
        if (dialog.open() == Window.OK) {
            Collection<String> toDelete = dialog.selectedConfigs();
            Map<String, Collection<String>> selectedDependencies = filterSelected(toDelete);
            if (selectedDependencies.size() == 0) {
                configService.write(toDelete);
            } else {
                String message = buildWarning(selectedDependencies);
                new MessageDialog(shell(), "Component in Use", null, message, MessageDialog.WARNING,
                        new String[] {"Ok"}, 0).open();
                // Return to selection dialog
                execute(event);
            }
        }
        return null;
    }

    /**
     * Updates the observers on component dependencies when components change.
     * 
     * @param updatedComponents The current list of components
     */
    private void updateObservers(Collection<ConfigInfo> updatedComponents) {
        Collection<String> names = ConfigInfo.names(updatedComponents);
        closeUnusedObservables(names);

        observerRegister.clear();
        for (final String name : names) {
            String pv = getPV(name);
            SERVER.dependencies(pv).addObserver(new BaseObserver<Collection<String>>() {
                @Override
                public void onValue(java.util.Collection<String> value) {
                    updateDependency(name, value);
                };
            });
            observerRegister.put(name, SERVER.dependencies(pv));
        }
    }

    /**
     * Checks whether components still exist and removes associated observables
     * if not.
     * 
     * @param updatedComponents The current list of components
     */
    private void closeUnusedObservables(Collection<String> updatedComponents) {
        for (String key : observerRegister.keySet()) {
            if (!updatedComponents.contains(key)) {
                observerRegister.get(key).close();
            }
        }
    }

    /**
     * Updates the list of configurations dependent on a given component, or
     * removes it from the list if there are none.
     * 
     * @param name The name of the component
     * @param value The list of dependent configurations
     */
    private void updateDependency(String name, Collection<String> value) {
        if (!value.isEmpty()) {
            dependencies.put(name, value);
        } else {
            dependencies.remove(name);
        }
    }

    /**
     * Finds the dynamic blockserver PV of a given component based on its name.
     * 
     * @param component
     *            The name of the component
     * @return The PV associated to the component
     */
    private String getPV(String component) {
        String pv = "";
        for (ConfigInfo compInfo : SERVER.componentsInfo().getValue()) {
            if (compInfo.name().equals(component)) {
                pv = compInfo.pv();
                break;
            }
        }
        return pv;
    }

    /**
     * Filter to return only dependencies of selected components.
     * 
     * @param toDelete The list of selected components to delete
     * @return A map containing dependencies for selected components only
     */
    private Map<String, Collection<String>> filterSelected(Collection<String> toDelete) {
        Map<String, Collection<String>> result = new HashMap<String, Collection<String>>();
        for (String key : toDelete) {
            if (dependencies.keySet().contains(key)) {
                result.put(key, dependencies.get(key));
            }
        }
        return result;
    }

    /**
     * Builds a warning message for display based on dependencies found in
     * components to delete.
     * 
     * @param dependencies The dependencies of components to delete
     * @return A warning message as string.
     */
    private String buildWarning(Map<String, Collection<String>> dependencies) {
        boolean multi = (dependencies.size() > 1);
        StringBuilder sb = new StringBuilder();
        sb.append("The following " + (multi ? "components are" : "component is")
                + " currently in use and thus can not be deleted:\n\n");
        for (String comp : dependencies.keySet()) {
            sb.append("Component: " + comp + "\nUsed in configuration(s): ");
            boolean first = true;
            for (String config : dependencies.get(comp)) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(config);
                first = false;
            }
            sb.append("\n\n");
        }
        sb.append("Please remove the component" + (multi ? "s" : "") + " from these configurations before deleting.");
        return sb.toString();
    }

}
