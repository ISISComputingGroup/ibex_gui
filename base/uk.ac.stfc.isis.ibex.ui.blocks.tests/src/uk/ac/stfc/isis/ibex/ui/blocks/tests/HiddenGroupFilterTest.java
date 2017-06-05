package uk.ac.stfc.isis.ibex.ui.blocks.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayGroup;
import uk.ac.stfc.isis.ibex.ui.blocks.groups.HiddenGroupFilter;

/**
*
*/
public class HiddenGroupFilterTest {
    
    private Collection<DisplayGroup> groups;
    private DisplayGroup groupWithVisibleBlocks;
    private DisplayGroup groupWithoutVisibleBlocks;
    
    @Before
    public void setUp() {
        
        groups = new ArrayList<DisplayGroup>();
        
        groupWithVisibleBlocks = Mockito.mock(DisplayGroup.class);
        Mockito.when(groupWithVisibleBlocks.containsAnyVisibleBlocks()).thenReturn(true);
        
        groupWithoutVisibleBlocks = Mockito.mock(DisplayGroup.class);
        Mockito.when(groupWithoutVisibleBlocks.containsAnyVisibleBlocks()).thenReturn(false);
    }

    @Test
    public void GIVEN_a_group_which_contains_visible_blocks_WHEN_groups_are_filtered_THEN_group_is_returned_unmodified() {
         // Arrange
         groups.add(groupWithVisibleBlocks);
         
         // Act
         Collection<DisplayGroup> result = HiddenGroupFilter.getVisibleGroups(groups, false);
         
         // Assert
         assertTrue(result.contains(groupWithVisibleBlocks));
    }
    
    @Test
    public void GIVEN_a_group_which_contains_no_visible_blocks_WHEN_groups_are_filtered_THEN_group_is_filtered_out() {
         // Arrange
         groups.add(groupWithoutVisibleBlocks);
         
         // Act
         Collection<DisplayGroup> result = HiddenGroupFilter.getVisibleGroups(groups, false);
         
         // Assert
         assertFalse(result.contains(groupWithoutVisibleBlocks));
    }
    
    @Test
    public void GIVEN_a_group_which_contains_visible_blocks_and_a_group_which_contains_no_visible_blocks_WHEN_groups_are_filtered_THEN_only_group_with_visible_blocks_is_returned() {
         // Arrange
         groups.add(groupWithVisibleBlocks); 
         groups.add(groupWithoutVisibleBlocks);
         
         // Act
         Collection<DisplayGroup> result = HiddenGroupFilter.getVisibleGroups(groups, false);
         
         // Assert
         assertTrue(result.contains(groupWithVisibleBlocks));
         assertFalse(result.contains(groupWithoutVisibleBlocks));
    }
    
    @Test
    public void GIVEN_a_group_which_contains_visible_blocks_WHEN_groups_are_filtered_and_show_hidden_blocks_is_true_THEN_group_is_returned_unmodified() {
         // Arrange
         groups.add(groupWithVisibleBlocks);
         
         // Act
         Collection<DisplayGroup> result = HiddenGroupFilter.getVisibleGroups(groups, true);
         
         // Assert
         assertTrue(result.contains(groupWithVisibleBlocks));
    }

    @Test
    public void GIVEN_a_group_which_contains_no_visible_blocks_WHEN_groups_are_filtered_and_show_hidden_blocks_is_true_THEN_group_is_returned_unmodified() {
         // Arrange
         groups.add(groupWithoutVisibleBlocks);
         
         // Act
         Collection<DisplayGroup> result = HiddenGroupFilter.getVisibleGroups(groups, true);
         
         // Assert
         assertTrue(result.contains(groupWithoutVisibleBlocks));
    }

    @Test
    public void GIVEN_a_group_which_contains_visible_blocks_and_a_group_which_contains_no_visible_blocks_WHEN_groups_are_filtered_and_show_hidden_blocks_is_true_THEN_both_groups_are_returned() {
         // Arrange
         groups.add(groupWithVisibleBlocks); 
         groups.add(groupWithoutVisibleBlocks);
         
         // Act
         Collection<DisplayGroup> result = HiddenGroupFilter.getVisibleGroups(groups, true);
         
         // Assert
         assertTrue(result.contains(groupWithVisibleBlocks));
         assertTrue(result.contains(groupWithoutVisibleBlocks));
    }
}
