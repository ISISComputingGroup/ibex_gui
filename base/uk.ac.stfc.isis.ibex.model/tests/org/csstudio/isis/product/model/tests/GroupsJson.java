
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

package uk.ac.stfc.isis.ibex.product.model.tests;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;

import uk.ac.stfc.isis.ibex.product.model.RecordState;
import uk.ac.stfc.isis.ibex.product.model.blocks.Block;
import uk.ac.stfc.isis.ibex.product.model.blocks.Group;
import uk.ac.stfc.isis.ibex.product.model.blocks.json.GroupsGsonWriter;
import uk.ac.stfc.isis.ibex.product.model.blocks.json.IGroupJsonWriter;
import org.junit.BeforeClass;
import org.junit.Test;

public class GroupsJson {

	private static IGroupJsonWriter jsonWriter;
	private static Collection<Group> twoGroups;
	private static Collection<Group> groupWithBlocks;
	
	@BeforeClass
	public static void Setup() {
		jsonWriter = new GroupsGsonWriter();
		
		twoGroups = new LinkedHashSet<Group>();
		twoGroups.add(new Group("empty1", Collections.<Block> emptySet()));
		twoGroups.add(new Group("empty2", Collections.<Block> emptySet()));
		
		groupWithBlocks = new LinkedHashSet<Group>();
		Collection<Block> blocks = new LinkedHashSet<Block>();
		blocks.add(mockBlock("Block1"));
		blocks.add(mockBlock("Block2"));
		
		groupWithBlocks.add(new Group("Group1", blocks));
	}
	
	private static Block mockBlock(final String name) {
		return new Block() {

			@Override
			public String name() {
				return name;
			}

			@Override
			public String currentValue() {
				return null;
			}

			@Override
			public String units() {
				return null;
			}

			@Override
			public String description() {
				return null;
			}

			@Override
			public Map<String, String> properties() {
				return null;
			}

			@Override
			public RecordState state() {
				return null;
			}
			
		};
	}
	
	@Test
	public void groupsWritten() {
		assertEquals("", "[{\"name\":\"empty1\",\"blocks\":[]},{\"name\":\"empty2\",\"blocks\":[]}]", jsonWriter.toJson(twoGroups) );
	}
	
	@Test
	public void blocksWritten() {
		assertEquals("", "[{\"name\":\"Group1\",\"blocks\":[\"Block1\",\"Block2\"]}]", jsonWriter.toJson(groupWithBlocks) );
	}
}
