
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.editing.BlockNameValidator;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;

public class BlockNameValidatorTest {
	
	private static final String VALID_BLOCK_NAME = "valid_block_name";
	
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
		when(mockConfig.getEditableBlocks()).thenReturn(blockList);
		
		validator = new BlockNameValidator(mockConfig, testBlock);
	}
	
	private void addBlock(EditableBlock block) {
		blockList.add(block);
	}
	
	@Test
	public void empty_block_name_is_invalid() {
		// Assert
		assertFalse(validator.isValidName(""));
		assertEquals(validator.getErrorMessage(), BlockNameValidator.EMPTY_NAME_MESSAGE);
	}
	
	@Test
	public void block_name_starting_with_lower_case_is_valid() {
		// Assert
		assertTrue(validator.isValidName("aBlock"));
	}
	
	@Test
	public void block_name_starting_with_a_capital_is_valid() {
		// Assert
		assertTrue(validator.isValidName("BlockName"));
	}
	
	@Test
	public void block_name_with_an_underscore_is_valid() {
		// Assert
		assertTrue(validator.isValidName("a_new_Block"));
	}
	
	@Test
	public void block_name_starting_with_a_number_is_invalid() {
		// Assert
		assertFalse(validator.isValidName("10_new_Blocks"));
		assertEquals(validator.getErrorMessage(), BlockNameValidator.INVALID_START_CHAR);
	}
	
	@Test
	public void block_name_starting_with_an_underscore_is_invalid() {
		// Assert
		assertFalse(validator.isValidName("_new_Block"));
		assertEquals(validator.getErrorMessage(), BlockNameValidator.INVALID_START_CHAR);
	}
	
	@Test
	public void block_name_with_explanation_mark_is_invalid() {
		// Assert
		assertFalse(validator.isValidName("new_Block!"));
		assertEquals(validator.getErrorMessage(), BlockNameValidator.INVALID_CHARS_MESSAGE);
	}
	
	@Test
	public void block_name_with_hyphen_is_invalid() {
		// Assert
		assertFalse(validator.isValidName("new-Block"));
		assertEquals(validator.getErrorMessage(), BlockNameValidator.INVALID_CHARS_MESSAGE);
	}
	
	@Test
	public void block_name_with_at_symbol_is_invalid() {
		// Assert
		assertFalse(validator.isValidName("new@Block"));
		assertEquals(validator.getErrorMessage(), BlockNameValidator.INVALID_CHARS_MESSAGE);
	}
	
	@Test
	public void duplicated_block_name_is_invalid() {
		// Arrange
		addBlock(testEditableBlock);
		
		// Assert
		assertFalse(validator.isValidName(VALID_BLOCK_NAME));
		assertEquals(validator.getErrorMessage(), BlockNameValidator.DUPLICATE_GROUP_MESSAGE + ": " + VALID_BLOCK_NAME);
	}
	
	@Test
	public void unique_block_name_is_valid() {
		// setup
		addBlock(testEditableBlock);
		
		// Assert
		assertTrue(validator.isValidName("other_name"));
	}
	
	@Test
	public void error_message_is_empty_before_validation() {
		// Assert
		assertEquals(validator.getErrorMessage(), "");
	}
	
	@Test
	public void error_message_is_empty_on_success() {
		// Act
		assertTrue(validator.isValidName("aBlock"));
		
		// Assert
		assertEquals(validator.getErrorMessage(), "");
	}
	
	@Test
	public void error_message_is_empty_on_success_after_previous_failutre() {	
		// Act
		validator.isValidName("_!");
		validator.isValidName("aBlock");
		
		// Assert
		assertEquals(validator.getErrorMessage(), "");
	}
}
