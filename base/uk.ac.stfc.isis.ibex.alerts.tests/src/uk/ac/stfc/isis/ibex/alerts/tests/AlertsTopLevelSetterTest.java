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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.alerts.AlertsServer;
import uk.ac.stfc.isis.ibex.alerts.AlertsTopLevelSetter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;


/**
 * Tests the AlertsTopLevelSetter class.
 */
@SuppressWarnings("unchecked")
public class AlertsTopLevelSetterTest {

    private AlertsServer mockAlertsServer;
    private Writable<String> mockMessageSetter;
    private Writable<String> mockEmailsSetter;
    private Writable<String> mockMobilesSetter;
    private AlertsTopLevelSetter alertsTopLevelSetter;

    @Before
    public void setUp() {
        mockAlertsServer = mock(AlertsServer.class);
        mockMessageSetter = mock(Writable.class);
        mockEmailsSetter = mock(Writable.class);
        mockMobilesSetter = mock(Writable.class);

        when(mockAlertsServer.setMessage()).thenReturn(mockMessageSetter);
        when(mockAlertsServer.setEmails()).thenReturn(mockEmailsSetter);
        when(mockAlertsServer.setMobiles()).thenReturn(mockMobilesSetter);

        alertsTopLevelSetter = new AlertsTopLevelSetter(mockAlertsServer);
    }

    @Test
    public void testSetMessage() {
        String message = "Test Message";
        alertsTopLevelSetter.setMessage(message);
        verify(mockMessageSetter).uncheckedWrite(message);
    }

    @Test
    public void testSetEmails() {
        String emails = "test@example.com";
        alertsTopLevelSetter.setEmails(emails);
        verify(mockEmailsSetter).uncheckedWrite(emails);
    }

    @Test
    public void testSetMobiles() {
        String mobiles = "1234567890";
        alertsTopLevelSetter.setMobiles(mobiles);
        verify(mockMobilesSetter).uncheckedWrite(mobiles);
    }
}
