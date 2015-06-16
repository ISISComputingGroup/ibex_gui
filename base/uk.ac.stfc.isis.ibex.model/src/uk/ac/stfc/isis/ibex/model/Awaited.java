package uk.ac.stfc.isis.ibex.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Awaited<T> extends UpdatedValue<T> {
	
	private final UpdatedValue<T> value;
	private final CountDownLatch latch = new CountDownLatch(1);

	private final PropertyChangeListener set = new PropertyChangeListener() {	
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			setValue(value.getValue());
			latch.countDown();
		}
	};
	
	public Awaited(final UpdatedValue<T> value) {
		this.value = value;
		if (value.isSet()) {
			setValue(value.getValue());
		} else {
			value.addPropertyChangeListener(set);
		}
	}
	
	public static <T> boolean returnedValue(UpdatedValue<T> value, int secondsToWait) {
		return new Awaited<>(value).until(secondsToWait);
	}
	
	/*
	 * Return true if the value was set; otherwise false.
	 */
	public boolean until(int secondsToWait) {
		startCountdown(secondsToWait);
		return isSet();
	}
	
	@Override
	public boolean isSet() {
		return value.isSet();
	}

	private void startCountdown(int secondsToWait) {
		try {
			latch.await(secondsToWait, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			value.removePropertyChangeListener(set);
		}
	}
}
