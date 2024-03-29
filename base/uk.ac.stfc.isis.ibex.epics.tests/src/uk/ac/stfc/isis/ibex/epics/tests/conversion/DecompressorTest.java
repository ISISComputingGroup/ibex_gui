package uk.ac.stfc.isis.ibex.epics.tests.conversion;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Decompressor;

/**
 * Tests for the Decompressor.
 *
 */
@SuppressWarnings({ "checkstyle:methodname", "checkstyle:magicnumber" })
public class DecompressorTest {
	
    @Test
    public void decompress_to_string() throws ConversionException,
	    UnsupportedEncodingException {
	// Arrange
	Decompressor dcmprssr = new Decompressor();
	String answer = "Hello, World";
	byte[] compressed = new byte[] {120, -100, -13, 72, -51, -55, -55,
		-41, 81, 8, -49, 47, -54, 73, 1, 0, 27, 52, 4, 73 };

	// Act
	byte[] result = dcmprssr.apply(compressed);
	String ans = new String(result, "UTF-8");

	// Assert
	assertEquals(ans, answer);
    }
}
