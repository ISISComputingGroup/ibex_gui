
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
    public void GIVEN_a_name_that_is_in_config_WHEN_getUniqueName_called_THEN_passed_name_changed_to_the_name_with_underscore_1() {
        EditableBlock blockThatExists = mock(EditableBlock.class);
        String name = "NAME"; 
        when(blockThatExists.getName()).thenReturn(name);
        when(mockConfig.getAllBlocks()).thenReturn(Arrays.asList(blockThatExists));
        String expected = name + "_1";
        String actual = blockViewModel.getUniqueName(name);
        assertEquals(expected , actual);
    }
    
    @Test
    public void GIVEN_a_name_that_is_in_config_and_another_similar_name_WHEN_getUniqueName_called_THEN_passed_name_changed_to_the_name_with_underscore_2() {
        EditableBlock blockThatExists1 = mock(EditableBlock.class);
        EditableBlock blockThatExists2 = mock(EditableBlock.class);
        String name = "NAME"; 
        when(blockThatExists1.getName()).thenReturn(name);
        when(blockThatExists2.getName()).thenReturn(name + "_1");
        when(mockConfig.getAllBlocks()).thenReturn(Arrays.asList(blockThatExists1, blockThatExists2));
        String expected = name + "_2";
        String actual = blockViewModel.getUniqueName(name);
        assertEquals(expected , actual);
    }
    
    @Test
    public void GIVEN_a_name_that_is_in_config_and_11_similar_names_WHEN_getUniqueName_called_THEN_passed_name_changed_to_the_name_with_underscore_11() {
        EditableBlock blockList[] = new EditableBlock[11];
        String name = "NAME";
        for (int i = 0; i < 11 ; i++){
            EditableBlock blockThatExists = mock(EditableBlock.class);
            blockList[i] = blockThatExists;
            if (i == 0 ) {
                when(blockList[i].getName()).thenReturn(name);
            } else{
                when(blockList[i].getName()).thenReturn(name + "_" + i);
            }
        }
        when(mockConfig.getAllBlocks()).thenReturn(Arrays.asList(blockList));
        String expected = name + "_11";
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
    public void GIVEN_a_name_that_is_only_numbers_and_in_config_WHEN_getUniqueName_called_THEN_name_changed_to_the_name_with_undercore_1() {
        EditableBlock blockThatExists = mock(EditableBlock.class);
        String name = "1234"; 
        when(blockThatExists.getName()).thenReturn(name);
        when(mockConfig.getAllBlocks()).thenReturn(Arrays.asList(blockThatExists));
        String expected = name + "_1";
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
    public void GIVEN_a_name_that_is_only_underscores_and_in_config_WHEN_getUniqueName_called_THEN_name_changed_to_the_name_with_underscore_1() {
        EditableBlock blockThatExists = mock(EditableBlock.class);
        String name = "_"; 
        when(blockThatExists.getName()).thenReturn(name);
        when(mockConfig.getAllBlocks()).thenReturn(Arrays.asList(blockThatExists));
        String expected = name + "_1";
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
    public void GIVEN_a_name_that_is_only_underscores_and_numbers_and_in_config_WHEN_getUniqueName_called_THEN_name_changed_to_the_name_with_underscore_1() {
        EditableBlock blockThatExists = mock(EditableBlock.class);
        String name = "_21_21_21_"; 
        when(blockThatExists.getName()).thenReturn(name);
        when(mockConfig.getAllBlocks()).thenReturn(Arrays.asList(blockThatExists));
        String expected = name + "_1";
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
    public void GIVEN_a_name_that_has_underscores_and_numbers_and_in_config_WHEN_getUniqueName_called_THEN_name_changed_to_the_name_with_underscore_1() {
        EditableBlock blockThatExists = mock(EditableBlock.class);
        String name = "NAME_21_21_21_NAME"; 
        when(blockThatExists.getName()).thenReturn(name);
        when(mockConfig.getAllBlocks()).thenReturn(Arrays.asList(blockThatExists));
        String expected = name + "_1";
        String actual = blockViewModel.getUniqueName(name);
        assertEquals(expected , actual);
    }
}
