
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

import uk.ac.stfc.isis.ibex.epics.observing.CachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.observing.Unsubscriber;

// A lot of unchecked type conversions for mocking purposes
@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class InitialiseOnSubscribeObservableTest {
	
	private static final String VALUE = "value";
	private static final String NEW_VALUE = "new value";
	
	@Test
	public void test_InitialiseOnSubscribeObservable_subscription() {
		// Arrange
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);
		
		// Mock observable with stub method for return value
		CachingObservable<String> mockObservable = mock(CachingObservable.class);
		when(mockObservable.getValue()).thenReturn(VALUE);
		
		// Object we are really testing
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(mockObservable);
		
		// Act
		Object returned = initObservable.addObserver(mockObserver);
		
		// Assert
		// The initialisable observer has its update method called once
		verify(mockObserver, times(1)).update(VALUE, null, false);
		
		// The InitialiseOnSubscribeObservable has the value returned from the mock observable
		assertEquals(initObservable.getValue(), VALUE);
		
		// A Unsubscriber is returned
		assertEquals(Unsubscriber.class, returned.getClass());
	}
	
	@Test
	public void test_initialise_on_subscribe_observable_on_value_change() {
		// Arrange
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);
		
		// A test observable that has public set methods
		TestableObservable<String> testableObservable = new TestableObservable<>();
		
		// Object we are really testing
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(testableObservable);
		
		// Act
		testableObservable.setValue(VALUE);
		initObservable.addObserver(mockObserver);
		testableObservable.setValue(NEW_VALUE);
				
		// Assert		
		// The initialisable observer has its update method called once, and on value method once
		verify(mockObserver, times(1)).update(VALUE, null, false);
		verify(mockObserver, times(1)).onValue(NEW_VALUE);
		
		// The InitialiseOnSubscribeObservable has the value returned from the mock observable
		assertEquals(NEW_VALUE, initObservable.getValue());
	}
	
	@Test
	public void test_initialise_on_subscribe_observable_on_connection_changed() {
		// Arrange
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);

		// A test observable that has public set methods
		TestableObservable<String> testableObservable = new TestableObservable<>();
		
		// Object we are really testing
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(testableObservable);
		
		// Act
		initObservable.addObserver(mockObserver);
		testableObservable.setConnectionStatus(true);
				
		// Assert
		// The initialisable observer has its update method called once, and on connection changed once
		verify(mockObserver, times(1)).update(null, null, false);
		verify(mockObserver, times(1)).onConnectionStatus(true);
	}
	
	@Test
	public void test_initialise_on_subscribe_observable_on_error() {
		// Arrange
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);

		// A test observable that has public set methods
		TestableObservable<String> testableObservable = new TestableObservable<>();
		
		// Object we are really testing
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(testableObservable);
		
		Exception exception = new Exception();
		
		// Act
		initObservable.addObserver(mockObserver);
		testableObservable.setError(exception);
				
		// Assert
		// The initialisable observer has its update method called once, and on error once
		verify(mockObserver, times(1)).update(null, null, false);
		verify(mockObserver, times(1)).onError(exception);
	}
	
	@Test
	public void test_InitialiseOnSubscribeObservable_with_multiple_observers() {
		// Arrange	
		// Two mock observers
		InitialisableObserver<String> mockObserverOne = mock(InitialisableObserver.class);
		InitialisableObserver<String> mockObserverTwo = mock(InitialisableObserver.class);
		
		// A test observable that has public set methods
		TestableObservable<String> testableObservable = new TestableObservable<>();

		// Object we are really testing
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(testableObservable);
		
		// Act
		testableObservable.setValue(VALUE);
		initObservable.addObserver(mockObserverOne);
		initObservable.addObserver(mockObserverTwo);
		testableObservable.setValue(NEW_VALUE);
		
		// Assert
		// Both observables are initialised with the update method, and on value is called once
		verify(mockObserverOne, times(1)).update(VALUE, null, false);
		verify(mockObserverTwo, times(1)).update(VALUE, null, false);
		verify(mockObserverOne, times(1)).onValue(NEW_VALUE);
		verify(mockObserverTwo, times(1)).onValue(NEW_VALUE);
	}
	
	@Test
	public void test_InitialiseOnSubscribeObservable_cancel_subscription_to_observable() {
		// Arrange
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);

		// A test observable that has public set methods
		TestableObservable<String> testableObservable = new TestableObservable<>();
		
		// Object we are really testing
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(testableObservable);
		
		// Act
		initObservable.addObserver(mockObserver);
		initObservable.close();
		testableObservable.setValue(NEW_VALUE);
		
		// Assert
		// The observer has unsubscribed, so does not have on value method called
		verify(mockObserver, times(1)).update(null, null, false);
		verify(mockObserver, times(0)).onValue(any(String.class));
	}
	
	@Test
	public void test_InitialiseOnSubscribeObservable_cancel_subscription_with_multiple_observers() {
		// Arrange	
		// Two mock observers
		InitialisableObserver<String> mockObserverOne = mock(InitialisableObserver.class);
		InitialisableObserver<String> mockObserverTwo = mock(InitialisableObserver.class);
		
		// A test observable that has public set methods
		TestableObservable<String> testableObservable = new TestableObservable<>();

		// Object we are really testing
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(testableObservable);
		
		// Act
		testableObservable.setValue(VALUE);
		initObservable.addObserver(mockObserverOne);
		Subscription unsubscriber = initObservable.addObserver(mockObserverTwo);
		unsubscriber.cancel();
		testableObservable.setValue(NEW_VALUE);
		
		// Assert
		// The first observer has update and on value called once each, the second just on value
		verify(mockObserverOne, times(1)).update(VALUE, null, false);
		verify(mockObserverTwo, times(1)).update(VALUE, null, false);
		verify(mockObserverOne, times(1)).onValue(NEW_VALUE);
		verify(mockObserverTwo, times(0)).onValue(any(String.class));
	}
	
	@Test
	public void test_InitialiseOnSubscribeObservable_subscribe_with_to_observable_with_error() {
		// Arrange
		Exception exception = new Exception();
		
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);
		
		// A test observable that has public set methods...
		TestableObservable<String> testableObservable = new TestableObservable<>();
		// ... and an error set
		testableObservable.setError(exception);
		
		// Object we are really testing		
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(testableObservable);
		
		// Act
		initObservable.addObserver(mockObserver);
		
		// Assert
		// The observers update method has the exception
		verify(mockObserver, times(1)).update(null, exception, false);
	}
	
	@Test
	public void test_InitialiseOnSubscribeObservable_adding_observer_more_than_once() {
		// Arrange
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);

		// A test observable that has public set methods
		TestableObservable<String> testableObservable = new TestableObservable<>();
		
		// Object we are really testing		
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(testableObservable);
		
		// Act
		initObservable.addObserver(mockObserver);
		initObservable.addObserver(mockObserver);
		testableObservable.setValue(NEW_VALUE);
		
		// Assert
		// Should have update method called twice...
		verify(mockObserver, times(2)).update(null, null, false);
		// ...but on value only once
		verify(mockObserver, times(1)).onValue(NEW_VALUE);
	}
	
	@Test
	public void test_InitialiseOnSubscribeObservable_setting_null_value_does_not_trigger_onValue_call() {
		// Arrange
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);

		// A test observable that has public set methods
		TestableObservable<String> testableObservable = new TestableObservable<>();

		// Object we are really testing		
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(testableObservable);
		
		// Act
		initObservable.addObserver(mockObserver);
		testableObservable.setValue(null);
		
		// Assert
		// On value method should not be called
		verify(mockObserver, times(0)).onValue(any(String.class));
	}
}
