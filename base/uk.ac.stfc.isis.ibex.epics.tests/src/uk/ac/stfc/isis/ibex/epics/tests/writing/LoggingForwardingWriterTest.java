
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
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.writing.ConfigurableWriter;
import uk.ac.stfc.isis.ibex.epics.writing.LoggingForwardingWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class LoggingForwardingWriterTest {

    private static final String ID = "test id";
    private static final String VALUE = "value";

    private Logger mockLogger;
    private ConfigurableWriter<String, String> mockInputWriter;
    private LoggingForwardingWriter<String> writer;

    @Before
    public void setUp() {
        // Arrange
        mockLogger = mock(Logger.class);
        mockInputWriter = mock(ConfigurableWriter.class);
        
        writer = new LoggingForwardingWriter<String>(mockLogger, ID, mockInputWriter);
    }

    @Test
    public void write_logs_input_value() {
        // Act
        writer.write(VALUE);
        
        // Assert
        String expectedMessage = ID + " " + VALUE;
        verify(mockLogger, times(1)).info(expectedMessage);
    }

    @Test
    public void write_forwards_value_to_input_writer() {
        // Act
        writer.write(VALUE);

        // Assert
        verify(mockInputWriter, times(1)).write(VALUE);
    }

    @Test
    public void canWrite_returns_same_canWrite_of_input_writer_true() {
        // Arrange
        when(mockInputWriter.canWrite()).thenReturn(true);

        // Act-Assert
        assertTrue(writer.canWrite());
    }

    @Test
    public void canWrite_returns_same_canWrite_of_input_writer_false() {
        // Arrange
        when(mockInputWriter.canWrite()).thenReturn(false);

        // Act-Assert
        assertFalse(writer.canWrite());
    }

    @Test
    public void onError_logs_input_exception() {
        // Arrange
        Exception exception = new Exception();

        // Act
        writer.onError(exception);

        // Assert
        String expectedMessage = ID + " " + exception.toString();
        verify(mockLogger, times(1)).info(expectedMessage);
    }

    @Test
    public void setting_can_write_has_no_effect_false_case() {
        // Arrange
        boolean expected = false;
        when(mockInputWriter.canWrite()).thenReturn(expected);

        // Act
        writer.onCanWriteChanged(!expected);

        // Assert
        assertEquals(expected, writer.canWrite());
        verify(mockInputWriter, never()).onCanWriteChanged(anyBoolean());
    }

    @Test
    public void setting_can_write_has_no_effect_true_case() {
        // Arrange
        boolean expected = true;
        when(mockInputWriter.canWrite()).thenReturn(expected);

        // Act
        writer.onCanWriteChanged(!expected);

        // Assert
        assertEquals(expected, writer.canWrite());
        verify(mockInputWriter, never()).onCanWriteChanged(anyBoolean());
    }

    @Test
    public void writeTo_forwards_writable_to_input_writer() {
        // Arrange
        Writable<String> mockWritable = mock(Writable.class);

        // Act
        writer.writeTo(mockWritable);

        // Assert
        verify(mockInputWriter, times(1)).writeTo(mockWritable);
    }

    @Test
    public void writeTo_returns_subscription_to_input_writer() {
        // Arrange
        Subscription mockSubscription = mock(Subscription.class);
        Writable<String> mockWritable = mock(Writable.class);
        when(mockInputWriter.writeTo(any(Writable.class))).thenReturn(mockSubscription);

        // Act
        Subscription returnedSubscription = writer.writeTo(mockWritable);

        // Assert
        assertSame(mockSubscription, returnedSubscription);
    }

    @Test
    public void close_unsubscribes_from_all_writables() {
        // Arrange
        Subscription mockSubscription1 = mock(Subscription.class);
        Subscription mockSubscription2 = mock(Subscription.class);
        Writable<String> mockWritable1 = mock(Writable.class);
        Writable<String> mockWritable2 = mock(Writable.class);

        when(mockInputWriter.writeTo(mockWritable1)).thenReturn(mockSubscription1);
        when(mockInputWriter.writeTo(mockWritable2)).thenReturn(mockSubscription2);

        writer.writeTo(mockWritable1);
        writer.writeTo(mockWritable2);

        // Act
        writer.close();
        
        // Assert
        verify(mockSubscription1, times(1)).removeObserver();
        verify(mockSubscription2, times(1)).removeObserver();
    }

    @Test(expected = IllegalArgumentException.class)
    public void initialisation_with_null_log_throws() {
        // Arrange
        new LoggingForwardingWriter<String>(null, ID, mockInputWriter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void initialisation_with_null_writer_throws() {
        // Arrange
        new LoggingForwardingWriter<String>(mockLogger, ID, null);
    }
}
