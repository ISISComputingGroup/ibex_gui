
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
 * 
 */
public class UserDetailsTest {
    @Test
    public void set_PI_from_Contact() {
        // Arrange
        Role PI = Role.getByString("PI");
        Role Contact = Role.getByString("Contact");
        UserDetails userTest = new UserDetails("John Smith", "STFC", Contact);

        // Act
        userTest.setRole(PI);

        // Assert
        assertEquals("PI", userTest.getRole().toString());
    }

    @Test
    public void set_Contact_from_PI() {
        // Arrange 
        Role PI = Role.getByString("PI");
        Role Contact = Role.getByString("Contact");
        UserDetails userTest = new UserDetails("John Smith", "STFC", PI);
        
        // Act
        userTest.setRole(Contact);
        
        // Assert 
        assertEquals("Contact", userTest.getRole().toString());
    }

    @Test
    public void overwrite_primary_role() {
        // Arrange
        Role PI = Role.getByString("PI");
        Role User = Role.getByString("User");
        UserDetails userTest = new UserDetails("John Smith", "STFC", User);

        // Act
        userTest.setPrimaryRole(PI);

        // Assert
        assertEquals("PI", userTest.getRole().toString());
    }

    @Test
    public void fail_overwrite_PI_primary_role() {
        // Arrange
        Role PI = Role.getByString("PI");
        Role User = Role.getByString("User");
        UserDetails userTest = new UserDetails("John Smith", "STFC", PI);

        // Act
        userTest.setPrimaryRole(User);

        // Assert
        assertEquals("PI", userTest.getRole().toString());
    }

    @Test
    public void fail_overwrite_contact_primary_role() {
        // Arrange
        Role PI = Role.getByString("PI");
        Role Contact = Role.getByString("Contact");
        UserDetails userTest = new UserDetails("John Smith", "STFC", PI);

        // Act
        userTest.setPrimaryRole(Contact);

        // Assert
        assertEquals("PI", userTest.getRole().toString());
    }

    @Test
    public void set_blank_role() {
        // Arrange
        Role PI = Role.getByString("PI");
        Role BLANK = Role.getByString("");
        UserDetails userTest = new UserDetails("John Smith", "STFC", PI);

        // Act
        userTest.setRole(BLANK);

        // Assert
        assertEquals("", userTest.getRole().toString());
    }

    @Test
    public void set_primary_role_blank() {
        // Arrange
        Role User = Role.getByString("User");
        Role BLANK = Role.getByString("");
        UserDetails userTest = new UserDetails("John Smith", "STFC", User);

        // Act
        userTest.setPrimaryRole(BLANK);

        // Assert
        assertEquals("User", userTest.getRole().toString());
    }

    @Test
    public void set_primary_role_from_blank() {
        // Arrange
        Role Contact = Role.getByString("Contact");
        Role BLANK = Role.getByString("");
        UserDetails userTest = new UserDetails("John Smith", "STFC", BLANK);

        // Act
        userTest.setPrimaryRole(Contact);

        // Assert
        assertEquals("Contact", userTest.getRole().toString());
    }

}
