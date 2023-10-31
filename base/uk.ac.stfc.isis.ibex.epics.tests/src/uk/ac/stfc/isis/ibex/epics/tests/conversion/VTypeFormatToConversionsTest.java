
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2023 Science & Technology Facilities Council.
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
import static org.junit.Assert.assertNotEquals;

import java.text.NumberFormat;
import java.util.function.Function;

import org.diirt.util.array.ListByte;
import org.diirt.util.array.ListFloat;
import org.diirt.util.text.NumberFormats;
import org.diirt.vtype.Display;
import org.diirt.vtype.VByteArray;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VEnum;
import org.diirt.vtype.VFloatArray;
import org.diirt.vtype.VInt;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VType;
import org.diirt.vtype.ValueFactory;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.ExponentialOnThresholdFormat;
import uk.ac.stfc.isis.ibex.epics.conversion.VTypeFormat;

/**
 * This class is responsible for testing the various to conversions.
 */
public class VTypeFormatToConversionsTest {

	// Byte array values missing as unable to produce from ValueFactory

	/**
	 * An Enum class to test with.
	 */
	private static final Class<TestingEnum> ENUM_TYPE = TestingEnum.class;
	
	@Test
	public void convert_to_number() throws ConversionException {
		// Arrange
		Function<VNumber, Number> converter = VTypeFormat.toNumber();
		VDouble vnum = ValueFactory.newVDouble(123.456);
		
		// Act
		Number result = converter.apply(vnum);
		
		// Assert
		assertEquals(result, 123.456);
	}
	
	@Test
	public void convert_to_number_with_precision() throws ConversionException {
		// Arrange
		Function<VNumber, Number> converter = VTypeFormat.toNumber();
		
		Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "", NumberFormats.format(1), 0.0, 0.0, 0.0, 0.0, 0.0);
		
		VDouble vnum = ValueFactory.newVDouble(123.456, display);
		
		// Act
		Number result = converter.apply(vnum);
		
		// Assert
		// Note: Number here should not be altered as there is no consideration of precision in toNumber
		assertEquals(result, 123.456);
	}
	
	@Test
	public void convert_to_number_with_precision_greater_than_digits() throws ConversionException {
		// Arrange
		Function<VNumber, Number> converter = VTypeFormat.toNumber();
		
		Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "", NumberFormats.format(5), 0.0, 0.0, 0.0, 0.0, 0.0);
		
		VDouble vnum = ValueFactory.newVDouble(123.4, display);
		
		// Act
		Number result = converter.apply(vnum);
		
		// Assert
		assertEquals(result, 123.4);
	}
	
	@Test
	public void conversion_no_precision() throws ConversionException {
		// Arrange
		Function<VNumber, Number> converter = VTypeFormat.toNumberWithPrecision();
		VDouble vnum = ValueFactory.newVDouble(123.456);
		
		// Act
		Number result = converter.apply(vnum);
		
		// Assert
		assertEquals(result, 123.456);
	}
	
	@Test
	public void conversion_with_precision() throws ConversionException {
		// Arrange
		Function<VNumber, Number> converter = VTypeFormat.toNumberWithPrecision();
		
		Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "", NumberFormats.format(1), 0.0, 0.0, 0.0, 0.0, 0.0);
		
		VDouble vnum = ValueFactory.newVDouble(123.456, display);
		
		// Act
		Number result = converter.apply(vnum);
		
		// Assert
		assertEquals(result, 123.5);
	}
	
	@Test
	public void conversion_with_precision_greater_than_digits() throws ConversionException {
		// Arrange
		Function<VNumber, Number> converter = VTypeFormat.toNumberWithPrecision();
		
		Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "", NumberFormats.format(5), 0.0, 0.0, 0.0, 0.0, 0.0);
		
		VDouble vnum = ValueFactory.newVDouble(123.4, display);
		
		// Act
		Number result = converter.apply(vnum);
		
		// Assert
		assertEquals(result, 123.4);
	}
	
	@Test
	public void convert_to_vfloat_array() throws ConversionException {
		// Arrange
		Function<VType, VFloatArray> converter = VTypeFormat.toVFloatArray();
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
		VFloatArray result = converter.apply(test);
		
		// Assert
		assertEquals(result, test);
	}

	@Test
	public void convert_to_vbyte_array() throws ConversionException {
		// Arrange
		Function<VType, VByteArray> converter = VTypeFormat.toVByteArray();
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
		VByteArray result = converter.apply(test);
		
		// Assert
		assertEquals(result, test);
	}	
	
	@Test
	public void convert_to_vint() throws ConversionException {
		// Arrange
		Function<VType, VInt> converter = VTypeFormat.toVInt();
		Integer test = 123;
		VInt value = ValueFactory.newVInt(test, null, null, null);
		
		// Act
		VInt result = converter.apply(value);
		
		// Assert
		assertEquals(result, value);
	}	

	@Test
	public void convert_to_long() throws ConversionException {
		// Arrange
		Function<VInt, Long> converter = VTypeFormat.toLong();
		Integer test = 123;
		VInt value = ValueFactory.newVInt(test, null, null, null);
		
		// Act
		Long result = converter.apply(value);
		
		// Assert
		Long check = (long) test;
		assertEquals(result, check);
	}
	
	@Test
	public void convert_to_enum() throws ConversionException {
		
		// Arrange
		Function<VEnum, TestingEnum> converter = VTypeFormat.toEnum(ENUM_TYPE);			
		VEnum value = ValueFactory.newVEnum(0, TestingEnum.TEST1.getNames(), null, null);
		
		// Act
		TestingEnum result = converter.apply(value);
		
		// Assert
		assertEquals(result, TestingEnum.TEST1);
	}

	@Test
	public void convert_to_enumString() throws ConversionException {
		
		// Arrange
		Function<VEnum, String> converter = VTypeFormat.toEnumString();				
		VEnum value = ValueFactory.newVEnum(0, TestingEnum.TEST1.getNames(), null, null);
		
		// Act
		String result = converter.apply(value);
		
		// Assert
		assertEquals(result, "TEST1");
	}
	
	/**
	 * Performs multiple tests to check the following:
	 * 1. If the number has additional trailing zeros due to precision - zeroes aren't padded
	 * 2. If the number has non-zero significant digits after decimal - they are retained
	 * @throws ConversionException
	 */
	@Test
	public void conversion_with_precision_remove_trailing_zeroes() throws ConversionException {
		// Arrange
		Function<VType, String> converter = VTypeFormat.defaultFormatterNoUnits();
		
		// Case - precision reduces the digits in the original number
		Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "", NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
		VDouble vnum = ValueFactory.newVDouble(123.123456789, display);
		
		NumberFormat exponentialWhenNeededFormat = new ExponentialOnThresholdFormat(vnum.getFormat());
		String resultWithPrecision = exponentialWhenNeededFormat.format(vnum.getValue());
		String result = converter.apply(vnum);
		
		// Assert - original number has more digits, so truncated number is expected result.
		assertEquals("123.123457", resultWithPrecision);
		assertEquals(resultWithPrecision, result);
		
		// Case - precision increases the digits in the original number
		vnum = ValueFactory.newVDouble(123.123, display);
		resultWithPrecision = exponentialWhenNeededFormat.format(vnum.getValue());
		result = converter.apply(vnum);
		
		// Assert - original number has fewer digits, so zero padded, precision implemented number isn't expected.
		assertNotEquals(resultWithPrecision, result);
		assertEquals("123.123000", resultWithPrecision);
		assertEquals("123.123", result);
		
		// Case - Original number is exactly 0.0
		vnum = ValueFactory.newVDouble(0.0, display);
		resultWithPrecision = exponentialWhenNeededFormat.format(vnum.getValue());
		result = converter.apply(vnum);
		
		// Assert - original number has fewer digits, so zero padded, precision implemented number isn't expected.
		assertNotEquals(resultWithPrecision, result);
		assertEquals("0.000000", resultWithPrecision);
		assertEquals("0.0", result);
		
		//Case - original number is integer, but format applied with precision
		VInt value = ValueFactory.newVInt(123, null, null, display);
		resultWithPrecision = exponentialWhenNeededFormat.format(value.getValue());
		result = converter.apply(value);
		
		// Assert - original number is integer, so zero padded, precision implemented number isn't expected. Result remains an integer
		assertNotEquals(resultWithPrecision, result);
		assertEquals("123.000000", resultWithPrecision);
		assertEquals("123", result);
	}
}
