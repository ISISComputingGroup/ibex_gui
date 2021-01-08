package uk.ac.stfc.isis.ibex.synoptic.tests.internal;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;

/**
 * Observable to be used for testing, allows access to setValue, setError and setConnectionChanged methods.
 * 
 * This is final, so no mocking this, or using it outside testing!
 */
final class TestableObservable<T> extends ClosableObservable<T> {
	@Override
	public void setValue(T value) {
		super.setValue(value);
	}
}
