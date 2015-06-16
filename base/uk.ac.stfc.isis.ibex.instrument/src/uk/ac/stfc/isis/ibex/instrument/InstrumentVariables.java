package uk.ac.stfc.isis.ibex.instrument;

import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.CachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ConvertingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

public class InstrumentVariables extends Closer {

	private final Channels channels;

	public InstrumentVariables(Channels channels) {
		this.channels = channels;
	}
	
	protected <T> InitialiseOnSubscribeObservable<T> reader(ChannelType<T> type, String address) {
		return autoInitialise(registerForClose(channels.getReader(type, address)));
	}
	
	protected <T> Writable<T> writable(ChannelType<T> channelType, String address) {
		return registerForClose(channels.getWriter(channelType, address));
	}
	
	protected static <S, T> InitialiseOnSubscribeObservable<T> convert(ClosableCachingObservable<S> observable, Converter<S, T> converter) {
		return autoInitialise(new ConvertingObservable<>(observable, converter));
	}
	
	protected static <T> InitialiseOnSubscribeObservable<T> autoInitialise(CachingObservable<T> observable) {
		return new InitialiseOnSubscribeObservable<>(observable);
	}
}
