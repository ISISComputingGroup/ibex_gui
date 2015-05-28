package uk.ac.stfc.isis.ibex.epics.conversion;

import org.apache.commons.codec.binary.Hex;

public class Hexer extends Converter<byte[], char[]> {

	@Override
	public char[] convert(byte[] value) {
		return Hex.encodeHex(value);
	}	
}
