
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

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;

public class BlocksTest extends EditableConfigurationTest {
	
	@Test
	public void a_new_config_has_no_blocks() {
		// Arrange
		EditableConfiguration edited = edit(emptyConfig());
		
		// Assert
		assertEmpty(edited.asConfiguration().getBlocks());
	}	
	
	@Test
	public void a_new_block_can_be_added() {
		// Arrange
		EditableConfiguration edited = edit(emptyConfig());
	
		// Act
		edited.addNewBlock();
		
		// Assert
		assertNotEmpty(edited.asConfiguration().getBlocks());
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
}
