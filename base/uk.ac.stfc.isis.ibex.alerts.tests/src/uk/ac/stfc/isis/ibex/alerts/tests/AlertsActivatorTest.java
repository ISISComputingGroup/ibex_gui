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
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.alerts.AlertsActivator;
import uk.ac.stfc.isis.ibex.alerts.AlertsControlVariables;
import uk.ac.stfc.isis.ibex.alerts.AlertsServer;

/**
 * Tests the AlertsActivator class.
 */
public class AlertsActivatorTest {

    private AlertsActivator activator;
    private BundleContext mockContext;

    @Before
    public void setUp() {
        mockContext = mock(BundleContext.class);
        activator = new AlertsActivator();
    }

    @Test
    public void testSingletonInstance() {
        assertNotNull(AlertsActivator.getInstance());
        assertEquals(activator, AlertsActivator.getInstance());
    }

    @Test
    public void testGetVariables() {
        AlertsControlVariables variables = activator.getVariables();
        assertNotNull(variables);
    }

    @Test
    public void testGetServer() {
        AlertsServer server = activator.getServer();
        assertNotNull(server);
    }

    @Test
    public void testStartSetsContext() throws Exception {
        activator.start(mockContext);
        assertEquals(mockContext, AlertsActivator.getContext());
    }

    @Test
    public void testStopClearsContext() throws Exception {
        activator.start(mockContext);
        activator.stop(mockContext);
        assertNull(AlertsActivator.getContext());
    }
}
