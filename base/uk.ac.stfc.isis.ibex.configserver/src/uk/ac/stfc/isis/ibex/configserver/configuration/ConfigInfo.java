
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

package uk.ac.stfc.isis.ibex.configserver.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.configserver.Configurations;

/**
 * Data object holding general config information.
 */
public class ConfigInfo {

    private final String name;
    private final String description;
    private final String pv;
    private String synoptic;
    private final List<String> history;
    private final Boolean isProtected;
    private final Boolean isDynamic;

    /**
     * Constructor.
     * 
     * @param name
     *            The config name
     * @param isProtected
     *            Config is protected and can only be modified or deleted in manager mode
     * @param isDynamic
     *            Config or component is dynamic and may be added or removed from components automatically
     * @param description
     *            The config description
     * @param pv
     *            The dynamic PV for the config
     * @param synoptic
     *            The default synoptic view for the config
     * @param history
     *            The history of the config.
     *  
     */
    public ConfigInfo(String name, boolean isProtected, boolean isDynamic, String description, String pv, String synoptic, Collection<String> history) {
        this.name = name;
        this.description = description;
        this.pv = pv;
        this.synoptic = synoptic;
        this.history = new ArrayList<>(history);
        this.isProtected = isProtected;
        this.isDynamic = isDynamic;
    }

    /**
     * @return The name of the config
     */
    public String name() {
        return name;
    }

    /**
     * @return The description of the config
     */
    public String description() {
        return description;
    }

    /**
     * @return The dynamic PV of the config
     */
    public String pv() {
        return pv;
    }

    /**
     * @return The default synoptic view for the config
     */
    public String synoptic() {
        return synoptic;
    }

    /**
     * Returns just the names of all config info objects passed in excluding
     * that of the current config.
     * 
     * @param infos
     *            The list of ConfigInfos
     * @return The list of config names without that of the current config
     */
    public static List<String> namesWithoutCurrent(Collection<ConfigInfo> infos) {
        List<String> filteredNames = names(infos);
        filteredNames.remove(Configurations.getInstance().display().displayCurrentConfig().getValue().name());
        return filteredNames;
    }

    /**
     * Reduces a list of ConfigInfo objects to a list of their names only.
     * 
     * @param infos
     *            The list of ConfigInfos
     * @return The list of configuration/component names
     */
    public static List<String> names(Collection<ConfigInfo> infos) {
        if (infos == null || infos.isEmpty()) {
            return Collections.emptyList();
        }

        return infos.stream()
                .map(ConfigInfo::name)
                .collect(Collectors.toList());
    }
    
    /**
     * Returns just the descriptions of all ConfigInfo objects passed in excluding
     * that of the current configuration/component.
     * 
     * @param infos
     *            The list of ConfigInfos
     * @return The list of configuration/component descriptions without the current one
     */
    public static List<String> descriptionsWithoutCurrent(Collection<ConfigInfo> infos) {
        List<String> filteredDescriptions = descriptions(infos);
        filteredDescriptions.remove(Configurations.getInstance().display().displayCurrentConfig().getValue().description());
        return filteredDescriptions;
    }
    
    /**
     * Reduces a list of ConfigInfo objects to a list of their descriptions only.
     * 
     * @param infos
     *            The list of ConfigInfos
     * @return The list of configuration/component descriptions
     */
    public static List<String> descriptions(Collection<ConfigInfo> infos) {
        if (infos == null) {
            return Collections.emptyList();
        }

        return infos.stream()
                .map(ConfigInfo::description)
                .collect(Collectors.toList());
    }
    
    /**
     * Returns a sorted map with the names and descriptions of all ConfigInfo objects passed in
     * excluding that of the current configuration or component.
     * 
     * @param infos
     *            The list of ConfigInfos
     * @return The sorted map of configuration/component name to descriptions without the current one
     */
    public static SortedMap<String, String> namesAndDescriptionsWithoutCurrent(Collection<ConfigInfo> infos) {
        SortedMap<String, String> filteredNamesAndDescriptions = namesAndDescriptions(infos);
        filteredNamesAndDescriptions.remove(Configurations.getInstance().display().displayCurrentConfig().getValue().name());
        return filteredNamesAndDescriptions;
    }
    
    /**
     * Returns a sorted map with the names and descriptions of all ConfigInfo objects passed in.
     * 
     * @param infos
     *            The list of ConfigInfos
     * @return The sorted map of configuration/component name to description
     */
    public static SortedMap<String, String> namesAndDescriptions(Collection<ConfigInfo> infos) {
    	if (infos == null || infos.isEmpty()) {
    		// note: this map needs to be mutable as we remove from it above.
    		// do not replace with Collections.emptySortedMap();
    		return new TreeMap<String, String>();
    	}
    	
    	SortedMap<String, String> result = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
    	
    	for (ConfigInfo config : infos) {
    		result.put(config.name, config.description);
    	}
    	
    	return result;
    }
    
    /**
     * returns configuration/component names and its protection status.
     * @param infos
     * @return collection of pair i.e configuration/component name and its protection flag
     */
    public static Map<String, Boolean> mapNamesWithTheirProtectionFlag(Collection<ConfigInfo> infos) {
    	if (infos == null || infos.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Boolean> namesWithProtectionFlag = new HashMap<String, Boolean>();
        for (ConfigInfo config: infos) {
            namesWithProtectionFlag.put(config.name(), config.isProtected());
        }
        
        return namesWithProtectionFlag;
    }
    
    /**
     * Checks if there is a protected configuration/component element within the given ConfigInfo collection.
     * @param infos
     *            The list of ConfigInfos
     * @return boolean: True if a protected configuration exists within given infos list, False if not
     */
    public static boolean hasProtectedElement(Collection<ConfigInfo> infos) {
    	if (infos == null) {
            return false;
        }

        boolean returnVal = infos.stream().anyMatch(config -> config.isProtected);

    	return returnVal;
    }

    /**
     * @return A collection of dates (as Strings) when the configuration was updated.
     */
    public Collection<String> getHistory() {
        return Collections.unmodifiableList(history);
    }
    
    /**
     * Returns if the config is protected or not.
     * @return boolean value if a config is protected or not
     */
    public Boolean isProtected() {
        return this.isProtected;
    }
    
    /**
     * Returns if the config is dynamic or not.
     * @return boolean value if a config is dynamic or not
     */
    public Boolean isDynamic() {
        return this.isDynamic;
    }
}
