
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

package uk.ac.stfc.isis.ibex.epics.conversion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class Decompressor extends Converter<byte[], byte[]> {
	
    private static final int KB_IN_BYTES = 1024;

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

        byte[] buffer = new byte[KB_IN_BYTES];
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