package uk.ac.stfc.isis.ibex.experimentdetails.internal;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;

public abstract class CollectionObserver<T> extends BaseObserver<Collection<T>>{

	@Override
	public void onValue(Collection<T> value) {
		updateCollection(value);
	}

	@Override
	public void onError(Exception e) {
		clear();
	}

	@Override
	public void onConnectionChanged(boolean isConnected) {
		if (!isConnected) {
			clear();
		}			
	}
	
	private void clear() {
		updateCollection(new ArrayList<T>());
	}
	
	protected abstract void updateCollection(Collection<T> values);
}