package uk.ac.stfc.isis.ibex.epics.tests.observing;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.observing.Unsubscriber;

@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class UnsubscriberTest {
	
	private Observer<String> mockObserver;
	
	private TestableObservable<String> testableObservable;
	private InitialiseOnSubscribeObservable<String> observable;
	
	private Unsubscriber<String> subscription;

	@Before
	public void setUp() {
		// Arrange		
		mockObserver = mock(Observer.class);
		
		testableObservable = new TestableObservable<>();
		observable = new InitialiseOnSubscribeObservable<String>(testableObservable);
		
		subscription = (Unsubscriber<String>) observable.addObserver(mockObserver);
	}
	
	@Test
	public void removing_observer_stops_observable_being_updated() {		
		// Act
		subscription.removeObserver();
		testableObservable.setValue(TestHelpers.STRING_VALUE);
		
		// Assert
		verify(mockObserver, times(0)).onValue(TestHelpers.STRING_VALUE);
	}
	
	@Test
	public void removing_observer_twice_does_nothing_the_second_time() {		
		// Act
		subscription.removeObserver();
		subscription.removeObserver();
		testableObservable.setValue(TestHelpers.STRING_VALUE);
		
		// Assert
		verify(mockObserver, times(0)).onValue(TestHelpers.STRING_VALUE);
	}
}
