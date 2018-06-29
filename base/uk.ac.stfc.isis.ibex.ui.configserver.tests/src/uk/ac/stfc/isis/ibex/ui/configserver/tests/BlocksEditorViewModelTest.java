
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.configserver.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.BlocksEditorViewModel;

public class BlocksEditorViewModelTest {

    private EditableConfiguration mockConfig;
    private BlocksEditorViewModel blockViewModel;

    @Before
    public void setUp() {
        mockConfig = mock(EditableConfiguration.class);
        blockViewModel = new BlocksEditorViewModel(mockConfig);
    }

    @Test
    public void GIVEN_a_name_that_is_not_in_config_WHEN_getUniqueName_called_THEN_passed_name_not_changed() {
        String name = "NAME"; 
        String expected = name;   
        String actual = blockViewModel.getUniqueName(expected);
        assertEquals(expected , actual);
    }
    
    @Test
    public void GIVEN_a_name_that_is_in_config_WHEN_getUniqueName_called_THEN_passed_name_changed_to_the_name_with_a_1_in_parenthesis() {
        EditableBlock blockThatExists = mock(EditableBlock.class);
        when(blockThatExists.getName()).thenReturn("NAME");
        when(mockConfig.getAllBlocks()).thenReturn(Arrays.asList(blockThatExists));
        String name = "NAME"; 
        String expected = name + " (1)";
        String actual = blockViewModel.getUniqueName(name);
        assertEquals(expected , actual);
    }
    
    @Test
    public void GIVEN_a_name_that_is_in_config_and_another_similar_name_WHEN_getUniqueName_called_THEN_passed_name_changed_to_the_name_with_a_2_in_parenthesis() {
        EditableBlock blockThatExists1 = mock(EditableBlock.class);
        EditableBlock blockThatExists2 = mock(EditableBlock.class);
        when(blockThatExists1.getName()).thenReturn("NAME");
        when(blockThatExists2.getName()).thenReturn("NAME (1)");
        when(mockConfig.getAllBlocks()).thenReturn(Arrays.asList(blockThatExists1, blockThatExists2));
        String name = "NAME"; 
        String expected = name + " (2)";
        String actual = blockViewModel.getUniqueName(name);
        assertEquals(expected , actual);
    }
    
    @Test
    public void GIVEN_a_name_that_is_in_config_and_10_similar_names_WHEN_getUniqueName_called_THEN_passed_name_changed_to_the_name_with_a_10_in_parenthesis() {
        EditableBlock blockThatExists1 = mock(EditableBlock.class);
        EditableBlock blockThatExists2 = mock(EditableBlock.class);
        EditableBlock blockThatExists3 = mock(EditableBlock.class);
        EditableBlock blockThatExists4 = mock(EditableBlock.class);
        EditableBlock blockThatExists5 = mock(EditableBlock.class);
        EditableBlock blockThatExists6 = mock(EditableBlock.class);
        EditableBlock blockThatExists7 = mock(EditableBlock.class);
        EditableBlock blockThatExists8 = mock(EditableBlock.class);
        EditableBlock blockThatExists9 = mock(EditableBlock.class);
        EditableBlock blockThatExists10 = mock(EditableBlock.class);
        EditableBlock blockThatExists11 = mock(EditableBlock.class);
        when(blockThatExists1.getName()).thenReturn("NAME");
        when(blockThatExists2.getName()).thenReturn("NAME (1)");
        when(blockThatExists3.getName()).thenReturn("NAME (2)");
        when(blockThatExists4.getName()).thenReturn("NAME (3)");
        when(blockThatExists5.getName()).thenReturn("NAME (4)");
        when(blockThatExists6.getName()).thenReturn("NAME (5)");
        when(blockThatExists7.getName()).thenReturn("NAME (6)");
        when(blockThatExists8.getName()).thenReturn("NAME (7)");
        when(blockThatExists9.getName()).thenReturn("NAME (8)");
        when(blockThatExists10.getName()).thenReturn("NAME (9)");
        when(blockThatExists11.getName()).thenReturn("NAME (10)");
        when(mockConfig.getAllBlocks()).thenReturn(Arrays.asList(blockThatExists1, blockThatExists2,blockThatExists3, blockThatExists4,blockThatExists5, blockThatExists6,blockThatExists7, blockThatExists8, blockThatExists9, blockThatExists10, blockThatExists11));
        String name = "NAME"; 
        String expected = name + " (11)";
        String actual = blockViewModel.getUniqueName(name);
        assertEquals(expected , actual);
    }
    
    @Test
    public void GIVEN_a_name_that_is_only_numbers_and_not_in_config_WHEN_getUniqueName_called_THEN_name_is_not_changed() {
        String name = "1234"; 
        String expected = name;
        String actual = blockViewModel.getUniqueName(name);
        assertEquals(expected , actual);
    }
    
    @Test
    public void GIVEN_a_name_that_is_only_numbers_and_in_config_WHEN_getUniqueName_called_THEN_name_changed_to_the_name_with_a_1_in_parenthesis() {
        EditableBlock blockThatExists = mock(EditableBlock.class);
        when(blockThatExists.getName()).thenReturn("1234");
        when(mockConfig.getAllBlocks()).thenReturn(Arrays.asList(blockThatExists));
        String name = "1234"; 
        String expected = name + " (1)";
        String actual = blockViewModel.getUniqueName(name);
        assertEquals(expected , actual);
    }
    
    @Test
    public void GIVEN_a_name_that_is_only_underscores_and_not_in_config_WHEN_getUniqueName_called_THEN_name_is_not_changed() {
        String name = "_"; 
        String expected = name;
        String actual = blockViewModel.getUniqueName(name);
        assertEquals(expected , actual);
    }
    
    @Test
    public void GIVEN_a_name_that_is_only_underscores_and_in_config_WHEN_getUniqueName_called_THEN_name_changed_to_the_name_with_a_1_in_parenthesis() {
        EditableBlock blockThatExists = mock(EditableBlock.class);
        when(blockThatExists.getName()).thenReturn("_");
        when(mockConfig.getAllBlocks()).thenReturn(Arrays.asList(blockThatExists));
        String name = "_"; 
        String expected = name + " (1)";
        String actual = blockViewModel.getUniqueName(name);
        assertEquals(expected , actual);
    }
    
    @Test
    public void GIVEN_a_name_that_is_only_underscores_and_numbers_and_not_in_config_WHEN_getUniqueName_called_THEN_name_is_not_changed() {
        String name = "21_21_21_"; 
        String expected = name;
        String actual = blockViewModel.getUniqueName(name);
        assertEquals(expected , actual);
    }
    
    @Test
    public void GIVEN_a_name_that_is_only_underscores_and_numbers_and_in_config_WHEN_getUniqueName_called_THEN_name_changed_to_the_name_with_a_1_in_parenthesis() {
        EditableBlock blockThatExists = mock(EditableBlock.class);
        when(blockThatExists.getName()).thenReturn("_21_21_21");
        when(mockConfig.getAllBlocks()).thenReturn(Arrays.asList(blockThatExists));
        String name = "_21_21_21"; 
        String expected = name + " (1)";
        String actual = blockViewModel.getUniqueName(name);
        assertEquals(expected , actual);
    }
    
    @Test
    public void GIVEN_a_name_that_has_underscores_and_numbers_and_not_in_config_WHEN_getUniqueName_called_THEN_name_is_not_changed() {
        String name = "NAME_21_21_21_"; 
        String expected = name;
        String actual = blockViewModel.getUniqueName(name);
        assertEquals(expected , actual);
    }
    
    @Test
    public void GIVEN_a_name_that_has_underscores_and_numbers_and_in_config_WHEN_getUniqueName_called_THEN_name_changed_to_the_name_with_a_1_in_parenthesis() {
        EditableBlock blockThatExists = mock(EditableBlock.class);
        when(blockThatExists.getName()).thenReturn("NAME_21_21_21");
        when(mockConfig.getAllBlocks()).thenReturn(Arrays.asList(blockThatExists));
        String name = "NAME_21_21_21"; 
        String expected = name + " (1)";
        String actual = blockViewModel.getUniqueName(name);
        assertEquals(expected , actual);
    }
}
