 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.configserver.editing.components.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableComponents;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.components.ComponentDuplicateChecker;

/**
 *
 */
public class ComponentDuplicateCheckerTest {

    private ComponentDuplicateChecker duplicateChecker;
    private EditableConfiguration mockConfig;

    private final static String BLOCK_1_NAME = "BLOCK_1";
    private final static String BLOCK_2_NAME = "BLOCK_2";
    private final static String BLOCK_3_NAME = "BLOCK_3";

    private final static String OTHER_COMP_NAME = "OTHER_COMPONENT";
    private final static String TEST_COMP_NAME_1 = "TEST_COMPONENT_1";
    private final static String TEST_COMP_NAME_2 = "TEST_COMPONENT_2";
    private final static String BASE_CONFIG_STRING = "base configuration";

    private Block mockBlock(String name, String component) {
        Block block = mock(EditableBlock.class);
        when(block.getName()).thenReturn(name);
        when(block.getComponent()).thenReturn(component);
        when(block.hasComponent()).thenReturn(component == null ? false : component.length() > 0);
        return block;
    }

    private Configuration mockComp(String name, Collection<Block> blocks) {
        Configuration comp = mock(Configuration.class);
        when(comp.getName()).thenReturn(name);
        when(comp.getBlocks()).thenReturn(blocks);
        return comp;
    }

    @Before
    public void setUp() {
        mockConfig = mock(EditableConfiguration.class);
        
        EditableBlock block1 = (EditableBlock) mockBlock(BLOCK_1_NAME, "");
        EditableBlock block2 = (EditableBlock) mockBlock(BLOCK_2_NAME, OTHER_COMP_NAME);

        Configuration otherComponent = mockComp(OTHER_COMP_NAME, Arrays.asList(block2));
        EditableComponents mockComponents = mock(EditableComponents.class);
        when(mockComponents.getSelected()).thenReturn(Arrays.asList(otherComponent));

        Collection<EditableBlock> mockBlocks = Arrays.asList(block1, block2);
        when(mockConfig.getAvailableBlocks()).thenReturn(mockBlocks);
        when(mockConfig.getEditableComponents()).thenReturn(mockComponents);

        duplicateChecker = new ComponentDuplicateChecker(mockConfig);
    }

    @Test
    public void GIVEN_checked_component_is_blank_THEN_no_conflicts_reported() {
        // Arrange
        Collection<Block> blocks = Collections.emptyList();
        Configuration comp = mockComp(TEST_COMP_NAME_1, blocks);

        // Act
        Map<String, Map<String, String>> actual = duplicateChecker.checkBlocks(Arrays.asList(comp));

        // Assert
        assertTrue(actual.isEmpty());
    }
    

    @Test
    public void GIVEN_base_config_contains_block_WHEN_checking_component_with_block_of_same_name_THEN_checked_component_contained_in_conflicts() {
        // Arrange
        Block duplicateBlock = mockBlock(BLOCK_1_NAME, null);
        Configuration comp = mockComp(TEST_COMP_NAME_1, Arrays.asList(duplicateBlock));

        // Act
        Map<String, Map<String, String>> conflicts = duplicateChecker.checkBlocks(Arrays.asList(comp));

        // Assert
        assertTrue(!conflicts.get(TEST_COMP_NAME_1).isEmpty());
    }

    @Test
    public void GIVEN_base_config_contains_block_WHEN_checking_component_with_block_of_same_name_THEN_duplicate_block_contained_in_conflicts_for_checked_component() {
        // Arrange
        Block duplicateBlock = mockBlock(BLOCK_1_NAME, null);
        Configuration comp = mockComp(TEST_COMP_NAME_1, Arrays.asList(duplicateBlock));

        // Act
        Map<String, Map<String, String>> conflictedComponents = duplicateChecker.checkBlocks(Arrays.asList(comp));
        Map<String, String> conflictedBlocks = conflictedComponents.get(TEST_COMP_NAME_1);

        // Assert
        assertTrue(!conflictedBlocks.get(BLOCK_1_NAME).isEmpty());
    }

    @Test
    public void GIVEN_base_config_contains_block_WHEN_checking_component_with_block_of_same_name_THEN_base_config_is_associated_as_source_of_block_conflict() {
        // Arrange
        Block duplicateBlock = mockBlock(BLOCK_1_NAME, null);
        Configuration comp = mockComp(TEST_COMP_NAME_1, Arrays.asList(duplicateBlock));
        String expected = BASE_CONFIG_STRING;

        // Act
        Map<String, Map<String, String>> conflictedComponents = duplicateChecker.checkBlocks(Arrays.asList(comp));
        Map<String, String> conflictedBlocks = conflictedComponents.get(TEST_COMP_NAME_1);
        String actual = conflictedBlocks.get(BLOCK_1_NAME);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_other_component_contains_block_WHEN_checking_component_with_block_of_same_name_THEN_other_component_is_associated_as_source_of_block_conflict() {
        // Arrange
        Block duplicateBlock = mockBlock(BLOCK_2_NAME, null);
        Configuration comp = mockComp(TEST_COMP_NAME_1, Arrays.asList(duplicateBlock));
        String expected = OTHER_COMP_NAME;

        // Act
        Map<String, Map<String, String>> conflictedComponents = duplicateChecker.checkBlocks(Arrays.asList(comp));
        Map<String, String> conflictedBlocks = conflictedComponents.get(TEST_COMP_NAME_1);
        String actual = conflictedBlocks.get(BLOCK_2_NAME);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_duplicates_between_two_components_WHEN_checking_those_two_components_THEN_conflict_is_reported_for_second_component() {
        // Arrange
        Block duplicateBlock1 = mockBlock(BLOCK_3_NAME, null);
        Configuration comp1 = mockComp(TEST_COMP_NAME_1, Arrays.asList(duplicateBlock1));

        Block duplicateBlock2 = mockBlock(BLOCK_3_NAME, null);
        Configuration comp2 = mockComp(TEST_COMP_NAME_2, Arrays.asList(duplicateBlock2));


        // Act
        Map<String, Map<String, String>> conflictedComponents = duplicateChecker.checkBlocks(Arrays.asList(comp1, comp2));

        // Assert
        assertTrue(!conflictedComponents.containsKey(TEST_COMP_NAME_1));
        assertTrue(conflictedComponents.containsKey(TEST_COMP_NAME_2));
    }

    @Test
    public void GIVEN_duplicates_between_two_components_WHEN_checking_those_two_components_THEN_first_checked_component_is_associated_as_source_of_block_conflict() {
        // Arrange
        Block duplicateBlock1 = mockBlock(BLOCK_3_NAME, null);
        Configuration comp1 = mockComp(TEST_COMP_NAME_1, Arrays.asList(duplicateBlock1));

        Block duplicateBlock2 = mockBlock(BLOCK_3_NAME, null);
        Configuration comp2 = mockComp(TEST_COMP_NAME_2, Arrays.asList(duplicateBlock2));

        String expected = TEST_COMP_NAME_1;

        // Act
        Map<String, Map<String, String>> conflictedComponents = duplicateChecker.checkBlocks(Arrays.asList(comp1, comp2));
        Map<String, String> conflictedBlocks = conflictedComponents.get(TEST_COMP_NAME_2);
        String actual = conflictedBlocks.get(BLOCK_3_NAME);

        // Assert
        assertEquals(expected, actual);
    }
}
