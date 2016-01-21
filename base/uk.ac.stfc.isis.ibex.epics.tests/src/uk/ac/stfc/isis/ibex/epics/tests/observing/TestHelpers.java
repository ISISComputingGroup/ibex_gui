package uk.ac.stfc.isis.ibex.epics.tests.observing;

import static org.mockito.Mockito.*;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;

@SuppressWarnings({ "unchecked" })
public final class TestHelpers {
	
	public static final String STRING_VALUE = "value";
	public static final String NEW_STRING_VALUE = "new_value";
	public static final Integer INT_VALUE = 123;
	public static final Integer NEW_INT_VALUE = 789;
	
	public static final Exception EXCEPTION = new Exception();
	
    private TestHelpers() {
    }

	/**
	 * Creates a mock ClosableCachingObservable that returns the object value when the getValue method is called.
	 *  
	 * @param <T> The type of object the observable will watch
	 *  
	 * @param value The value to return from the getValue method
	 * @return A CachingObservable mock object with a stub method for getValue
	 */
	public static <T> ClosableObservable<T> getClosableCachingObservable(T value) {
		ClosableObservable<T> mockObservableReturnsValue = mock(ClosableObservable.class);
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
	public static <T> ClosableObservable<T> getCachingObservable(T value) {
		ClosableObservable<T> mockObservableReturnsValue = mock(ClosableObservable.class);
		when(mockObservableReturnsValue.getValue()).thenReturn(value);
		
		return mockObservableReturnsValue;
	}
}
