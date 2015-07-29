
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ObservablePair;
import uk.ac.stfc.isis.ibex.epics.observing.Pair;

// A lot of unchecked type conversions for mocking purposes
@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
/**
 * Test for SwitchingObservable. See InitialisOnSubscribeObservableTest for more test touching
 * the higher observable classes.
 */
public class ObservablePairTest {
	
	private static final String STRING_VALUE = "value";
	private static final String NEW_STRING_VALUE = "new value";
	private static final Integer INTEGER_VALUE = 314;
	private static final Integer NEW_INTEGER_VALUE = 413;
	
	@Captor ArgumentCaptor<Pair<String, Integer>> pairCaptor;
	
	@Before
	public void setUp() {		
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test_ObservablePair_updates_first_then_second() {
		// Arrange	
		InitialisableObserver<Pair<String, Integer>> mockObserver = mock(InitialisableObserver.class);
		
		// A test observable that has public set methods with String type, looked at by another observer
		TestableObservable<String> testableStringObservable = new TestableObservable<>();
		testableStringObservable.setValue(STRING_VALUE);
		InitialiseOnSubscribeObservable<String> initStringObservable = new InitialiseOnSubscribeObservable<>(testableStringObservable);
		
		// A test observable that has public set methods with integer type, looked at by another observer
		TestableObservable<Integer> testableIntegerObservable = new TestableObservable<Integer>();
		testableIntegerObservable.setValue(INTEGER_VALUE);
		InitialiseOnSubscribeObservable<Integer> initIntegerObservable = new InitialiseOnSubscribeObservable<>(testableIntegerObservable);
		
		// Object we are really testing
		ObservablePair<String, Integer> observablePair = new ObservablePair<String, Integer>(initStringObservable, initIntegerObservable);
		
		// Act
		observablePair.addObserver(mockObserver);
		testableStringObservable.setValue(NEW_STRING_VALUE);
		testableIntegerObservable.setValue(NEW_INTEGER_VALUE);
		
		// Assert
		// The observer has its on value method called twice, and has the new values
		verify(mockObserver, times(2)).onValue(pairCaptor.capture());
		assertEquals(NEW_STRING_VALUE, pairCaptor.getValue().first);
		assertEquals(NEW_INTEGER_VALUE, pairCaptor.getValue().second);
	}
	
	@Test
	public void test_ObservablePair_updates_second_then_first() {
		// Arrange	
		InitialisableObserver<Pair<String, Integer>> mockObserver = mock(InitialisableObserver.class);
		
		// A test observable that has public set methods with String type, looked at by another observer
		TestableObservable<String> testableStringObservable = new TestableObservable<>();
		testableStringObservable.setValue(STRING_VALUE);
		InitialiseOnSubscribeObservable<String> initStringObservable = new InitialiseOnSubscribeObservable<>(testableStringObservable);
		
		// A test observable that has public set methods with integer type, looked at by another observer
		TestableObservable<Integer> testableIntegerObservable = new TestableObservable<Integer>();
		testableIntegerObservable.setValue(INTEGER_VALUE);
		InitialiseOnSubscribeObservable<Integer> initIntegerObservable = new InitialiseOnSubscribeObservable<>(testableIntegerObservable);
		
		// Object we are really testing
		ObservablePair<String, Integer> observablePair = new ObservablePair<String, Integer>(initStringObservable, initIntegerObservable);
		
		// Act
		observablePair.addObserver(mockObserver);
		testableIntegerObservable.setValue(NEW_INTEGER_VALUE);
		testableStringObservable.setValue(NEW_STRING_VALUE);
		
		// Assert
		// The observer has its on value method called twice, and has the new values
		verify(mockObserver, times(2)).onValue(pairCaptor.capture());
		assertEquals(NEW_STRING_VALUE, pairCaptor.getValue().first);
		assertEquals(NEW_INTEGER_VALUE, pairCaptor.getValue().second);
	}
	
	@Test
	public void test_ObservablePair_cancel_subscription() {
		// Arrange
		InitialisableObserver<Pair<String, Integer>> mockObserver = mock(InitialisableObserver.class);
		
		// A test observable that has public set methods with String type, looked at by another observer
		TestableObservable<String> testableStringObservable = new TestableObservable<>();
		testableStringObservable.setValue(STRING_VALUE);
		InitialiseOnSubscribeObservable<String> initStringObservable = new InitialiseOnSubscribeObservable<>(testableStringObservable);
		
		// A test observable that has public set methods with integer type, looked at by another observer
		TestableObservable<Integer> testableIntegerObservable = new TestableObservable<Integer>();
		testableIntegerObservable.setValue(INTEGER_VALUE);
		InitialiseOnSubscribeObservable<Integer> initIntegerObservable = new InitialiseOnSubscribeObservable<>(testableIntegerObservable);
		
		// Object we are really testing
		ObservablePair<String, Integer> observablePair = new ObservablePair<String, Integer>(initStringObservable, initIntegerObservable);
		
		// Act
		observablePair.addObserver(mockObserver);
		observablePair.close();
		testableIntegerObservable.setValue(NEW_INTEGER_VALUE);
		testableStringObservable.setValue(NEW_STRING_VALUE);
		
		// Assert
		// Both subscriptions are closed, so no onValue calls
		verify(mockObserver, times(0)).onValue(any(Pair.class));
	}
	
	@Test
	public void test_ObservablePair_on_error() {
		// Arrange
		Exception exception = new Exception();
		
		// Mock observer, templated objects need cast
		InitialisableObserver<Pair<String, Integer>> mockObserver = mock(InitialisableObserver.class);
		
		// A test observable that has public set methods with String type, looked at by another observer
		TestableObservable<String> testableStringObservable = new TestableObservable<>();
		testableStringObservable.setValue(STRING_VALUE);
		InitialiseOnSubscribeObservable<String> initStringObservable = new InitialiseOnSubscribeObservable<>(testableStringObservable);
		
		// A test observable that has public set methods with integer type, looked at by another observer
		TestableObservable<Integer> testableIntegerObservable = new TestableObservable<Integer>();
		testableIntegerObservable.setValue(INTEGER_VALUE);
		InitialiseOnSubscribeObservable<Integer> initIntegerObservable = new InitialiseOnSubscribeObservable<>(testableIntegerObservable);
		
		// Object we are really testing
		ObservablePair<String, Integer> observablePair = new ObservablePair<>(initStringObservable, initIntegerObservable);
		
		// Act
		observablePair.addObserver(mockObserver);
		testableStringObservable.setError(exception);
		
		// Assert
		// The on error call is passed through
		verify(mockObserver, times(1)).onError(exception);
	}
}
