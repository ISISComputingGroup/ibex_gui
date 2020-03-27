
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

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

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

    /**
     * Constructor.
     * 
     * @param name
     *            The config name
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
    public ConfigInfo(String name, boolean isProtected, String description, String pv, String synoptic, Collection<String> history) {
        this.name = name;
        this.description = description;
        this.pv = pv;
        this.synoptic = synoptic;
        this.history = new ArrayList<>(history);
        this.isProtected = isProtected;
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
    public static Collection<String> namesWithoutCurrent(Collection<ConfigInfo> infos) {
        Collection<String> filteredNames = names(infos);
        filteredNames.remove(Configurations.getInstance().display().displayCurrentConfig().getValue().name());
        return filteredNames;
    }

    /**
     * Reduces a list of ConfigInfo objects to a list of their names only.
     * 
     * @param infos
     *            The list of ConfigInfos
     * @return The list of config names
     */
    public static Collection<String> names(Collection<ConfigInfo> infos) {
        if (infos == null) {
            return Collections.emptyList();
        }

        return Lists.newArrayList(Iterables.transform(infos, new Function<ConfigInfo, String>() {
            @Override
            public String apply(ConfigInfo info) {
                return info.name();
            }
        }));
    }
    
    /**
     * returns config/Component names and its protection status.
     * @param infos
     * @return collection of Pair i.e config/comp name and its proteciton flag
     */
    public static Map<String, Boolean> mapNamesWithTheirProtectionFlag(Collection<ConfigInfo> infos) {
        if (infos == null) {
            return Collections.emptyMap();
        }
        Map<String, Boolean> namesWithProtectionFlag = new HashMap<String, Boolean>();
        for (ConfigInfo config: infos) {
            namesWithProtectionFlag.put(config.name(), config.isProtected());
        }
        
        return namesWithProtectionFlag;
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
}
