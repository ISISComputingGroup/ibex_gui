package uk.ac.stfc.isis.ibex.epics.conversion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class Decompressor extends Converter<byte[], byte[]> {
	
	@Override
	public byte[] convert(byte[] value) throws ConversionException {
		final Inflater decompressor = new Inflater();
		try {
			return decompress(value, decompressor);
		} catch (IOException | DataFormatException e) {
			throw new ConversionException(e.getMessage());
		} finally {
			decompressor.end();
		}			   
	}
	
	private byte[] decompress(byte[] value, Inflater decompressor) throws DataFormatException, IOException {
		decompressor.setInput(value);

		byte[] buffer = new byte[1024];  
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(value.length); 
		while (notFinishedOrStarvedOfInput(decompressor)) {
			int count = decompressor.inflate(buffer);  
			outputStream.write(buffer, 0, count);  		
		}  
		
		outputStream.close();
		
		return decompressor.finished() ? outputStream.toByteArray() :  new byte[1];
	}
	
	private boolean notFinishedOrStarvedOfInput(Inflater decompressor) {
		return !decompressor.finished() && !decompressor.needsInput();
	}
}	