
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
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;
import uk.ac.stfc.isis.ibex.ui.runcontrol.RunControlViewModel;

@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:methodname"})
public class RunControlViewModelTest {

    private RunControlViewModel runControlViewModel;
    private RunControlServer runControlServer;
    private ConfigServer configServer;
    private Configuration config;
    private ForwardingObservable<Configuration> configObservable;

    private Collection<Block> blocks;

    @Before
    public void setUp() {
        // Arrange
        runControlServer = mock(RunControlServer.class);

        blocks = new ArrayList<>();
        addBlocks(blocks, 5);

        config = mock(Configuration.class);
        when(config.getBlocks()).thenReturn(blocks);

        configObservable = mock(ForwardingObservable.class);
        when(configObservable.getValue()).thenReturn(config);
        
        configServer = mock(ConfigServer.class);
        when(configServer.currentConfig()).thenReturn(configObservable);

        runControlViewModel = new RunControlViewModel(configServer, runControlServer);
    }

    public void addBlocks(Collection<Block> blocks, int numberOfBlocks) {
        for (int i = 1; i <= numberOfBlocks; i++) {
            Block block = new Block("block" + Integer.toString(i), null, false, false);
            block.setRCLowLimit(i);
            block.setRCHighLimit(i + numberOfBlocks);
            block.setRCEnabled(true);
            blocks.add(block);
        }
    }

    @Test
    public void get_current_config_block_returns_block_with_matching_name() {
        // Act
        Block returnedBlock = runControlViewModel.getCurrentConfigBlock("block1");
        
        // Assert
        assertEquals("block1", returnedBlock.getName());
    }

    @Test
    public void get_current_config_block_that_does_not_exist_returns_null() {
        // Act
        Block returnedBlock = runControlViewModel.getCurrentConfigBlock("block0");

        // Assert
        assertNull(returnedBlock);
    }

}
