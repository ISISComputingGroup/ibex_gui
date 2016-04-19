
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
import uk.ac.stfc.isis.ibex.epics.writing.BaseWritable;
import uk.ac.stfc.isis.ibex.epics.writing.ConfigurableWriter;
import uk.ac.stfc.isis.ibex.epics.writing.ForwardingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class SameTypeWritableTest {

    private static String VALUE = "123";

    private Writable<String> mockDestination;
    private TestWritable<String> testDestination;

    // This field is used to test functionality of the BaseWritable base class
    private BaseWritable<String> baseWritable;

    // This field is used to test functionality of the ForwardingWritable base
    // class
    private ForwardingWritable<String, String> forwardingWritable;

    // This field is used to test functionality of the SameTypeWritable class
    private SameTypeWritable<String> writable;

    @Before
    public void setUp() {
        // Use a mock destination where possible; use a TestWritable when
        // triggering a method call is needed
        mockDestination = mock(Writable.class);
        testDestination = new TestWritable<>();
    }

    @Test
    public void last_error_is_null_at_initialisation() {
        // Arrange
        baseWritable = new SameTypeWritable<String>(mockDestination);

        // Assert
        assertNull(baseWritable.lastError());
    }

    @Test
    public void subscribing_a_writer_updates_the_writer_canWrite() {
        // Arrange
        baseWritable = new SameTypeWritable<String>(mockDestination);
        ConfigurableWriter<String, String> mockWriter = mock(ConfigurableWriter.class);

        // Act
        baseWritable.subscribe(mockWriter);

        // Assert
        verify(mockWriter, times(1)).onCanWriteChanged(anyBoolean());
    }

    @Test
    public void subscribing_a_writer_updates_the_writer_onError() {
        // Arrange
        baseWritable = new SameTypeWritable<String>(mockDestination);
        ConfigurableWriter<String, String> mockWriter = mock(ConfigurableWriter.class);

        // Act
        baseWritable.subscribe(mockWriter);

        // Assert
        verify(mockWriter, times(1)).onError(any(Exception.class));
    }

    @Test
    public void last_error_is_updated_on_destination_error() {
        // Arrange
        baseWritable = new SameTypeWritable<String>(testDestination);

        Exception exception = new Exception();

        // Act
        testDestination.simulateError(exception);

        // Assert
        assertEquals(exception, baseWritable.lastError());
    }

    @Test
    public void all_subscribed_writers_are_notified_in_case_of_error() {
        // Arrange
        baseWritable = new SameTypeWritable<String>(testDestination);

        ConfigurableWriter<String, String> mockWriter1 = mock(ConfigurableWriter.class);
        ConfigurableWriter<String, String> mockWriter2 = mock(ConfigurableWriter.class);
        baseWritable.subscribe(mockWriter1);
        baseWritable.subscribe(mockWriter2);

        Exception exception = new Exception();

        // Act
        testDestination.simulateError(exception);

        // Assert
        verify(mockWriter1, times(1)).onError(exception);
        verify(mockWriter2, times(1)).onError(exception);
    }

    @Test
    public void can_write_is_updated_on_destination_can_write_change_true_case() {
        // Arrange
        baseWritable = new SameTypeWritable<String>(testDestination);
        boolean expected = true;

        // Act
        testDestination.simulateCanWriteChanged(expected);

        // Assert
        assertEquals(expected, baseWritable.canWrite());
    }

    @Test
    public void can_write_is_updated_on_destination_can_write_change_false_case() {
        // Arrange
        baseWritable = new SameTypeWritable<String>(testDestination);
        boolean expected = false;
        
        // Act
        testDestination.simulateCanWriteChanged(expected);

        // Assert
        assertEquals(expected, baseWritable.canWrite());
    }

    @Test
    public void all_subscribed_writers_are_notified_of_can_write_changed() {
        // Arrange
        baseWritable = new SameTypeWritable<String>(testDestination);

        ConfigurableWriter<String, String> mockWriter1 = mock(ConfigurableWriter.class);
        ConfigurableWriter<String, String> mockWriter2 = mock(ConfigurableWriter.class);
        baseWritable.subscribe(mockWriter1);
        baseWritable.subscribe(mockWriter2);

        boolean expected = true;

        // Act
        testDestination.simulateCanWriteChanged(expected);

        // Assert
        verify(mockWriter1, times(1)).onCanWriteChanged(expected);
        verify(mockWriter2, times(1)).onCanWriteChanged(expected);
    }

    @Test
    public void subscribing_a_writer_returns_an_unsubscriber_for_the_writer() {
        // Arrange
        baseWritable = new SameTypeWritable<String>(testDestination);

        ConfigurableWriter<String, String> mockWriter = mock(ConfigurableWriter.class);
        Subscription returnedSubscription = baseWritable.subscribe(mockWriter);

        Exception exception = new Exception();

        // Act
        returnedSubscription.removeObserver();

        // Assert
        testDestination.simulateError(exception);
        verify(mockWriter, never()).onError(exception);
    }

    @Test
    public void a_writer_gets_subscribed_only_once() {
        // Arrange
        baseWritable = new SameTypeWritable<String>(testDestination);

        ConfigurableWriter<String, String> mockWriter = mock(ConfigurableWriter.class);

        Exception exception = new Exception();

        // Act
        baseWritable.subscribe(mockWriter);
        baseWritable.subscribe(mockWriter);

        testDestination.simulateError(exception);

        // Assert
        verify(mockWriter, times(1)).onError(exception);
    }

    @Test
    public void a_null_value_is_not_written() {
        // Arrange
        forwardingWritable = new SameTypeWritable<>(mockDestination);

        // Act
        forwardingWritable.write(null);

        // Assert
        verify(mockDestination, never()).write(anyString());
    }

    @Test
    public void closing_the_writable_closes_current_destination_writable() {
        // Arrange
        forwardingWritable = new SameTypeWritable<>(mockDestination);
        verify(mockDestination, never()).close();

        // Act
        forwardingWritable.close();

        // Assert
        verify(mockDestination, times(1)).close();
    }

    @Test
    public void closing_the_writable_unsubscribes_from_current_destination_writable() {
        // Arrange
        Subscription mockSubscription = mock(Subscription.class);
        when(mockDestination.subscribe(any(ConfigurableWriter.class))).thenReturn(mockSubscription);

        forwardingWritable = new SameTypeWritable<>(mockDestination);
        verify(mockSubscription, never()).removeObserver();

        // Act
        forwardingWritable.close();

        // Assert
        verify(mockSubscription, times(1)).removeObserver();
    }

    @Test
    public void closing_the_writable_stops_updating_the_destination_writable_on_further_writes() {
        // Arrange
        forwardingWritable = new SameTypeWritable<>(mockDestination);

        // Act
        forwardingWritable.close();
        forwardingWritable.write(VALUE);

        // Assert
        verify(mockDestination, never()).write(anyString());
    }
    
    @Test
    public void can_write_is_equal_to_destination_writable_can_write_true_case() {
        // Arrange
        boolean expected = true;
        when(mockDestination.canWrite()).thenReturn(expected);
        forwardingWritable = new SameTypeWritable<String>(mockDestination);

        // Assert
        assertEquals(expected, forwardingWritable.canWrite());
    }

    @Test
    public void can_write_is_equal_to_destination_writable_can_write_false_case() {
        // Arrange
        boolean expected = false;
        when(mockDestination.canWrite()).thenReturn(expected);
        forwardingWritable = new SameTypeWritable<String>(mockDestination);

        // Assert
        assertEquals(expected, forwardingWritable.canWrite());
    }

    @Test
    public void setting_a_new_destination_writable_updates_can_write_to_that_of_the_new_destination_true_case() {
        // Arrange
        boolean newValue = true;
        boolean oldValue = !newValue;
        Writable<String> mockOldDestination = mock(Writable.class);
        when(mockOldDestination.canWrite()).thenReturn(oldValue);
        when(mockDestination.canWrite()).thenReturn(newValue);

        forwardingWritable = new SameTypeWritable<>(mockOldDestination);
        assertEquals(oldValue, forwardingWritable.canWrite());

        // Act
        forwardingWritable.setWritable(mockDestination);

        // Assert
        assertEquals(newValue, forwardingWritable.canWrite());
    }

    @Test
    public void setting_a_new_destination_writable_subscribes_to_the_new_destination() {
        // Arrange
        Writable<String> mockOldDestination = mock(Writable.class);
        forwardingWritable = new SameTypeWritable<>(mockOldDestination);
        verify(mockDestination, never()).subscribe(any(ConfigurableWriter.class));

        // Act
        forwardingWritable.setWritable(mockDestination);

        // Assert
        verify(mockDestination, times(1)).subscribe(any(ConfigurableWriter.class));
    }

    @Test
    public void setting_a_new_destination_writable_closes_old_destination() {
        // Arrange
        Writable<String> mockOldDestination = mock(Writable.class);
        forwardingWritable = new SameTypeWritable<>(mockOldDestination);
        verify(mockOldDestination, never()).close();

        // Act
        forwardingWritable.setWritable(mockDestination);

        // Assert
        verify(mockOldDestination, times(1)).close();
    }

    @Test
    public void setting_a_new_destination_writable_unsubscribes_from_old_destination() {
        // Arranged
        Subscription mockSubscription = mock(Subscription.class);
        Writable<String> mockOldDestination = mock(Writable.class);
        when(mockOldDestination.subscribe(any(ConfigurableWriter.class))).thenReturn(mockSubscription);

        forwardingWritable = new SameTypeWritable<>(mockOldDestination);
        verify(mockSubscription, never()).removeObserver();

        // Act
        forwardingWritable.setWritable(mockDestination);

        // Assert
        verify(mockSubscription, times(1)).removeObserver();
    }

    @Test
    public void setting_a_new_destination_writable_stops_writing_to_old_destination() {
        // Arrange
        Writable<String> mockOldDestination = mock(Writable.class);
        forwardingWritable = new SameTypeWritable<>(mockOldDestination);

        // Act
        forwardingWritable.setWritable(mockDestination);
        forwardingWritable.write(VALUE);

        // Assert
        verify(mockOldDestination, never()).write(anyString());
    }

    @Test
    public void setting_a_new_destination_writable_allows_to_write_to_new_destination() {
        // Arrange
        Writable<String> mockOldDestination = mock(Writable.class);
        forwardingWritable = new SameTypeWritable<>(mockOldDestination);
        verify(mockDestination, never()).write(anyString());

        // Act
        forwardingWritable.setWritable(mockDestination);
        forwardingWritable.write(VALUE);

        // Assert
        verify(mockDestination, times(1)).write(VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setting_a_new_destination_writable_throws_for_null_destination() {
        // Arrange
        Writable<String> mockOldDestination = mock(Writable.class);
        forwardingWritable = new SameTypeWritable<>(mockOldDestination);

        // Act
        forwardingWritable.setWritable(null);
    }

    @Test
    public void write_writes_the_value_without_transformations() {
        // Arrange
        writable = new SameTypeWritable<>(mockDestination);
        verify(mockDestination, never()).write(anyString());

        // Act
        writable.write(VALUE);

        // Assert
        verify(mockDestination, times(1)).write(anyString());
        verify(mockDestination, times(1)).write(VALUE);
    }
}


