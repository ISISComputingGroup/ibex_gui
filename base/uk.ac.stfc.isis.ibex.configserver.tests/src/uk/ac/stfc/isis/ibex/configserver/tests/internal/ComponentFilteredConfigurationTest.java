
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

package uk.ac.stfc.isis.ibex.configserver.tests.internal;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.configserver.internal.ComponentFilteredConfiguration;

@SuppressWarnings("checkstyle:methodname")
public class ComponentFilteredConfigurationTest {
	
	@Before
    public void setup() {
	}
	
    public static void assertGroupsAreEqual(Group expected, Group actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getComponent(), actual.getComponent());
        assertEquals(expected.getBlocks(), expected.getBlocks());
	}

    public static void assertGroupListsAreEqual(List<Group> emptyList, List<Group> actual) {
        assertEquals(emptyList.size(), actual.size());
        if (emptyList.size() > 0) {
            for (int i = 0; i < emptyList.size(); i++) {
                assertGroupsAreEqual(emptyList.get(i), actual.get(i));
            }
        }
    }

    @Test
    public void GIVEN_no_groups_WHEN_groups_filtered_THEN_filtered_groups_is_empty() {
        List<Group> empty = new ArrayList<>();
        assertGroupListsAreEqual(empty, ComponentFilteredConfiguration.filterGroups(empty));
    }

    @Test
    public void GIVEN_one_non_component_group_WHEN_groups_filtered_THEN_filtered_groups_is_same_as_original() {
        // Arrange
        List<Group> groups = new ArrayList<>();
        List<String> blocks = new ArrayList<>();
        blocks.add("block1");
        blocks.add("block2");
        groups.add(new Group("name", blocks, null));

        // Assert
        assertGroupListsAreEqual(groups, ComponentFilteredConfiguration.filterGroups(groups));
    }

    @Test
    public void GIVEN_two_non_component_groups_WHEN_groups_filtered_THEN_filtered_groups_is_same_as_original() {
        // Arrange
        List<String> blocks = new ArrayList<>();
        blocks.add("block1");
        blocks.add("block2");

        List<Group> groups = new ArrayList<>();
        groups.add(new Group("name1", blocks, null));
        groups.add(new Group("name2", blocks, null));

        // Assert
        assertGroupListsAreEqual(groups, ComponentFilteredConfiguration.filterGroups(groups));
    }

    @Test
    public void
            GIVEN_several_component_groups_WHYEN_groups_filtered_THEN_filtered_groups_are_the_same_but_without_blocks() {
        // Arrange
        List<String> blocks = new ArrayList<>();
        blocks.add("block1");
        blocks.add("block2");

        List<String> groupNames = new ArrayList<>();
        groupNames.add("name1");
        groupNames.add("name2");
        
        String component = "my_component";

        List<Group> groups = new ArrayList<Group>();
        for (String name : groupNames) {
            groups.add(new Group(name, blocks, component));
        }

        List<Group> expected = new ArrayList<Group>();
        for (String name : groupNames) {
            expected.add(new Group(name, new ArrayList<>(), component));
        }

        // Assert
        assertGroupListsAreEqual(expected, ComponentFilteredConfiguration.filterGroups(groups));
    }

    @Test
    public void
            GIVEN_a_component_and_non_component_group_WHEN_groups_filtered_THEN_component_group_has_blocks_removed() {
        // Arrange
        List<String> blocks = new ArrayList<String>();
        blocks.add("block1");
        blocks.add("block2");

        Group component_group = new Group("name1", blocks, "my_component");
        Group component_group_no_blocks = new Group("name1", new ArrayList<>(), "my_component");
        Group non_component_group = new Group("name1", blocks, null);

        List<Group> groups = new ArrayList<Group>();
        groups.add(component_group);
        groups.add(non_component_group);

        List<Group> expected = new ArrayList<Group>();
        expected.add(component_group_no_blocks);
        expected.add(non_component_group);

        // Assert
        assertGroupListsAreEqual(expected, ComponentFilteredConfiguration.filterGroups(groups));
    }
}
