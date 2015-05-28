package uk.ac.stfc.isis.ibex.epics.observing;


/**
 * An observable whose source is another observable.
 *
 */
public class ForwardingObservable<T> extends BaseCachingObservable<T> implements ClosableCachingObservable<T> {
		
	private final BaseObserver<T> sourceObserver = new BaseObserver<T>() {
		@Override
		public void onValue(T value) {
			setValue(value);
		}

		@Override
		public void onError(Exception e) {
			setError(e);
		}

		@Override
		public void onConnectionChanged(boolean isConnected) {
			setConnectionChanged(isConnected);
		}
	};
	
	private Subscription sourceSubscription;

	protected synchronized void setSource(CachingObservable<T> newSource) {
		cancelSubscription();	
		sourceObserver.onConnectionChanged(false);
		
		sourceObserver.onConnectionChanged(newSource.isConnected());
		
		Exception error = newSource.lastError();
		if (error != null) {
			sourceObserver.onError(error);
		}
		
		T value = newSource.value();
		if (value != null) {
			sourceObserver.onValue(value);
		}
		
		sourceSubscription = newSource.subscribe(sourceObserver);
	}
	
	@Override
	public void close() {
		cancelSubscription();
	}
	
	private void cancelSubscription() {
		if (sourceSubscription != null) {
			sourceSubscription.cancel();
		}
	}
}
