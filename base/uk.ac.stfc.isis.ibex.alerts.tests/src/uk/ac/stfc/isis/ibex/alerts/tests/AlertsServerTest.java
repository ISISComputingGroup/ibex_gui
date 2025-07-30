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

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.alerts.*;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

/**
 * Tests the AlertsServer class.
 */
@SuppressWarnings("unchecked")
public class AlertsServerTest {

    private AlertsControlVariables mockVariables;
    private AlertsServer alertsServer;

    @Before
    public void setUp() {
        mockVariables = mock(AlertsControlVariables.class);
        alertsServer = new AlertsServer(mockVariables);
    }

    @Test
    public void testGetLowLimit() {
        String blockName = "testBlock";
        ForwardingObservable<Double> mockObservable = mock(ForwardingObservable.class);
        when(mockVariables.getLowLimit(blockName)).thenReturn(mockObservable);

        ForwardingObservable<Double> result = alertsServer.getLowLimit(blockName);

        assertEquals(mockObservable, result);
        verify(mockVariables).getLowLimit(blockName);
    }

    @Test
    public void testSetLowLimit() {
        String blockName = "testBlock";
        Writable<Double> mockWritable = mock(Writable.class);
        when(mockVariables.setLowLimit(blockName)).thenReturn(mockWritable);

        Writable<Double> result = alertsServer.setLowLimit(blockName);

        assertEquals(mockWritable, result);
        verify(mockVariables).setLowLimit(blockName);
    }

    @Test
    public void testGetHighLimit() {
        String blockName = "testBlock";
        ForwardingObservable<Double> mockObservable = mock(ForwardingObservable.class);
        when(mockVariables.getHighLimit(blockName)).thenReturn(mockObservable);

        ForwardingObservable<Double> result = alertsServer.getHighLimit(blockName);

        assertEquals(mockObservable, result);
        verify(mockVariables).getHighLimit(blockName);
    }

    @Test
    public void testSetHighLimit() {
        String blockName = "testBlock";
        Writable<Double> mockWritable = mock(Writable.class);
        when(mockVariables.setHighLimit(blockName)).thenReturn(mockWritable);

        Writable<Double> result = alertsServer.setHighLimit(blockName);

        assertEquals(mockWritable, result);
        verify(mockVariables).setHighLimit(blockName);
    }

    @Test
    public void testGetEnabled() {
        String blockName = "testBlock";
        ForwardingObservable<String> mockObservable = mock(ForwardingObservable.class);
        when(mockVariables.getEnabled(blockName)).thenReturn(mockObservable);

        ForwardingObservable<String> result = alertsServer.getEnabled(blockName);

        assertEquals(mockObservable, result);
        verify(mockVariables).getEnabled(blockName);
    }

    @Test
    public void testSetEnabled() {
        String blockName = "testBlock";
        Writable<String> mockWritable = mock(Writable.class);
        when(mockVariables.setEnabled(blockName)).thenReturn(mockWritable);

        Writable<String> result = alertsServer.setEnabled(blockName);

        assertEquals(mockWritable, result);
        verify(mockVariables).setEnabled(blockName);
    }
   
    @Test
    public void testGetDelayIn() {
        String blockName = "testBlock";
        ForwardingObservable<Double> mockObservable = mock(ForwardingObservable.class);
        when(mockVariables.getDelayIn(blockName)).thenReturn(mockObservable);

        ForwardingObservable<Double> result = alertsServer.getDelayIn(blockName);

        assertEquals(mockObservable, result);
        verify(mockVariables).getDelayIn(blockName);
    }

    @Test
    public void testSetDelayIn() {
        String blockName = "testBlock";
        Writable<Double> mockWritable = mock(Writable.class);
        when(mockVariables.setDelayIn(blockName)).thenReturn(mockWritable);

        Writable<Double> result = alertsServer.setDelayIn(blockName);

        assertEquals(mockWritable, result);
        verify(mockVariables).setDelayIn(blockName);
    }
 
    @Test
    public void testGetDelayOut() {
        String blockName = "testBlock";
        ForwardingObservable<Double> mockObservable = mock(ForwardingObservable.class);
        when(mockVariables.getDelayOut(blockName)).thenReturn(mockObservable);

        ForwardingObservable<Double> result = alertsServer.getDelayOut(blockName);

        assertEquals(mockObservable, result);
        verify(mockVariables).getDelayOut(blockName);
    }

    @Test
    public void testSetDelayOut() {
        String blockName = "testBlock";
        Writable<Double> mockWritable = mock(Writable.class);
        when(mockVariables.setDelayOut(blockName)).thenReturn(mockWritable);

        Writable<Double> result = alertsServer.setDelayOut(blockName);

        assertEquals(mockWritable, result);
        verify(mockVariables).setDelayOut(blockName);
    }
   
    @Test
    public void testGetMessage() {
        ForwardingObservable<String> mockObservable = mock(ForwardingObservable.class);
        when(mockVariables.getMessage()).thenReturn(mockObservable);

        ForwardingObservable<String> result = alertsServer.getMessage();

        assertEquals(mockObservable, result);
        verify(mockVariables).getMessage();
    }

    @Test
    public void testSetMessage() {
        Writable<String> mockWritable = mock(Writable.class);
        when(mockVariables.setMessage()).thenReturn(mockWritable);

        Writable<String> result = alertsServer.setMessage();

        assertEquals(mockWritable, result);
        verify(mockVariables).setMessage();
    }

    @Test
    public void testGetEmails() {
        ForwardingObservable<String> mockObservable = mock(ForwardingObservable.class);
        when(mockVariables.getEmails()).thenReturn(mockObservable);

        ForwardingObservable<String> result = alertsServer.getEmails();

        assertEquals(mockObservable, result);
        verify(mockVariables).getEmails();
    }

    @Test
    public void testSetEmails() {
        Writable<String> mockWritable = mock(Writable.class);
        when(mockVariables.setEmails()).thenReturn(mockWritable);

        Writable<String> result = alertsServer.setEmails();

        assertEquals(mockWritable, result);
        verify(mockVariables).setEmails();
    }

    @Test
    public void testGetMobiles() {
        ForwardingObservable<String> mockObservable = mock(ForwardingObservable.class);
        when(mockVariables.getMobiles()).thenReturn(mockObservable);

        ForwardingObservable<String> result = alertsServer.getMobiles();

        assertEquals(mockObservable, result);
        verify(mockVariables).getMobiles();
    }

    @Test
    public void testSetMobiles() {
        Writable<String> mockWritable = mock(Writable.class);
        when(mockVariables.setMobiles()).thenReturn(mockWritable);

        Writable<String> result = alertsServer.setMobiles();

        assertEquals(mockWritable, result);
        verify(mockVariables).setMobiles();
    }
}
