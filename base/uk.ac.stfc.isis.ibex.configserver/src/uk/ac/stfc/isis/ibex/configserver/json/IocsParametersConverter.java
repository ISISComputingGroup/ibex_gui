
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

package uk.ac.stfc.isis.ibex.configserver.json;

import java.beans.PropertyChangeSupport;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Function;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import uk.ac.stfc.isis.ibex.configserver.internal.IocParameters;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.json.LowercaseEnumTypeAdapterFactory;
import uk.ac.stfc.isis.ibex.epics.conversion.json.SpecificClassExclusionStrategy;

/**
 * Creates a map of IOC parameters for each IOC specified in the JSON.
 */
public class IocsParametersConverter implements Function<String, Map<String, IocParameters>> {
	
	private static final ExclusionStrategy EXCLUDE_PROPERTY_CHANGE_SUPPORT = 
			new SpecificClassExclusionStrategy(PropertyChangeSupport.class);
	
	private static final Gson GSON = new GsonBuilder()
			.registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
			.setExclusionStrategies(EXCLUDE_PROPERTY_CHANGE_SUPPORT)
			.create();

    // CHECKSTYLE:OFF The declaration format for GSON's TypeToken upsets
    // CheckStyle.
    /**
     * The data format for the conversion.
     * 
     * E.g. {"MOTORSIM": {"running": false}, "TPG300_01": {"running": false} }
     */
	private static final Type SERVER_IOC_DATA_FORMAT = new TypeToken<Map<String, IocParameters>>() { }.getType();
    // CHECKSTYLE:ON

	@Override
	public Map<String, IocParameters> apply(String json) throws ConversionException {
		try {
			return GSON.fromJson(json, SERVER_IOC_DATA_FORMAT);
		} catch (JsonSyntaxException e) {
			throw new ConversionException("Error parsing json", e);
		}
	}
}
