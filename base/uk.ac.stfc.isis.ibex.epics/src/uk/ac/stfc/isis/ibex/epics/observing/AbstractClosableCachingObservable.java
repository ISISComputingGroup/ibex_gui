package uk.ac.stfc.isis.ibex.epics.observing;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractClosableCachingObservable<T> implements ClosableCachingObservable<T> {

	private final Collection<Observer<T>> observers = new CopyOnWriteArrayList<>();
	private T value;
	private boolean isConnected;
	private Exception lastError;
	
	@Override
	public Subscription addObserver(Observer<T> observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
		
		return new Unsubscriber<Observer<T>>(observers, observer);
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public boolean isConnected() {
		return isConnected;
	}

	@Override
	public Exception lastError() {
		return lastError;
	}
	
	@Override
	public void close()
	{
		// Do nothing
	}
	
	protected void setValue(T value) {
		if (value == null) {
			return;
		}
		
		this.value = value;
		
		for (Observer<T> observer : observers) {
			observer.onValue(value);
		}
	}

	protected void setError(Exception e) {
		lastError = e;
		
		for (Observer<T> observer : observers) {
			observer.onError(e);
		}
	}
	
	protected void setConnectionStatus(boolean isConnected) {
		this.isConnected = isConnected;
		
		for (Observer<T> observer : observers) {
			observer.onConnectionStatus(isConnected);
		}
	}

}
