package uk.ac.stfc.isis.ibex.epics.writing;

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;

public class Writers<T> extends BaseWriter<T, T> implements Closable {
	
	private Subscription destinationSubscription;
	
	private Writers(Writable<T> destination) {
		writeTo(destination);
		destinationSubscription = destination.subscribe(this);
	}
	
	public static <T> Writers<T> forDestination(Writable<T> destination) {
		return new Writers<T>(destination);
	}

	@Override
	public void write(T value) {
		writeToWritables(value);
	}
	
	@Override
	public void close() {
		destinationSubscription.cancel();
	}
}
