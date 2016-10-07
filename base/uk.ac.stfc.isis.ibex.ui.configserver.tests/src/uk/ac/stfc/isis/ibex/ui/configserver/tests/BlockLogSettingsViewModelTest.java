
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

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.BlockLogSettingsViewModel;

@SuppressWarnings("checkstyle:methodname")
public class BlockLogSettingsViewModelTest {

    private EditableBlock mockBlock;

    public void setUpMockBlockByMode(Boolean periodic, int rate, float deadband) {
        when(mockBlock.getLogRate()).thenReturn(rate);
        when(mockBlock.getLogDeadband()).thenReturn(deadband);
        when(mockBlock.getLogPeriodic()).thenReturn(periodic);
    }

    public void mockPeriodicBlock(EditableBlock mockBlock, int rate, float deadband) {
        setUpMockBlockByMode(true, rate, deadband);
    }

    public void mockDeadbandBlock(EditableBlock mockBlock, int rate, float deadband) {
        setUpMockBlockByMode(false, rate, deadband);
    }

    public void verifyModelValues(BlockLogSettingsViewModel vm, String textBox, Boolean enabled, Boolean periodic) {
        assertEquals(textBox, vm.getTextBoxText());
        assertEquals(enabled, vm.getEnabled());
        if (periodic) {
            assertEquals(BlockLogSettingsViewModel.PERIODIC_STRING, vm.getComboText());
        } else {
            assertEquals(BlockLogSettingsViewModel.MONITOR_STRING, vm.getComboText());
        }
    }

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
	    
        mockPeriodicBlock(mockBlock, rate_value, deadband_value);

        // Act
        BlockLogSettingsViewModel vm = new BlockLogSettingsViewModel(mockBlock);
        
        // Assert
        verifyModelValues(vm, Integer.toString(rate_value), true, true);
	}

    @Test
    public void
            GIVEN_block_with_periodic_scan_and_zero_rate_WHEN_view_model_initialized_with_block_THEN_view_model_is_disabled_the_combo_box_indicates_periodic_scan_and_the_text_box_matches_the_scan_value() {

        // Arrange
        final int rate_value = 0;
        final float deadband_value = 0.54f;

        mockPeriodicBlock(mockBlock, rate_value, deadband_value);

        // Act
        BlockLogSettingsViewModel vm = new BlockLogSettingsViewModel(mockBlock);

        // Assert
        verifyModelValues(vm, Integer.toString(rate_value), false, true);
    }

    @Test
    public void
            GIVEN_block_with_periodic_scan_and_negative_rate_WHEN_view_model_initialized_with_block_THEN_view_model_is_disabled_the_combo_box_indicates_periodic_scan_and_the_text_box_value_is_zero() {

        // Arrange
        final int rate_value = -6;
        final float deadband_value = 0.54f;

        mockPeriodicBlock(mockBlock, rate_value, deadband_value);

        // Act
        BlockLogSettingsViewModel vm = new BlockLogSettingsViewModel(mockBlock);

        // Assert
        verifyModelValues(vm, Integer.toString(0), false, true);
    }

    @Test
    public void
            GIVEN_block_in_deadband_mode_with_positive_band_WHEN_view_model_initialized_with_block_THEN_view_model_is_enabled_the_combo_box_indicates_deadband_mode_and_the_text_box_value_matches_the_blocks() {

        // Arrange
        final int rate_value = 13;
        final float deadband_value = 0.54f;

        mockDeadbandBlock(mockBlock, rate_value, deadband_value);

        // Act
        BlockLogSettingsViewModel vm = new BlockLogSettingsViewModel(mockBlock);

        // Assert
        verifyModelValues(vm, Float.toString(deadband_value), true, false);
    }

    @Test
    public void
            GIVEN_block_in_deadband_mode_with_zero_band_WHEN_view_model_initialized_with_block_THEN_view_model_is_enabled_the_combo_box_indicates_deadband_mode_and_the_text_box_value_is_zero() {

        // Arrange
        final int rate_value = 13;
        final float deadband_value = 0.0f;

        mockDeadbandBlock(mockBlock, rate_value, deadband_value);

        // Act
        BlockLogSettingsViewModel vm = new BlockLogSettingsViewModel(mockBlock);

        // Assert
        verifyModelValues(vm, Float.toString(deadband_value), true, false);
    }

    @Test
    public void
            GIVEN_block_in_deadband_mode_with_negative_band_WHEN_view_model_initialized_with_block_THEN_view_model_is_disabled_the_mode_is_switched_to_periodic_and_the_text_box_value_is_zero() {

        // Arrange
        final int rate_value = 13;
        final float deadband_value = -134.012f;

        mockDeadbandBlock(mockBlock, rate_value, deadband_value);

        // Act
        BlockLogSettingsViewModel vm = new BlockLogSettingsViewModel(mockBlock);

        // Assert
        verifyModelValues(vm, Integer.toString(0), false, true);
    }

    @Test
    public void
            GIVEN_block_in_deadband_mode_WHEN_set_enabled_false_THEN_view_model_is_disabled_the_mode_is_switched_to_periodic_and_the_text_box_value_is_zero() {

        // Arrange
        final int rate_value = 13;
        final float deadband_value = 52.5f;

        mockDeadbandBlock(mockBlock, rate_value, deadband_value);

        // Act
        BlockLogSettingsViewModel vm = new BlockLogSettingsViewModel(mockBlock);
        verifyModelValues(vm, Float.toString(deadband_value), true, false);
        vm.setEnabled(false);

        // Assert
        verifyModelValues(vm, Integer.toString(0), false, true);
    }

    @Test
    public void
            GIVEN_block_in_periodic_mode_WHEN_set_enabled_false_THEN_view_model_is_disabled_the_mode_is_switched_to_periodic_and_the_text_box_value_is_zero() {

        // Arrange
        final int rate_value = 20;
        final float deadband_value = 52.5f;

        mockPeriodicBlock(mockBlock, rate_value, deadband_value);

        // Act
        BlockLogSettingsViewModel vm = new BlockLogSettingsViewModel(mockBlock);
        verifyModelValues(vm, Integer.toString(rate_value), true, true);
        vm.setEnabled(false);

        // Assert
        verifyModelValues(vm, Integer.toString(0), false, true);
    }

    @Test
    public void
            GIVEN_block_with_periodic_scan_and_zero_rate_WHEN_set_enabled_true_THEN_view_model_is_enabled_the_combo_box_indicates_periodic_scan_and_the_text_box_matches_the_default_scan_value() {

        // Arrange
        final int rate_value = 0;
        final float deadband_value = 0.54f;
        final int expected_default_rate = Block.DEFAULT_SCAN_RATE;

        mockPeriodicBlock(mockBlock, rate_value, deadband_value);

        // Act
        BlockLogSettingsViewModel vm = new BlockLogSettingsViewModel(mockBlock);
        verifyModelValues(vm, Integer.toString(rate_value), false, true);
        vm.setEnabled(true);

        // Assert
        verifyModelValues(vm, Integer.toString(expected_default_rate), true, true);
    }

}
