
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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import org.apache.logging.log4j.Logger;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.writing.LoggingForwardingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class LoggingForwardingWritableTest {

    private static final String ID = "id";
    private static final String VALUE = "value";

    @Test
    public void a_values_is_logged_when_written() {
        // Arrange
        Logger mockLogger = mock(Logger.class);
        Writable<String> mockDestination = mock(Writable.class);
        Converter<String, String> mockConverter = mock(Converter.class);

        LoggingForwardingWritable<String> writable = new LoggingForwardingWritable<>(mockLogger, ID, mockDestination,
                mockConverter);

        String expectedMessage = ID + " " + VALUE;
        verify(mockLogger, never()).info(anyString());

        // Act
        writable.write(VALUE);

        // Assert
        verify(mockLogger, times(1)).info(anyString());
        verify(mockLogger, times(1)).info(expectedMessage);
    }
}
