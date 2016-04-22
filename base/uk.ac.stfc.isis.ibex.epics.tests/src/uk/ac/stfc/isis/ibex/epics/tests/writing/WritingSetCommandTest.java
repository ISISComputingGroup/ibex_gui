
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

package uk.ac.stfc.isis.ibex.epics.tests.writing;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.writing.ConfigurableWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.epics.writing.WritingSetCommand;

@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class WritingSetCommandTest {

    private static final String VALUE = "value";

    private Writable<String> mockDestination;

    @Before
    public void setUp() {
        // Arrange
        mockDestination = mock(Writable.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void Test() {
        // Act
        WritingSetCommand.forDestination(null);
    }

    @Test
    public void a_new_command_subscribes_to_destination_writable() {
        // Arrange
        verify(mockDestination, never()).subscribe(any(ConfigurableWriter.class));

        // Act
        WritingSetCommand.forDestination(mockDestination);

        // Assert
        verify(mockDestination, times(1)).subscribe(any(ConfigurableWriter.class));
    }

    @Test
    public void sending_a_value_writes_the_value_to_destination() {
        // Arrange
        WritingSetCommand<String> command = WritingSetCommand.forDestination(mockDestination);
        verify(mockDestination, never()).write(anyString());

        // Act
        command.send(VALUE);

        // Assert
        verify(mockDestination, times(1)).write(anyString());
        verify(mockDestination, times(1)).write(VALUE);
    }

    @Test
    public void closing_a_command_unsubscribes_from_the_destination() {
        // Arrange
        Subscription mockSubscription = mock(Subscription.class);
        when(mockDestination.subscribe(any(ConfigurableWriter.class))).thenReturn(mockSubscription);
        WritingSetCommand<String> command = WritingSetCommand.forDestination(mockDestination);

        verify(mockSubscription, never()).removeObserver();

        // Act
        command.close();

        // Assert
        verify(mockSubscription, times(1)).removeObserver();
    }

    @Test
    public void closing_a_command_stops_sending_values_to_destination() {
        // Arrange
        Subscription mockSubscription = mock(Subscription.class);
        when(mockDestination.subscribe(any(ConfigurableWriter.class))).thenReturn(mockSubscription);
        WritingSetCommand<String> command = WritingSetCommand.forDestination(mockDestination);

        // Act
        command.close();
        command.send(VALUE);

        // Assert
        verify(mockDestination, never()).write(anyString());
    }

    @Test
    public void canSend_follows_destination_writable_can_write() {
        // Arrange
        StubWritable<String> stubDestination = new StubWritable<>();
        WritingSetCommand<String> command = WritingSetCommand.forDestination(stubDestination);

        assertFalse(stubDestination.canWrite());

        // Act-Assert
        stubDestination.simulateCanWriteChanged(true);
        assertTrue(command.getCanSend());

        stubDestination.simulateCanWriteChanged(false);
        assertFalse(command.getCanSend());
    }
}


