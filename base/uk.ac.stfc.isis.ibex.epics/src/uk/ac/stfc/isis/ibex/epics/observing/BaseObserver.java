package uk.ac.stfc.isis.ibex.epics.observing;

public abstract class BaseObserver<T> implements InitialisableObserver<T> {
	
	@Override
	public void update(T value, Exception error, boolean isConnected) {		
		onConnectionChanged(isConnected);
		
		if (error != null) {
			onError(error);
			return;
		}
		
		if (isConnected && value != null) {
			onValue(value);			
		}	
	}
}
