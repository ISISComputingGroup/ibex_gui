package uk.ac.stfc.isis.ibex.epics.writing;

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;

public abstract class ForwardingWritable<TIn, TOut> extends BaseWritable<TIn> implements ClosableWritable<TIn> {

	private ConfigurableWriter<TIn, TOut> forwardingWriter = new BaseWriter<TIn, TOut>() {
		@Override
		public void write(TIn value) {
			writeToWritables(transform(value));
		}
		
		public void onError(Exception e) {
			error(e);
		};
		
		public void onCanWriteChanged(boolean canWrite) {
			canWriteChanged(canWrite);
		};
	};
	
	private Subscription readingSubscription;
	private Subscription writingSubsciption;
	
	@Override
	public void write(TIn value) {
		if (value != null) {
			forwardingWriter.write(value);
		}
	}
	
	@Override
	public void close() {
		cancelSubscriptions();
	}
	
	protected void setWritable(Writable<TOut> destination) {
		cancelSubscriptions();
		forwardingWriter.onCanWriteChanged(false);
		forwardingWriter.onCanWriteChanged(destination.canWrite());
		
		readingSubscription = destination.subscribe(forwardingWriter);
		writingSubsciption = forwardingWriter.writeTo(destination);
	}
	
	protected abstract TOut transform(TIn value);

	private void cancelSubscriptions() {
		if (readingSubscription != null) {
			readingSubscription.cancel();
		}
		
		if (writingSubsciption != null) {
			writingSubsciption.cancel();
		}
	}
}
