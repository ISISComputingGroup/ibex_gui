
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
import java.util.function.Function;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonDeserialisingConverter;
import uk.ac.stfc.isis.ibex.experimentdetails.Role;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;

/**
 * Converts JSON to UserDetails object.
 */
public class UserDetailsConverter implements Function<String, Collection<UserDetails>> {

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
    
    /**
	 *  Takes a list of users and combines those that have the same name and institute into one.
	 *  @param originalList The original list
	 *  @return The new list, with combined roles
	 */
    public static Collection<UserDetails> combineSameUsers(Collection<UserDetails> originalList) {
    	List<UserDetails> newList = new ArrayList<UserDetails>();
		
        for (UserDetails oldUser : originalList) {
        	boolean userAdded = false;
        	for (UserDetails newUser: newList) {
        		if (oldUser.getName().equals(newUser.getName()) && oldUser.getInstitute().equals(newUser.getInstitute())) {
        			newUser.setPrimaryRole(newUser.getRole());
        			userAdded = true;
        		}
        	}
        	if (!userAdded) {
        		newList.add(oldUser);
        	}
		}
        
        return newList;
	}
    
	@Override
	public Collection<UserDetails> apply(String value)
			throws ConversionException {
        Function<String, IntermediateUserDetails[]> jsonConverter = new JsonDeserialisingConverter<>(
                IntermediateUserDetails[].class);
        // Convert to intermediate
        IntermediateUserDetails[] parsed = jsonConverter.apply(value);
		
        ArrayList<UserDetails> userDetails = new ArrayList<>();
        
        for (IntermediateUserDetails user : parsed) {
        	userDetails.add(new UserDetails(user.name, user.institute, user.role));
        }
        
        return combineSameUsers(userDetails);
	}

}
