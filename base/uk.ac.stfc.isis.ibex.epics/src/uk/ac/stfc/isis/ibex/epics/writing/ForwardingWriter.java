package uk.ac.stfc.isis.ibex.epics.writing;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;

public abstract class ForwardingWriter<TIn, TOut> implements ConfigurableWriter<TIn, TOut>, Closable {

	private ConfigurableWriter<TIn, TOut> writer;
	private Collection<Subscription> subscriptions = new ArrayList<>();
	
	protected void setWriter(ConfigurableWriter<TIn, TOut> writer) {
		this.writer = writer;
	}
	
	@Override
	public void write(TIn value) {
		writer.write(value);
	}

	@Override
	public boolean canWrite() {
		return writer.canWrite();
	}
	
	@Override
	public Subscription writeTo(Writable<TOut> writable) {
		Subscription writerSubscription = writer.writeTo(writable);
		subscriptions.add(writerSubscription);
		
		return writerSubscription;
	};

	@Override
	public void close() {
		for (Subscription subscription : subscriptions) {
			subscription.cancel();
		}
	}
}
