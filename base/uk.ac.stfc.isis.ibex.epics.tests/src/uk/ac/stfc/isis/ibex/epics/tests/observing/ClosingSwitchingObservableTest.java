/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.epics.tests.observing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ClosingSwitchableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;

// A lot of unchecked type conversions for mocking purposes
@SuppressWarnings("unchecked")
public class ClosingSwitchingObservableTest {

	private static final String value = "value";
	private static final String newValue = "new value";

	@Test
	public void test_ClosableSwitchableObservable_close_source() {
		// Arrange
		// Mock observer
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);

		// Mock observable, that has a stub for getValue()
		ClosableCachingObservable<String> mockObservable = mock(ClosableCachingObservable.class);
		when(mockObservable.getValue()).thenReturn(value);

		// Object we are really testing
		ClosingSwitchableObservable<String> switchableObservable = new ClosingSwitchableObservable<>(mockObservable);

		// Act
		switchableObservable.subscribe(mockObserver);
		switchableObservable.close();

		// Assert
		// The original ClosableCachingObservable is closed
		verify(mockObservable, times(1)).close();
		// Even though it is closed value is still set
		assertEquals(value, switchableObservable.getValue());
		// Observer never did anything
		verify(mockObserver, times(0)).update(anyString(), any(Exception.class), anyBoolean());
		verify(mockObserver, times(0)).onValue(anyString());
	}

	@Test
	public void test_ClosableSwitchableObservable_switch_closes_observable() {
		// Arrange
		// Mock observer
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);

		// Mock observables with stub methods returning different values
		ClosableCachingObservable<String> mockObservableOne = mock(ClosableCachingObservable.class);
		when(mockObservableOne.getValue()).thenReturn(value);

		ClosableCachingObservable<String> mockObservableTwo = mock(ClosableCachingObservable.class);
		when(mockObservableTwo.getValue()).thenReturn(newValue);

		// Object we are really testing, subscribed to mockObservableOne
		ClosingSwitchableObservable<String> switchableObservable = new ClosingSwitchableObservable<>(mockObservableOne);

		// Act
		switchableObservable.subscribe(mockObserver);
		// Do the switch
		switchableObservable.switchTo(mockObservableTwo);

		// Assert
		// The original ClosableCachingObservable is closed
		verify(mockObservableOne, times(1)).close();
	}

}
