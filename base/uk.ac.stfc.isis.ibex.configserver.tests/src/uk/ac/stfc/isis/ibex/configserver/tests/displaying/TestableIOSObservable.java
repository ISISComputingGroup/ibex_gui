package uk.ac.stfc.isis.ibex.configserver.tests.displaying;

import uk.ac.stfc.isis.ibex.epics.observing.CachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;

/**
 * Observable to be used for testing, allows access to setValue, setError and setConnectionChanged methods.
 * 
 * This is final, so no mocking this, or using it outside testing!
 */
final class TestableIOSObservable<T> extends InitialiseOnSubscribeObservable<T> {
	public TestableIOSObservable(CachingObservable<T> source) {
		super(source);
	}

	@Override
	public void setValue(T value) {
		super.setValue(value);
	}

	@Override
	public void setError(Exception e) {
		super.setError(e);
	}
	
	@Override
	public void setConnectionStatus(boolean isConnected) {
		super.setConnectionStatus(isConnected);
	}
};
