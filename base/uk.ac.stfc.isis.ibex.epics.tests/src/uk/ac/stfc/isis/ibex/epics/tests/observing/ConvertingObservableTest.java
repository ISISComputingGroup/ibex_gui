
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
	
	private Integer value;
	private String convertedValue;
	private String exceptionMessage;
	
	@Captor ArgumentCaptor<Exception> exceptionCaptor;
	
	@Before
	public void setUp() {
		value = 123;
		convertedValue = "converted value";
		exceptionMessage = "conversion exception!";
		
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test_ConvertingObservable_converts() throws ConversionException {
		//Arrange	
		// Mock observer
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);
		
		// Test observables
		TestableObservable<Integer> testObservable = new TestableObservable<>();
		InitialiseOnSubscribeObservable<Integer> initObservable = new InitialiseOnSubscribeObservable<Integer>(testObservable);
		
		// Mock converter, with a stub method
		Converter<Integer, String> mockConverter = mock(Converter.class);
		when(mockConverter.convert(value)).thenReturn(convertedValue);
		
		// Object we are really testing
		ConvertingObservable<Integer, String> convertObservable = new ConvertingObservable<>(initObservable, mockConverter);
		
		//Act
		convertObservable.subscribe(mockObserver);
		convertObservable.setSource(initObservable);
		testObservable.setValue(value);
		
		//Assert
		// The initialisable observer has its update method called once
		verify(mockObserver, times(1)).onValue(convertedValue);
	}
	
	@Test
	public void test_ConvertingObservable_with_conversion_exception() throws ConversionException {
		//Arrange	
		// Mock observer
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);
		
		// Test observables
		TestableObservable<Integer> testObservable = new TestableObservable<>();
		InitialiseOnSubscribeObservable<Integer> initObservable = new InitialiseOnSubscribeObservable<Integer>(testObservable);
		
		// Mock converter, with a stub method
		Converter<Integer, String> mockConverter = mock(Converter.class);
		when(mockConverter.convert(value)).thenThrow(new ConversionException(exceptionMessage));
		
		// Object we are really testing
		ConvertingObservable<Integer, String> convertObservable = new ConvertingObservable<>(initObservable, mockConverter);
		
		//Act
		convertObservable.subscribe(mockObserver);
		convertObservable.setSource(initObservable);
		testObservable.setValue(value);
		
		//Assert
		// The initialisable observer has its update method called once
		verify(mockObserver, times(0)).onValue(anyString());
		verify(mockObserver, times(1)).onError(exceptionCaptor.capture());
		assertEquals(exceptionMessage, exceptionCaptor.getValue().getMessage());
	}
}
