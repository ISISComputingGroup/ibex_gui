
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

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.ConvertingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;

import java.lang.String;

// A lot of unchecked type conversions for mocking purposes
@SuppressWarnings("unchecked")
public class ConvertingObservableTest {
	
	private static final Integer value = 123;
	private static final Integer newValue = 456;
	private static final String convertedValue = "converted value";
	private static final String newConvertedValue = "new converted value";
	private static final String exceptionMessage = "converions exception!";
	
	@Captor ArgumentCaptor<Exception> exceptionCaptor;
	
	@Before
	public void setUp() {
		// This is to initialise the exceptionCaptor
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test_ConvertingObservable_converts() throws ConversionException {
		//Arrange	
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);
		
		// initObservable is what our ConvertingObservable looks at, and testObservable we can call set methods on
		TestableObservable<Integer> testObservable = new TestableObservable<>();
		InitialiseOnSubscribeObservable<Integer> initObservable = new InitialiseOnSubscribeObservable<Integer>(testObservable);
		
		// Mock converter, with a stub conversion method
		Converter<Integer, String> mockConverter = mock(Converter.class);
		when(mockConverter.convert(value)).thenReturn(convertedValue);
		
		// Object we are really testing
		ConvertingObservable<Integer, String> convertObservable = new ConvertingObservable<>(initObservable, mockConverter);
		
		//Act
		convertObservable.subscribe(mockObserver);
		convertObservable.setSource(initObservable);
		testObservable.setValue(value);
		
		//Assert
		// The initialisable observer has its update method called once, with the converted value
		verify(mockObserver, times(1)).onValue(convertedValue);
	}
	
	@Test
	public void test_ConvertingObservable_with_conversion_exception() throws ConversionException {
		//Arrange
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);
		
		// initObservable is what our ConvertingObservable looks at, and testObservable we can call set methods on
		TestableObservable<Integer> testObservable = new TestableObservable<>();
		InitialiseOnSubscribeObservable<Integer> initObservable = new InitialiseOnSubscribeObservable<Integer>(testObservable);
		
		// Mock converter, with a stub conversion method
		Converter<Integer, String> mockConverter = mock(Converter.class);
		when(mockConverter.convert(value)).thenThrow(new ConversionException(exceptionMessage));
		
		// Object we are really testing
		ConvertingObservable<Integer, String> convertObservable = new ConvertingObservable<>(initObservable, mockConverter);
		
		//Act
		convertObservable.subscribe(mockObserver);
		convertObservable.setSource(initObservable);
		testObservable.setValue(value);
		
		//Assert
		// The initialisable observer has its onError message called once, for the ConversionException
		verify(mockObserver, times(0)).onValue(anyString());
		verify(mockObserver, times(1)).onError(exceptionCaptor.capture());
		assertEquals(exceptionMessage, exceptionCaptor.getValue().getMessage());
	}
	
	@Test
	public void test_ConvertingObservable_observable_has_error() throws ConversionException {
		//Arrange
		Exception exception = new Exception();
		
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);
		
		// initObservable is what our ConvertingObservable looks at, and testObservable we can call set methods on
		TestableObservable<Integer> testObservable = new TestableObservable<>();
		InitialiseOnSubscribeObservable<Integer> initObservable = new InitialiseOnSubscribeObservable<Integer>(testObservable);
		
		// Mock converter, with a stub conversion method
		Converter<Integer, String> mockConverter = mock(Converter.class);
		when(mockConverter.convert(value)).thenReturn(convertedValue);
		
		// Object we are really testing
		ConvertingObservable<Integer, String> convertObservable = new ConvertingObservable<>(initObservable, mockConverter);
		
		//Act
		convertObservable.subscribe(mockObserver);
		convertObservable.setSource(initObservable);
		testObservable.setError(exception);
		
		//Assert
		// The initialisable observer has its update method called once
		verify(mockObserver, times(1)).onError(exception);
	}
	
	@Test
	public void test_ConvertingObservable_closes_connection() throws ConversionException {
		//Arrange	
		// Mock observer
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);
		
		// initObservable is what our ConvertingObservable looks at, and testObservable we can call set methods on
		TestableObservable<Integer> testObservable = new TestableObservable<>();
		InitialiseOnSubscribeObservable<Integer> initObservable = new InitialiseOnSubscribeObservable<Integer>(testObservable);
		
		// Mock converter, with a stub conversion method
		Converter<Integer, String> mockConverter = mock(Converter.class);
		when(mockConverter.convert(value)).thenReturn(convertedValue);
		when(mockConverter.convert(newValue)).thenReturn(newConvertedValue);
		
		// Object we are really testing
		ConvertingObservable<Integer, String> convertObservable = new ConvertingObservable<>(initObservable, mockConverter);
		
		//Act
		// Subscribe to the observable, then close the converting observable
		convertObservable.subscribe(mockObserver);
		convertObservable.setSource(initObservable);
		testObservable.setValue(value);
		convertObservable.close();
		testObservable.setValue(newValue);
		
		//Assert
		// The initialisable observer has its update method called once, and not for the new value
		verify(mockObserver, times(1)).onValue(convertedValue);
		verify(mockObserver, times(0)).onValue(newConvertedValue);
	}
}
