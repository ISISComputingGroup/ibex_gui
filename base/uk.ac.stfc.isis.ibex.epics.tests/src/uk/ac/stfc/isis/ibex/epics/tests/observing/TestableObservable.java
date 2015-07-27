package uk.ac.stfc.isis.ibex.epics.tests.observing;

import uk.ac.stfc.isis.ibex.epics.observing.BaseCachingObservable;

/**
 * Observable to be used for testing, allows access to setValue, setError and setConnectionChanged methods.
 * 
 * This is final, so no mocking this!
 */
final class TestableObservable<T> extends BaseCachingObservable<T> {
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
