
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

package uk.ac.stfc.isis.ibex.configserver.tests.editing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.BlockRules;
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.BlockNameValidator;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableComponents;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;

@SuppressWarnings("checkstyle:methodname")
public class BlockNameValidatorTest {
	
	private static final String VALID_BLOCK_NAME = "valid_block_name";
	
	
	private static final String REGEX = "\\w*";
	private static final String REGEX_MESSAGE = "REGEX FAILED";
	private static final String DISALLOWED_NAME = "bad";
	private BlockRules mockBlockRules = new BlockRules(REGEX, REGEX_MESSAGE, Arrays.asList(DISALLOWED_NAME));
	
	private Block testBlock;

	private EditableBlock testEditableBlock;
	
	private ArrayList<EditableBlock> blockList;

	private EditableConfiguration mockConfig;
	private BlockNameValidator validator;
	
	@Before
	public void setUp() {
		// Arrange
		
        testBlock = new Block(VALID_BLOCK_NAME, null, false, false);
		
		testEditableBlock = new EditableBlock(testBlock);

		blockList = new ArrayList<>();
		blockList.add(testEditableBlock);
		
		mockConfig = mock(EditableConfiguration.class);
        when(mockConfig.getAllBlocks()).thenReturn(blockList);
		
		validator = new BlockNameValidator(mockConfig, testBlock);
	}
	
	private void addBlock(EditableBlock block) {
		blockList.add(block);
	}
	
	@Test
	public void empty_block_name_is_invalid() {
		// Assert
		assertFalse(validator.isValidName("", mockBlockRules));
		assertEquals(validator.getErrorMessage(), BlockNameValidator.EMPTY_NAME_MESSAGE);
	}
	
	@Test
	public void forbidden_block_name_is_invalid() {
		// Assert
		assertFalse(validator.isValidName(DISALLOWED_NAME, mockBlockRules));
		assertEquals(validator.getErrorMessage(), BlockNameValidator.FORBIDDEN_NAME_MESSAGE + DISALLOWED_NAME);
	}
	
	@Test
	public void forbidden_block_name_is_invalid_case_insensitive() {
		// Assert
		assertFalse(validator.isValidName(DISALLOWED_NAME.toUpperCase(), mockBlockRules));
		assertEquals(validator.getErrorMessage(), BlockNameValidator.FORBIDDEN_NAME_MESSAGE + DISALLOWED_NAME);
	}
	
	@Test
	public void containing_forbidden_block_name_is_valid() {
		// Assert
		assertTrue(validator.isValidName("abad", mockBlockRules));
	}
	
	@Test
	public void forbidden_regex_is_invalid() {
		// Assert
		assertFalse(validator.isValidName("^&", mockBlockRules));
		assertEquals(validator.getErrorMessage(), REGEX_MESSAGE);
	}
	
	@Test
	public void duplicated_block_name_is_invalid() {
		// Arrange
		addBlock(testEditableBlock);
		
		// Assert
		assertFalse(validator.isValidName(VALID_BLOCK_NAME, mockBlockRules));
		assertEquals(validator.getErrorMessage(), BlockNameValidator.DUPLICATE_GROUP_MESSAGE + ": " + VALID_BLOCK_NAME);
	}
	
	@Test
	public void unique_block_name_is_valid() {
		// setup
		addBlock(testEditableBlock);
		
		// Assert
		assertTrue(validator.isValidName("other_name", mockBlockRules));
	}
	
	@Test
	public void error_message_is_empty_before_validation() {
		// Assert
		assertEquals(validator.getErrorMessage(), "");
	}
	
	@Test
	public void error_message_is_empty_on_success() {
		// Act
		assertTrue(validator.isValidName("aBlock", mockBlockRules));
		
		// Assert
		assertEquals(validator.getErrorMessage(), "");
	}
	
	@Test
	public void error_message_is_empty_on_success_after_previous_failure() {	
		// Act
		validator.isValidName("", mockBlockRules);
		validator.isValidName("aBlock", mockBlockRules);
		
		// Assert
		assertEquals(validator.getErrorMessage(), "");
	}

    @Test
	public void duplicated_block_name_from_component_is_invalid() {
        // Arrange
        Configuration comp = mock(Configuration.class);
        when(comp.getBlocks()).thenReturn(Arrays.asList(testBlock));
        EditableComponents editableComp = mock(EditableComponents.class);
        when(editableComp.getSelected()).thenReturn(Arrays.asList(comp));
        when(mockConfig.getEditableComponents()).thenReturn(editableComp);
        
        // Assert
        assertFalse(validator.isValidName(VALID_BLOCK_NAME, mockBlockRules));
        assertEquals(validator.getErrorMessage(), BlockNameValidator.DUPLICATE_GROUP_MESSAGE + ": " + VALID_BLOCK_NAME);
	    
	}
}
