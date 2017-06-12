
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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.writing.Writable;

@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class TransformingWriterTest {

    private static final String INPUT_VALUE = "input";
    private static final String OUTPUT_VALUE = "output";

    @Test
    public void write_writes_the_transformed_value_to_the_destination_writable() throws IOException {
        // Arrange
        TestableTransformingWriter<String, String> writer = new TestableTransformingWriter<>();
        writer.associate(INPUT_VALUE, OUTPUT_VALUE);
        
        Writable<String> mockWritable = mock(Writable.class);
        writer.writeTo(mockWritable);
        
        verify(mockWritable, never()).write(anyString());

        // Act
        writer.write(INPUT_VALUE);

        // Assert
        verify(mockWritable, times(1)).write(anyString());
        verify(mockWritable, times(1)).write(OUTPUT_VALUE);
    }

    @Test
    public void write_does_not_write_a_value_that_transforms_to_null() throws IOException {
        // Arrange
        TestableTransformingWriter<String, String> writer = new TestableTransformingWriter<>();
        writer.associate(INPUT_VALUE, null);

        Writable<String> mockWritable = mock(Writable.class);
        writer.writeTo(mockWritable);

        // Act
        writer.write(INPUT_VALUE);

        // Assert
        verify(mockWritable, never()).write(anyString());
    }
}
