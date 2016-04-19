
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

import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.writing.ConfigurableWriter;

@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class BaseWritableTest {

    private TestableBaseWritable<String> baseWritable;

    @Test
    public void last_error_is_null_at_initialisation() {
        // Arrange
        baseWritable = new TestableBaseWritable<String>();

        // Assert
        assertNull(baseWritable.lastError());
    }

    @Test
    public void subscribing_a_writer_updates_the_writer_canWrite() {
        // Arrange
        baseWritable = new TestableBaseWritable<String>();
        ConfigurableWriter<String, String> mockWriter = mock(ConfigurableWriter.class);

        // Act
        baseWritable.subscribe(mockWriter);

        // Assert
        verify(mockWriter, times(1)).onCanWriteChanged(anyBoolean());
    }

    @Test
    public void subscribing_a_writer_updates_the_writer_onError() {
        // Arrange
        baseWritable = new TestableBaseWritable<String>();
        ConfigurableWriter<String, String> mockWriter = mock(ConfigurableWriter.class);

        // Act
        baseWritable.subscribe(mockWriter);

        // Assert
        verify(mockWriter, times(1)).onError(any(Exception.class));
    }

    @Test
    public void last_error_is_updated_on_destination_error() {
        // Arrange
        baseWritable = new TestableBaseWritable<String>();
        Exception exception = new Exception();

        // Act
        baseWritable.error(exception);

        // Assert
        assertEquals(exception, baseWritable.lastError());
    }

    @Test
    public void all_subscribed_writers_are_notified_in_case_of_error() {
        // Arrange
        baseWritable = new TestableBaseWritable<String>();

        ConfigurableWriter<String, String> mockWriter1 = mock(ConfigurableWriter.class);
        ConfigurableWriter<String, String> mockWriter2 = mock(ConfigurableWriter.class);
        baseWritable.subscribe(mockWriter1);
        baseWritable.subscribe(mockWriter2);

        Exception exception = new Exception();

        // Act
        baseWritable.error(exception);

        // Assert
        verify(mockWriter1, times(1)).onError(exception);
        verify(mockWriter2, times(1)).onError(exception);
    }

    @Test
    public void can_write_is_updated_on_destination_can_write_change_true_case() {
        // Arrange
        baseWritable = new TestableBaseWritable<String>();
        boolean expected = true;

        // Act
        baseWritable.canWriteChanged(expected);

        // Assert
        assertEquals(expected, baseWritable.canWrite());
    }

    @Test
    public void can_write_is_updated_on_destination_can_write_change_false_case() {
        // Arrange
        baseWritable = new TestableBaseWritable<String>();
        boolean expected = false;

        // Act
        baseWritable.canWriteChanged(expected);

        // Assert
        assertEquals(expected, baseWritable.canWrite());
    }

    @Test
    public void all_subscribed_writers_are_notified_of_can_write_changed() {
        // Arrange
        baseWritable = new TestableBaseWritable<String>();

        ConfigurableWriter<String, String> mockWriter1 = mock(ConfigurableWriter.class);
        ConfigurableWriter<String, String> mockWriter2 = mock(ConfigurableWriter.class);
        baseWritable.subscribe(mockWriter1);
        baseWritable.subscribe(mockWriter2);

        boolean expected = true;

        // Act
        baseWritable.canWriteChanged(expected);

        // Assert
        verify(mockWriter1, times(1)).onCanWriteChanged(expected);
        verify(mockWriter2, times(1)).onCanWriteChanged(expected);
    }

    @Test
    public void subscribing_a_writer_returns_an_unsubscriber_for_the_writer() {
        // Arrange
        baseWritable = new TestableBaseWritable<String>();

        ConfigurableWriter<String, String> mockWriter = mock(ConfigurableWriter.class);
        Subscription returnedSubscription = baseWritable.subscribe(mockWriter);

        Exception exception = new Exception();

        // Act
        returnedSubscription.removeObserver();

        // Assert
        baseWritable.error(exception);
        verify(mockWriter, never()).onError(exception);
    }

    @Test
    public void a_writer_gets_subscribed_only_once() {
        // Arrange
        baseWritable = new TestableBaseWritable<String>();

        ConfigurableWriter<String, String> mockWriter = mock(ConfigurableWriter.class);

        Exception exception = new Exception();

        // Act
        baseWritable.subscribe(mockWriter);
        baseWritable.subscribe(mockWriter);

        baseWritable.error(exception);

        // Assert
        verify(mockWriter, times(1)).onError(exception);
    }

}
