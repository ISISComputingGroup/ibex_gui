package uk.ac.stfc.isis.ibex.ui.graphing.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * InputStream taking it's source from a ByteBuffer, without copying entire array.
 */
public class ByteBufferInputStream extends InputStream {
	
	private final ByteBuffer buffer;
	
	public ByteBufferInputStream(ByteBuffer buffer) {
		this.buffer = buffer;
	}
    
	/**
	 * Read the number of remaining bytes in the buffer.
	 */
	@Override
    public int available() {
    	return buffer.remaining();
    }
	
	@Override
	public boolean markSupported() {
		return true;
	}
	
	@Override
	public void mark(int readlimit) {
		buffer.mark();
	}
	
	@Override
	public void reset() {
		buffer.reset();
	}
	
	/**
	 * Read a single byte from the buffer.
	 */
	@Override
	public int read() throws IOException {
		if (!buffer.hasRemaining()) {
			return -1;
		}
		return Byte.toUnsignedInt(buffer.get());
	}
	
	/**
	 * Read up to len bytes from the buffer into the provided
	 * byte array, starting at offset off
	 */
	@Override
	public int read(byte[] b, int off, int len) {
		if (!buffer.hasRemaining()) {
			return -1;
		}
		len = Math.min(len, buffer.remaining());
		buffer.get(b, off, len);
		return len;
	}

}