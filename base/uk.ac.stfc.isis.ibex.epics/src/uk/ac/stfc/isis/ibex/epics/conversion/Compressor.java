
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