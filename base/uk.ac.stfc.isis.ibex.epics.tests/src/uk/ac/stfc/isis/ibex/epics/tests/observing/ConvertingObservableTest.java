
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
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

package uk.ac.stfc.isis.ibex.epics.tests.observing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.observing.ConvertingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;

// A lot of unchecked type conversions for mocking purposes
@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
@RunWith(MockitoJUnitRunner.class)
public class ConvertingObservableTest {
	
	private static final Integer INT_THROWS_EXCEPTION_VALUE = 666;
	private static final String CONVERTED_VALUE = "converted from 123";
	private static final String NEW_CONVERTED_VALUE = "converted from 456";
	private static final String EXCEPTION_MESSAGE = "converions exception!";
	
    private Observer<String> mockObserver;
	
	private TestableObservable<Integer> testObservable;	
	private ConvertingObservable<Integer, String> convertObservable;
	
	private Function<Integer, String> mockConverter;
	
	@Captor
	private ArgumentCaptor<Exception> exceptionCaptor;
	
	@Before
	public void setUp() throws ConversionException {
		
		// Arrange		
        mockObserver = mock(Observer.class);
		
		testObservable = new TestableObservable<>();

		mockConverter = mock(Function.class);
		when(mockConverter.apply(TestHelpers.INT_VALUE)).thenReturn(CONVERTED_VALUE);
		when(mockConverter.apply(INT_THROWS_EXCEPTION_VALUE)).thenThrow(new ConversionException(EXCEPTION_MESSAGE));
		when(mockConverter.apply(TestHelpers.NEW_INT_VALUE)).thenReturn(NEW_CONVERTED_VALUE);
		
        convertObservable = new ConvertingObservable<>(testObservable, mockConverter);
		convertObservable.subscribe(mockObserver);
	}
	
	@Test
	public void when_observable_value_is_set_converted_value_is_returned_through_onValue_method_on_observer() {
		// Act
		testObservable.setValue(TestHelpers.INT_VALUE);
		
		//Assert - The initialisable observer has its update method called once, with the converted value
		verify(mockObserver, times(1)).onValue(CONVERTED_VALUE);
	}
	
	@Test
	public void when_converter_thows_error_onError_method_on_observer_is_called_with_error() {
		//Act
		convertObservable.subscribe(mockObserver);
		testObservable.setValue(INT_THROWS_EXCEPTION_VALUE);
		
		//Assert - The initialisable observer has its onError message called once, for the ConversionException
		verify(mockObserver, times(1)).onError(exceptionCaptor.capture());
		assertEquals(EXCEPTION_MESSAGE, exceptionCaptor.getValue().getMessage());
	}
	
	@Test
	public void when_watched_observable_has_error_observer_has_onError_method_called_with_the_exception() {
		//Act
		testObservable.setError(TestHelpers.EXCEPTION);
		
		//Assert - The initialisable observer has its onError method called
		verify(mockObserver, times(1)).onError(TestHelpers.EXCEPTION);
	}
	
	@Test
    public void when_watched_observable_has_connection_status_changed_observer_has_onConnectionStatus_method_called() {
		//Act
		testObservable.setConnectionStatus(true);
		
		//Assert - The initialisable observer has its onError method called
		verify(mockObserver, times(1)).onConnectionStatus(true);
	}
		
	@Test
	public void when_observable_closes_source_observers_do_not_get_new_value() throws ConversionException {
		//Act
		convertObservable.close();
		testObservable.setValue(TestHelpers.NEW_INT_VALUE);
		
		//Assert - The initialisable observer has its update method called once, and not for the new value
		verify(mockObserver, times(0)).onValue(NEW_CONVERTED_VALUE);
	}
	
	@Test
	public void when_observable_source_value_is_set_to_null_nothing_happens() throws ConversionException {
		//Act
		testObservable.setValue(null);
		
		//Assert
		verify(mockObserver, times(0)).onValue(anyString());
		verify(mockObserver, times(0)).onError(any(Exception.class));
	}

    @Test
    public void GIVEN_observable_has_a_value_but_did_have_an_error_WHEN_observe_THEN_new_value_is_set_on_observable() {
        // Arrange
        testObservable.setError(TestHelpers.EXCEPTION);
        testObservable.setValue(TestHelpers.NEW_INT_VALUE);
        testObservable.setConnectionStatus(true);

        // Act
        ConvertingObservable<Integer, Integer> noConversionObservable =
                new ConvertingObservable<>(testObservable, Function.identity());
        Integer result = noConversionObservable.getValue();

        // Assert
        assertEquals(TestHelpers.NEW_INT_VALUE, result);
    }
}
