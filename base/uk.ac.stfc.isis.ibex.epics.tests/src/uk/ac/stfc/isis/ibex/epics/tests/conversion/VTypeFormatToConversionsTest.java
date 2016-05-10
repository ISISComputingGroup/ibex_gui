
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

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.widgets.Display;
import org.junit.Test;
import org.omg.CORBA.portable.ValueFactory;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.conversion.VTypeFormat;

/**
 * This class is responsible for testing the various to conversions 
 *
 */
@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename", "checkstyle:methodname" })
public class VTypeFormatToConversionsTest {

	// Byte array values missing as unable to produce from ValueFactory

	/**
	 * An Enum class to test with.
	 */
	private static final Class<TestingEnum> ENUM_TYPE = TestingEnum.class;
	
	@Test
	public void convert_to_number() throws ConversionException {
		// Arrange
		Converter<VNumber, Number> converter = VTypeFormat.toNumber();
		VDouble vnum = ValueFactory.newVDouble(123.456);
		
		// Act
		Number result = converter.convert(vnum);
		
		// Assert
		assertEquals(result, 123.456);
	}
	
	@Test
	public void convert_to_number_with_precision() throws ConversionException {
		// Arrange
		Converter<VNumber, Number> converter = VTypeFormat.toNumber();
		
		Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "", NumberFormats.format(1), 0.0, 0.0, 0.0, 0.0, 0.0);
		
		VDouble vnum = ValueFactory.newVDouble(123.456, display);
		
		// Act
		Number result = converter.convert(vnum);
		
		// Assert
		// Note: Number here should not be altered as there is no consideration of precision in toNumber
		assertEquals(result, 123.456);
	}
	
	@Test
	public void convert_to_number_with_precision_greater_than_digits() throws ConversionException {
		// Arrange
		Converter<VNumber, Number> converter = VTypeFormat.toNumber();
		
		Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "", NumberFormats.format(5), 0.0, 0.0, 0.0, 0.0, 0.0);
		
		VDouble vnum = ValueFactory.newVDouble(123.4, display);
		
		// Act
		Number result = converter.convert(vnum);
		
		// Assert
		assertEquals(result, 123.4);
	}
	
	@Test
	public void conversion_no_precision() throws ConversionException {
		// Arrange
		Converter<VNumber, Number> converter = VTypeFormat.toNumberWithPrecision();
		VDouble vnum = ValueFactory.newVDouble(123.456);
		
		// Act
		Number result = converter.convert(vnum);
		
		// Assert
		assertEquals(result, 123.456);
	}
	
	@Test
	public void conversion_with_precision() throws ConversionException {
		// Arrange
		Converter<VNumber, Number> converter = VTypeFormat.toNumberWithPrecision();
		
		Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "", NumberFormats.format(1), 0.0, 0.0, 0.0, 0.0, 0.0);
		
		VDouble vnum = ValueFactory.newVDouble(123.456, display);
		
		// Act
		Number result = converter.convert(vnum);
		
		// Assert
		assertEquals(result, 123.5);
	}
	
	@Test
	public void conversion_with_precision_greater_than_digits() throws ConversionException {
		// Arrange
		Converter<VNumber, Number> converter = VTypeFormat.toNumberWithPrecision();
		
		Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "", NumberFormats.format(5), 0.0, 0.0, 0.0, 0.0, 0.0);
		
		VDouble vnum = ValueFactory.newVDouble(123.4, display);
		
		// Act
		Number result = converter.convert(vnum);
		
		// Assert
		assertEquals(result, 123.4);
	}
	
	@Test
	public void convert_to_vfloat_array() throws ConversionException {
		// Arrange
		Converter<VType, VFloatArray> converter = VTypeFormat.toVFloatArray();
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
		VFloatArray test = ValueFactory.newVFloatArray(data, null, null, null);
		
		// Act
		VFloatArray result = converter.convert(test);
		
		// Assert
		assertEquals(result, test);
	}

	@Test
	public void convert_to_vbyte_array() throws ConversionException {
		// Arrange
		Converter<VType, VByteArray> converter = VTypeFormat.toVByteArray();
		ListByte data = new ListByte() {
			@Override
			public int size() {
				return 0;
			}

			@Override
			public byte getByte(int index) {
				return 0;
			}
		};
		VByteArray test = (VByteArray) ValueFactory.newVNumberArray(data, null, null, null);
		
		// Act
		VByteArray result = converter.convert(test);
		
		// Assert
		assertEquals(result, test);
	}	
	
	@Test
	public void convert_to_vint() throws ConversionException {
		// Arrange
		Converter<VType, VInt> converter = VTypeFormat.toVInt();
		Integer test = 123;
		VInt value = ValueFactory.newVInt(test, null, null, null);
		
		// Act
		VInt result = converter.convert(value);
		
		// Assert
		assertEquals(result, value);
	}	

	@Test
	public void convert_to_long() throws ConversionException {
		// Arrange
		Converter<VInt, Long> converter = VTypeFormat.toLong();
		Integer test = 123;
		VInt value = ValueFactory.newVInt(test, null, null, null);
		
		// Act
		Long result = converter.convert(value);
		
		// Assert
		Long check = (long) test;
		assertEquals(result, check);
	}
	
	@Test
	public void convert_to_enum() throws ConversionException {
		
		// Arrange
		Converter<VEnum, TestingEnum> converter = VTypeFormat.toEnum(ENUM_TYPE);			
		VEnum value = ValueFactory.newVEnum(0, TestingEnum.TEST1.getNames(), null, null);
		
		// Act
		TestingEnum result = converter.convert(value);
		
		// Assert
		assertEquals(result, TestingEnum.TEST1);
	}

	@Test
	public void convert_to_enumString() throws ConversionException {
		
		// Arrange
		Converter<VEnum, String> converter = VTypeFormat.toEnumString();				
		VEnum value = ValueFactory.newVEnum(0, TestingEnum.TEST1.getNames(), null, null);
		
		// Act
		String result = converter.convert(value);
		
		// Assert
		assertEquals(result, "TEST1");
	}
}
