package uk.ac.stfc.isis.ibex.beamstatus.internal;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

public class EnumConverter<E extends Enum<E>> extends Converter<E, String> {
	@Override
	public String convert(E value) throws ConversionException {
		if (value == null) {
			return "UNKNOWN";
		}
		
		return value.toString();
	}
}
