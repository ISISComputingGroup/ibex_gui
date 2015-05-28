package uk.ac.stfc.isis.ibex.dae.updatesettings;

import uk.ac.stfc.isis.ibex.dae.internal.SettingsGateway;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

public class ObservingUpdateSettings extends XMLBackedUpdateSettings implements Closable {
	
	private final SettingsGateway gateway;

	public ObservingUpdateSettings(InitialiseOnSubscribeObservable<String> settingsSource, Writable<String> settingsDestination) {

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
