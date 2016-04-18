
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
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class SameTypeWritableTest {

    private Writable<String> mockDestination;
    private TestWritable<String> testDestination;

    // This field is used to test functionality of the base writable class
    private BaseWritable<String> baseWritable;

    @Before
    public void setUp() {
        // Use a mock destination where possible; use a TestWritable when
        // triggering a method call is needed
        mockDestination = mock(Writable.class);
        testDestination = new TestWritable<>();
    }

    @Test
    public void can_write_is_false_at_initialisation() {
        // Arrange
        baseWritable = new SameTypeWritable<String>(mockDestination);

        // Assert
        assertFalse(baseWritable.canWrite());
    }

    @Test
    public void last_error_is_null_at_initialisation() {
        // Arrange
        baseWritable = new SameTypeWritable<String>(mockDestination);

        // Assert
        assertNull(baseWritable.lastError());
    }

    @Test
    public void subscribe_updates_writable_canWrite() {
        // Arrange
        baseWritable = new SameTypeWritable<String>(mockDestination);
        ConfigurableWriter<String, String> mockWriter = mock(ConfigurableWriter.class);

        // Act
        baseWritable.subscribe(mockWriter);

        // Assert
        verify(mockWriter, times(1)).onCanWriteChanged(anyBoolean());
    }

    @Test
    public void subscribe_updates_writable_onError() {
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
    public void subscribe_returns_an_unsubscriber_that_removes_the_input_writer_from_the_list_of_writers() {
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

}


