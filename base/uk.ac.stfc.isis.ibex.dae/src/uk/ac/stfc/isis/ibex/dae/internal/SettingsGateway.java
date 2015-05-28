package uk.ac.stfc.isis.ibex.dae.internal;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.epics.writing.BaseWriter;
import uk.ac.stfc.isis.ibex.epics.writing.ConfigurableWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

public abstract class SettingsGateway implements Closable {
	private InitialisableObserver<String> settingsObserver = new BaseObserver<String>() {
		@Override
		public void onValue(String value) {
			setFromText(value);
		}

		@Override
		public void onError(Exception e) {
			
		}

		@Override
		public void onConnectionChanged(boolean isConnected) {
			
		}	
	};
	
	private final Subscription sourceSubscription;
	private final Subscription destinationSubscription;
	private final Subscription writerSubscription;

	private ConfigurableWriter<String, String> updateWriter = new BaseWriter<String, String>() {
		@Override
		public void write(String value) {
			writeToWritables(value);
		}
	};

	public SettingsGateway(InitialiseOnSubscribeObservable<String> settingsSource, Writable<String> settingsDestination) {
		sourceSubscription = settingsSource.subscribe(settingsObserver);
		destinationSubscription = settingsDestination.subscribe(updateWriter);
		writerSubscription = updateWriter.writeTo(settingsDestination);
	}
	
	public void sendUpdate() {
		updateWriter.write(asText());
	}

	protected abstract String asText();
	
	protected abstract void setFromText(String text);
	
	@Override
	public void close() {
		sourceSubscription.cancel();
		destinationSubscription.cancel();
		writerSubscription.cancel();
	}
}
