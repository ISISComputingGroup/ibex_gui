
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

import java.util.Collection;
import java.util.List;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableGroup;

@SuppressWarnings("checkstyle:methodname")
public class GroupsTest extends EditableConfigurationTest {
		
	@Test
	public void a_new_group_can_be_added() {
		EditableConfiguration edited = edit(emptyConfig());
		assertEmpty(edited.asConfiguration().getGroups());
		
		edited.addNewGroup();
		assertNotEmpty(edited.asConfiguration().getGroups());
	}
	
	@Test
	public void a_group_can_be_removed() {
		groups.add(EMPTY_GROUP_01);
		EditableConfiguration edited = edit(config());

		assertContains(edited.asConfiguration().getGroups(), EMPTY_GROUP_01);
		
		EditableGroup jaws = getFirst(edited.getEditableGroups());
		edited.removeGroup(jaws);		
		assertEmpty(edited.asConfiguration().getGroups());
	}
	
	@Test
	public void two_groups_can_be_swapped() {
		groups.add(EMPTY_GROUP_01);
		groups.add(EMPTY_GROUP_02);
		EditableConfiguration edited = edit(config());
		
		List<EditableGroup> grps = (List<EditableGroup>) edited.getEditableGroups();

		edited.swapGroups(grps.get(0), grps.get(1));
		
		grps = (List<EditableGroup>) edited.getEditableGroups();
			
		assertEquals(grps.get(0).getName(), "EMPTY_GROUP_02");
		assertEquals(grps.get(1).getName(), "EMPTY_GROUP_01");
	}
	
	@Test
	public void GIVEN_group_with_one_block_THEN_editable_group_contains_block() {
		blocks.add(GAPX);
		groups.add(JAWS);
		EditableConfiguration edited = edit(config());
		
		List<Group> grps = (List<Group>) edited.asConfiguration().getGroups();

		assertContains(grps, JAWS);
		assertContains(grps.get(0).getBlocks(), "GAPX");
		
		Collection<EditableBlock> selectedBlocks = getFirst(edited.getEditableGroups()).getSelectedBlocks();
		assertBlocksAreEqual(blocks, selectedBlocks);
	}
	
	@Test
	public void GIVEN_group_with_one_block_THEN_block_not_in_config_available_blocks() {
		blocks.add(GAPY);
		blocks.add(GAPX);
		groups.add(JAWS);
		EditableConfiguration edited = edit(config());
		
		List<EditableBlock> availableBlocks = (List<EditableBlock>) edited.getAvailableBlocks();

		assertEquals(availableBlocks.size(), 1);
		assertEquals(availableBlocks.get(0).getName(), "GAPY");
	}
	
	@Test
	public void GIVEN_group_with_one_block_THEN_other_block_in_groups_unselected_blocks() {
		blocks.add(GAPY);
		blocks.add(GAPX);
		groups.add(JAWS);
		EditableConfiguration edited = edit(config());
		
		List<EditableBlock> selectedBlocks = (List<EditableBlock>) getFirst(edited.getEditableGroups()).getUnselectedBlocks();

		assertEquals(selectedBlocks.size(), 1);
		assertEquals(selectedBlocks.get(0).getName(), "GAPY");
	}
}
