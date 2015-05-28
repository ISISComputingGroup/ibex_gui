package uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels;

import uk.ac.stfc.isis.ibex.dae.internal.SettingsGateway;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

public class ObservingTimeChannels extends XmlBackedTimeChannels implements Closable {
	private final SettingsGateway gateway;

	public ObservingTimeChannels(InitialiseOnSubscribeObservable<String> settingsSource, Writable<String> settingsDestination) {

		gateway = new SettingsGateway(settingsSource, settingsDestination) {	
			@Override
			protected void setFromText(String text) {
				setXml(text);
			}
			
			@Override
			protected String asText() {
				return xml();
			}
		};
	}
	
	public void sendUpdate() {
		gateway.sendUpdate();
	}

	@Override
	public void close() {
		gateway.close();
	}
}
