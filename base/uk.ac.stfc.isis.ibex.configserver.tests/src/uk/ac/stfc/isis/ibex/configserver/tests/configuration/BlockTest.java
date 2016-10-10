
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
package uk.ac.stfc.isis.ibex.configserver.tests.configuration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;

@SuppressWarnings("checkstyle:methodname")
public class BlockTest {

    private void verifyBlockProperties(Block block, String name, String pv, boolean visible, boolean local,
            String component, float lowLimit, float highLimit, Boolean runcontrol, boolean logPeriodic, int logRate,
            float logDeadband) {
        assertEquals(name, block.getName());
        assertEquals(pv, block.getPV());
        assertEquals(visible, block.getIsVisible());
        assertEquals(local, block.getIsLocal());
        assertEquals(component, block.getComponent());
        assertEquals(lowLimit, block.getRCLowLimit(), 0);
        assertEquals(highLimit, block.getRCHighLimit(), 0);
        assertEquals(runcontrol, block.getRCEnabled());
        assertEquals(logPeriodic, block.getLogPeriodic());
        assertEquals(logRate, block.getLogRate());
        assertEquals(logDeadband, block.getLogDeadband(), 0);
    }

    @Test
    public void WHEN_new_block_is_created_THEN_properties_have_correct_default_values() {
        // Arrange
        String name = "Block name";
        String pv = "MY:PV";
        boolean visible = true;
        boolean local = true;

        String expectedComponent = null;
        float expectedLowLimit = 0.0f;
        float expectedHighLimit = 0.0f;
        Boolean expectedRunControl = false;
        Boolean expectedLogPeriodic = true;
        int expectedLogRate = 30;
        float expectedLogDeadband = 0.0f;

        // Act
        Block block = new Block(name, pv, visible, local);

        // Assert
        verifyBlockProperties(block, name, pv, visible, local, expectedComponent, expectedLowLimit, expectedHighLimit,
                expectedRunControl, expectedLogPeriodic, expectedLogRate, expectedLogDeadband);

    }

//    public Block(String name, String pv, boolean visible, boolean local) {
//        this(name, pv, visible, local, null, 0.0f, 0.0f, false, true, DEFAULT_SCAN_RATE, 0.0f);
//    }

//    public Block(String name, String pv, boolean visible, boolean local, String component, float lowlimit,
//            float highlimit, Boolean runcontrol, boolean logPeriodic, int logRate, float logDeadband) {
}
