package uk.ac.stfc.isis.ibex.epics.observing;

public abstract class ObserverAdapter<T> implements Observer<T>  {
	@Override
    public void onValue(T value) {
	}
    
	@Override
    public void onError(Exception e) {
	}

	@Override
	public void onConnectionChanged(boolean isConnected) {
		
	}
}
