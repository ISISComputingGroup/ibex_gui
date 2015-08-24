
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

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.CachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.SwitchableObservable;

// A lot of unchecked type conversions for mocking purposes
@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
/**
 * Test for SwitchingObservable. See InitialisOnSubscribeObservableTest for more test touching
 * the higher observable classes.
 */
public class SwitchableObservableTest {
	
	private InitialisableObserver<String> mockObserver;
	
	private CachingObservable<String> mockObservableOne;
	private CachingObservable<String> mockObservableTwo;
	private CachingObservable<String> mockObservableWithNullValue;
	
	private SwitchableObservable<String> switchableObservable;
	
	@Before
	public void setUp() {
		// Arrange
		mockObserver = mock(InitialisableObserver.class);
		
		mockObservableOne = TestHelpers.getCachingObservable(TestHelpers.STRING_VALUE);		
		mockObservableTwo = TestHelpers.getCachingObservable(TestHelpers.NEW_STRING_VALUE);
		mockObservableWithNullValue = TestHelpers.getCachingObservable(null);
		
		switchableObservable = new SwitchableObservable<>(mockObservableOne);
		switchableObservable.addObserver(mockObserver);
	}
	
	@Test
	public void switching_observable_calls_on_connection_status_on_observer() {
		// Act - Do the switch
		switchableObservable.switchTo(mockObservableTwo);
		
		// Assert
		verify(mockObserver, atLeastOnce()).onConnectionStatus(false);
	}
	
	@Test
	public void switching_observable_returns_the_value_of_the_watched_observable() {
		// Act - Do the switch
		switchableObservable.switchTo(mockObservableTwo);
				
		// The SwitchableObservable has the new Obervable's value
		assertEquals(switchableObservable.getValue(), TestHelpers.NEW_STRING_VALUE);
	}
	
	@Test
	public void switching_observable_calls_on_value_on_observer_with_new_value() {
		// Act - Do the switch
		switchableObservable.switchTo(mockObservableTwo);
		
		// Assert
		verify(mockObserver, times(1)).onValue(TestHelpers.NEW_STRING_VALUE);
	}
	
	@Test
	public void switching_to_observable_with_a_null_value_does_not_call_onValue_on_observer() {
		// Act
		switchableObservable.switchTo(mockObservableWithNullValue);
		
		// Assert - The initialisable observer has its onConnectionChanged called twice and onValue called once
		verify(mockObserver, times(0)).onValue(any(String.class));
	}
	
	@Test
	public void switching_to_observable_with_a_null_value_means_old_observable_value_is_still_returned() {
		// Act
		switchableObservable.switchTo(mockObservableWithNullValue);
		
		// Assert - Might need to think about if this is desirable?
		assertEquals(switchableObservable.getValue(), TestHelpers.STRING_VALUE);
	}
}
