
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.writing.OnCanWriteChangeListener;
import uk.ac.stfc.isis.ibex.epics.writing.OnErrorListener;

@SuppressWarnings({ "checkstyle:methodname" })
public class BaseWritableTest {

    private TestableBaseWritable<String> writable;

    @Test
    public void last_error_is_null_at_initialisation() {
        // Arrange
        writable = new TestableBaseWritable<String>();

        // Assert
        assertNull(writable.lastError());
    }

    @Test
    public void WHEN_adding_an_OnCanWriteChangeListener_THEN_can_write_changed_called() {
        // Arrange
        writable = new TestableBaseWritable<String>();
        OnCanWriteChangeListener mockListener = mock(OnCanWriteChangeListener.class);

        // Act
        writable.addOnCanWriteChangeListener(mockListener);

        // Assert
        verify(mockListener, times(1)).onCanWriteChanged(anyBoolean());
    }

    @Test
    public void WHEN_adding_an_OnErrorListener_THEN_error_called() {
        // Arrange
        writable = new TestableBaseWritable<String>();
        OnErrorListener mockListener = mock(OnErrorListener.class);

        // Act
        writable.addOnErrorListener(mockListener);

        // Assert
        verify(mockListener, times(1)).onError(null);
    }

    @Test
    public void last_error_is_updated_on_destination_error() {
        // Arrange
        writable = new TestableBaseWritable<String>();
        Exception exception = new Exception();

        // Act
        writable.error(exception);

        // Assert
        assertEquals(exception, writable.lastError());
    }

    @Test
    public void GIVEN_multiple_error_listeners_WHEN_in_error_THEN_all_listeners_are_notified() {
        // Arrange
        writable = new TestableBaseWritable<String>();

        OnErrorListener mockListener1 = mock(OnErrorListener.class);
        OnErrorListener mockListener2 = mock(OnErrorListener.class);
        
        writable.addOnErrorListener(mockListener1);
        writable.addOnErrorListener(mockListener2);

        Exception exception = new Exception();

        // Act
        writable.error(exception);

        // Assert
        verify(mockListener1, times(1)).onError(exception);
        verify(mockListener2, times(1)).onError(exception);
    }

    @Test
    public void can_write_is_updated_on_destination_can_write_change_true_case() {
        // Arrange
        writable = new TestableBaseWritable<String>();
        boolean expected = true;

        // Act
        writable.canWriteChanged(expected);

        // Assert
        assertEquals(expected, writable.canWrite());
    }

    @Test
    public void can_write_is_updated_on_destination_can_write_change_false_case() {
        // Arrange
        writable = new TestableBaseWritable<String>();
        boolean expected = false;

        // Act
        writable.canWriteChanged(expected);

        // Assert
        assertEquals(expected, writable.canWrite());
    }

    @Test
    public void GIVEN_multiple_can_write_listeners_WHEN_can_write_changed_THEN_all_listeners_are_notified() {
        // Arrange
        writable = new TestableBaseWritable<String>();

        OnCanWriteChangeListener mockListener1 = mock(OnCanWriteChangeListener.class);
        OnCanWriteChangeListener mockListener2 = mock(OnCanWriteChangeListener.class);
        
        writable.addOnCanWriteChangeListener(mockListener1);
        writable.addOnCanWriteChangeListener(mockListener2);

        boolean expected = true;

        // Act
        writable.canWriteChanged(expected);

        // Assert
        verify(mockListener1, times(1)).onCanWriteChanged(expected);
        verify(mockListener2, times(1)).onCanWriteChanged(expected);
    }

    @Test
    public void GIVEN_error_listener_added_WHEN_same_listener_added_and_in_error_THEN_listener_called_once() {
        // Arrange
        writable = new TestableBaseWritable<String>();

        OnErrorListener mockListener = mock(OnErrorListener.class);

        Exception exception = new Exception();

        // Act
        writable.addOnErrorListener(mockListener);
        writable.addOnErrorListener(mockListener);

        writable.error(exception);

        // Assert
        verify(mockListener, times(1)).onError(exception);
    }
    
    @Test
    public void GIVEN_write_changed_listener_added_WHEN_same_listener_added_and_in_write_changes_THEN_listener_called_once() {
        // Arrange
        writable = new TestableBaseWritable<String>();

        OnCanWriteChangeListener mockListener = mock(OnCanWriteChangeListener.class);

        // Act
        writable.addOnCanWriteChangeListener(mockListener);
        writable.addOnCanWriteChangeListener(mockListener);

        writable.canWriteChanged(true);

        // Assert
        verify(mockListener, times(1)).onCanWriteChanged(true);
    }

    @Test
    public void GIVEN_write_changed_listener_added_WHEN_write_change_listener_removed_THEN_listener_not_called() {
        // Arrange
        writable = new TestableBaseWritable<String>();

        OnCanWriteChangeListener mockListener = mock(OnCanWriteChangeListener.class);

        // Act
        writable.addOnCanWriteChangeListener(mockListener);
        writable.removeOnCanWriteChangeListener(mockListener);

        writable.canWriteChanged(true);

        // Assert
        verify(mockListener, times(0)).onCanWriteChanged(true);
    }
    
    @Test
    public void GIVEN_on_error_listener_added_WHEN_on_error_listener_removed_THEN_listener_not_called() {
        // Arrange
        writable = new TestableBaseWritable<String>();

        OnErrorListener mockListener = mock(OnErrorListener.class);

        // Act
        writable.addOnErrorListener(mockListener);
        writable.removeOnErrorListener(mockListener);

        Exception exception = new Exception();
        writable.error(exception);

        // Assert
        verify(mockListener, times(0)).onError(exception);
    }
    
}
