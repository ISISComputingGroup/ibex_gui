package uk.ac.stfc.isis.ibex.configserver.pv;

import java.util.Locale;

import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.NumberWithUnitsChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

public class ChannelTypeLookup {

	public static ChannelType<String> get(String type) {
		switch (type.toUpperCase(Locale.ENGLISH)) {
		case "AI":
			return new NumberWithUnitsChannel();
		case "STRINGIN":
		case "STRINGOUT":
			return new StringChannel();
		default:
			return new DefaultChannel();
		}
	}
}
