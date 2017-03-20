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
package uk.ac.stfc.isis.ibex.configserver.editing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.ComponentInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;

/**
 * Class used to check for conflicts caused by duplicate elements to decide if a
 * given component can be added to a configuration.
 */
public class DuplicateChecker {

    private Configuration baseConfig;
    private Collection<Configuration> components;
    private Map<String, Set<String>> allBlocks;


    /**
     * Sets the base configuration to check against.
     * 
     * @param base The base configuration
     */
    public void setBase(Configuration base) {
        ConfigServer configServer = Configurations.getInstance().server();
        this.baseConfig = base;
        this.components = configServer.componentDetails().getValue();
        init();
    }

    /** Used for testing.
     * 
     * @param base The base configuration
     * @param components The custom components.
     */
    public void setBase(Configuration base, Collection<Configuration> components) {
        this.baseConfig = base;
        this.components = components;
        init();
    }

    private void init() {
        allBlocks = new HashMap<String, Set<String>>();
        addConfigBlocks();
    }

    private void addConfigBlocks() {
        allBlocks = new HashMap<String, Set<String>>();
        for (Block block : baseConfig.getBlocks()) {
            if (!block.hasComponent()) {
                String name = block.getName().toUpperCase();
                Set<String> source = new HashSet<>();
                source.add("base configuration");
                allBlocks.put(name, source);
            }
        }
    }

    private void addComponents(Collection<Configuration> components) {
        for (Configuration comp : components) {
            addBlocks(comp.getBlocks(), comp.getName());
        }
    }

    private void addBlocks(Collection<Block> blocks, String compName) {
        for (Block block : blocks) {
            String name = block.getName().toUpperCase();
            if (allBlocks.containsKey(name)) {
                allBlocks.get(name).add(compName);
            } else {
                Set<String> source = new HashSet<>();
                source.add(compName);
                allBlocks.put(name, source);
            }
        }
    }

    /**
     * Checks the validity of the configuration set as base.
     * 
     * @return The map of duplicate blocks and their sources
     */
    public Map<String, Set<String>> checkOnLoad() {
        init();
        addComponents(filterNative(components));
        return getConflicts();
    }

    /**
     * Checks the validity of the configuration set as base after adding a set
     * of components to it.
     * 
     * @param toAdd The components to be added to this config
     * @return The map of duplicate blocks and their sources
     */
    public Map<String, Set<String>> checkOnAdd(Collection<Configuration> toAdd) {
        init();
        addComponents(filterNative(components));
        addComponents(toAdd);
        return getConflicts();
    }

    /**
     * Checks the validity of a configuration after one of its components has
     * been edited.
     * 
     * @param edited The edited component.
     * @return The map of duplicate blocks and their sources
     */
    public Map<String, Set<String>> checkOnEdit(Configuration edited) {
        init();
        addComponents(filterNative(replaceEdited(edited)));
        return getConflicts();
    }

    private Map<String, Set<String>> getConflicts() {
        Map<String, Set<String>> result = new HashMap<String, Set<String>>();
        for (String key : allBlocks.keySet()) {
            if (allBlocks.get(key).size() > 1) {
                result.put(key, allBlocks.get(key));
            }
        }
        return result;
    }

    /**
     * Finds if duplicates for a given name exist in the config to check.
     * 
     * @param name
     *            The block name to check for.
     * @return The name of duplicate blocks.
     */
    public String findDuplicate(String name) {
        addComponents(components);
        for (String key : allBlocks.keySet()) {
            if (name.equalsIgnoreCase(key)) {
                return key;
            }
        }
        return null;
    }

    /**
     * Filters a list of components to only contain the ones that are part of
     * the currently set base configuration.
     * 
     * @param components The list of all available components
     * @return The list of currently selected components
     */
    private Collection<Configuration> filterNative(Collection<Configuration> components) {
        Collection<Configuration> filtered = new ArrayList<>();
        for (Configuration comp : components) {
            for (ComponentInfo selectedComp : baseConfig.getComponents()) {
                if (comp.getName().equals(selectedComp.getName())) {
                    filtered.add(comp);
                }
            }
        }
        return filtered;
    }

    /**
     * Replaces a component in the list of available components with an edited
     * version.
     * 
     * @param edited The edited component to replace
     * @return The list of all available components updated with the edited component
     */
    private Collection<Configuration> replaceEdited(Configuration edited) {
        Collection<Configuration> result = new ArrayList<Configuration>();
        for (Configuration comp : components) {
            if (comp.getName() != edited.getName()) {
                result.add(comp);
            } else {
                result.add(edited);
            }
        }
        return result;
    }
}
