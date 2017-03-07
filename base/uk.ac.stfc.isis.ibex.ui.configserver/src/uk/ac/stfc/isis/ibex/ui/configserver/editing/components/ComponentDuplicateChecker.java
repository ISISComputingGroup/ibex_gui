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
package uk.ac.stfc.isis.ibex.ui.configserver.editing.components;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableComponents;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;

/**
 * Class used to check for conflicts caused by duplicate elements to decide if a
 * given component can be added to a configuration.
 */
public class ComponentDuplicateChecker {

    private EditableConfiguration config;
    private EditableComponents components;
    private Map<String, String> allCurrentBlocks;

    /**
     * Constructor.
     * 
     * @param config The currently edited configuration
     */
    public ComponentDuplicateChecker(EditableConfiguration config) {
        this.config = config;
        components = config.getEditableComponents();
    }

    private void refreshBlocks() {
        allCurrentBlocks = new HashMap<String, String>();
        allCurrentBlocks.putAll(getConfigBlocks());
        allCurrentBlocks.putAll(getComponentBlocks());
    }

    private Map<String, String> getConfigBlocks() {
        Map<String, String> result = new HashMap<String, String>();
        for (Block block : config.getAvailableBlocks()) {
            if (!block.hasComponent()) {
                String name = block.getName();
                String source = "Base Configuration";
                result.put(name, source);
            }
        }
        return result;
    }

    private Map<String, String> getComponentBlocks() {
        Map<String, String> result = new HashMap<String, String>();
        if (components == null) {
            return result;
        }
        for (Configuration comp : components.getSelected()) {
            for (Block block : comp.getBlocks()) {
                result.put(block.getName(), comp.getName());
            }
        }
        return result;
    }

    private void addCurrentBlocks(Configuration toAdd) {
        for (Block block : toAdd.getBlocks()) {
            allCurrentBlocks.put(block.getName(), toAdd.getName());
        }
    }

    /**
     * Checks for block duplicates in the current configuration for a given set
     * of components to be added, and returns a map of conflicts for each of
     * those components.
     * 
     * @param toToggle The list of components to be added.
     * @return A map of conflicts per component (empty if none).
     */
    public Map<String, Map<String, String>> checkBlocks(Collection<Configuration> toToggle) {
        refreshBlocks();
        Map<String, Map<String, String>> allConflicts = new HashMap<String, Map<String, String>>();

        for (Configuration comp : toToggle) {
            Map<String, String> conflicts = getBlockConflicts(comp);
            if (!conflicts.isEmpty()) {
                allConflicts.put(comp.getName(), conflicts);
            } else {
                addCurrentBlocks(comp);
            }
        }
        return allConflicts;
    }

    private Map<String, String> getBlockConflicts(Configuration toCheck) {
        Map<String, String> conflicts = new HashMap<String, String>();

        for (Block block : toCheck.getBlocks()) {
            if (allCurrentBlocks.containsKey(block.getName())) {
                conflicts.put(block.getName(), allCurrentBlocks.get(block.getName()));
            }
        }
        return conflicts;
    }
}
