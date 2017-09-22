
/**
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.validators.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.validators.PvValidator;

@SuppressWarnings("checkstyle:methodname")
public class PvValidatorTest {

    /**
     * Test method for {@link uk.ac.stfc.isis.ibex.validators.PvValidator#validatePvAddress(java.lang.String)}.
     */
    @Test
    public void GIVEN_plain_pv_address_WHEN_validate_THEN_valid() {
        // Arrange
        String testAddress = "valid";
        // Act
        PvValidator addressValid = new PvValidator();
        // Assert
        assertTrue(addressValid.validatePvAddress(testAddress));
    }

    @Test
    public void GIVEN_pv_address_with_field_WHEN_validate_THEN_valid() {
        // Arrange
        String testAddress = "valid.field";
        // Act
        PvValidator addressValid = new PvValidator();
        // Assert
        assertTrue(addressValid.validatePvAddress(testAddress));
    }

    @Test
    public void GIVEN_pv_address_with_non_alpha_character_WHEN_validate_THEN_valid() {
        // Arrange
        String testAddress = "va_:lid.fi_:eld";
        // Act
        PvValidator addressValid = new PvValidator();
        // Assert
        assertTrue(addressValid.validatePvAddress(testAddress));
    }

    @Test
    public void GIVEN_pv_address_with_dash_WHEN_validate_THEN_valid() {
        // Arrange
        String testAddress = "va_:l-d.fi_:eld";
        // Act
        PvValidator addressValid = new PvValidator();
        // Assert
        assertTrue(addressValid.validatePvAddress(testAddress));
    }

    @Test
    public void GIVEN_pv_address_two_field_WHEN_validate_THEN_error() {
        // Arrange
        String testAddress = "invalid.field.field";
        // Act
        PvValidator addressValid = new PvValidator();
        // Assert
        assertFalse(addressValid.validatePvAddress(testAddress));
    }

    @Test
    public void GIVEN_pv_address_blank_field_WHEN_validate_THEN_error() {
        // Arrange
        String testAddress = "invalid.";
        // Act
        PvValidator addressValid = new PvValidator();
        // Assert
        assertFalse(addressValid.validatePvAddress(testAddress));
    }

    @Test
    public void GIVEN_pv_address_just_field_WHEN_validate_THEN_error() {
        // Arrange
        String testAddress = ".invalid";
        // Act
        PvValidator addressValid = new PvValidator();
        // Assert
        assertFalse(addressValid.validatePvAddress(testAddress));
    }

    /**
     * Test method for
     * {@link uk.ac.stfc.isis.ibex.validators.PvValidator#validatePvAddress(java.lang.String)}
     * .
     */
    @Test
    public void invalid_pv_address() {
        // Arrange
        String testAddress = "invalid@";
        // Act
        PvValidator addressValid = new PvValidator();
        // Assert
        assertFalse(addressValid.validatePvAddress(testAddress));
    }

    /**
     * Test method for {@link uk.ac.stfc.isis.ibex.validators.PvValidator#getErrorMessage()}.
     */
    @Test
    public void get_message_for_valid() {
        // Arrange
        String expected = "";
        String testAddress = "valid";
        // Act
        PvValidator addressValid = new PvValidator();
        addressValid.validatePvAddress(testAddress);
        // Assert
        assertEquals(expected, addressValid.getErrorMessage());
    }

    /**
     * Test method for
     * {@link uk.ac.stfc.isis.ibex.validators.PvValidator#getErrorMessage()}.
     */
    @Test
    public void get_message_for_invalid() {
        // Arrange
        String expected = PvValidator.ADDRESS_FORMAT;
        String testAddress = "invalid@";
        // Act
        PvValidator addressValid = new PvValidator();
        addressValid.validatePvAddress(testAddress);
        // Assert
        assertEquals(expected, addressValid.getErrorMessage());
    }

    @Test
    public void get_message_for_empty_address_string() {
        // Arrange
        String expected = PvValidator.ADDRESS_EMPTY;
        String testAddress = "";
        // Act
        PvValidator addressValid = new PvValidator();
        addressValid.validatePvAddress(testAddress);
        // Assert
        assertEquals(expected, addressValid.getErrorMessage());
    }

}
