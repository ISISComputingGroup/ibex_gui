
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

package uk.ac.stfc.isis.ibex.configserver.internal;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.BlockRules;
import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.configserver.ServerStatus;
import uk.ac.stfc.isis.ibex.configserver.configuration.BannerItem;
import uk.ac.stfc.isis.ibex.configserver.configuration.ComponentInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.validators.BlockServerNameValidator;

/**
 * Interface for converters from block server to internal objects.
 */
public interface Converters {

    /**
     * @return converter for converting a to a configuration.
     */
	Converter<String, Configuration> toConfig();

    /**
     * @return converter for converting to a list of configurations.
     */
    Converter<String, Collection<Configuration>> toConfigList();

    /**
     * @return converter for converting to server status.
     */
	Converter<String, ServerStatus> toServerStatus();
	
    /**
     * @return converter for the block rules.
     */
	Converter<String, BlockRules> toBlockRules();

    /**
     * @return converter for block server text validator rules.
     */
    Converter<String, BlockServerNameValidator> toBlockServerTextValidor();

    /**
     * @return converter for converting to config info.
     */
	Converter<String, Collection<ConfigInfo>> toConfigsInfo();

    /**
     * @return converter for converting to component info.
     */
	Converter<String, Collection<ComponentInfo>> toComponents();

    /**
     * @return converter for converting to IOCs.
     */
	Converter<String, Collection<EditableIoc>> toIocs();

    /**
     * @return converter for converting to PVs.
     */
	Converter<String, Collection<PV>> toPVs();

    /**
     * @return converter for converting a configuration to string.
     */
	Converter<Configuration, String> configToString();

    /**
     * @return converter for converting a list of config names to string.
     */
	Converter<Collection<String>, String> namesToString();

    /**
     * @return converter for converting a name to string.
     */
	Converter<String, String> nameToString();

    /**
     * @return converter for converting to a list of IOC states.
     */
	Converter<String, Collection<IocState>> toIocStates();

    /**
     * @return converter for converting to a list of names.
     */
	Converter<String, Collection<String>> toNames();

    /**
     * @return converter for the banner description
     */
	Converter<String, Collection<BannerItem>> toBannerDescription();

}
