
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
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.validators.BlockServerNameValidation;
import uk.ac.stfc.isis.ibex.validators.GroupNameValidator;
import uk.ac.stfc.isis.ibex.validators.GroupNamesProvider;

@SuppressWarnings("checkstyle:methodname")
public class GroupNameListValidatorTest {

    GroupNamesProvider groupNamesProvider;
    List<String> namesToValidate;
    GroupNameValidator validator;
    MessageDisplayerStub messageDisplayer;
    private BlockServerNameValidation groupRules;
    private ArrayList<String> disallowed = new ArrayList<String>();

    @Before
    public void setUp() {
        namesToValidate = new ArrayList<String>();

        groupNamesProvider = new GroupNamesProvider() {

            @Override
            public List<String> getGroupNames() {
                return namesToValidate;
            }
        };

        messageDisplayer = new MessageDisplayerStub();

        groupRules = new BlockServerNameValidation("^[a-zA-Z]\\w*$", "Error message", disallowed);

        validator = new GroupNameValidator(groupNamesProvider, messageDisplayer, groupRules);

    }

    @Test
    public void GIVEN_empty_list_THEN_valid() {
        // Arrange
        String name = "valid123";

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertTrue(result.isOK());
        assertNull("Displayed error message", messageDisplayer.message);
    }

    @Test
    public void GIVEN_empty_group_name_THEN_invalid() {
        // Arrange
        String name = "";

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertFalse(result.isOK());
        assertTrue("Error message contains 'empty'", result.getMessage().contains("empty"));
        assertEquals("Displayed error message", result.getMessage(), messageDisplayer.message);
    }

    @Test
    public void GIVEN_invalid_group_name_THEN_invalid() {
        // Arrange
        String expected_error_message = "Error message";
        groupRules = new BlockServerNameValidation("^[a-zA-Z]\\w*$", expected_error_message, new ArrayList<String>());
        String name = "invalid&";

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertFalse(result.isOK());
        assertTrue("Error message", result.getMessage().contains(expected_error_message));
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
        assertTrue("Error message \'" + message + "\' is not corrent",
                message.contains("Group name cannot be"));
        assertEquals("Displayed error message", result.getMessage(), messageDisplayer.message);
    }

    @Test
    public void GIVEN_duplicate_name_THEN_invalid() {
        // Arrange
        String name = "valid123";
        namesToValidate.add(name);
        namesToValidate.add(name);

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertFalse(result.isOK());
        String message = result.getMessage();
        assertTrue("Error message", message.contains("unique"));
        assertEquals("Displayed error message", result.getMessage(), messageDisplayer.message);
    }

    @Test
    public void GIVEN_no_group_rules_THEN_valid() {
        // Arrange
        String name = "valid123";
        validator = new GroupNameValidator(groupNamesProvider, messageDisplayer, null);

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertTrue(result.isOK());
        assertNull("Displayed error message", messageDisplayer.message);
    }

    @Test
    public void GIVEN_no_disallowed_THEN_valid() {
        // Arrange
        String name = "valid123";
        groupRules = new BlockServerNameValidation("^[a-zA-Z]\\w*$", "Error message", null);
        validator = new GroupNameValidator(groupNamesProvider, messageDisplayer, groupRules);

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertTrue(result.isOK());
        assertNull("Displayed error message", messageDisplayer.message);
    }

    @Test
    public void GIVEN_no_reg_ex_THEN_valid() {
        // Arrange
        String name = "valid123";
        groupRules = new BlockServerNameValidation(null, "Error message", null);
        validator = new GroupNameValidator(groupNamesProvider, messageDisplayer, groupRules);

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertTrue(result.isOK());
    }

    @Test
    public void GIVEN_null_name_THEN_invalid() {
        // Arrange
        String name = null;

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertFalse(result.isOK());
        assertEquals("Displayed error message", result.getMessage(), messageDisplayer.message);
    }

    @Test
    public void GIVEN_invlaid_name_in_list_but_not_being_edited_THEN_invalid() {
        // Arrange
        String expected_error_message = "Error message";
        groupRules = new BlockServerNameValidation("^[a-zA-Z]\\w*$", expected_error_message, new ArrayList<String>());
        String valid_name = "valid123";
        String invalid_name = "&%*&\"^*()";

        namesToValidate.add(valid_name);
        namesToValidate.add(invalid_name);

        // Act
        IStatus result = validator.validate(valid_name);

        // Assert
        assertFalse(result.isOK());
        String message = result.getMessage();
        assertTrue("Error message", message.contains(expected_error_message));
        assertEquals("Displayed error message", result.getMessage(), messageDisplayer.message);
    }

}
