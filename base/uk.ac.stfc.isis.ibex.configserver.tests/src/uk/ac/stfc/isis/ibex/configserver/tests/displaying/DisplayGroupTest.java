 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2017 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.configserver.tests.displaying;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;

import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayGroup;

/**
 * Unit tests for the DisplayGroup class.
 */
public class DisplayGroupTest {

    private static final String HIDDEN_BLOCK_NAME = "HIDDEN_BLOCK";
    private static final String VISIBLE_BLOCK_NAME = "VISIBLE_BLOCK";
    private DisplayBlock visibleBlock;
    private DisplayBlock hiddenBlock;
    private Collection<DisplayBlock> allBlocks;

    @Before
    public void setUp() {
        
        visibleBlock = Mockito.mock(DisplayBlock.class);
        Mockito.when(visibleBlock.getIsVisible()).thenReturn(true);
        Mockito.when(visibleBlock.getName()).thenReturn(VISIBLE_BLOCK_NAME);
        
        hiddenBlock = Mockito.mock(DisplayBlock.class);
        Mockito.when(hiddenBlock.getIsVisible()).thenReturn(false);
        Mockito.when(hiddenBlock.getName()).thenReturn(HIDDEN_BLOCK_NAME);
        
        allBlocks = Lists.newArrayList(visibleBlock, hiddenBlock);
            
    }

    @Test
    public void GIVEN_a_display_group_with_a_visible_block_WHEN_contains_any_visible_blocks_is_called_THEN_returns_true() {
        // Arrange

        Group groupWithAVisibleBlock = Mockito.mock(Group.class);       
        Mockito.when(groupWithAVisibleBlock.getBlocks())
            .thenReturn(Lists.newArrayList(VISIBLE_BLOCK_NAME));

        DisplayGroup displayGroupWithAVisibleBlock = new DisplayGroup(groupWithAVisibleBlock, allBlocks);
        
        // Assert
        assertTrue(displayGroupWithAVisibleBlock.containsAnyVisibleBlocks());
    }
    
    @Test
    public void GIVEN_a_display_group_without_a_visible_block_WHEN_contains_any_visible_blocks_is_called_THEN_returns_false() {
        // Arrange
        
        Group groupWithoutAVisibleBlock = Mockito.mock(Group.class);       
        Mockito.when(groupWithoutAVisibleBlock.getBlocks())
            .thenReturn(Lists.newArrayList(HIDDEN_BLOCK_NAME));
        
        DisplayGroup displayGroupWithoutAVisibleBlock = new DisplayGroup(groupWithoutAVisibleBlock, allBlocks);
                
        // Assert
        assertFalse(displayGroupWithoutAVisibleBlock.containsAnyVisibleBlocks());
    }

    @Test
    public void GIVEN_a_display_group_with_a_visible_block_and_a_hidden_block_WHEN_contains_any_visible_blocks_is_called_THEN_returns_true() {
        // Arrange
        
        Group groupWithVisibleAndHiddenBlocks = Mockito.mock(Group.class);       
        Mockito.when(groupWithVisibleAndHiddenBlocks.getBlocks())
            .thenReturn(Lists.newArrayList(VISIBLE_BLOCK_NAME, HIDDEN_BLOCK_NAME));
        
        DisplayGroup displayGroupWithVisibleAndHiddenBlocks = new DisplayGroup(groupWithVisibleAndHiddenBlocks, allBlocks);
                
        // Assert
        assertTrue(displayGroupWithVisibleAndHiddenBlocks.containsAnyVisibleBlocks());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void GIVEN_a_display_group_without_any_blocks_WHEN_contains_any_visible_blocks_is_called_THEN_returns_false() {
        // Arrange
        
        Group groupWithNoBlocks = Mockito.mock(Group.class);       
        Mockito.when(groupWithNoBlocks.getBlocks())
            .thenReturn(Collections.EMPTY_LIST);
        
        DisplayGroup displayGroupWithNoBlocks = new DisplayGroup(groupWithNoBlocks, allBlocks);
                
        // Assert
        assertFalse(displayGroupWithNoBlocks.containsAnyVisibleBlocks());
    }

}
