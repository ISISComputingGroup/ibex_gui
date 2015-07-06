
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

package uk.ac.stfc.isis.ibex.experimentdetails.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.json.JsonDeserialisingConverter;
import uk.ac.stfc.isis.ibex.json.LowercaseEnumTypeAdapterFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserDetailsConverter extends
		Converter<String, Collection<UserDetails>> {
	
	private final Gson gson = new GsonBuilder().registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory()).create();

	@Override
	public Collection<UserDetails> convert(String value)
			throws ConversionException {
		Converter<String, UserDetails[]> jsonConverter = new JsonDeserialisingConverter<>(UserDetails[].class, gson);
		UserDetails[] parsed = jsonConverter.convert(value);
		
		List<UserDetails> userDetails = new ArrayList<>();
		
		for (UserDetails user : parsed) {
			userDetails.add(new UserDetails(user));
		}
		
		return userDetails;
	}

}
