
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

import java.util.ArrayList;
import java.util.List;

import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VDouble;
import org.epics.vtype.VEnum;
import org.epics.vtype.VFloatArray;
import org.epics.vtype.VInt;
import org.epics.vtype.VNumber;
import org.epics.vtype.VType;
import org.epics.vtype.ValueFactory;
import org.epics.util.array.ListFloat;
import org.epics.util.text.NumberFormats;
import org.epics.util.time.Timestamp;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.conversion.VTypeFormat;

/**
 * This class is responsible for testing the various to conversions 
 *
 */
@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:localvariablename", "checkstyle:methodname"})
public class VTypeFormatToConversionsTest {

	// Byte array values missing as unable to produce from ValueFactory
	
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
	public void converst_to_number_with_precision() throws ConversionException {
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
	public void converst_to_number_with_precision_greater_than_digits() throws ConversionException {
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
		Time time = new Time() {
			@Override
			public boolean isTimeValid() {
				return false;
			}
			@Override
			public Timestamp getTimestamp() {
				return Timestamp.now();
			}
			@Override
			public Integer getTimeUserTag() {
				return null;
			}
		};
		Alarm alarm = new Alarm() {
			@Override
			public AlarmSeverity getAlarmSeverity() {
				return AlarmSeverity.NONE;
			}
			@Override
			public String getAlarmName() {
				return null;
			}
		};
		Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit", NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
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
		VFloatArray test = ValueFactory.newVFloatArray(data , alarm, time, display);
		
		// Act
		VFloatArray result = converter.convert(test);
		
		// Assert
		assertEquals(result, test);
	}

	@Test
	public void convert_to_vint() throws ConversionException {
		// Arrange
		Converter<VType, VInt> converter = VTypeFormat.toVInt();
		Time time = new Time() {
			@Override
			public boolean isTimeValid() {
				return false;
			}
			@Override
			public Timestamp getTimestamp() {
				return Timestamp.now();
			}
			@Override
			public Integer getTimeUserTag() {
				return null;
			}
		};
		Alarm alarm = new Alarm() {
			@Override
			public AlarmSeverity getAlarmSeverity() {
				return AlarmSeverity.NONE;
			}
			@Override
			public String getAlarmName() {
				return null;
			}
		};
		Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit", NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
		Integer test = 123;
		VInt value = ValueFactory.newVInt(test, alarm, time, display);
		
		// Act
		VInt result = converter.convert(value);
		
		// Assert
		assertEquals(result, value);
	}
	

	@Test
	public void convert_to_long() throws ConversionException {
		// Arrange
		Converter<VInt, Long> converter = VTypeFormat.toLong();
		Time time = new Time() {
			@Override
			public boolean isTimeValid() {
				return false;
			}
			@Override
			public Timestamp getTimestamp() {
				return Timestamp.now();
			}
			@Override
			public Integer getTimeUserTag() {
				return null;
			}
		};
		Alarm alarm = new Alarm() {
			@Override
			public AlarmSeverity getAlarmSeverity() {
				return AlarmSeverity.NONE;
			}
			@Override
			public String getAlarmName() {
				return null;
			}
		};
		Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit", NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
		Integer test = 123;
		VInt value = ValueFactory.newVInt(test, alarm, time, display);
		
		// Act
		Long result = converter.convert(value);
		
		// Assert
		Long check = (long) test;
		assertEquals(result, check);
	}

	//An Enum class to test with

	
	private static final Class<TestingEnum> enumType = TestingEnum.class;
	
	@Test
	public void convert_to_enum() throws ConversionException {
		
		// Arrange
		Converter<VEnum, TestingEnum> converter = VTypeFormat.toEnum(enumType);
		Time time = new Time() {
			@Override
			public boolean isTimeValid() {
				return false;
			}
			@Override
			public Timestamp getTimestamp() {
				return Timestamp.now();
			}
			@Override
			public Integer getTimeUserTag() {
				return null;
			}
		};
		Alarm alarm = new Alarm() {
			@Override
			public AlarmSeverity getAlarmSeverity() {
				return AlarmSeverity.NONE;
			}
			@Override
			public String getAlarmName() {
				return null;
			}
		};
				
		VEnum value = ValueFactory.newVEnum(0, TestingEnum.TEST1.getNames(), alarm, time);
		
		// Act
		TestingEnum result = converter.convert(value);
		
		// Assert
		assertEquals(result, TestingEnum.TEST1);
	}

	@Test
	public void convert_to_enumString() throws ConversionException {
		
		// Arrange
		Converter<VEnum, String> converter = VTypeFormat.toEnumString();
		Time time = new Time() {
			@Override
			public boolean isTimeValid() {
				return false;
			}
			@Override
			public Timestamp getTimestamp() {
				return Timestamp.now();
			}
			@Override
			public Integer getTimeUserTag() {
				return null;
			}
		};
		Alarm alarm = new Alarm() {
			@Override
			public AlarmSeverity getAlarmSeverity() {
				return AlarmSeverity.NONE;
			}
			@Override
			public String getAlarmName() {
				return null;
			}
		};
				
		VEnum value = ValueFactory.newVEnum(0, TestingEnum.TEST1.getNames(), alarm, time);
		
		// Act
		String result = converter.convert(value);
		
		// Assert
		assertEquals(result, "TEST1");
	}
}
