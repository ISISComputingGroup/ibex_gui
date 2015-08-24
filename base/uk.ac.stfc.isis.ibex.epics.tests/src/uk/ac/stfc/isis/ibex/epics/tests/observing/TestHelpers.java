package uk.ac.stfc.isis.ibex.epics.tests.observing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;

@SuppressWarnings({ "unchecked" })
public class TestHelpers {
	
	public static final String VALUE = "value";
	public static final String NEW_VALUE = "new_value";
	
	/**
	 * Creates a mock ClosableCachingObservable that returns the object value when the getValue method is called.
	 *  
	 * @param <T> The type of object the observable will watch
	 *  
	 * @param value The value to return from the getValue method
	 * @return A ClosableCachingObservable mock object with a stub method for getValue
	 */
	public static <T> ClosableCachingObservable<T> getClosableCachingObservable(T value) {
		ClosableCachingObservable<T> mockObservableReturnsValue = mock(ClosableCachingObservable.class);
		when(mockObservableReturnsValue.getValue()).thenReturn(value);
		
		return mockObservableReturnsValue;
	}
	
	/**
	 * Creates a mock CachingObservable that returns the object value when the getValue method is called.
	 *  
	 * @param <T> The type of object the observable will watch
	 *  
	 * @param value The value to return from the getValue method
	 * @return A ClosableCachingObservable mock object with a stub method for getValue
	 */
	public static <T> ClosableCachingObservable<T> getCachingObservable(T value) {
		ClosableCachingObservable<T> mockObservableReturnsValue = mock(ClosableCachingObservable.class);
		when(mockObservableReturnsValue.getValue()).thenReturn(value);
		
		return mockObservableReturnsValue;
	}

}
