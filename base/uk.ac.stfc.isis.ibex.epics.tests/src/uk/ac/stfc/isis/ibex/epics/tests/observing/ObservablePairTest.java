
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
@SuppressWarnings("unchecked")
/**
 * Test for SwitchingObservable. See InitialisOnSubscribeObservableTest for more test touching
 * the higher observable classes.
 */
public class ObservablePairTest {
	
	private String stringValue;
	private String newStringValue;
	private Integer integerValue;
	private Integer newIntegerValue;
	
	@Captor ArgumentCaptor<Pair<String, Integer>> pairCaptor;
	
	@Before
	public void setUp() {
		stringValue = "value";
		newStringValue = "new value";
		integerValue = 314;
		newIntegerValue = 413;
		
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test_ObservablePair_updates_first_then_second() {
		//Arrange	
		// Mock observer, templated objects need cast
		InitialisableObserver<Pair<String, Integer>> mockObserver = mock(InitialisableObserver.class);
		
		// Testable observable with String type
		TestableObservable<String> testableStringObservable = new TestableObservable<>();
		testableStringObservable.setValue(stringValue);
		// InitialiseOnSubscribeObservable looking at the testable observable
		InitialiseOnSubscribeObservable<String> initStringObservable = new InitialiseOnSubscribeObservable<>(testableStringObservable);
		
		// Testable observable with integer type
		TestableObservable<Integer> testableIntegerObservable = new TestableObservable<Integer>();
		testableIntegerObservable.setValue(integerValue);
		// InitialiseOnSubscribeObservable looking at the testable observable
		InitialiseOnSubscribeObservable<Integer> initIntegerObservable = new InitialiseOnSubscribeObservable<>(testableIntegerObservable);
		
		// Object we are really testing
		ObservablePair<String, Integer> observablePair = new ObservablePair<String, Integer>(initStringObservable, initIntegerObservable);
		observablePair.subscribe(mockObserver);
		
		//Act
		testableStringObservable.setValue(newStringValue);
		testableIntegerObservable.setValue(newIntegerValue);
		
		//Assert
		// The observer is called in the following way
		verify(mockObserver, times(0)).update(new Pair<String, Integer>(null, null), null, false);
		
		verify(mockObserver, times(2)).onValue(pairCaptor.capture());
		assertEquals(newStringValue, pairCaptor.getValue().first);
		assertEquals(newIntegerValue, pairCaptor.getValue().second);
	}
	
	@Test
	public void test_ObservablePair_updates_second_then_first() {
		//Arrange	
		// Mock observer, templated objects need cast
		InitialisableObserver<Pair<String, Integer>> mockObserver = mock(InitialisableObserver.class);
		
		// Testable observable with String type
		TestableObservable<String> testableStringObservable = new TestableObservable<>();
		testableStringObservable.setValue(stringValue);
		// InitialiseOnSubscribeObservable looking at the testable observable
		InitialiseOnSubscribeObservable<String> initStringObservable = new InitialiseOnSubscribeObservable<>(testableStringObservable);
		
		// Testable observable with integer type
		TestableObservable<Integer> testableIntegerObservable = new TestableObservable<Integer>();
		testableIntegerObservable.setValue(integerValue);
		// InitialiseOnSubscribeObservable looking at the testable observable
		InitialiseOnSubscribeObservable<Integer> initIntegerObservable = new InitialiseOnSubscribeObservable<>(testableIntegerObservable);
		
		// Object we are really testing
		ObservablePair<String, Integer> observablePair = new ObservablePair<String, Integer>(initStringObservable, initIntegerObservable);
		observablePair.subscribe(mockObserver);
		
		//Act
		testableIntegerObservable.setValue(newIntegerValue);
		testableStringObservable.setValue(newStringValue);
		
		//Assert
		// The observer is called in the following way
		verify(mockObserver, times(0)).update(new Pair<String, Integer>(null, null), null, false);
		
		verify(mockObserver, times(2)).onValue(pairCaptor.capture());
		assertEquals(newStringValue, pairCaptor.getValue().first);
		assertEquals(newIntegerValue, pairCaptor.getValue().second);
	}
	
	@Test
	public void test_ObservablePair_cancel_subscription() {
		//Arrange	
		// Mock observer, templated objects need cast
		InitialisableObserver<Pair<String, Integer>> mockObserver = mock(InitialisableObserver.class);
		
		// Testable observable with String type
		TestableObservable<String> testableStringObservable = new TestableObservable<>();
		testableStringObservable.setValue(stringValue);
		// InitialiseOnSubscribeObservable looking at the testable observable
		InitialiseOnSubscribeObservable<String> initStringObservable = new InitialiseOnSubscribeObservable<>(testableStringObservable);
		
		// Testable observable with integer type
		TestableObservable<Integer> testableIntegerObservable = new TestableObservable<Integer>();
		testableIntegerObservable.setValue(integerValue);
		// InitialiseOnSubscribeObservable looking at the testable observable
		InitialiseOnSubscribeObservable<Integer> initIntegerObservable = new InitialiseOnSubscribeObservable<>(testableIntegerObservable);
		
		// Object we are really testing
		ObservablePair<String, Integer> observablePair = new ObservablePair<String, Integer>(initStringObservable, initIntegerObservable);
		observablePair.subscribe(mockObserver);
		
		//Act
		observablePair.close();
		testableIntegerObservable.setValue(newIntegerValue);
		testableStringObservable.setValue(newStringValue);
		
		//Assert
		// Both subscriptions are closed, so no onValue calls
		verify(mockObserver, times(0)).onValue(any(Pair.class));
	}
	
	@Test
	public void test_ObservablePair_on_error() {
		//Arrange
		Exception exception = new Exception();
		
		// Mock observer, templated objects need cast
		InitialisableObserver<Pair<String, Integer>> mockObserver = mock(InitialisableObserver.class);
		
		// Testable observable with String type
		TestableObservable<String> testableStringObservable = new TestableObservable<>();
		testableStringObservable.setValue(stringValue);
		// InitialiseOnSubscribeObservable looking at the testable observable
		InitialiseOnSubscribeObservable<String> initStringObservable = new InitialiseOnSubscribeObservable<>(testableStringObservable);
		
		// Testable observable with integer type
		TestableObservable<Integer> testableIntegerObservable = new TestableObservable<Integer>();
		testableIntegerObservable.setValue(integerValue);
		// InitialiseOnSubscribeObservable looking at the testable observable
		InitialiseOnSubscribeObservable<Integer> initIntegerObservable = new InitialiseOnSubscribeObservable<>(testableIntegerObservable);
		
		// Object we are really testing
		ObservablePair<String, Integer> observablePair = new ObservablePair<>(initStringObservable, initIntegerObservable);
		observablePair.subscribe(mockObserver);
		
		//Act
		testableStringObservable.setError(exception);
		
		//Assert
		// Both subscriptions are closed, so no onValue calls
		verify(mockObserver, times(1)).onError(exception);
	}
}
