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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.configserver.editing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.ComponentInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;

/**
 * Class used to check for conflicts caused by duplicate items to decide if a
 * given component can be added to a configuration.
 * @param <T> the type to check for duplicates
 */
public abstract class DuplicateChecker<T extends INamedInComponent> {

    private Configuration baseConfig;
    private Collection<Configuration> components;
    private Map<String, Set<String>> allItems;

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

    /**
     * Initialises by adding the items from the current configuration
     */
    private void init() {
        addConfigItems();
    }

    /**
     * Adds to the Map the items of the current configuration
     */
    private void addConfigItems() {
        allItems = new HashMap<String, Set<String>>();
        for (T item : getItems(baseConfig)) {
            if (!item.inComponent()) {
                String name = item.getName().toUpperCase();
                Set<String> source = new HashSet<>();
                source.add(baseConfig.getName() + " (base configuration)");
                allItems.put(name, source);
            }
        }
    }

    /**
     * @param config the configuration
     * @return the configurations items
     */
    abstract Collection<T> getItems(Configuration config);

    /**
     * Adds to the Map the items and IOCs of the given components
     * @param components the components
     */
    private void addComponents(Collection<Configuration> components) {
        for (Configuration comp : components) {
            addItems(getItems(comp), comp.getName());
        }
    }

    /**
     * Adds to the Map the items of the given component
     * @param items the items to add
     * @param compName the name of the component the items belong to
     */
    private void addItems(Collection<T> items, String compName) {
        for (T item : items) {
            String name = item.getName().toUpperCase();
            if (allItems.containsKey(name)) {
                allItems.get(name).add(compName);
            } else {
                Set<String> source = new HashSet<>();
                source.add(compName);
                allItems.put(name, source);
            }
        }
    }

    /**
     * Checks the configuration set as base for duplicate items.
     * 
     * @return The map of duplicate items and their sources
     */
    public Map<String, Set<String>> checkItemsOnLoad() {
        init();
        addComponents(filterNative(components));
        return getConflicts(allItems);
    }

    /**
     * Checks the configuration set as base for duplicate items after adding a set
     * of components to it.
     * 
     * @param toAdd The components to be added to this config
     * @return The map of duplicate items and their sources
     */
    public Map<String, Set<String>> checkItemsOnAdd(Collection<Configuration> toAdd) {
        init();
        addComponents(filterNative(components));
        addComponents(toAdd);
        return getConflicts(allItems);
    }

    /**
     * Checks a configuration for duplicate items after one of its components has
     * been edited.
     * 
     * @param edited The edited component.
     * @return The map of duplicate items and their sources
     */
    public Map<String, Set<String>> checkItemsOnEdit(Configuration edited) {
        init();
        addComponents(filterNative(replaceEdited(edited)));
        return getConflicts(allItems);
    }
    
    
    /**
     * Checks a configuration for duplicate items if a given item were to be added.
     * 
     * @param item the item which the user wants to add
     * @param comp the name of the component which the item is being added to
     * @return The map of duplicate items and their sources
     */
    public Map<String, Set<String>> checkItemsOnAddItem(T item, String comp) {
        init();
        Collection<Configuration> activeComponents = filterNative(components);
        // Only check for conflicts if the component being edited is active
        if (activeComponents.stream().anyMatch(c -> c.getName().equals(comp))) {
            addComponents(activeComponents);
            addItems(Arrays.asList(item), "The current component");
        }
        return getConflicts(allItems);
    }

    /**
     * @param allMap the map to check for conflicts
     * @return a map containing the conflicts
     */
    private Map<String, Set<String>> getConflicts(Map<String, Set<String>> allMap) {
        Map<String, Set<String>> result = new HashMap<String, Set<String>>();
        for (String key : allMap.keySet()) {
            if (allMap.get(key).size() > 1) {
                result.put(key, allMap.get(key));
            }
        }
        return result;
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
            if (!comp.getName().equals(edited.getName())) {
                result.add(comp);
            } else {
                result.add(edited);
            }
        }
        return result;
    }
}
