
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

package uk.ac.stfc.isis.ibex.json;

import java.beans.PropertyChangeSupport;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSerialisingConverter<T> extends Converter<T, String> {

	private final static ExclusionStrategy EXCLUDE_PROPERTY_CHANGE_SUPPORT = 
			new SpecificClassExclusionStrategy(PropertyChangeSupport.class);
	
	private final Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
			.setExclusionStrategies(EXCLUDE_PROPERTY_CHANGE_SUPPORT)
			.create();

	private final Class<T> classOfT;
	
	public JsonSerialisingConverter(Class<T> classOfT) {
		this.classOfT = classOfT;
	}
	
	@Override
	public String convert(T value) throws ConversionException {
		return gson.toJson(value, classOfT);
	}
}
