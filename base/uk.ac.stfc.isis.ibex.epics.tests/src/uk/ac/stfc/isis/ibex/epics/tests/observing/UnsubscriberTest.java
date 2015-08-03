package uk.ac.stfc.isis.ibex.epics.tests.observing;

import static org.mockito.Mockito.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.observing.Unsubscriber;

@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class UnsubscriberTest {

	private static final String FIRST_VALUE = "first value";
	private static final String SECOND_VALUE = "second value";
	
	@Test
	public void test_Unsubscriber_cancel() {
		// Arrange	
		// Mock observer
		Observer<String> mockObserver = mock(Observer.class);
		
		// Mock observable that our other observer is looking at
		TestableObservable<String> testableObservable = new TestableObservable<>(); 
		
		InitialiseOnSubscribeObservable<String> observable = new InitialiseOnSubscribeObservable<String>(testableObservable);
		
		// Object we are really testing
		Unsubscriber<String> subscription = (Unsubscriber<String>) observable.addObserver(mockObserver);
		
		// Act
		testableObservable.setValue(FIRST_VALUE);
		subscription.removeObserver();
		testableObservable.setValue(SECOND_VALUE);
		
		// Assert
		// The initialisable observer has its update method called once
		verify(mockObserver, times(1)).onValue(FIRST_VALUE);
		verify(mockObserver, times(0)).onValue(SECOND_VALUE);
	}
	
	@Test
	public void test_Unsubscriber_cancelling_twice_does_nothing() {
		// Arrange	
		// Mock observer
		Observer<String> mockObserver = mock(Observer.class);
		
		// Mock observable that our other observer is looking at
		TestableObservable<String> testableObservable = new TestableObservable<>(); 
		
		InitialiseOnSubscribeObservable<String> observable = new InitialiseOnSubscribeObservable<String>(testableObservable);
		
		// Object we are really testing
		Unsubscriber<String> subscription = (Unsubscriber<String>) observable.addObserver(mockObserver);
		
		// Act
		testableObservable.setValue(FIRST_VALUE);
		subscription.removeObserver();
		subscription.removeObserver();
		testableObservable.setValue(SECOND_VALUE);
		
		// Assert
		// The initialisable observer has its update method called once
		verify(mockObserver, times(1)).onValue(FIRST_VALUE);
		verify(mockObserver, times(0)).onValue(SECOND_VALUE);
	}
}
