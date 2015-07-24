package uk.ac.stfc.isis.ibex.epics.tests.observing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.CachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.SwitchableObservable;

// A lot of unchecked type conversions for mocking purposes
@SuppressWarnings("unchecked")
/**
 * Test for SwitchingObservable. See InitialisOnSubscribeObservableTest for more test touching
 * the higher observable classes.
 */
public class SwitchingObservableTest {
	
	private String value;
	private String newValue;
	
	@Before
	public void setUp() {
		value = "value";
		newValue = "new value";
	}
	
	@Test
	public void test_SwitchingObservable_switch() {
		//Arrange	
		// Mock observer, templated objects need cast
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);
		
		// Mock observables with stub methods returning different values
		CachingObservable<String> mockObservableOne = mock(CachingObservable.class);
		when(mockObservableOne.getValue()).thenReturn(value);
		
		CachingObservable<String> mockObservableTwo = mock(CachingObservable.class);
		when(mockObservableTwo.getValue()).thenReturn(newValue);
		
		// Object we are really testing
		SwitchableObservable<String> switchableObservable = new SwitchableObservable<>(mockObservableOne);
		
		//Act
		switchableObservable.subscribe(mockObserver);
		// Do the switch
		switchableObservable.switchTo(mockObservableTwo);
		
		//Assert
		// The initialisable observer has its onConnectionChanged called twice and onValue called once.
		// Note here that the switch calls onValue on the observer, but the initialise does not.
		verify(mockObserver, times(2)).onConnectionChanged(false);
		verify(mockObserver, times(1)).onValue(newValue);
		
		// The SwitchableObservable has the new Obervable's value
		assertEquals(switchableObservable.getValue(), newValue);
	}
	
	@Test
	public void test_SwitchingObservable_switch_to_object_with_null_value() {
		//Arrange	
		// Mock observer, templated objects need cast
		InitialisableObserver<String> mockObserver = mock(InitialisableObserver.class);
		
		// Mock observables with stub methods returning different values
		CachingObservable<String> mockObservableOne = mock(CachingObservable.class);
		when(mockObservableOne.getValue()).thenReturn(value);
		
		CachingObservable<String> mockObservableTwo = mock(CachingObservable.class);
		when(mockObservableTwo.getValue()).thenReturn(null);
		
		// Object we are really testing
		SwitchableObservable<String> switchableObservable = new SwitchableObservable<>(mockObservableOne);
		
		//Act
		switchableObservable.subscribe(mockObserver);
		// Do the switch
		switchableObservable.switchTo(mockObservableTwo);
		
		//Assert
		// The initialisable observer has its onConnectionChanged called twice and onValue called once
		verify(mockObserver, times(2)).onConnectionChanged(false);
		verify(mockObserver, times(0)).onValue(any(String.class));
		
		// The SwitchableObservable has the old Obervable's value
		// Might need to think about if this is desirable?
		assertEquals(switchableObservable.getValue(), value);
	}
}
