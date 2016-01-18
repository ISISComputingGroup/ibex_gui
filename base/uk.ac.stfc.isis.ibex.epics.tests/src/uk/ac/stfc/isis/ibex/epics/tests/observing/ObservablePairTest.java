
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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ObservablePair;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.observing.Pair;

// A lot of unchecked type conversions for mocking purposes
@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
/**
 * Test for SwitchingObservable. See InitialisOnSubscribeObservableTest for more test touching
 * the higher observable classes.
 */
public class ObservablePairTest {
	
    private Observer<Pair<String, Integer>> mockObserver;
	
	private TestableObservable<String> testableStringObservable;
	private TestableObservable<Integer> testableIntegerObservable;
	
	private ForwardingObservable<String> initStringObservable;
	private ForwardingObservable<Integer> initIntegerObservable;
	
	private ObservablePair<String, Integer> observablePair;
	
	@Captor
	private ArgumentCaptor<Pair<String, Integer>> pairCaptor;
	
	@Before
	public void setUp() {
		// This is to initialise the captor
		MockitoAnnotations.initMocks(this);
		
        mockObserver = mock(Observer.class);
		
		testableStringObservable = new TestableObservable<>();
		testableStringObservable.setValue(TestHelpers.STRING_VALUE);
		initStringObservable = new ForwardingObservable<>(testableStringObservable);
		
		testableIntegerObservable = new TestableObservable<Integer>();
		testableIntegerObservable.setValue(TestHelpers.INT_VALUE);
		initIntegerObservable = new ForwardingObservable<>(testableIntegerObservable);
		
		observablePair = new ObservablePair<String, Integer>(initStringObservable, initIntegerObservable);
		observablePair.addObserver(mockObserver);
	}
	
	@Test
	public void update_first_value_in_pair_followed_by_second_value_calls_on_value_twice_and_sets_value() {		
		// Act
		testableStringObservable.setValue(TestHelpers.NEW_STRING_VALUE);
		testableIntegerObservable.setValue(TestHelpers.NEW_INT_VALUE);
		
		// Assert - The observer has its on value method called twice, and has the new values
		verify(mockObserver, times(2)).onValue(pairCaptor.capture());
		assertEquals(TestHelpers.NEW_STRING_VALUE, pairCaptor.getValue().first);
		assertEquals(TestHelpers.NEW_INT_VALUE, pairCaptor.getValue().second);
	}
	
	@Test
	public void update_second_value_in_pair_followed_by_first_value_calls_on_value_twice_and_sets_value() {
		testableIntegerObservable.setValue(TestHelpers.NEW_INT_VALUE);
		testableStringObservable.setValue(TestHelpers.NEW_STRING_VALUE);
		
		// Assert - The observer has its on value method called twice, and has the new values
		verify(mockObserver, times(2)).onValue(pairCaptor.capture());
		assertEquals(TestHelpers.NEW_STRING_VALUE, pairCaptor.getValue().first);
		assertEquals(TestHelpers.NEW_INT_VALUE, pairCaptor.getValue().second);
	}
	
	@Test
	public void calling_close_on_observable_stops_observer_being_updated() {		
		// Act
		observablePair.close();
		testableIntegerObservable.setValue(TestHelpers.NEW_INT_VALUE);
		testableStringObservable.setValue(TestHelpers.NEW_STRING_VALUE);
		
		// Assert - Both subscriptions are closed, so no onValue calls
		verify(mockObserver, times(0)).onValue(any(Pair.class));
	}
	
	@Test
	public void setting_an_error_on_one_observable_triggers_onError_method_on_observer() {
		testableStringObservable.setError(TestHelpers.EXCEPTION);
		
		// Assert - The on error call is passed through
		verify(mockObserver, times(1)).onError(TestHelpers.EXCEPTION);
	}
	
	@Test
	public void setting_connection_status_on_one_observable_triggers_onConnectionStatus_method_on_observer() {
		testableStringObservable.setConnectionStatus(true);
		
		// Assert - The on error call is passed through
		verify(mockObserver, times(1)).onConnectionStatus(true);
	}
	
	@Test
	public void setting_one_observable_value_to_null_does_nothing() {
		testableStringObservable.setValue(null);
		
		// Assert - The on error call is passed through
		verify(mockObserver, times(0)).onValue(any(Pair.class));
	}
}
