
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
import uk.ac.stfc.isis.ibex.configserver.configuration.Component;
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

	Converter<String, Configuration> toConfig();

	Converter<String, ServerStatus> toServerStatus();
	
    /**
     * @return converter for the block rules.
     */
	Converter<String, BlockRules> toBlockRules();

    /**
     * @return converter for block server text validator rules.
     */
    Converter<String, BlockServerNameValidator> toBlockServerTextValidor();

	Converter<String, Collection<ConfigInfo>> toConfigsInfo();

	Converter<String, Collection<Component>> toComponents();

	Converter<String, Collection<EditableIoc>> toIocs();

	Converter<String, Collection<PV>> toPVs();

	Converter<Configuration, String> configToString();

	Converter<Collection<String>, String> namesToString();

	Converter<String, String> nameToString();

	Converter<String, Collection<IocState>> toIocStates();

	Converter<String, Collection<String>> toNames();
	
	Converter<String, Collection<BannerItem>> toBannerDescription();

}
