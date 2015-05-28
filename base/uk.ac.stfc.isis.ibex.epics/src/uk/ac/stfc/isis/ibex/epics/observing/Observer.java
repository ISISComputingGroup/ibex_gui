package uk.ac.stfc.isis.ibex.epics.observing;

/**
 * The observer interface.
 *
 */
public interface Observer<T> {
	
    void onValue(T value);
    
    void onError(Exception e);
    
    void onConnectionChanged(boolean isConnected);    
}
