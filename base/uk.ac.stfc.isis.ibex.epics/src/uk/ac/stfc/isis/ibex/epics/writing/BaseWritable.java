package uk.ac.stfc.isis.ibex.epics.writing;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.observing.Unsubscriber;

public abstract class BaseWritable<T> implements Writable<T> {

	private final Collection<ConfigurableWriter<?, ?>> writers = new CopyOnWriteArrayList<>();

	private boolean canWrite;
	private Exception lastError;
	
	@Override
	public Subscription subscribe(ConfigurableWriter<?, ?> writer) {
		writer.onCanWriteChanged(canWrite);
		writer.onError(lastError);
		
		if (!writers.contains(writer)) {
			writers.add(writer);
		}
		
		return new Unsubscriber<ConfigurableWriter<?, ?>>(writers, writer);
	}
	
	public boolean canWrite() {
		return canWrite;
	}
	
	public Exception lastError() {
		return lastError;
	}
	
	protected void error(Exception e) {
		lastError = e;
		for (ConfigurableWriter<?, ?> writer : writers) {
			writer.onError(e);
		}
	}
	
	protected void canWriteChanged(boolean canWrite) {
		this.canWrite = canWrite;
		for (ConfigurableWriter<?, ?> writer : writers) {
			writer.onCanWriteChanged(canWrite);
		}
	}
}
