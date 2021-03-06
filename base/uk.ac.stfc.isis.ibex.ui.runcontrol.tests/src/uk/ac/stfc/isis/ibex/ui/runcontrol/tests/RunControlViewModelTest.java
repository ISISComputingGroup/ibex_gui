
/**
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

package uk.ac.stfc.isis.ibex.ui.runcontrol.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;
import uk.ac.stfc.isis.ibex.ui.runcontrol.RunControlViewModel;

@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:methodname" })
public class RunControlViewModelTest {

    private RunControlViewModel runControlViewModel;
    private RunControlServer runControlServer;

    private Collection<DisplayBlock> blocks;

    private DisplayBlock mockBlock;
    
    private static final double ASSERTION_TOLERANCE = 1e-15;

    @Before
    public void setUp() {
        // Arrange
        runControlServer = mock(RunControlServer.class);

        mockBlock = mock(DisplayBlock.class);
        when(mockBlock.getRunControlHighLimit()).thenReturn(0.);
        when(mockBlock.getRunControlLowLimit()).thenReturn(0.);
        when(mockBlock.getRunControlEnabled()).thenReturn(false);


        blocks = new ArrayList<>();
        runControlViewModel = new RunControlViewModel(blocks, runControlServer);

        runControlViewModel.setRunControlHighLimit(0.);
        runControlViewModel.setRunControlLowLimit(0.);
    }

    @Test
    public void WHEN_valid_limits_set_THEN_send_enabled() {
        // Act
        runControlViewModel.setRunControlLowLimit(0.);
        runControlViewModel.setRunControlHighLimit(1.);

        // Assert
        assertEquals(runControlViewModel.getSendEnabled(), true);
    }

    @Test
    public void WHEN_null_block_is_set_THEN_low_limit_emptied() {
        // Act
        runControlViewModel.setSource(null);

        // Assert
        assertEquals(runControlViewModel.getRunControlLowLimit(), 0., ASSERTION_TOLERANCE);
    }

    @Test
    public void WHEN_null_block_is_set_THEN_high_limit_emptied() {
        // Act
        runControlViewModel.setSource(null);

        // Assert
        assertEquals(runControlViewModel.getRunControlHighLimit(), 0., ASSERTION_TOLERANCE);
    }

    @Test
    public void WHEN_null_block_is_set_THEN_rc_disabled() {
        // Act
        runControlViewModel.setSource(null);

        // Assert
        assertEquals(runControlViewModel.getRunControlEnabled(), false);
    }

    @Test
    public void WHEN_null_block_is_set_THEN_view_model_is_not_in_error() {
        // Act
        runControlViewModel.setSource(null);

        // Assert
        assertEquals(runControlViewModel.getError().isError(), false);
    }

    @Test
    public void WHEN_new_block_is_set_THEN_high_limit_is_set_to_blocks() {
        // Arrange
        double newHigh = 1.0;
        when(mockBlock.getRunControlHighLimit()).thenReturn(newHigh);

        // Act
        runControlViewModel.setSource(mockBlock);

        // Assert
        assertEquals(runControlViewModel.getRunControlHighLimit(), newHigh, ASSERTION_TOLERANCE);
    }

    @Test
    public void WHEN_new_block_is_set_THEN_low_limit_is_set_to_blocks() {
        // Arrange
        double newLow = -1.0;
        when(mockBlock.getRunControlHighLimit()).thenReturn(newLow);

        // Act
        runControlViewModel.setSource(mockBlock);

        // Assert
        assertEquals(runControlViewModel.getRunControlHighLimit(), newLow, ASSERTION_TOLERANCE);
    }

    @Test
    public void WHEN_new_block_is_set_THEN_rc_enabled_is_set_to_blocks() {
        // Arrange
        when(mockBlock.getRunControlEnabled()).thenReturn(true);

        // Act
        runControlViewModel.setSource(mockBlock);

        // Assert
        assertEquals(runControlViewModel.getRunControlEnabled(), true);
    }
}
