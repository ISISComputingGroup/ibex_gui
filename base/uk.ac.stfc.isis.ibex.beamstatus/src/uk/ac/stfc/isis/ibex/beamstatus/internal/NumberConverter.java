package uk.ac.stfc.isis.ibex.beamstatus.internal;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

public class NumberConverter extends Converter<Number, String>{

	@Override
	public String convert(Number value) throws ConversionException {
		if (value == null) {
			return "UNKNOWN";
		}
		
		return value.toString();
	}
}
