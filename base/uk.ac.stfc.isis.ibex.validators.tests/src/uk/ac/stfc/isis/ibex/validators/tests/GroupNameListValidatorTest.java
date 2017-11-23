
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2017
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
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

import uk.ac.stfc.isis.ibex.validators.BlockServerNameValidator;
import uk.ac.stfc.isis.ibex.validators.GroupNameValidator;
import uk.ac.stfc.isis.ibex.validators.GroupNamesProvider;

@SuppressWarnings("checkstyle:methodname")
public class GroupNameListValidatorTest {

    GroupNamesProvider groupNamesProvider;
    List<String> namesToValidate;
    GroupNameValidator validator;
    MessageDisplayerStub messageDisplayer;
    private BlockServerNameValidator groupRules;
    private ArrayList<String> disallowed = new ArrayList<String>();
    private static final String EXPECTED_ERROR_MESSAGE = "Error message";

    @Before
    public void setUp() {
        disallowed = new ArrayList<String>();
        namesToValidate = new ArrayList<String>();

        groupNamesProvider = new GroupNamesProvider() {
            @Override
            public List<String> getGroupNames() {
                return namesToValidate;
            }
        };

        messageDisplayer = new MessageDisplayerStub();
        groupRules = new BlockServerNameValidator("^[a-zA-Z]\\w*$", EXPECTED_ERROR_MESSAGE, disallowed);
        validator = new GroupNameValidator(groupNamesProvider, messageDisplayer, groupRules);
    }

    @Test
    public void GIVEN_empty_list_THEN_valid() {
        // Arrange
        String name = "valid123";

        // Act
        IStatus result = validator.validate(name);

        assertNoError(result);
    }

    @Test
    public void GIVEN_empty_group_name_for_selected_item_THEN_invalid() {
        // Arrange
        String name = "";
        validator.setSelectedIndex(0);

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertFalse(result.isOK());
        assertTrue("Error message contains 'empty'", result.getMessage().contains("empty"));
        assertEquals("Displayed error message", result.getMessage(), messageDisplayer.message);
    }

    @Test
    public void GIVEN_empty_group_name_for_unselected_item_THEN_valid() {
        // Arrange
        String name = "";
        validator.setSelectedIndex(-1);

        // Act
        IStatus result = validator.validate(name);

        assertNoError(result);
    }

    @Test
    public void GIVEN_invalid_group_name_THEN_invalid() {
        // Arrange

        groupRules = new BlockServerNameValidator("^[a-zA-Z]\\w*$", EXPECTED_ERROR_MESSAGE, new ArrayList<String>());
        String name = "invalid&";
        validator.setSelectedIndex(0);

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertFalse(result.isOK());
        assertTrue("Error message", result.getMessage().contains(EXPECTED_ERROR_MESSAGE));
        assertEquals("Displayed error message", result.getMessage(), messageDisplayer.message);
    }

    @Test
    public void GIVEN_disallowed_group_name_THEN_invalid() {
        // Arrange
        String name = "name";
        disallowed.add(name);
        validator.setSelectedIndex(0);

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
        String duplicateName = "valid123";
        String originalName = "valid12";
        namesToValidate.add(originalName);
        namesToValidate.add(duplicateName);
        validator.setSelectedIndex(0);

        // Act
        IStatus result = validator.validate(duplicateName);

        // Assert
        assertFalse(result.isOK());
        String message = result.getMessage();
        assertTrue("Error message", message.contains("unique"));
        assertEquals("Displayed error message", result.getMessage(), messageDisplayer.message);
    }

    @Test
    public void GIVEN_duplicate_name_is_selected_name_THEN_valid() {
        /**
         * Here we are testing the scenario where group name entered is: 1)
         * groupname 2) groupname& (this is invalid so will not set the group
         * name in the list) 3) groupname (revert to original name but this is
         * the same as the original name it is not a duplicate)
         */
        // Arrange
        String duplicateName = "valid123";
        namesToValidate.add(duplicateName);
        validator.setSelectedIndex(0);

        // Act
        IStatus result = validator.validate(duplicateName);

        // Assert
        assertNoError(result);
    }

    /**
     * @param result
     */
    private void assertNoError(IStatus result) {
        assertTrue(result.isOK());
        assertNull("Displayed error message", messageDisplayer.message);
    }

    @Test
    public void GIVEN_null_for_group_rules_THEN_valid() {
        // Arrange
        String name = "valid123";
        validator = new GroupNameValidator(groupNamesProvider, messageDisplayer, null);
        validator.setSelectedIndex(0);

        // Act
        IStatus result = validator.validate(name);

        assertNoError(result);
    }

    @Test
    public void GIVEN_null_for_disallowed_THEN_valid() {
        // Arrange
        String name = "valid123";

        groupRules = new BlockServerNameValidator("^[a-zA-Z]\\w*$", EXPECTED_ERROR_MESSAGE, null);
        validator = new GroupNameValidator(groupNamesProvider, messageDisplayer, groupRules);
        validator.setSelectedIndex(0);

        // Act
        IStatus result = validator.validate(name);

        assertNoError(result);
    }

    @Test
    public void GIVEN_null_for_reg_ex_THEN_valid() {
        // Arrange
        String name = "valid123";

        groupRules = new BlockServerNameValidator(null, EXPECTED_ERROR_MESSAGE, null);
        validator = new GroupNameValidator(groupNamesProvider, messageDisplayer, groupRules);
        validator.setSelectedIndex(0);

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertTrue(result.isOK());
    }

    @Test
    public void GIVEN_null_name_THEN_invalid() {
        // Arrange
        String name = null;
        validator.setSelectedIndex(0);

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertFalse(result.isOK());
        assertEquals("Displayed error message", result.getMessage(), messageDisplayer.message);
    }

    @Test
    public void GIVEN_invalid_name_in_list_but_not_being_edited_THEN_invalid() {
        // Arrange
        groupRules = new BlockServerNameValidator("^[a-zA-Z]\\w*$", EXPECTED_ERROR_MESSAGE, new ArrayList<String>());
        String validName = "valid123";
        validator.setSelectedIndex(0);
        String invalidName = "&%*&\"^*()";

        namesToValidate.add(validName);
        namesToValidate.add(invalidName);

        // Act
        IStatus result = validator.validate(validName);

        // Assert
        assertFalse(result.isOK());
        String message = result.getMessage();
        assertTrue("Error message", message.contains(EXPECTED_ERROR_MESSAGE));
        assertEquals("Displayed error message", result.getMessage(), messageDisplayer.message);
    }

    @Test
    public void WHEN_name_other_is_lower_case_THEN_invalid() {
        // Arrange
        String expectedErrorMessage = "Other";
        String name = "other";
        validator.setSelectedIndex(0);

        // Act
        IStatus result = validator.validate(name);

        // Assert
        assertFalse(result.isOK());
        assertTrue("Error message", result.getMessage().contains(expectedErrorMessage));
        assertEquals("Displayed error message", result.getMessage(), messageDisplayer.message);
    }

    @Test
    public void WHEN_name_other_is_upper_case_THEN_invalid() {
        // Arrange
        String expectedErrorMessage = "Other";
        String name = "OTHER";
        validator.setSelectedIndex(0);
        // Act
        IStatus result = validator.validate(name);
        // Assert
        assertFalse(result.isOK());
        assertTrue("Error message", result.getMessage().contains(expectedErrorMessage));
        assertEquals("Displayed error message", result.getMessage(), messageDisplayer.message);
    }

}
