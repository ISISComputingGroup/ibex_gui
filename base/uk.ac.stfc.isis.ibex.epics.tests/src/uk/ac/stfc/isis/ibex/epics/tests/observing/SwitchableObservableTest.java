
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
@SuppressWarnings("unchecked")
/**
 * Test for SwitchingObservable. See InitialisOnSubscribeObservableTest for more test touching
 * the higher observable classes.
 */
public class SwitchableObservableTest {
	
	private String value;
	private String newValue;
	
	@Before
	public void setUp() {
		value = "value";
		newValue = "new value";
	}
	
	@Test
	public void test_SwitchableObservable_switch() {
		//Arrange	
		// Mock observer, templated objects need cast
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);
		
		// Mock observables with stub methods returning different values
		CachingObservable<String> mockObservableOne = mock(CachingObservable.class);
		when(mockObservableOne.getValue()).thenReturn(value);
		
		CachingObservable<String> mockObservableTwo = mock(CachingObservable.class);
		when(mockObservableTwo.getValue()).thenReturn(newValue);
		
		// Object we are really testing
		SwitchableObservable<String> switchableObservable = new SwitchableObservable<>(mockObservableOne);
		
		//Act
		switchableObservable.subscribe(mockObserver);
		// Do the switch
		switchableObservable.switchTo(mockObservableTwo);
		
		//Assert
		// The initialisable observer has its onConnectionChanged called twice and onValue called once.
		// Note here that the switch calls onValue on the observer, but the initialise does not.
		verify(mockObserver, times(2)).onConnectionChanged(false);
		verify(mockObserver, times(1)).onValue(newValue);
		
		// The SwitchableObservable has the new Obervable's value
		assertEquals(switchableObservable.getValue(), newValue);
	}
	
	@Test
	public void test_SwitchableObservable_switch_to_object_with_null_value() {
		//Arrange	
		// Mock observer, templated objects need cast
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);
		
		// Mock observables with stub methods returning different values
		CachingObservable<String> mockObservableOne = mock(CachingObservable.class);
		when(mockObservableOne.getValue()).thenReturn(value);
		
		CachingObservable<String> mockObservableTwo = mock(CachingObservable.class);
		when(mockObservableTwo.getValue()).thenReturn(null);
		
		// Object we are really testing
		SwitchableObservable<String> switchableObservable = new SwitchableObservable<>(mockObservableOne);
		
		//Act
		switchableObservable.subscribe(mockObserver);
		// Do the switch
		switchableObservable.switchTo(mockObservableTwo);
		
		//Assert
		// The initialisable observer has its onConnectionChanged called twice and onValue called once
		verify(mockObserver, times(2)).onConnectionChanged(false);
		verify(mockObserver, times(0)).onValue(any(String.class));
		
		// The SwitchableObservable has the old Obervable's value
		// Might need to think about if this is desirable?
		assertEquals(switchableObservable.getValue(), value);
	}
}
