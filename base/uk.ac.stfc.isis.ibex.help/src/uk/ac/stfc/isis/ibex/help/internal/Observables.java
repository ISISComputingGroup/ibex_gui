package uk.ac.stfc.isis.ibex.help.internal;

import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

public class Observables extends InstrumentVariables {

	public final InitialiseOnSubscribeObservable<String> revision = reader(new StringChannel(), "CS:VERSION:SVN:REV");
	public final InitialiseOnSubscribeObservable<String> date = reader(new StringChannel(), "CS:VERSION:SVN:DATE");

	public Observables(Channels channels) {
		super(channels);
	}
}
