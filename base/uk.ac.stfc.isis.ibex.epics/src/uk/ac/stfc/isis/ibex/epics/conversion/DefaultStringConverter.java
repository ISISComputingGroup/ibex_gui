package uk.ac.stfc.isis.ibex.epics.conversion;

public class DefaultStringConverter extends Converter<String, Object>{

	@Override
	public Object convert(String value) throws ConversionException {		
		return value;
	}
}
