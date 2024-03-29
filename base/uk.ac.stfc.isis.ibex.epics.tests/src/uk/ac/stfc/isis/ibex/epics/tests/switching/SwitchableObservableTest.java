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

package uk.ac.stfc.isis.ibex.epics.tests.switching;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableObservable;
import uk.ac.stfc.isis.ibex.epics.tests.observing.TestHelpers;

@RunWith(MockitoJUnitRunner.Strict.class)
public class SwitchableObservableTest {

    @Mock private Observer<String> mockObserver;
	
	private ClosableObservable<String> mockObservableReturnsValue;
	private ClosableObservable<String> mockObservableReturnsNewValue;
	private ClosableObservable<String> mockObservableReturnsNull;
	
    private SwitchableObservable<String> switchableObservable;
	
	@Before
	public void setUp() {
		// Arrange		
        mockObservableReturnsValue = TestHelpers.getClosableCachingObservable(TestHelpers.STRING_VALUE);
        mockObservableReturnsNewValue = TestHelpers.getClosableCachingObservable(TestHelpers.NEW_STRING_VALUE);
        mockObservableReturnsNull = TestHelpers.getClosableCachingObservable(null);

		// The real observable to test
        switchableObservable = new SwitchableObservable<>(mockObservableReturnsValue);
		switchableObservable.subscribe(mockObserver);
	}

	@Test
	public void calling_close_calls_close_on_source_observable() {
		// Act
		switchableObservable.close();

		// Assert - The original ClosableCachingObservable is closed
        verify(mockObservableReturnsValue, Mockito.atLeast(1)).close();
	}
	
	@Test
	public void calling_close_still_returns_value_of_closed_observable() {
		// Act
		switchableObservable.close();
		
		// Assert - Even though it is closed value is still set
		assertEquals(TestHelpers.STRING_VALUE, switchableObservable.getValue());
	}
	
	@Test
	public void calling_close_does_not_call_onValue_method_on_observer() {
		// Act
		switchableObservable.close();
		
		// Assert - Observer never did anything
		verify(mockObserver, times(0)).onValue(anyString());
	}
	
	@Test
	public void calling_close_does_not_call_onError_method_on_observer() {
		// Act
		switchableObservable.close();
		
		// Assert - Observer never did anything
		verify(mockObserver, times(0)).onError(any(Exception.class));
	}
	
	@Test
    public void calling_close_calls_onConnectionStatus_method_on_observer_with_isConnected_false() {
		// Act
		switchableObservable.close();
		
		// Assert - Observer never did anything
        verify(mockObserver, times(1)).onConnectionStatus(false);
	}
	
	@Test
	public void switching_observables_calls_close_on_old_observable() {
		// Act
        switchableObservable.setSource(mockObservableReturnsNewValue);

		// Assert - The original ClosableCachingObservable is closed
        verify(mockObservableReturnsValue, Mockito.atLeast(1)).close();
	}
	
	@Test
	public void switching_observables_returns_new_observables_value() {
		// Act
        switchableObservable.setSource(mockObservableReturnsNewValue);

		// Assert - The original ClosableCachingObservable is closed
		assertEquals(TestHelpers.NEW_STRING_VALUE, switchableObservable.getValue());
	}
	
	@Test
	public void switching_to_null_observable_returns_old_value() {
		// Act
        switchableObservable.setSource(mockObservableReturnsNull);
		
		// Assert - The original value is returned
		assertEquals(TestHelpers.STRING_VALUE, switchableObservable.getValue());
	}
	
	@Test
	public void switching_to_null_observable_closes_old_observable() {
		// Act
        switchableObservable.setSource(mockObservableReturnsNull);
		
		// Assert - The original value is returned
        verify(mockObservableReturnsValue, Mockito.atLeast(1)).close();
	}
}