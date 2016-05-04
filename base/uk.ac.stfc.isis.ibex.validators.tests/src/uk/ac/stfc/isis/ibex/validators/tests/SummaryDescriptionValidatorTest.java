
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

import org.eclipse.core.runtime.IStatus;
import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.validators.BlockServerNameValidator;
import uk.ac.stfc.isis.ibex.validators.SummaryDescriptionValidator;

@SuppressWarnings("checkstyle:methodname")
public class SummaryDescriptionValidatorTest {

    SummaryDescriptionValidator validator;
    MessageDisplayerStub messageDisplayer;
    private BlockServerNameValidator decriptionRules;
    private ArrayList<String> disallowed = new ArrayList<String>();

    @Before
    public void setUp() {

        messageDisplayer = new MessageDisplayerStub();

        decriptionRules = new BlockServerNameValidator("^[a-zA-Z][\\w ]*$", "Error message", disallowed);

        validator = new SummaryDescriptionValidator(messageDisplayer, decriptionRules);
    }

    @Test
    public void GIVEN_empty_description_THEN_invalid() {
        // Arrange
        String name = "";

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertFalse(result.isOK());
        assertEquals("Displayed error message", "Description must not be empty", messageDisplayer.message);
    }

    @Test
    public void GIVEN_valid_description_THEN_valid() {
        // Arrange
        String name = "This is valid";

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertTrue(result.isOK());
        assertNull("Displayed error message", messageDisplayer.message);
    }

    @Test
    public void GIVEN_invalid_group_name_THEN_invalid() {
        // Arrange
        String expectedErrorMessage = "Error message";
        decriptionRules =
                new BlockServerNameValidator("^[a-zA-Z]\\w*$", expectedErrorMessage, new ArrayList<String>());
        String name = "invalid&";

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertFalse(result.isOK());
        assertTrue("Error message", result.getMessage().contains(expectedErrorMessage));
        assertEquals("Displayed error message", result.getMessage(), messageDisplayer.message);
    }

    @Test
    public void GIVEN_disallowed_group_name_THEN_invalid() {
        // Arrange
        String name = "name";
        disallowed.add(name);

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertFalse(result.isOK());
        String message = result.getMessage();
        assertTrue("Error message", message.contains(name));
        assertTrue("Error message \'" + message + "\' is not corrent", message.contains("Description cannot be"));
        assertEquals("Displayed error message", result.getMessage(), messageDisplayer.message);
    }

    @Test
    public void GIVEN_no_group_rules_THEN_valid() {
        // Arrange
        String name = "valid123";
        validator = new SummaryDescriptionValidator(messageDisplayer, null);

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertTrue(result.isOK());
        assertNull("Displayed error message", messageDisplayer.message);
    }

    @Test
    public void GIVEN_no_message_displayer_WHEN_error_THEN_return_valid() {
        // Arrange
        String name = "valid123";
        validator = new SummaryDescriptionValidator(null, decriptionRules);

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertTrue(result.isOK());
    }

}
