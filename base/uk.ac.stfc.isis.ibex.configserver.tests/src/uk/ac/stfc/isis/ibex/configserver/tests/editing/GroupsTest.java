
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

import java.util.List;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableGroup;

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
		groups.add(JAWS);
		EditableConfiguration edited = edit(config());

		assertContains(edited.asConfiguration().getGroups(), JAWS);
		
		EditableGroup jaws = getFirst(edited.getEditableGroups());
		edited.removeGroup(jaws);		
		assertEmpty(edited.asConfiguration().getGroups());
	}
	
	@Test
	public void two_groups_can_be_swapped() {
		groups.add(JAWS);
		groups.add(TEMPERATURE);
		EditableConfiguration edited = edit(config());
		
		List<EditableGroup> grps = (List<EditableGroup>) edited.getEditableGroups();

		edited.swapGroups(grps.get(0), grps.get(1));
		
		grps = (List<EditableGroup>) edited.getEditableGroups();
			
		assertEquals(grps.get(0).getName(), "TEMPERATURE");
		assertEquals(grps.get(1).getName(), "JAWS");
		
	}
}
