
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.model.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;


/**
 * For testing the awaited class.
 */
@SuppressWarnings("checkstyle:methodname")
public class AwaitedTest {

    private CountDownLatch mockLatch;
    private UpdatedValue<String> mockValue;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        mockLatch = mock(CountDownLatch.class);
        mockValue = (UpdatedValue<String>) mock(UpdatedValue.class);
    }

    @Test
    public void GIVEN_value_already_set_WHEN_until_called_THEN_does_not_wait() throws InterruptedException {

        // Arrange
        when(mockValue.isSet()).thenReturn(true);

        Awaited<String> awaited = new Awaited<String>(mockValue, mockLatch);

        // Act
        awaited.until(0);

        // Assert
        verify(mockLatch, times(0)).await(0, TimeUnit.SECONDS);
    }
    
    @Test
    public void GIVEN_value_not_set_WHEN_until_called_THEN_does_waits() throws InterruptedException {

        // Arrange
        when(mockValue.isSet()).thenReturn(false);

        Awaited<String> awaited = new Awaited<String>(mockValue, mockLatch);

        // Act
        awaited.until(0);

        // Assert
        verify(mockLatch, times(1)).await(0, TimeUnit.SECONDS);
    }
    
    @Test
    public void GIVEN_value_not_set_WHEN_awaited_created_THEN_property_listener_added() throws InterruptedException {

        // Arrange
        when(mockValue.isSet()).thenReturn(false);

        // Act
        new Awaited<String>(mockValue, mockLatch);

        // Assert
        verify(mockValue, times(1)).addPropertyChangeListener(any(PropertyChangeListener.class));
    }
    
    @Test
    public void GIVEN_value_not_set_and_until_called_WHEN_property_listener_fired_THEN_latch_counted_down() throws InterruptedException {
        // Arrange
        when(mockValue.isSet()).thenReturn(false);
        ArgumentCaptor<PropertyChangeListener> argument = ArgumentCaptor.forClass(PropertyChangeListener.class);
        
        Awaited<String> awaited = new Awaited<String>(mockValue, mockLatch);
        boolean isSet = awaited.until(0);
        verify(mockValue).addPropertyChangeListener(argument.capture());
        
        // Act
        argument.getValue().propertyChange(new PropertyChangeEvent("", "", "", ""));

        // Assert
        verify(mockLatch, times(1)).countDown();
        assertEquals(isSet, false);
    }
}