
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import uk.ac.stfc.isis.ibex.epics.observing.BufferedObservablePair;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.observing.Pair;

// A lot of unchecked type conversions for mocking purposes
@SuppressWarnings({ "unchecked" })
/**
 * Test for BufferedObservablePair
 */
public class BufferedObservablePairTest {
	
    private Observer<Pair<String, Integer>> mockObserver;
	
	private TestableObservable<String> testableStringObservable;
	private TestableObservable<Integer> testableIntegerObservable;
	
	private ForwardingObservable<String> initStringObservable;
	private ForwardingObservable<Integer> initIntegerObservable;
	
    private BufferedObservablePair<String, Integer> bufferedObservablePair;
	
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
		
        bufferedObservablePair =
                new BufferedObservablePair<String, Integer>(initStringObservable, initIntegerObservable);
        bufferedObservablePair.addObserver(mockObserver);
	}
	
	@Test
    public void WHEN_only_first_value_set_THEN_observers_not_notified() {
		// Act
		testableStringObservable.setValue(TestHelpers.NEW_STRING_VALUE);
		
        // Assert - The observer has not had its onValue called and the public
        // value remains null.
        verify(mockObserver, times(0)).onValue(pairCaptor.capture());
        assertEquals(null, bufferedObservablePair.getValue());
	}

    @Test
    public void WHEN_only_second_value_set_THEN_observers_not_notified() {
        // Act
        testableIntegerObservable.setValue(TestHelpers.NEW_INT_VALUE);

        // Assert - The observer has not had its onValue called and the public
        // value remains null.
        verify(mockObserver, times(0)).onValue(pairCaptor.capture());
        assertEquals(null, bufferedObservablePair.getValue());
    }

    @Test
    public void WHEN_both_values_set_THEN_observers_notified_and_stored_value_correct() {
        // Act
        testableStringObservable.setValue(TestHelpers.NEW_STRING_VALUE);
        testableIntegerObservable.setValue(TestHelpers.NEW_INT_VALUE);

        // Assert - The observer has its onValue called once and has the new
        // value
        verify(mockObserver, times(1)).onValue(pairCaptor.capture());
        assertEquals(TestHelpers.NEW_STRING_VALUE, pairCaptor.getValue().first);
        assertEquals(TestHelpers.NEW_INT_VALUE, pairCaptor.getValue().second);
    }

    @Test
    public void
            GIVEN_value_pair_in_buffer_WHEN_new_first_value_observed_THEN_values_collated_into_most_recent_value_pair_and_set() {
        // Arrange
        testableStringObservable.setValue("initial_val");
        testableIntegerObservable.setValue(TestHelpers.NEW_INT_VALUE);

        // Act
        testableStringObservable.setValue("new_val");

        // Assert - The observer has its on value method called twice, and has
        // the most recent values for both values in the pair.
        verify(mockObserver, times(2)).onValue(pairCaptor.capture());
        assertEquals("new_val", pairCaptor.getValue().first);
        assertEquals(TestHelpers.NEW_INT_VALUE, pairCaptor.getValue().second);
    }
}
