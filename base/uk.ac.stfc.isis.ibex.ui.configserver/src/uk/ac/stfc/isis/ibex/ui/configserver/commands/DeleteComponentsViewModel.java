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
package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Viewmodel providing information for the Delete Component Dialog.
 */
public class DeleteComponentsViewModel extends ModelObject {

    private Map<String, Collection<String>> dependencies;

    /**
     * Constructor for the ViewModel.
     * 
     * @param dependencies The components' dependencies
     */
    public DeleteComponentsViewModel(Map<String, Collection<String>> dependencies) {
        this.dependencies = dependencies;

    }

    /**
     * Filter to return only dependencies of selected components.
     * 
     * @param toDelete
     *            The list of selected components to delete
     * @return A map containing dependencies for selected components only
     */
    public Map<String, Collection<String>> filterSelected(Collection<String> toDelete) {
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
     * @param dependencies
     *            The dependencies of components to delete
     * @return A warning message as string.
     */
    public String buildWarning(Map<String, Collection<String>> dependencies) {
        boolean multi = (dependencies.size() > 1);
        StringBuilder sb = new StringBuilder();
        sb.append("The following " + (multi ? "components are" : "component is")
                + " currently in use and so cannot be deleted:\n\n");
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

    /**
     * @return The map of all dependencies.
     */
    public Map<String, Collection<String>> getDependencies() {
        return dependencies;
    }

}
