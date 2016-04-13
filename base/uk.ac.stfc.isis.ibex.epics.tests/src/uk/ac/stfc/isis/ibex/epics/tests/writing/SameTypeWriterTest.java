
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

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class SameTypeWriterTest {

    private SameTypeWriter<String> writer;
    private static final String FIRST_VALUE = "first value";

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
    public void last_written_contains_last_written_value() {
        // Arrange
        assertNotEquals(writer.lastWritten(), FIRST_VALUE);

        // Act
        writer.write(FIRST_VALUE);

        // Assert
        assertEquals(FIRST_VALUE, writer.lastWritten());
    }

    @Test
    public void writing_a_value_pushes_the_value_to_all_writables() {
        // Arrange
        Writable<String> mockWritable1 = mock(Writable.class);
        Writable<String> mockWritable2 = mock(Writable.class);

        writer.writeTo(mockWritable1);
        writer.writeTo(mockWritable2);

        // Act
        writer.write(FIRST_VALUE);

        // Assert
        verify(mockWritable1, times(1)).write(FIRST_VALUE);
        verify(mockWritable2, times(1)).write(FIRST_VALUE);
    }

    @Test
    public void writeTo_only_adds_a_writable_once() {
        // Arrange
        Writable<String> mockWritable = mock(Writable.class);

        // Act
        writer.writeTo(mockWritable);
        writer.writeTo(mockWritable);
        writer.write(FIRST_VALUE);

        // Assert
        verify(mockWritable, times(1)).write(FIRST_VALUE);
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

}
