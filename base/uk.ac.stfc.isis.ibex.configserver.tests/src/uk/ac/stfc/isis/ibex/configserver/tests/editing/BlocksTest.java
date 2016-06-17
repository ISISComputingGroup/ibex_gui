
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
import java.util.Collection;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;

@SuppressWarnings("checkstyle:methodname")
public class BlocksTest extends EditableConfigurationTest {
	
	@Test
	public void a_new_config_has_no_blocks() {
		// Arrange
		EditableConfiguration edited = edit(emptyConfig());
		
		// Assert
		assertEmpty(edited.asConfiguration().getBlocks());
	}	

    @Test
    public void a_new_block_can_be_created() {
        // Arrange
        EditableConfiguration edited = edit(emptyConfig());

        // Act
        EditableBlock block = edited.createNewBlock();

        // Assert
        assertEquals(EditableBlock.class, block.getClass());
    }

	@Test
    public void a_new_block_can_be_added_to_the_configuration() {
		// Arrange
		EditableConfiguration edited = edit(emptyConfig());
        EditableBlock block = edited.createNewBlock();
	
		// Act
		edited.addNewBlock(block);
		
		// Assert
		assertNotEmpty(edited.asConfiguration().getBlocks());
	}

    @Test
    public void a_new_block_gets_a_unique_name() {
        // Arrange
        EditableConfiguration edited = edit(emptyConfig());
        EditableBlock block1 = edited.createNewBlock();
        edited.addNewBlock(block1);

        // Act
        EditableBlock block2 = edited.createNewBlock();

        // Assert
        assertNotEquals(block1.getName(), block2.getName());
    }

	@Test
	public void a_block_can_be_removed() {
		// Arrange
		blocks.add(GAPX);
		EditableConfiguration edited = edit(config());
		
		// Act
		EditableBlock gapx = getFirst(edited.getEditableBlocks());
		edited.removeBlock(gapx);
		
		// Assert
		assertEmpty(edited.asConfiguration().getBlocks());
	}
	
	@Test
	public void multiple_blocks_can_be_removed() {
		// Arrange
		blocks.add(GAPX);
		blocks.add(GAPY);
		EditableConfiguration edited = edit(config());
		
		// Act
		Collection<EditableBlock> returnedBlocks = edited.getEditableBlocks();
		edited.removeBlocks(new ArrayList<EditableBlock>(returnedBlocks));
		
		// Assert
		assertEmpty(edited.asConfiguration().getBlocks());
	}
}
