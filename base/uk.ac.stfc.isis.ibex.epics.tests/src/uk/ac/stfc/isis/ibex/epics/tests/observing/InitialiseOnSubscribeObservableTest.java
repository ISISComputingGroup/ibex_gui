package uk.ac.stfc.isis.ibex.epics.tests.observing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.BaseCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.CachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.observing.Unsubscriber;

import java.lang.String;

// A lot of unchecked type conversions for mocking purposes
@SuppressWarnings("unchecked")
public class InitialiseOnSubscribeObservableTest {
	
	private String value;
	private String newValue;
	
	// We need to do this to get access to the setters for testing
	class TestableObservable<T> extends BaseCachingObservable<T> {
		@Override
		public void setValue(T value) {
			super.setValue(value);
		}
	
		@Override
		public void setError(Exception e) {
			super.setError(e);
		}
		
		@Override
		public void setConnectionChanged(boolean isConnected) {
			super.setConnectionChanged(isConnected);
		}
	};
	
	@Before
	public void setUp() {
		value = "value";
		newValue = "new value";
	}
	
	@Test
	public void test_InitialiseOnSubscribeObservable_subscription() {
		//Arrange	
		// Mock observer, templated objects need cast
		InitialisableObserver<String> mockObserver = (InitialisableObserver<String>) mock(InitialisableObserver.class);
		
		// Mock observable with stub method
		CachingObservable<String> mockObservable = (CachingObservable<String>) mock(CachingObservable.class);
		when(mockObservable.getValue()).thenReturn(value);
		
		// Object we are really testing
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(mockObservable);
		
		//Act
		Object returned = initObservable.subscribe(mockObserver);
		
		//Assert
		// The initialisable observer has its update method called once
		verify(mockObserver, times(1)).update(value, null, false);
		
		// The InitialiseOnSubscribeObservable has the value returned from the mock observable
		assertEquals(initObservable.getValue(), value);
		
		// A Unsubscriber is returned
		assertEquals(Unsubscriber.class, returned.getClass());
	}
	
	@Test
	public void test_initialise_on_subscribe_observable_on_value_change() {
		//Arrange
		TestableObservable<String> testableObservable = new TestableObservable<>();
		
		InitialisableObserver<String> mockObserver = (InitialisableObserver<String>) mock(InitialisableObserver.class);
		
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(testableObservable);
		
		//Act
		testableObservable.setValue(value);
		initObservable.subscribe(mockObserver);
		testableObservable.setValue(newValue);
				
		//Assert
		
		// The initialisable observer has its update method called once, and on value method once
		verify(mockObserver, times(1)).update(value, null, false);
		verify(mockObserver, times(1)).onValue(newValue);
		
		// The InitialiseOnSubscribeObservable has the value returned from the mock observable
		assertEquals(initObservable.getValue(), newValue);
	}
	
	@Test
	public void test_initialise_on_subscribe_observable_on_connection_change() {
		//Arrange
		TestableObservable<String> testableObservable = new TestableObservable<>();
		
		InitialisableObserver<String> mockObserver = (InitialisableObserver<String>) mock(InitialisableObserver.class);
		
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(testableObservable);
		
		//Act
		initObservable.subscribe(mockObserver);
		testableObservable.setConnectionChanged(true);
				
		//Assert
		
		// The initialisable observer has its update method called once
		verify(mockObserver, times(1)).update(null, null, false);
		verify(mockObserver, times(1)).onConnectionChanged(true);
	}
	
	@Test
	public void test_initialise_on_subscribe_observable_on_error() {
		//Arrange
		TestableObservable<String> testableObservable = new TestableObservable<>();
		
		InitialisableObserver<String> mockObserver = (InitialisableObserver<String>) mock(InitialisableObserver.class);
		
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(testableObservable);
		
		Exception e = new Exception();
		
		//Act
		initObservable.subscribe(mockObserver);
		testableObservable.setError(e);
				
		//Assert
		
		// The initialisable observer has its update method called once
		verify(mockObserver, times(1)).update(null, null, false);
		verify(mockObserver, times(1)).onError(e);
	}
	
	@Test
	public void test_InitialiseOnSubscribeObservable_with_multiple_observers() {
		//Arrange	
		// Two mock observers
		InitialisableObserver<String> mockObserverOne = (InitialisableObserver<String>) mock(InitialisableObserver.class);
		InitialisableObserver<String> mockObserverTwo = (InitialisableObserver<String>) mock(InitialisableObserver.class);
		
		// Mock observable with stub method
		TestableObservable<String> testableObservable = new TestableObservable<>();

		// Object we are really testing
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(testableObservable);
		
		//Act
		testableObservable.setValue(value);
		initObservable.subscribe(mockObserverOne);
		initObservable.subscribe(mockObserverTwo);
		testableObservable.setValue(newValue);
		
		//Assert
		// The initialisable observer has its update method called once
		verify(mockObserverOne, times(1)).update(value, null, false);
		verify(mockObserverTwo, times(1)).update(value, null, false);
		verify(mockObserverOne, times(1)).onValue(newValue);
		verify(mockObserverTwo, times(1)).onValue(newValue);
	}
	
	@Test
	public void test_InitialiseOnSubscribeObservable_cancel_subscription_with_multiple_observers() {
		//Arrange	
		// Two mock observers
		InitialisableObserver<String> mockObserverOne = (InitialisableObserver<String>) mock(InitialisableObserver.class);
		InitialisableObserver<String> mockObserverTwo = (InitialisableObserver<String>) mock(InitialisableObserver.class);
		
		// Mock observable with stub method
		TestableObservable<String> testableObservable = new TestableObservable<>();

		// Object we are really testing
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(testableObservable);
		
		//Act
		testableObservable.setValue(value);
		initObservable.subscribe(mockObserverOne);
		Subscription unsubscriber = initObservable.subscribe(mockObserverTwo);
		unsubscriber.cancel();
		testableObservable.setValue(newValue);
		
		//Assert
		// The initialisable observer has its update method called once
		verify(mockObserverOne, times(1)).update(value, null, false);
		verify(mockObserverTwo, times(1)).update(value, null, false);
		verify(mockObserverOne, times(1)).onValue(newValue);
		verify(mockObserverTwo, times(0)).onValue(any(String.class));
	}
	
	@Test
	public void test_InitialiseOnSubscribeObservable_cancel_subscription_to_observable() {
		//Arrange
		InitialisableObserver<String> mockObserver = (InitialisableObserver<String>) mock(InitialisableObserver.class);
		
		TestableObservable<String> testableObservable = new TestableObservable<>();
		
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(testableObservable);
		
		//Act
		initObservable.subscribe(mockObserver);
		initObservable.close();
		testableObservable.setValue(newValue);
		
		//Assert
		verify(mockObserver, times(1)).update(null, null, false);
		verify(mockObserver, times(0)).onValue(any(String.class));
	}
	
	@Test
	public void test_InitialiseOnSubscribeObservable_subscribe_with_to_observable_with_error() {
		//Arrange
		Exception exception = new Exception();
		
		InitialisableObserver<String> mockObserver = (InitialisableObserver<String>) mock(InitialisableObserver.class);
		
		TestableObservable<String> testableObservable = new TestableObservable<>();
		testableObservable.setError(exception);
		
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(testableObservable);
		
		//Act
		initObservable.subscribe(mockObserver);
		
		//Assert
		verify(mockObserver, times(1)).update(null, exception, false);
	}
	
	@Test
	public void test_InitialiseOnSubscribeObservable_adding_observer_more_than_once() {
		//Arrange
		
		InitialisableObserver<String> mockObserver = (InitialisableObserver<String>) mock(InitialisableObserver.class);
		
		TestableObservable<String> testableObservable = new TestableObservable<>();
		
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(testableObservable);
		
		//Act
		initObservable.subscribe(mockObserver);
		initObservable.subscribe(mockObserver);
		testableObservable.setValue(newValue);
		
		//Assert
		verify(mockObserver, times(2)).update(null, null, false);
		//Should only have the on value method added once, 
		verify(mockObserver, times(1)).onValue(newValue);
	}
	
	@Test
	public void test_InitialiseOnSubscribeObservable_setting_null_value_does_not_trigger_onValue_call() {
		//Arrange
		
		InitialisableObserver<String> mockObserver = (InitialisableObserver<String>) mock(InitialisableObserver.class);
		
		TestableObservable<String> testableObservable = new TestableObservable<>();
		
		InitialiseOnSubscribeObservable<String> initObservable = new InitialiseOnSubscribeObservable<>(testableObservable);
		
		//Act
		initObservable.subscribe(mockObserver);
		testableObservable.setValue(null);
		
		//Assert
		//Should only have the on value method added once, 
		verify(mockObserver, times(0)).onValue(any(String.class));
	}
}
