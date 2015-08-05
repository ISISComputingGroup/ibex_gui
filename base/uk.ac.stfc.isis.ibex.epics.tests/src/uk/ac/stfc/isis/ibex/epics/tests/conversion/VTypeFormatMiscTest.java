
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

import org.epics.vtype.VEnum;
import org.epics.vtype.VFloatArray;
import org.epics.vtype.ValueFactory;
import org.epics.util.array.ListFloat;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.conversion.VTypeFormat;

/**
 * This class is responsible for ... 
 *
 */
public class VTypeFormatMiscTest {

	// Byte array values missing as unable to produce from ValueFactory
	
	@Test
	public void convert_enum_value() throws ConversionException {
		// Arrange
		Converter<VEnum, String> converter = VTypeFormat.enumValue();
				
		VEnum value = ValueFactory.newVEnum(0, TestingEnum.TEST1.getNames(), null, null);
		
		// Act
		String result = converter.convert(value);
		
		// Assert
		assertEquals(result, TestingEnum.TEST1.toString());
	}

	@Test
	public void convert_extract_floats() throws ConversionException {
		// Arrange
		Converter<VFloatArray, float[]> converter = VTypeFormat.extractFloats();
		
		ListFloat data = new ListFloat() {
			@Override
			public int size() {
				return 0;
			}
			@Override
			public float getFloat(int index) {
				return 0;
			}
		};
		VFloatArray value = ValueFactory.newVFloatArray(data, null, null, null);
		float[] test = new float[] {};
		
		// Act
		float[] result = converter.convert(value);

		// Assert
		assertArrayEquals(result, test, 0);
	}

}
