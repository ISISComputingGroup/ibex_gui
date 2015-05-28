package uk.ac.stfc.isis.ibex.epics.conversion;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class Dehexer extends Converter<char[], byte[]> {
	
	@Override
	public byte[] convert(char[] value) throws ConversionException {
		try {			
			return Hex.decodeHex(value);
		} catch (DecoderException e) {
			throw new ConversionException(e.getMessage());
		}
	}
}
