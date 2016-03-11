
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
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.experimentdetails.Role;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.json.JsonDeserialisingConverter;
import uk.ac.stfc.isis.ibex.json.LowercaseEnumTypeAdapterFactory;

/**
 * Converts JSON to UserDetails object.
 */
public class UserDetailsConverter extends
		Converter<String, Collection<UserDetails>> {
	
	private final Gson gson = new GsonBuilder().registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory()).create();

    /**
     * An intermediate class for Gson to use when converting from JSON.
     * 
     * Gson doesn't call the constructor of UserDetails so we need to use an
     * intermediate class.
     */
    class IntermediateUserDetails {
        public String name;
        public String institute;
        public Role role;
    }

	@Override
	public Collection<UserDetails> convert(String value)
			throws ConversionException {
        Converter<String, IntermediateUserDetails[]> jsonConverter = new JsonDeserialisingConverter<>(
                IntermediateUserDetails[].class, gson);
        // Convert to intermediate
        IntermediateUserDetails[] parsed = jsonConverter.convert(value);
		
        Map<String, UserDetails> userDetails = new HashMap<String, UserDetails>();
		
        // Creates UserDetails and assigns primary role if name appears multiple
        // times
        for (IntermediateUserDetails user : parsed) {
            if (userDetails.containsKey(user.name)) {
                userDetails.get(user.name).setPrimaryRole(user.role);
            } else {
                userDetails.put(user.name, new UserDetails(user.name, user.institute, user.role));
            }
		}

        return new ArrayList<UserDetails>(userDetails.values());
	}

}
