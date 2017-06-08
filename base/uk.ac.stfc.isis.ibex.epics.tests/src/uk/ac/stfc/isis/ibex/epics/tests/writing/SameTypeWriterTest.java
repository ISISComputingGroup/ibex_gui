
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.epics.tests.writing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class SameTypeWriterTest {

    private SameTypeWriter<String> writer;
    private static final String A_VALUE = "a value";

    @Before
    public void setUp() {
        // Arrange
        writer = new SameTypeWriter<>();
    }

    @Test
    public void canWrite_is_false_by_default() {
        // Assert
        assertFalse(writer.canWrite());
    }

    @Test
    public void can_write_can_be_set_to_true() {
        // Arrange
        boolean expected = true;
        assertFalse(writer.canWrite());

        // Act
        writer.onCanWriteChanged(expected);

        // Assert
        assertEquals(expected, writer.canWrite());
    }

    @Test
    public void can_write_can_be_set_to_false() {
        // Arrange
        boolean expected = false;
        writer.onCanWriteChanged(true);

        // Act
        writer.onCanWriteChanged(expected);

        // Assert
        assertEquals(expected, writer.canWrite());
    }

    @Test
    public void last_written_is_null_by_default() {
        // Assert
        assertNull(writer.lastWritten());
    }

    @Test
    public void last_written_contains_last_written_value() throws IOException {
        // Arrange
        assertNotEquals(writer.lastWritten(), A_VALUE);

        // Act
        writer.write(A_VALUE);

        // Assert
        assertEquals(A_VALUE, writer.lastWritten());
    }

    @Test
    public void writing_a_value_pushes_the_value_to_all_writables() throws IOException {
        // Arrange
        Writable<String> mockWritable1 = mock(Writable.class);
        Writable<String> mockWritable2 = mock(Writable.class);

        writer.writeTo(mockWritable1);
        writer.writeTo(mockWritable2);

        // Act
        writer.write(A_VALUE);

        // Assert
        verify(mockWritable1, times(1)).write(A_VALUE);
        verify(mockWritable2, times(1)).write(A_VALUE);
    }

    @Test
    public void a_writable_is_added_only_once() throws IOException {
        // Arrange
        Writable<String> mockWritable = mock(Writable.class);

        // Act
        writer.writeTo(mockWritable);
        writer.writeTo(mockWritable);
        writer.write(A_VALUE);

        // Assert
        verify(mockWritable, times(1)).write(A_VALUE);
    }

    @Test
    public void adding_a_writable_returns_an_unsubscriber_that_removes_the_writable_from_the_list_of_writables() throws IOException {
        // Arrange
        Writable<String> mockWritable = mock(Writable.class);
        Subscription returnedSubscription = writer.writeTo(mockWritable);

        // Act
        returnedSubscription.removeObserver();

        // Assert
        writer.write(A_VALUE);
        verify(mockWritable, never()).write(A_VALUE);
    }

    @Test
    public void last_error_is_null_by_default() {
        // Assert
        assertNull(writer.lastError());
    }

    @Test
    public void last_error_contains_last_error_set() {
        // Arrange
        Exception exception = new Exception();

        // Act
        writer.onError(exception);

        // Assert
        assertEquals(exception, writer.lastError());
    }

    @Test
    public void canWrite_contains_the_latest_can_write_from_all_writables_subscriptions() {
        // Arrange
        StubWritable<String> stubWritable1 = new StubWritable<>();
        StubWritable<String> stubWritable2 = new StubWritable<>();

        assertFalse(stubWritable1.canWrite());
        assertFalse(stubWritable2.canWrite());
        assertFalse(writer.canWrite());

        writer.writeTo(stubWritable1);
        writer.writeTo(stubWritable2);

        assertFalse(writer.canWrite());

        // Act
        stubWritable1.simulateCanWriteChanged(true);
        assertFalse(writer.canWrite());

        stubWritable1.subscribe(writer);
        stubWritable2.subscribe(writer);

        stubWritable1.simulateCanWriteChanged(true);
        assertTrue(stubWritable1.canWrite());
        assertFalse(stubWritable2.canWrite());
        assertTrue(writer.canWrite());

        stubWritable2.simulateCanWriteChanged(false);
        assertTrue(stubWritable1.canWrite());
        assertFalse(stubWritable2.canWrite());
        assertFalse(writer.canWrite());
    }
}
