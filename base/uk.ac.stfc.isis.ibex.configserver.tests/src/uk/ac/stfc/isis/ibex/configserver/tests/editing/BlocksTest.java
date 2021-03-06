
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.editing.BlockFactory;
import uk.ac.stfc.isis.ibex.configserver.editing.DuplicateBlockNameException;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;

@SuppressWarnings("checkstyle:methodname")
public class BlocksTest extends EditableConfigurationTest {

	@Test
    public void GIVEN_new_config_THEN_list_of_blocks_is_empty() {
		// Arrange
        EditableConfiguration edited = edit(emptyConfig());
		
		// Assert
		assertEmpty(edited.asConfiguration().getBlocks());
	}	

    @Test
    public void WHEN_new_block_is_created_THEN_new_object_is_not_null_and_of_type_EditableBlock() {
        // Arrange
        EditableConfiguration edited = edit(emptyConfig());
        BlockFactory blockFactory = new BlockFactory(edited);

        // Act
        EditableBlock block = blockFactory.createNewBlock(Optional.empty());

        // Assert
        assertEquals(EditableBlock.class, block.getClass());
    }
    
    @Test
    public void WHEN_new_block_is_created_with_a_starting_PV_THEN_that_PV_is_set() {
        // Arrange
        String testPV = "TEST:PV";
        EditableConfiguration edited = edit(emptyConfig());
        BlockFactory blockFactory = new BlockFactory(edited);

        // Act
        EditableBlock block = blockFactory.createNewBlock(Optional.of(testPV));

        // Assert
        assertEquals(testPV, block.getPV());
    }
    
    @Test
    public void WHEN_new_block_is_created_with_no_PV_THEN_PV_is_set_to_empty_string() {
        // Arrange
        EditableConfiguration edited = edit(emptyConfig());
        BlockFactory blockFactory = new BlockFactory(edited);

        // Act
        EditableBlock block = blockFactory.createNewBlock(Optional.empty());

        // Assert
        assertEquals("", block.getPV());
    }

	@Test
    public void
            GIVEN_a_new_block_and_config_WHEN_block_is_added_to_config_THEN_list_of_blocks_in_config_contains_new_block()
                    throws DuplicateBlockNameException {
		// Arrange
		EditableConfiguration edited = edit(emptyConfig());
        BlockFactory blockFactory = new BlockFactory(edited);
        EditableBlock block = blockFactory.createNewBlock(Optional.empty());
	
		// Act
		edited.addNewBlock(block);
		
		// Assert
		assertNotEmpty(edited.asConfiguration().getBlocks());
	}

    @Test
    public void GIVEN_a_block_in_the_config_WHEN_another_block_is_created_THEN_those_blocks_have_unique_names()
            throws DuplicateBlockNameException {
        // Arrange
        EditableConfiguration edited = edit(emptyConfig());
        BlockFactory blockFactory = new BlockFactory(edited);
        EditableBlock block1 = blockFactory.createNewBlock(Optional.empty());
        edited.addNewBlock(block1);

        // Act
        EditableBlock block2 = blockFactory.createNewBlock(Optional.empty());

        // Assert
        assertNotEquals(block1.getName(), block2.getName());
    }

    @Test
    public void GIVEN_a_block_in_the_config_WHEN_trying_to_add_block_of_same_name_THEN_block_is_not_added()
            throws DuplicateBlockNameException {
        // Arrange
        EditableConfiguration edited = edit(emptyConfig());
        BlockFactory blockFactory = new BlockFactory(edited);
        EditableBlock block1 = blockFactory.createNewBlock(Optional.empty());
        edited.addNewBlock(block1);

        // Act
        EditableBlock block2 = blockFactory.createNewBlock(Optional.empty());
        block2.setName(block1.getName());

        // Assert
        assertTrue(!edited.getAllBlocks().contains(block2));
        
    }
	@Test
    public void GIVEN_a_block_is_stored_in_config_WHEN_block_is_removed_THEN_block_is_no_longer_stored_in_config() {
		// Arrange
		blocks.add(GAPX);
		EditableConfiguration edited = edit(config());
		
		// Act
		EditableBlock gapx = getFirst(edited.getAllBlocks());
		edited.removeBlocks(Arrays.asList(gapx));
		
		// Assert
		assertEmpty(edited.asConfiguration().getBlocks());
	}
	
	@Test
    public void WHEN_multiple_blocks_removed_from_config_THEN_all_those_blocks_no_longer_stored_in_config() {
		// Arrange
		blocks.add(GAPX);
		blocks.add(GAPY);
		EditableConfiguration edited = edit(config());
		
		// Act
		Collection<EditableBlock> returnedBlocks = edited.getAllBlocks();
		edited.removeBlocks(new ArrayList<EditableBlock>(returnedBlocks));
		
		// Assert
		assertEmpty(edited.asConfiguration().getBlocks());
	}
}
