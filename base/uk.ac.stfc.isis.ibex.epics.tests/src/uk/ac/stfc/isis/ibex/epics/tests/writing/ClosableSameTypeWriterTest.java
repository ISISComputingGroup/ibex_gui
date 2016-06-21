
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableSameTypeWriter;
import uk.ac.stfc.isis.ibex.epics.writing.ConfigurableWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class ClosableSameTypeWriterTest {

    private static final String INPUT_VALUE = "input value";

    private Subscription mockSubscription;
    private Writable<String> mockWritable;

    @Before
    public void setUp() {
        // Arrange
        mockSubscription = mock(Subscription.class);
        mockWritable = mock(Writable.class);
        when(mockWritable.subscribe(any(ConfigurableWriter.class))).thenReturn(mockSubscription);
    }

    @Test
    public void writer_can_be_constructed_with_static_method() {
        // Act
        Object returnedObject = ClosableSameTypeWriter.newInstance(mockWritable);
        
        // Assert
        assertEquals(ClosableSameTypeWriter.class, returnedObject.getClass());
    }

    @Test
    public void constructor_adds_input_writable_to_list_of_writables() {
        // Act
        ClosableSameTypeWriter<String> writer = new ClosableSameTypeWriter<>(mockWritable);
        writer.write(INPUT_VALUE);

        // Assert
        verify(mockWritable, times(1)).write(INPUT_VALUE);
    }

    @Test
    public void writer_subscribes_to_writable_at_initialisation() {
        // Act
        ClosableSameTypeWriter<String> writer = new ClosableSameTypeWriter<>(mockWritable);

        // Assert
        verify(mockWritable, times(1)).subscribe(writer);
    }

    @Test
    public void when_writer_is_closed_it_is_unsubscribed_from_writable() {
        // Arrange
        ClosableSameTypeWriter<String> writer = new ClosableSameTypeWriter<>(mockWritable);

        // Act
        writer.close();

        // Assert
        verify(mockSubscription, times(1)).removeObserver();
    }

    @Test
    public void closed_writer_can_still_write_to_writable() {
        // Arrange
        ClosableSameTypeWriter<String> writer = new ClosableSameTypeWriter<>(mockWritable);

        // Act
        writer.close();
        writer.write(INPUT_VALUE);

        // Assert
        verify(mockWritable, times(1)).write(INPUT_VALUE);
    }
}
