
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
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

import uk.ac.stfc.isis.ibex.epics.observing.ConcatenatingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;

// A lot of unchecked type conversions for mocking purposes
@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
@RunWith(MockitoJUnitRunner.class)
/**
 * Test for SwitchingObservable. See InitialisOnSubscribeObservableTest for more test touching
 * the higher observable classes.
 */
public class ConcatenatingObservableTest {
	
    private Observer<String> mockObserver;
	
	private TestableObservable<String> testableFirstObservable= new TestableObservable<>();
	private TestableObservable<String> testableSecondObservable = new TestableObservable<>();
	
	private ForwardingObservable<String> initFirstObservable;
	private ForwardingObservable<String> initSecondObservable;
	
	private ConcatenatingObservable concatObservable;
	
	@Captor
	private ArgumentCaptor<String> captor;
	
	@Before
	public void setUp() {
        mockObserver = mock(Observer.class);
		
        initFirstObservable = new ForwardingObservable<>(testableFirstObservable);
        testableFirstObservable.setValue(TestHelpers.STRING_VALUE);
        
		initSecondObservable = new ForwardingObservable<>(testableSecondObservable);
		testableSecondObservable.setValue(TestHelpers.STRING_VALUE);
	      
		concatObservable = new ConcatenatingObservable(initFirstObservable, initSecondObservable);
		concatObservable.subscribe(mockObserver);
	}
	
	@Test
	public void WHEN_first_value_changes_THEN_concatonated_value_returned() {		
		// Act
	    testableFirstObservable.setValue(TestHelpers.NEW_STRING_VALUE);
		
		// Assert - The observer has its on value method called twice, and has the new values
		verify(mockObserver, times(1)).onValue(captor.capture());
		assertEquals(TestHelpers.NEW_STRING_VALUE + " " + TestHelpers.STRING_VALUE, captor.getValue());
	}
	
    @Test
    public void WHEN_second_value_changes_THEN_concatonated_value_returned() {       
        // Act
        testableSecondObservable.setValue(TestHelpers.NEW_STRING_VALUE);
        
        // Assert - The observer has its on value method called twice, and has the new values
        verify(mockObserver, times(1)).onValue(captor.capture());
        assertEquals(TestHelpers.STRING_VALUE + " " + TestHelpers.NEW_STRING_VALUE, captor.getValue());
    }
	
	@Test
	public void WHEN_close_called_on_observable_THEN_stops_observer_being_updated() {		
		// Act
	    concatObservable.close();
	    testableFirstObservable.setValue(TestHelpers.NEW_STRING_VALUE);
	    testableSecondObservable.setValue(TestHelpers.NEW_STRING_VALUE);
		
		// Assert - Both subscriptions are closed, so no onValue calls
		verify(mockObserver, times(0)).onValue(any(String.class));
	}
	
	@Test
	public void WHEN_error_set_on_first_observable_THEN_triggers_onError_method_on_observer() {
	    testableFirstObservable.setError(TestHelpers.EXCEPTION);
		
		verify(mockObserver, times(1)).onError(TestHelpers.EXCEPTION);
	}
	
	@Test
	public void WHEN_connection_status_set_on_first_observable_THEN_triggers_onConnectionStatus_method_on_observer() {
	    testableFirstObservable.setConnectionStatus(true);
		
		verify(mockObserver, times(1)).onConnectionStatus(true);
	}
	
    @Test
    public void WHEN_error_set_on_second_observable_THEN_onError_not_triggered_on_observer() {
        testableSecondObservable.setError(TestHelpers.EXCEPTION);
        
        verify(mockObserver, times(0)).onError(TestHelpers.EXCEPTION);
    }
    
    @Test
    public void WHEN_connection_status_set_on_second_observable_THEN_onConnectionStatus_not_triggered_on_observer() {
        testableSecondObservable.setConnectionStatus(false);
        
        verify(mockObserver, times(0)).onConnectionStatus(false);
    }
	
	@Test
	public void WHEN_first_observable_value_set_to_empty_THEN_second_value_returned() {
	    testableSecondObservable.setValue(TestHelpers.NEW_STRING_VALUE);
	    testableFirstObservable.setValue("");
		
        verify(mockObserver, times(2)).onValue(captor.capture());
        assertEquals(TestHelpers.NEW_STRING_VALUE, captor.getValue());
	}
	
    @Test
    public void WHEN_second_observable_value_set_to_empty_THEN_first_value_returned() {
        testableFirstObservable.setValue(TestHelpers.NEW_STRING_VALUE);
        testableSecondObservable.setValue("");
        
        verify(mockObserver, times(2)).onValue(captor.capture());
        assertEquals(TestHelpers.NEW_STRING_VALUE, captor.getValue());
    }
    
    @Test
    public void WHEN_both_observables_set_to_empty_THEN_empty_string_returned() {
        testableFirstObservable.setValue("");
        testableSecondObservable.setValue("");
        
        verify(mockObserver, times(2)).onValue(captor.capture());
        assertEquals("", captor.getValue());
    }
}
