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
    private final static String BLOCK_1_COMP_NAME = "";
    private final static String BLOCK_2_COMP_NAME = "OTHER_COMPONENT";

    private final static String TEST_COMP_NAME = "TEST_COMPONENT";
    private final static String BASE_CONFIG_STRING = "Base Configuration";

    private EditableBlock mockBlock(String name, String component) {
        EditableBlock block = mock(EditableBlock.class);
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
        
        EditableBlock block1 = mockBlock(BLOCK_1_NAME, BLOCK_1_COMP_NAME);
        EditableBlock block2 = mockBlock(BLOCK_2_NAME, BLOCK_2_COMP_NAME);

        Collection<EditableBlock> mockBlocks = Arrays.asList(block1, block2);
        when(mockConfig.getAvailableBlocks()).thenReturn(mockBlocks);

        duplicateChecker = new ComponentDuplicateChecker(mockConfig);
    }

    @Test
    public void GIVEN_component_is_blank_WHEN_adding_THEN_no_conflicts_reported() {
        // Arrange
        Collection<Block> blocks = Collections.emptyList();
        Configuration comp = mockComp(TEST_COMP_NAME, blocks);

        // Act
        Map<String, Map<String, String>> actual = duplicateChecker.checkBlocks(Arrays.asList(comp));

        // Assert
        assertTrue(actual.isEmpty());
    }
    

    @Test
    public void GIVEN_base_config_contains_block_WHEN_adding_component_with_block_of_same_name_THEN_conflict_is_reported() {
        // Arrange
        EditableBlock duplicateBlock1 = mockBlock(BLOCK_1_NAME, null);
        Configuration comp = mockComp(TEST_COMP_NAME, Arrays.asList(duplicateBlock1));

        // Act
        Map<String, Map<String, String>> conflicts = duplicateChecker.checkBlocks(Arrays.asList(comp));

        // Assert
        assertTrue(!conflicts.get(TEST_COMP_NAME).isEmpty());
    }

    @Test
    public void GIVEN_base_config_contains_block_WHEN_adding_component_with_block_of_same_name_THEN_conflicts_contain_offending_block_name() {
        // Arrange
        EditableBlock duplicateBlock1 = mock(EditableBlock.class);
        when(duplicateBlock1.getName()).thenReturn(BLOCK_1_NAME);

        Configuration comp = mock(Configuration.class);
        when(comp.getName()).thenReturn(TEST_COMP_NAME);
        when(comp.getBlocks()).thenReturn(Arrays.asList(duplicateBlock1));

        // Act
        Map<String, Map<String, String>> conflictedComponents = duplicateChecker.checkBlocks(Arrays.asList(comp));
        Map<String, String> conflictedBlocks = conflictedComponents.get(TEST_COMP_NAME);

        // Assert
        assertTrue(!conflictedBlocks.get(BLOCK_1_NAME).isEmpty());
    }

    @Test
    public void GIVEN_base_config_contains_block_WHEN_adding_component_with_block_of_same_name_THEN_conflicts_cite_base_config_as_conflicting_source() {
        // Arrange
        EditableBlock duplicateBlock1 = mock(EditableBlock.class);
        when(duplicateBlock1.getName()).thenReturn(BLOCK_1_NAME);

        Configuration comp = mock(Configuration.class);
        when(comp.getName()).thenReturn(TEST_COMP_NAME);
        when(comp.getBlocks()).thenReturn(Arrays.asList(duplicateBlock1));

        String expected = BASE_CONFIG_STRING;

        // Act
        Map<String, Map<String, String>> conflictedComponents = duplicateChecker.checkBlocks(Arrays.asList(comp));
        Map<String, String> conflictedBlocks = conflictedComponents.get(TEST_COMP_NAME);
        String actual = conflictedBlocks.get(BLOCK_1_NAME);

        // Assert
        assertEquals(expected, actual);
    }

    // TODO: conflict with block from component
    // TODO: conflict with block from just added component
    // TODO: if adding multiple components then only conflict-free ones should
    // be added
    // TODO: if adding then removing component then blocks should be removed
    // TODO: adding comp with block after removing duplicate block from config
    // should yield no conflicts
}
