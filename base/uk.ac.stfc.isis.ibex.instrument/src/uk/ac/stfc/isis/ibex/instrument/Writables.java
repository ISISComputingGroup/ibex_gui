package uk.ac.stfc.isis.ibex.instrument;

import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

public class Writables extends Closer {
	private final Channels channels;

	public Writables(Channels channels) {
		this.channels = channels;
	}
	
	protected <T> Writable<T> writable(ChannelType<T> channelType, String address) {
		return registerForClose(channels.getWriter(channelType, address));
	}
}
