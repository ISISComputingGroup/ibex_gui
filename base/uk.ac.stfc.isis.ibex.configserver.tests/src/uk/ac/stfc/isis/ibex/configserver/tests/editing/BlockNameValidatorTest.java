package uk.ac.stfc.isis.ibex.configserver.tests.editing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
		
		testBlock = new Block(VALID_BLOCK_NAME, null, false, false, null);
		
		testEditableBlock = new EditableBlock(testBlock);

		blockList = new ArrayList<>();
		blockList.add(testEditableBlock);
		
		mockConfig = mock(EditableConfiguration.class);
		when(mockConfig.getEditableBlocks()).thenReturn(blockList);
		
		validator = new BlockNameValidator(mockConfig, testBlock);
	}
	
	@Test
	public void empty_block_name_is_invalid() {
		// Assert
		assertFalse(validator.validate(""));
		assertEquals(validator.getError(), BlockNameValidator.EMPTY_NAME_MESSAGE);
	}
	
	@Test
	public void block_name_starting_with_lower_case_is_valid() {
		// Assert
		assertTrue(validator.validate("aBlock"));
	}
	
	@Test
	public void block_name_starting_with_a_capital_is_valid() {
		// Assert
		assertTrue(validator.validate("BlockName"));
	}
	
	@Test
	public void block_name_with_an_underscore_is_valid() {
		// Assert
		assertTrue(validator.validate("a_new_Block"));
	}
	
	@Test
	public void block_name_starting_with_a_number_is_invalid() {
		// Assert
		assertFalse(validator.validate("10_new_Blocks"));
		assertEquals(validator.getError(), BlockNameValidator.INVALID_START_CHAR);
	}
	
	@Test
	public void block_name_starting_with_an_underscore_is_invalid() {
		// Assert
		assertFalse(validator.validate("_new_Block"));
		assertEquals(validator.getError(), BlockNameValidator.INVALID_START_CHAR);
	}
	
	@Test
	public void block_name_with_explanation_mark_is_invalid() {
		// Assert
		assertFalse(validator.validate("new_Block!"));
		assertEquals(validator.getError(), BlockNameValidator.INVALID_CHARS_MESSAGE);
	}
	
	@Test
	public void block_name_with_hyphen_is_invalid() {
		// Assert
		assertFalse(validator.validate("new-Block"));
		assertEquals(validator.getError(), BlockNameValidator.INVALID_CHARS_MESSAGE);
	}
	
	@Test
	public void block_name_with_at_symbol_is_invalid() {
		// Assert
		assertFalse(validator.validate("new@Block"));
		assertEquals(validator.getError(), BlockNameValidator.INVALID_CHARS_MESSAGE);
	}
	
	@Test
	public void duplicated_block_name_is_invalid() {
		// Assert
		assertFalse(validator.validate(VALID_BLOCK_NAME));
		assertEquals(validator.getError(), BlockNameValidator.DUPLICATE_GROUP_MESSAGE + ": " + VALID_BLOCK_NAME);
	}
	
	@Test
	public void error_message_is_empty_before_validation() {
		// Assert
		assertEquals(validator.getError(), "");
	}
	
	@Test
	public void error_message_is_empty_on_success() {
		// Act
		assertTrue(validator.validate("aBlock"));
		
		// Assert
		assertEquals(validator.getError(), "");
	}
	
	@Test
	public void error_message_is_empty_on_success_after_previous_failutre() {
		// Act
		validator.validate("_!");
		validator.validate("aBlock");
		
		// Assert
		assertEquals(validator.getError(), "");
	}
}
