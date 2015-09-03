
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

import uk.ac.stfc.isis.ibex.configserver.Configurations;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class ConfigInfo {

	private final String name;
	private final String description;
	private final String pv;
	
	public ConfigInfo(String name, String description, String pv) {
		this.name = name;
		this.description = description;
		this.pv = pv;
	}
	
	public String name() {
		return name;
	}
	
	public String description() {
		return description;
	}
	
	public String pv() {
		return pv;
	}
	
	public static Collection<String> namesWithoutCurrent(Collection<ConfigInfo> infos) {
		Collection<String> filteredNames = names(infos);
		filteredNames.remove(Configurations.getInstance().display().displayCurrentConfig().getValue().name());
		return filteredNames;
	}
	
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
