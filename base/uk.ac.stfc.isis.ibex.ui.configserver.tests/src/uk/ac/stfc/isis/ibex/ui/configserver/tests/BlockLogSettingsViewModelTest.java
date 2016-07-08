
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.BlockLogSettingsViewModel;

@SuppressWarnings("checkstyle:methodname")
public class BlockLogSettingsViewModelTest {

    private EditableBlock mockBlock;

    @Before
    public void setUp() {
        mockBlock = mock(EditableBlock.class);
    }

	@Test
    public void
            GIVEN_block_with_periodic_scan_and_positive_rate_WHEN_view_model_initialized_with_block_THEN_view_model_is_enabled_the_combo_box_indicates_periodic_scan_and_the_text_box_matches_the_scan_value() {
	    
	    // Arrange
	    final int rate_value = 7;
	    final float deadband_value = 0.54f;
	    
        when(mockBlock.getLogRate()).thenReturn(rate_value);
        when(mockBlock.getLogDeadband()).thenReturn(deadband_value);
        when(mockBlock.getLogPeriodic()).thenReturn(true);

        // Act
        BlockLogSettingsViewModel vm = new BlockLogSettingsViewModel(mockBlock);
        
        // Assert
        assertEquals(Integer.toString(rate_value), vm.getTextBoxText());
        assertTrue(vm.getEnabled());
        assertEquals(vm.getComboText(), BlockLogSettingsViewModel.PERIODIC_STRING);
	}

    @Test
    public void
            GIVEN_block_with_periodic_scan_and_zero_rate_WHEN_view_model_initialized_with_block_THEN_view_model_is_disabled_the_combo_box_indicates_periodic_scan_and_the_text_box_matches_the_scan_value() {

        // Arrange
        final int rate_value = 0;
        final float deadband_value = 0.54f;

        when(mockBlock.getLogRate()).thenReturn(rate_value);
        when(mockBlock.getLogDeadband()).thenReturn(deadband_value);
        when(mockBlock.getLogPeriodic()).thenReturn(true);

        // Act
        BlockLogSettingsViewModel vm = new BlockLogSettingsViewModel(mockBlock);

        // Assert
        assertEquals(Integer.toString(rate_value), vm.getTextBoxText());
        assertFalse(vm.getEnabled());
        assertEquals(vm.getComboText(), BlockLogSettingsViewModel.PERIODIC_STRING);
    }

    @Test
    public void
            GIVEN_block_with_periodic_scan_and_negative_rate_WHEN_view_model_initialized_with_block_THEN_view_model_is_disabled_the_combo_box_indicates_periodic_scan_and_the_text_box_value_is_zero() {

        // Arrange
        final int rate_value = -6;
        final float deadband_value = 0.54f;

        when(mockBlock.getLogRate()).thenReturn(rate_value);
        when(mockBlock.getLogDeadband()).thenReturn(deadband_value);
        when(mockBlock.getLogPeriodic()).thenReturn(true);

        // Act
        BlockLogSettingsViewModel vm = new BlockLogSettingsViewModel(mockBlock);

        // Assert
        assertEquals("0", vm.getTextBoxText());
        assertFalse(vm.getEnabled());
        assertEquals(vm.getComboText(), BlockLogSettingsViewModel.PERIODIC_STRING);
    }
}
