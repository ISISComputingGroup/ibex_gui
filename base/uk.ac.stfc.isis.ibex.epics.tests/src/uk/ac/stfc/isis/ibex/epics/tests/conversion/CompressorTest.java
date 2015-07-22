
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

package uk.ac.stfc.isis.ibex.epics.tests.conversion;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.conversion.Compressor;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;

public class CompressorTest {
	
	@Test
	public void compress_string() throws ConversionException {
		// Arrange
		Compressor cmprssr = new Compressor();
		String text = "Hello, World";
		byte[] answer = new byte[] {120, -100, -13, 72, -51, -55, -55, -41, 81, 8, -49, 47, -54, 73, 1, 0, 27, 52, 4, 73};
		
		// Act
		byte[] result = cmprssr.convert(text.getBytes());
		
		// Assert
		assertArrayEquals(result, answer);
	}
}
