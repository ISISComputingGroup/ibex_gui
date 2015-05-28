package uk.ac.stfc.isis.ibex.epics.conversion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;;

public class Compressor extends Converter<byte[], byte[]> {
		
	@Override
	public byte[] convert(byte[] value) throws ConversionException {
		final Deflater compressor = new Deflater();
		compressor.setInput(value);
		compressor.finish();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(value.length); 
		
		byte[] buffer = new byte[1024];  
		while (!compressor.finished()) {  
			int count = compressor.deflate(buffer);  
			outputStream.write(buffer, 0, count);  
		}  
		
		compressor.end();
		try {
			outputStream.close();
		} catch (IOException e) {
			throw new ConversionException(e.getMessage());
		}
				   
		return outputStream.toByteArray();
	}	
}	