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
package uk.ac.stfc.isis.ibex.ui.alerts.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.ui.alerts.Activator;

/**
 * Tests the Activator class for the Alerts UI plugin.
 */
public class ActivatorTest {

    private Activator activator;
    private BundleContext mockContext;
    @Before
    public void setUp() {
    	mockContext = mock(BundleContext.class);
        activator = new Activator();
    }

    @Test
    public void testStartSetsSharedInstance() throws Exception {
        activator.start(mockContext);
        assertNotNull("Shared instance should be set after start", Activator.getDefault());
    }

    @Test
    public void testStopClearsSharedInstance() throws Exception {
        activator.start(mockContext);
        activator.stop(mockContext);
        assertNull("Shared instance should be cleared after stop", Activator.getDefault());
    }

    @Test
    public void testGetDefaultReturnsSharedInstance() throws Exception {
        activator.start(mockContext);
        assertEquals("getDefault should return the shared instance", activator, Activator.getDefault());
    }
}
