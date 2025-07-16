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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collections;
		
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.ac.stfc.isis.ibex.alerts.AlertsServer;
import uk.ac.stfc.isis.ibex.configserver.Displaying;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayAlerts;
import uk.ac.stfc.isis.ibex.configserver.displaying.TopLevelAlertSettings;
import uk.ac.stfc.isis.ibex.ui.alerts.AlertsViewModel;

public class AlertsViewModelTest {
	AutoCloseable openMocks;
    private AlertsViewModel viewModel;

    @Mock
    private Displaying mockConfig;

    @Mock
    private AlertsServer mockAlertsServer;

    @Mock
    private DisplayAlerts mockDisplayAlert;

    @Mock
    private TopLevelAlertSettings mockTopLevelSettings;

    @Before
    public void setUp() {
    	openMocks = MockitoAnnotations.openMocks(this);
        when(mockConfig.getDisplayAlerts()).thenReturn(Collections.singletonList(mockDisplayAlert));
        when(mockConfig.getTopLevelAlertSettings()).thenReturn(mockTopLevelSettings);

        viewModel = new AlertsViewModel(mockConfig, mockAlertsServer);
    }

    @After
    public void tearDown() throws Exception {
    	openMocks.close();
    }
    
    @Test
    public void testSetAndGetLowLimit() {
        viewModel.setLowLimit(10.0);
        assertEquals(Double.valueOf(10.0), viewModel.getLowLimit());
    }

    @Test
    public void testSetAndGetHighLimit() {
        viewModel.setHighLimit(20.0);
        assertEquals(Double.valueOf(20.0), viewModel.getHighLimit());
    }

    @Test
    public void testSetAndGetEnabled() {
        viewModel.setEnabled(true);
        assertTrue(viewModel.isEnabled());
    }

    @Test
    public void testSetAndGetDelayIn() {
        viewModel.setDelayIn(5.0);
        assertEquals(Double.valueOf(5.0), viewModel.getDelayIn());
    }

    @Test
    public void testSetAndGetDelayOut() {
        viewModel.setDelayOut(15.0);
        assertEquals(Double.valueOf(15.0), viewModel.getDelayOut());
    }

    @Test
    public void testSetAndGetMessage() {
        viewModel.setMessage("Test Message");
        assertEquals("Test Message", viewModel.getMessage());
    }

    @Test
    public void testSetAndGetEmails() {
        viewModel.setEmails("test@example.com");
        assertEquals("test@example.com", viewModel.getEmails());
    }

    @Test
    public void testSetAndGetMobiles() {
        viewModel.setMobiles("1234567890");
        assertEquals("1234567890", viewModel.getMobiles());
    }

    @Test
    public void testResetAlertSettings() {
        viewModel.resetAlertSettings();
        // Verify that the reset logic is invoked without exceptions
    }

    @Test
    public void testSetSource() {
        when(mockDisplayAlert.getHighLimit()).thenReturn(20.0);
        when(mockDisplayAlert.getLowLimit()).thenReturn(10.0);
        when(mockDisplayAlert.getEnabled()).thenReturn(true);
        when(mockDisplayAlert.getDelayIn()).thenReturn(5.0);
        when(mockDisplayAlert.getDelayOut()).thenReturn(15.0);

        viewModel.setSource(mockDisplayAlert);

        assertEquals(Double.valueOf(20.0), viewModel.getHighLimit());
        assertEquals(Double.valueOf(10.0), viewModel.getLowLimit());
        assertTrue(viewModel.isEnabled());
        assertEquals(Double.valueOf(5.0), viewModel.getDelayIn());
        assertEquals(Double.valueOf(15.0), viewModel.getDelayOut());
    }

    @Test
    public void testResetTopLevelSettings() {
        when(mockTopLevelSettings.getMessage()).thenReturn("Default Message");
        when(mockTopLevelSettings.getEmails()).thenReturn("default@example.com");
        when(mockTopLevelSettings.getMobiles()).thenReturn("9876543210");

        viewModel.resetTopLevelSettings(viewModel);

        assertEquals("Default Message", viewModel.getMessage());
        assertEquals("default@example.com", viewModel.getEmails());
        assertEquals("9876543210", viewModel.getMobiles());
    }
    
    @Test
    public void testGetMaskedEmails() {
        // Test with a single email
        viewModel.setEmails("test@example.com");
        assertEquals("te**@example.com", viewModel.getMaskedEmails());

        // Test with multiple emails
        viewModel.setEmails("test@example.com; user@domain.com");
        assertEquals("te**@example.com; us**@domain.com", viewModel.getMaskedEmails());

        // Test with empty email
        viewModel.setEmails("");
        assertEquals("", viewModel.getMaskedEmails());

        // Test with null email
        viewModel.setEmails(null);
        assertEquals("", viewModel.getMaskedEmails());
    }

    @Test
    public void testGetMaskedMobiles() {
        // Test with a single mobile number
        viewModel.setMobiles("1234567890");
        assertEquals("12******90", viewModel.getMaskedMobiles());

        // Test with multiple mobile numbers
        viewModel.setMobiles("1234567890; 9876543210");
        assertEquals("12******90; 98******10", viewModel.getMaskedMobiles());

        // Test with empty mobile number
        viewModel.setMobiles("");
        assertEquals("", viewModel.getMaskedMobiles());

        // Test with null mobile number
        viewModel.setMobiles(null);
        assertEquals("", viewModel.getMaskedMobiles());
    }
}

