
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

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.writing.ConfigurableWriter;
import uk.ac.stfc.isis.ibex.epics.writing.ForwardingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class ForwardingWritableTest {

    private static final String VALUE = "123";
    private static final String CONVERTED_VALUE = "456";

    private Writable<String> mockDestination;
    private Converter<String, String> mockConverter;

    @Before
    public void setUp() {
        // Arrange
        mockDestination = mock(Writable.class);
        mockConverter = mock(Converter.class);
    }

    private ForwardingWritable<String, String> createWritable() {
        return new ForwardingWritable<String, String>(mockDestination, mockConverter);
    }

    @Test
    public void a_null_value_is_not_written() throws IOException {
        // Arrange
        ForwardingWritable<String, String> forwardingWritable = createWritable();

        // Act
        forwardingWritable.write(null);

        // Assert
        verify(mockDestination, never()).write(anyString());
    }

    @Test
    public void closing_the_writable_closes_current_destination_writable() {
        // Arrange
        ForwardingWritable<String, String> forwardingWritable = createWritable();
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

        ForwardingWritable<String, String> forwardingWritable = createWritable();

        verify(mockSubscription, never()).removeObserver();

        // Act
        forwardingWritable.close();

        // Assert
        verify(mockSubscription, times(1)).removeObserver();
    }

    @Test
    public void closing_the_writable_stops_updating_the_destination_writable_on_further_writes() throws IOException {
        // Act
        ForwardingWritable<String, String> forwardingWritable = createWritable();
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

        // Act
        ForwardingWritable<String, String> forwardingWritable = createWritable();

        // Assert
        assertEquals(expected, forwardingWritable.canWrite());
    }

    @Test
    public void can_write_is_equal_to_destination_writable_can_write_false_case() {
        // Arrange
        boolean expected = false;
        when(mockDestination.canWrite()).thenReturn(expected);

        // Act
        ForwardingWritable<String, String> forwardingWritable = createWritable();

        // Assert
        assertEquals(expected, forwardingWritable.canWrite());
    }

    @Test
    public void setting_a_new_destination_writable_updates_can_write_to_that_of_the_new_destination_true_case() {
        // Arrange
        boolean newValue = true;
        boolean oldValue = !newValue;
        when(mockDestination.canWrite()).thenReturn(oldValue);

        Writable<String> mockNewDestination = mock(Writable.class);
        when(mockNewDestination.canWrite()).thenReturn(newValue);

        ForwardingWritable<String, String> forwardingWritable = createWritable();
        assertEquals(oldValue, forwardingWritable.canWrite());

        // Act
        forwardingWritable.setWritable(mockNewDestination);

        // Assert
        assertEquals(newValue, forwardingWritable.canWrite());
    }

    @Test
    public void setting_a_new_destination_writable_subscribes_to_the_new_destination() {
        // Arrange
        Writable<String> mockNewDestination = mock(Writable.class);
        ForwardingWritable<String, String> forwardingWritable = createWritable();

        verify(mockNewDestination, never()).subscribe(any(ConfigurableWriter.class));

        // Act
        forwardingWritable.setWritable(mockNewDestination);

        // Assert
        verify(mockNewDestination, times(1)).subscribe(any(ConfigurableWriter.class));
    }

    @Test
    public void setting_a_new_destination_writable_closes_old_destination() {
        // Arrange
        Writable<String> mockNewDestination = mock(Writable.class);
        ForwardingWritable<String, String> forwardingWritable = createWritable();

        verify(mockDestination, never()).close();

        // Act
        forwardingWritable.setWritable(mockNewDestination);

        // Assert
        verify(mockDestination, times(1)).close();
    }

    @Test
    public void setting_a_new_destination_writable_unsubscribes_from_old_destination() {
        // Arranged
        Subscription mockSubscription = mock(Subscription.class);
        when(mockDestination.subscribe(any(ConfigurableWriter.class))).thenReturn(mockSubscription);

        Writable<String> mockNewDestination = mock(Writable.class);

        ForwardingWritable<String, String> forwardingWritable = createWritable();

        verify(mockSubscription, never()).removeObserver();

        // Act
        forwardingWritable.setWritable(mockNewDestination);

        // Assert
        verify(mockSubscription, times(1)).removeObserver();
    }

    @Test
    public void setting_a_new_destination_writable_stops_writing_to_old_destination() throws IOException {
        // Arrange
        Writable<String> mockNewDestination = mock(Writable.class);
        ForwardingWritable<String, String> forwardingWritable = createWritable();

        // Act
        forwardingWritable.setWritable(mockNewDestination);
        forwardingWritable.write(VALUE);

        // Assert
        verify(mockDestination, never()).write(anyString());
    }

    @Test
    public void setting_a_new_destination_writable_allows_to_write_to_new_destination() throws ConversionException, IOException {
        // Arrange
        Writable<String> mockNewDestination = mock(Writable.class);
        when(mockConverter.convert(VALUE)).thenReturn(CONVERTED_VALUE);
        ForwardingWritable<String, String> forwardingWritable = createWritable();

        verify(mockNewDestination, never()).write(anyString());

        // Act
        forwardingWritable.setWritable(mockNewDestination);
        forwardingWritable.write(VALUE);

        // Assert
        verify(mockNewDestination, times(1)).write(anyString());
        verify(mockNewDestination, times(1)).write(CONVERTED_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setting_a_new_destination_writable_throws_for_null_destination() {
        // Arrange
        ForwardingWritable<String, String> forwardingWritable = createWritable();

        // Act
        forwardingWritable.setWritable(null);
    }

    @Test
    public void write_writes_the_converted_value() throws ConversionException, IOException {
        // Arrange
        when(mockConverter.convert(VALUE)).thenReturn(CONVERTED_VALUE);
        ForwardingWritable<String, String> forwardingWritable = createWritable();
        verify(mockDestination, never()).write(anyString());

        // Act
        forwardingWritable.write(VALUE);

        // Assert
        verify(mockDestination, times(1)).write(anyString());
        verify(mockDestination, times(1)).write(CONVERTED_VALUE);
    }

    @Test
    public void value_conversion_error_is_stored_in_last_error() throws ConversionException, IOException {
        // Arrange
        ConversionException exception = new ConversionException("conversion error");
        when(mockConverter.convert(VALUE)).thenThrow(exception);
        ForwardingWritable<String, String> forwardingWritable = createWritable();

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
        when(mockConverter.convert(VALUE)).thenThrow(exception);

        ForwardingWritable<String, String> forwardingWritable = createWritable();

        // Act
        forwardingWritable.write(VALUE);

        // Assert
        verify(mockDestination, never()).write(anyString());
    }

    @Test
    public void an_error_from_destination_writable_is_stored_in_last_error() {
        // Arrange
        StubWritable<String> stubDestination = new StubWritable<>();
        Exception exception = new Exception();

        ForwardingWritable<String, String> forwardingWritable = new ForwardingWritable<>(stubDestination,
                mockConverter);

        assertNull(forwardingWritable.lastError());

        // Act
        stubDestination.simulateError(exception);

        // Assert
        assertEquals(exception, forwardingWritable.lastError());
    }
}



