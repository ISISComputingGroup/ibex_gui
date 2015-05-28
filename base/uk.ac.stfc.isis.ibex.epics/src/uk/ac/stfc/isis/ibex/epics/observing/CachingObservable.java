package uk.ac.stfc.isis.ibex.epics.observing;

public interface CachingObservable<T> extends Observable<T> {
	T value();
	
	boolean isConnected();
	
	Exception lastError();
}
