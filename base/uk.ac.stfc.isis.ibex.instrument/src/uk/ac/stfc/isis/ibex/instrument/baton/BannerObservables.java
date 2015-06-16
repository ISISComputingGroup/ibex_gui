package uk.ac.stfc.isis.ibex.instrument.baton;

import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;
import uk.ac.stfc.isis.ibex.instrument.internal.MachineName;
import uk.ac.stfc.isis.ibex.instrument.internal.UserName;

public class BannerObservables extends InstrumentVariables {
	public final Writable<String> requestPV = writable(new StringChannel(), "CS:CONTROL:REQUEST");
	public final InitialiseOnSubscribeObservable<String> controlPV = reader(new StringChannel(), "CS:CONTROL");
	public final String self = UserName.get() + "@" + MachineName.get();
	
	public BannerObservables(Channels channels) {
		super(channels);
	}
	
	public void sendRequest() {
		requestPV.write(self);
	}
	
}
