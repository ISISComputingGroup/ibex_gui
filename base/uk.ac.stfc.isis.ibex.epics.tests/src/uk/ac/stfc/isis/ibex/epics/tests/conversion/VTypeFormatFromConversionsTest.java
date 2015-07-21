
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

import java.util.Arrays;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VDouble;
import org.epics.vtype.VFloatArray;
import org.epics.vtype.VInt;
import org.epics.vtype.VLong;
import org.epics.vtype.VShort;
import org.epics.vtype.VString;
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
 * This class is responsible for testing the various from conversions
 *
 */
@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:localvariablename", "checkstyle:methodname"})
public class VTypeFormatFromConversionsTest {
	
	// Byte array values missing as unable to produce from ValueFactory

	@Test
	public void convert_from_vfloat_array() throws ConversionException {
		// Arrange
		Converter<VType, float[]> converter = VTypeFormat.fromVFloatArray();
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
		VFloatArray value = ValueFactory.newVFloatArray(data, alarm, time, display);
		
		// Act
		float[] result = converter.convert(value);
		
		// Assert
		assertEquals(Arrays.toString(result), "[]");
	}

	@Test
	public void convert_from_vstring() throws ConversionException {
		// Arrange
		Converter<VString, String> converter = VTypeFormat.fromVString();
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
		
		String test = "Test";
		VString value = ValueFactory.newVString(test , alarm, time);
		
		// Act
		String result = converter.convert(value);
		
		// Assert
		assertEquals(result, test);
	}

	@Test
	public void convert_from_double() throws ConversionException {
		// Arrange
		Converter<VDouble, Double> converter = VTypeFormat.fromDouble();

		Double number = new Double(123.456);
		
		VDouble value = ValueFactory.newVDouble(number);
		
		// Act
		Double result = converter.convert(value);
		
		// Assert
		assertEquals(result, number);
	}

	@Test
	public void convert_from_vint() throws ConversionException {
		// Arrange
		Converter<VInt, Integer> converter = VTypeFormat.fromVInt();
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
		Integer number = new Integer(123);
		
		VInt value = ValueFactory.newVInt(number, alarm, time, display);
		
		// Act
		Integer result = converter.convert(value);
		
		// Assert
		assertEquals(result, number);
	}

	@Test
	public void convert_from_short() throws ConversionException {
		// Arrange
		Converter<VShort, Short> converter = VTypeFormat.fromShort();
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
		Short number = new Short((short) 123);
		
		VShort value = ValueFactory.newVShort(number, alarm, time, display);
		
		// Act
		Short result = converter.convert(value);
		
		// Assert
		assertEquals(result, number);
	}

	@Test
	public void convert_from_long() throws ConversionException {
		// Arrange
		Converter<VLong, Long> converter = VTypeFormat.fromLong();
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
		Long number = new Long(123);
		
		VLong value = ValueFactory.newVLong(number, alarm, time, display);
		
		// Act
		Long result = converter.convert(value);
		
		// Assert
		assertEquals(result, number);
	}

	
}
