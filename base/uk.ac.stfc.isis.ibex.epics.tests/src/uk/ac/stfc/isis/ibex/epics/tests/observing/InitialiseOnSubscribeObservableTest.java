
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.observing.Unsubscriber;

// A lot of unchecked type conversions for mocking purposes
@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class InitialiseOnSubscribeObservableTest {
	
    Observer<String> mockObserver;
    Observer<String> mockObserverTwo;
	
    ClosableObservable<String> mockObservable;
	TestableObservable<String> testableObservable;
	
	Object addObserverReturnedObject;
	
	ForwardingObservable<String> initObservableCachingSource;
	ForwardingObservable<String> initObservableTestableSource;
	
	@Before
	public void setUp() {
		// Arrange
        mockObserver = mock(Observer.class);
        mockObserverTwo = mock(Observer.class);
		
		mockObservable = TestHelpers.getCachingObservable(TestHelpers.STRING_VALUE);
		testableObservable = new TestableObservable<>();
		testableObservable.setValue(TestHelpers.STRING_VALUE);
		testableObservable.setError(TestHelpers.EXCEPTION);		

		// The real observables to test
        initObservableCachingSource = new ForwardingObservable<>(mockObservable);
		initObservableCachingSource.subscribe(mockObserver);
		initObservableCachingSource.subscribe(mockObserverTwo);
		
		initObservableTestableSource = new ForwardingObservable<>(testableObservable);
		addObserverReturnedObject = initObservableTestableSource.subscribe(mockObserver);
		initObservableTestableSource.subscribe(mockObserverTwo);
	}
	
	@Test
	public void the_observable_is_created_and_returns_the_value_of_the_observable_it_watches() {
		// Assert - The InitialiseOnSubscribeObservable has the value returned from the mock observable
		assertEquals(initObservableCachingSource.getValue(), TestHelpers.STRING_VALUE);
	}
	
	@Test
	public void an_observer_is_added_and_the_observer_has_its_update_method_called() {
		// Assert - The initialisable observer has its update method called twice, once for each observable subscribed to
		verify(mockObserver, times(1)).update(TestHelpers.STRING_VALUE, TestHelpers.EXCEPTION, false);
	}
	
	@Test
	public void an_observer_is_added_and_an_unsubscribe_object_is_returned() {
		// Assert - An Unsubscriber is returned
		assertEquals(Unsubscriber.class, addObserverReturnedObject.getClass());
	}
	
	@Test
	public void setting_watched_observable_value_means_observable_watching_returns_new_value() {
		// Act
		testableObservable.setValue(TestHelpers.NEW_STRING_VALUE);
		
		// Assert - The InitialiseOnSubscribeObservable has the value returned from the mock observable
		assertEquals(TestHelpers.NEW_STRING_VALUE, initObservableTestableSource.getValue());
	}
	
	@Test
	public void setting_watched_observable_value_calls_observer_onValue_method() {
		// Act
		testableObservable.setValue(TestHelpers.NEW_STRING_VALUE);
				
		// Assert - The observer has its on value method called once, with the new value
		verify(mockObserver, times(1)).onValue(TestHelpers.NEW_STRING_VALUE);
	}
	
	@Test
	public void setting_watched_observable_connection_status_calls_observer_onConnectionStatus_method() {
		// Act
		testableObservable.setConnectionStatus(true);
				
		// Assert - The initialisable observer has its connection status changed once
		verify(mockObserver, times(1)).onConnectionStatus(true);
	}
	
	@Test
	public void setting_watched_observable_error_status_calls_observer_onError_method() {
		// Act
		testableObservable.setError(TestHelpers.EXCEPTION);
				
		// Assert - The initialisable observer has its error method called once
		verify(mockObserver, times(1)).onError(TestHelpers.EXCEPTION);
	}
	
	@Test
	public void with_multiple_observers_subscribed_all_observers_get_update_method_called() {
		// Assert - Both observables are initialised with the update method
		verify(mockObserver, times(1)).update(TestHelpers.STRING_VALUE, TestHelpers.EXCEPTION, false);
		verify(mockObserverTwo, times(1)).update(TestHelpers.STRING_VALUE, TestHelpers.EXCEPTION, false);
	}
	
	@Test
	public void multiple_observers_all_get_onValue_method_called_when_value_changes() {
		// Act
		testableObservable.setValue(TestHelpers.NEW_STRING_VALUE);
		
		// Assert - Both observables have their on value called once
		verify(mockObserver, times(1)).onValue(TestHelpers.NEW_STRING_VALUE);
		verify(mockObserverTwo, times(1)).onValue(TestHelpers.NEW_STRING_VALUE);
	}
		
	@Test
	public void calling_close_disconnects_from_watched_observable() {
		// Act
		initObservableTestableSource.close();
		testableObservable.setValue(TestHelpers.NEW_STRING_VALUE);
		
		// Assert - The observer has unsubscribed, so does not have on value method called
		verify(mockObserver, times(0)).onValue(any(String.class));
	}
	
	@Test
	public void calling_removeObserver_on_unsubscriber_stops_observer_being_update_on_value_changes() {
		// Act
		((Unsubscriber<String>) addObserverReturnedObject).cancelSubscription();
		testableObservable.setValue(TestHelpers.NEW_STRING_VALUE);
		
		// Assert - The first observer does not have its onValue method called, the second does not
		verify(mockObserver, times(0)).onValue(TestHelpers.NEW_STRING_VALUE);
		verify(mockObserverTwo, times(1)).onValue(any(String.class));
	}

	@Test
	public void adding_observer_more_than_once_calls_upadate_method_twice() {
		// Act
		initObservableCachingSource.subscribe(mockObserver);
		
		// Assert - Should have update method called
		verify(mockObserver, atLeastOnce()).update(TestHelpers.STRING_VALUE, null, false);
	}
	
	@Test
	public void adding_observer_more_than_once_only_calls_onValue_method_once() {		
		// Act
		initObservableCachingSource.subscribe(mockObserver);
		testableObservable.setValue(TestHelpers.NEW_STRING_VALUE);
		
		// Assert - Should only have the onValue method called once
		verify(mockObserver, times(1)).onValue(TestHelpers.NEW_STRING_VALUE);
	}
	
	@Test
	public void source_observable_having_null_value_set_does_not_update_observer() {
		// Act
		testableObservable.setValue(null);
		
		// Assert - On value method should not be called
		verify(mockObserver, times(0)).onValue(any(String.class));
	}
	
	@Test
	public void source_observable_changing_from_null_value_to_non_null_value_updates_observer() {
		// Act
		testableObservable.setValue(null);
		testableObservable.setValue(TestHelpers.NEW_STRING_VALUE);
		
		// Assert - On value method should not be called
		verify(mockObserver, times(1)).onValue(TestHelpers.NEW_STRING_VALUE);
	}
}
