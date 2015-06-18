package uk.ac.stfc.isis.ibex.epics.writing;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.observing.Unsubscriber;

/**
 * An abstract class for defining basic writer classes.
 *
 * @param <TIn> the type of data coming in
 * @param <TOut> the type of data to output
 */
public abstract class BaseWriter<TIn, TOut> implements ConfigurableWriter<TIn, TOut> {
		
	private final Collection<Writable<TOut>> writables = new CopyOnWriteArrayList<>();
		
	private TOut lastWritten;
	private Exception lastError;
	private boolean canWrite;
	
	public TOut lastWritten() {
		return lastWritten;
	}
	
	public Exception lastError() {
		return lastError;
	}	
	
	public boolean canWrite() {
		return canWrite;
	}

	@Override
	public Subscription writeTo(Writable<TOut> writable) {
		if (!writables.contains(writable)) {
			writables.add(writable);
		}
		
		return new Unsubscriber<Writable<TOut>>(writables, writable);
	}

	@Override
	public void onError(Exception e) {
		lastError = e;
	}

	@Override
	public void onCanWriteChanged(boolean canWrite) {		
		this.canWrite = canWrite;
	}
	
	protected void writeToWritables(TOut value) {
		lastWritten = value;
		for (Writable<TOut> writable : writables) {
			writable.write(value);
		}
	}
}
