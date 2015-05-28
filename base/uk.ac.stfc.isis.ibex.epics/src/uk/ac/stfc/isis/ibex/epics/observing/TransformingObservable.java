package uk.ac.stfc.isis.ibex.epics.observing;


/**
 * The abstract base class for classes that transform the observed value into something else.
 * For example: changing an enum into a string
 *
 */
public abstract class TransformingObservable<T1, T2> extends BaseCachingObservable<T2> implements ClosableCachingObservable<T2> {

	private ClosableCachingObservable<T1> source;
	private Subscription sourceSubscription;

	private final BaseObserver<T1> sourceObserver = new BaseObserver<T1>() {
		@Override
		public void onValue(T1 value) {
			setValue(transform(value));
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

	public final void setSource(ClosableCachingObservable<T1> source) {
		cancelSubscription();
		this.source = source;
		sourceObserver.update(source.value(), source.lastError(), source.isConnected());
		sourceSubscription = source.subscribe(sourceObserver);
	}
	
	protected abstract T2 transform(T1 value);

	@Override
	public void close() {
		cancelSubscription();
		source.close();
	}
	
	private void cancelSubscription() {
		if (sourceSubscription != null) {
			sourceSubscription.cancel();
		}
	}	
}
