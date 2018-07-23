
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.experimentdetails.tests.database;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.experimentdetails.Role;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.experimentdetails.internal.UserDetailsConverter;

/**
 * Unit tests for UserDetails.
 */
@SuppressWarnings("checkstyle:methodname")
public class UserDetailsConverterTest {
    @Test
    public void test_GIVEN_list_containing_one_user_WHEN_combine_users_called_THEN_list_and_role_unchanged() {
    	Collection<UserDetails> list = new ArrayList<>();
    	list.add(new UserDetails("name", "institute", Role.BLANK));
    	
    	list = UserDetailsConverter.combineSameUsers(list);
    	
    	assertEquals(1, list.size());
    	list.forEach((u) -> assertEquals(Role.BLANK, u.getRole()));
    }

    @Test
    public void test_GIVEN_list_containing_two_unique_users_WHEN_combine_users_called_THEN_list_and_roles_unchanged() {
    	Collection<UserDetails> list = new ArrayList<>();
    	list.add(new UserDetails("name", "institute", Role.BLANK));
    	list.add(new UserDetails("name1", "institute", Role.BLANK));
    	
    	list = UserDetailsConverter.combineSameUsers(list);
    	
    	assertEquals(2, list.size());
    	list.forEach((u) -> assertEquals(Role.BLANK, u.getRole()));
    }
    
    @Test
    public void test_GIVEN_list_containing_two_same_users_WHEN_combine_users_called_THEN_list_contains_one_user_with_priority_role() {
    	Collection<UserDetails> list = new ArrayList<>();
    	String NAME = "name";
    	String INST = "inst";
    	list.add(new UserDetails(NAME, INST, Role.PI));
    	list.add(new UserDetails(NAME, INST, Role.USER));
    	
    	list = UserDetailsConverter.combineSameUsers(list);
    	
    	assertEquals(1, list.size());
    	list.forEach((u) -> assertEquals(Role.PI, u.getRole()));
    }    
}
