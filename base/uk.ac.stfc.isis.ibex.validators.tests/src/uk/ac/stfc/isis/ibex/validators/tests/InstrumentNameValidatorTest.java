
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

package uk.ac.stfc.isis.ibex.validators.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.validators.InstrumentNameValidator;

@SuppressWarnings("checkstyle:methodname")
public class InstrumentNameValidatorTest {

    private static final String PREFIX = "NDW";
    private ArrayList<String> knownValidNames;

    @Before
    public void setUp() {
        knownValidNames = new ArrayList<String>();
        knownValidNames.add("valid0");
        knownValidNames.add("valid1");
    }

    /**
     * Test method for
     * {@link uk.ac.stfc.isis.ibex.validators.InstrumentNameValidator#validateInstrumentName(java.lang.String)}.
     */
    @Test
    public void WHEN_alphanumeric_name_with_prefix_is_validated_THEN_name_is_valid() {
        // Arrange
        String name = PREFIX + "valid123";
        InstrumentNameValidator valid = new InstrumentNameValidator(knownValidNames, () -> false);

        // Act - Assert
        assertTrue(valid.validateInstrumentName(name));
    }

    /**
     * Test method for
     * {@link uk.ac.stfc.isis.ibex.validators.InstrumentNameValidator#validateInstrumentName(java.lang.String)}.
     */
    @Test
    public void WHEN_alphanumeric_name_with_dash_and_with_prefix_is_validated_THEN_name_is_valid() {
        // Arrange
        String name = PREFIX + "valid-123";
        InstrumentNameValidator valid = new InstrumentNameValidator(knownValidNames, () -> false);

        // Act - Assert
        assertTrue(valid.validateInstrumentName(name));
    }

    /**
     * Test method for
     * {@link uk.ac.stfc.isis.ibex.validators.InstrumentNameValidator#validateInstrumentName(java.lang.String)}.
     */
    @Test
    public void WHEN_known_valid_name_without_prefix_is_validated_THEN_name_is_valid() {
        // Arrange
        String name = knownValidNames.get(0);
        InstrumentNameValidator valid = new InstrumentNameValidator(knownValidNames, () -> false);

        // Act - Assert
        assertTrue(valid.validateInstrumentName(name));
    }

    /**
     * Test method for
     * {@link uk.ac.stfc.isis.ibex.validators.InstrumentNameValidator#validateInstrumentName(java.lang.String)}.
     */
    @Test
    public void WHEN_non_alphanumeric_name_is_validated_THEN_name_is_invalid() {
        // Arrange
        String name = PREFIX + "invalid@";
        InstrumentNameValidator valid = new InstrumentNameValidator(knownValidNames, () -> false);

        // Act - Assert
        assertFalse(valid.validateInstrumentName(name));
    }

    /**
     * Test method for
     * {@link uk.ac.stfc.isis.ibex.validators.InstrumentNameValidator#validateInstrumentName(java.lang.String)}.
     */
    @Test
    public void WHEN_valid_name_is_validated_THEN_message_is_correct() {
        // Arrange
        String expected = "";
        String name = PREFIX + "valid123";
        InstrumentNameValidator valid = new InstrumentNameValidator(knownValidNames, () -> false);

        // Act
        valid.validateInstrumentName(name);

        // Assert
        assertEquals(expected, valid.getErrorMessage());
    }

    /**
     * Test method for
     * {@link uk.ac.stfc.isis.ibex.validators.InstrumentNameValidator#validateInstrumentName(java.lang.String)}.
     */
    @Test
    public void WHEN_non_alphanumeric_name_is_validated_THEN_message_is_correct() {
        // Arrange
        String expected = InstrumentNameValidator.NAME_FORMAT_MSG;
        String name = PREFIX + "invalid@";
        InstrumentNameValidator valid = new InstrumentNameValidator(knownValidNames, () -> false);

        // Act
        valid.validateInstrumentName(name);

        // Assert
        assertEquals(expected, valid.getErrorMessage());
    }

    /**
     * Test method for
     * {@link uk.ac.stfc.isis.ibex.validators.InstrumentNameValidator#validateInstrumentName(java.lang.String)}.
     */
    @Test
    public void WHEN_empty_name_is_validated_THEN_message_is_correct() {
        // Arrange
        String expected = InstrumentNameValidator.NAME_EMPTY_MSG;
        String name = "";
        InstrumentNameValidator valid = new InstrumentNameValidator(knownValidNames, () -> false);

        // Act
        valid.validateInstrumentName(name);

        // Assert
        assertEquals(expected, valid.getErrorMessage());
    }
}
