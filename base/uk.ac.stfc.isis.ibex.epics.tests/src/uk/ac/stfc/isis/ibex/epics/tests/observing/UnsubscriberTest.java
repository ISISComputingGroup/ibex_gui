package uk.ac.stfc.isis.ibex.epics.tests.observing;

import static org.mockito.Mockito.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.observing.Unsubscriber;

@SuppressWarnings("unchecked")
public class UnsubscriberTest {

	private String firstValue = "first value";
	private String secondValue = "second value";
	
	@Test
	public void test_Unsubscriber_cancel() {
		// Arrange	
		// Mock observer
		Observer<String> mockObserver = mock(Observer.class);
		
		// Mock observable that our other observer is looking at
		TestableObservable<String> testableObservable = new TestableObservable<>(); 
		
		InitialiseOnSubscribeObservable<String> observable = new InitialiseOnSubscribeObservable<String>(testableObservable);
		
		// Object we are really testing
		Unsubscriber<String> subscription = (Unsubscriber<String>) observable.subscribe(mockObserver);
		
		// Act
		testableObservable.setValue(firstValue);
		subscription.cancel();
		testableObservable.setValue(secondValue);
		
		// Assert
		// The initialisable observer has its update method called once
		verify(mockObserver, times(1)).onValue(firstValue);
		verify(mockObserver, times(0)).onValue(secondValue);
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
		Unsubscriber<String> subscription = (Unsubscriber<String>) observable.subscribe(mockObserver);
		
		// Act
		testableObservable.setValue(firstValue);
		subscription.cancel();
		subscription.cancel();
		testableObservable.setValue(secondValue);
		
		// Assert
		// The initialisable observer has its update method called once
		verify(mockObserver, times(1)).onValue(firstValue);
		verify(mockObserver, times(0)).onValue(secondValue);
	}
}
