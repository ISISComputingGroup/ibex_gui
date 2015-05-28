package uk.ac.stfc.isis.ibex.epics.observing;

public interface InitialisableObserver<T> extends Observer<T> {
	void update(T value, Exception error, boolean isConnected);
}
