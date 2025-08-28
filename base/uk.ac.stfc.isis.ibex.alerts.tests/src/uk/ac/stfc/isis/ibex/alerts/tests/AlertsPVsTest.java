/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2025
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
package uk.ac.stfc.isis.ibex.alerts.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import uk.ac.stfc.isis.ibex.alerts.AlertsPVs;

/**
 * Tests the AlertsPVs class.
 */
public class AlertsPVsTest {

    private AlertsPVs alertsPVs;

    @Before
    public void setUp() {
        alertsPVs = new AlertsPVs();
    }

    @Test
    public void testGetEmailsPv() {
        String expected = "CS:AC:ALERTS:EMAILS:SP";
        assertEquals(expected, alertsPVs.getEmailsPv());
    }

    @Test
    public void testGetMobilesPv() {
        String expected = "CS:AC:ALERTS:MOBILES:SP";
        assertEquals(expected, alertsPVs.getMobilesPv());
    }

    @Test
    public void testGetMessagePv() {
        String expected = "CS:AC:ALERTS:MESSAGE:SP";
        assertEquals(expected, alertsPVs.getMessagePv());
    }

    @Test
    public void testGetLowLimitPv() {
        String blockName = "testBlock";
        String expected = "CS:SB:testBlock:AC:LOW";
        assertEquals(expected, alertsPVs.getLowLimitPv(blockName));
    }

    @Test
    public void testGetHighLimitPv() {
        String blockName = "testBlock";
        String expected = "CS:SB:testBlock:AC:HIGH";
        assertEquals(expected, alertsPVs.getHighLimitPv(blockName));
    }

    @Test
    public void testGetEnablePv() {
        String blockName = "testBlock";
        String expected = "CS:SB:testBlock:AC:ENABLE";
        assertEquals(expected, alertsPVs.getEnablePv(blockName));
    }

    @Test
    public void testGetDelayInPv() {
        String blockName = "testBlock";
        String expected = "CS:SB:testBlock:AC:IN:DELAY";
        assertEquals(expected, alertsPVs.getDelayInPv(blockName));
    }

    @Test
    public void testGetDelayOutPv() {
        String blockName = "testBlock";
        String expected = "CS:SB:testBlock:AC:OUT:DELAY";
        assertEquals(expected, alertsPVs.getDelayOutPv(blockName));
    }
}
