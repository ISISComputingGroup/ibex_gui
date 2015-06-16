package uk.ac.stfc.isis.ibex.epics.writing;

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.model.SetCommand;

public class WritingSetCommand<T> extends SetCommand<T> implements Closable {
		
	private final Subscription destinationSubscription;
	private final Subscription writerSubscription;	
	
	private final BaseWriter<T, T> destinationWriter = new BaseWriter<T, T>() {
		@Override
		public void onCanWriteChanged(boolean canWrite) {
			setCanSend(canWrite);
		}

		@Override
		public void write(T value) {
			writeToWritables(value);
		}
		
	};
	
	private WritingSetCommand(Writable<T> destination) {
		writerSubscription = destinationWriter.writeTo(destination);
		destinationSubscription = destination.subscribe(destinationWriter);
	}
	
	public static <T> WritingSetCommand<T> forDestination(Writable<T> destination) {
		return new WritingSetCommand<T>(destination);
	}
	
	@Override
	public void send(T value) {
		destinationWriter.write(value);;
	}
	
	@Override
	public void close() {
		writerSubscription.cancel();
		destinationSubscription.cancel();
	}
}
