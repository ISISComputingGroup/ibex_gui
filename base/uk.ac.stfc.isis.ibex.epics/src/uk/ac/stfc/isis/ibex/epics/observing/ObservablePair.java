package uk.ac.stfc.isis.ibex.epics.observing;

import uk.ac.stfc.isis.ibex.epics.observing.BaseCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;


/**
 * Links together two related observables.
 *
 */
public class ObservablePair<T1, T2> 
				extends BaseCachingObservable<Pair<T1, T2>> 
				implements ClosableCachingObservable<Pair<T1, T2>> {

	private abstract class PairObserver<T> extends BaseObserver<T> {
		@Override
		public void onError(Exception e) {
			setError(e);
		}

		@Override
		public void onConnectionChanged(boolean isConnected) {
			setConnectionChanged(isConnected);
		}	
	}
	
	private final InitialisableObserver<T1> firstObserver = new PairObserver<T1>() {
		@Override
		public void onValue(T1 value) {
			setValue(new Pair<>(value, secondOrNull()));
		}
	};
	
	private final InitialisableObserver<T2> secondObserver = new PairObserver<T2>() {
		@Override
		public void onValue(T2 value) {
			setValue(new Pair<>(firstOrNull(), value));
		}
	};

	private final Subscription firstSubscription;
	private final Subscription secondSubscription;
	
	public ObservablePair(InitialiseOnSubscribeObservable<T1> firstSource, InitialiseOnSubscribeObservable<T2> secondSource) {
		firstSubscription = firstSource.subscribe(firstObserver);
		secondSubscription = secondSource.subscribe(secondObserver);
	}
	
	@Override
	public void close() {
		firstSubscription.cancel();
		secondSubscription.cancel();
	}

	private T1 firstOrNull() {
		Pair<T1, T2> pair = value();
		return pair != null ? pair.first : null;
	}
	
	private T2 secondOrNull() {
		Pair<T1, T2> pair = value();
		return pair != null ? pair.second : null;
	}
}
