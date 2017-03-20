
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

import java.util.Collection;
import java.util.Collections;

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
	
	/**
	 * Constructor.
	 * 
	 * @param name The config name
	 * @param description The config description
	 * @param pv The dynamic PV for the config
	 */
	public ConfigInfo(String name, String description, String pv) {
		this.name = name;
		this.description = description;
		this.pv = pv;
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
     * Returns just the names of all config info objects passed in excluding
     * that of the current config.
     * 
     * @param infos The list of ConfigInfos
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
	 * @param infos The list of ConfigInfos
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
}
