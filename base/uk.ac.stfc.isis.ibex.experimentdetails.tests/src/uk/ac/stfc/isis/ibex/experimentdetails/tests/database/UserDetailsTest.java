
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

import org.junit.Test;

import uk.ac.stfc.isis.ibex.experimentdetails.Role;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;

/**
 * Unit tests for UserDetails
 */
@SuppressWarnings("checkstyle:methodname")
public class UserDetailsTest {
    @Test
    public void set_pi_from_contact() {
        // Arrange
        Role pi = Role.getByString("PI");
        Role contact = Role.getByString("Contact");
        UserDetails userTest = new UserDetails("John Smith", "STFC", contact);

        // Act
        userTest.setRole(pi);

        // Assert
        assertEquals("PI", userTest.getRole().toString());
    }

    @Test
    public void set_contact_from_pi() {
        // Arrange 
        Role pi = Role.getByString("PI");
        Role contact = Role.getByString("Contact");
        UserDetails userTest = new UserDetails("John Smith", "STFC", pi);
        
        // Act
        userTest.setRole(contact);
        
        // Assert 
        assertEquals("Contact", userTest.getRole().toString());
    }

    @Test
    public void overwrite_primary_role() {
        // Arrange
        Role pi = Role.getByString("PI");
        Role user = Role.getByString("User");
        UserDetails userTest = new UserDetails("John Smith", "STFC", user);

        // Act
        userTest.setPrimaryRole(pi);

        // Assert
        assertEquals("PI", userTest.getRole().toString());
    }

    @Test
    public void fail_overwrite_pi_primary_role() {
        // Arrange
        Role pi = Role.getByString("PI");
        Role user = Role.getByString("User");
        UserDetails userTest = new UserDetails("John Smith", "STFC", pi);

        // Act
        userTest.setPrimaryRole(user);

        // Assert
        assertEquals("PI", userTest.getRole().toString());
    }

    @Test
    public void fail_overwrite_contact_primary_role() {
        // Arrange
        Role pi = Role.getByString("PI");
        Role contact = Role.getByString("Contact");
        UserDetails userTest = new UserDetails("John Smith", "STFC", pi);

        // Act
        userTest.setPrimaryRole(contact);

        // Assert
        assertEquals("PI", userTest.getRole().toString());
    }

    @Test
    public void set_blank_role() {
        // Arrange
        Role pi = Role.getByString("PI");
        Role blank = Role.getByString("");
        UserDetails userTest = new UserDetails("John Smith", "STFC", pi);

        // Act
        userTest.setRole(blank);

        // Assert
        assertEquals("", userTest.getRole().toString());
    }

    @Test
    public void set_primary_role_blank() {
        // Arrange
        Role user = Role.getByString("User");
        Role blank = Role.getByString("");
        UserDetails userTest = new UserDetails("John Smith", "STFC", user);

        // Act
        userTest.setPrimaryRole(blank);

        // Assert
        assertEquals("User", userTest.getRole().toString());
    }

    @Test
    public void set_primary_role_from_blank() {
        // Arrange
        Role contact = Role.getByString("Contact");
        Role blank = Role.getByString("");
        UserDetails userTest = new UserDetails("John Smith", "STFC", blank);

        // Act
        userTest.setPrimaryRole(contact);

        // Assert
        assertEquals("Contact", userTest.getRole().toString());
    }

}
