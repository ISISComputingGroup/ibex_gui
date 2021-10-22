
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

import java.io.IOException;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.writing.TransformingWritableWithConverter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class TransformingWritableWithConverterTest {

    private static final String VALUE = "123";
    private static final String CONVERTED_VALUE = "456";

    private Writable<String> mockDestination;
    private Function<String, String> mockConverter = input -> input == VALUE ? CONVERTED_VALUE : null;

    @Before
    public void setUp() {
        // Arrange
        mockDestination = mock(Writable.class);
        mockConverter = mock(Function.class);
    }

    private TransformingWritableWithConverter<String, String> createWritable() {
        return new TransformingWritableWithConverter<String, String>(mockDestination, mockConverter);
    }

    @Test
    public void value_conversion_error_is_stored_in_last_error() throws ConversionException, IOException {
        // Arrange
        ConversionException exception = new ConversionException("conversion error");
        when(mockConverter.apply(VALUE)).thenThrow(exception);
        TransformingWritableWithConverter<String, String> forwardingWritable = createWritable();

        assertNull(forwardingWritable.lastError());

        // Act
        forwardingWritable.write(VALUE);

        // Assert
        assertEquals(exception, forwardingWritable.lastError());
    }

    @Test
    public void when_converter_throws_conversion_error_then_writable_does_not_write_to_destination()
            throws ConversionException, IOException {
        // Arrange
        ConversionException exception = new ConversionException("conversion error");
        when(mockConverter.apply(VALUE)).thenThrow(exception);

        TransformingWritableWithConverter<String, String> forwardingWritable = createWritable();

        // Act
        forwardingWritable.write(VALUE);

        // Assert
        verify(mockDestination, never()).write(anyString());
    }
}



