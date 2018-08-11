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

import java.util.Arrays;
import java.util.function.Function;

import org.diirt.util.array.ListByte;
import org.diirt.util.array.ListFloat;
import org.diirt.vtype.VByteArray;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VFloatArray;
import org.diirt.vtype.VInt;
import org.diirt.vtype.VLong;
import org.diirt.vtype.VShort;
import org.diirt.vtype.VString;
import org.diirt.vtype.VType;
import org.diirt.vtype.ValueFactory;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.VTypeFormat;

/**
 * This class is responsible for testing the various from conversions.
 */
public class VTypeFormatFromConversionsTest {

    // Byte array values missing as unable to produce from ValueFactory

    @Test
    public void convert_from_vfloat_array() throws ConversionException {
	// Arrange
	Function<VType, float[]> converter = VTypeFormat.fromVFloatArray();

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

	// Act
	float[] result = converter.apply(value);

	// Assert
	assertEquals(Arrays.toString(result), "[]");
    }
    
    @Test
    public void convert_from_vbyte_array() throws ConversionException {
	// Arrange
	Function<VByteArray, String> converter = VTypeFormat.fromVByteArray();

	ListByte data = new ListByte() {
	    @Override
	    public int size() {
		return 1;
	    }

		@Override
		public byte getByte(int index) {
			return 0x30;
		}
	};
	VByteArray value = (VByteArray) ValueFactory.newVNumberArray(data, null, null, null);

	// Act
	String result = converter.apply(value);

	// Assert
	assertEquals(result, "0");
    }    
    

    @Test
    public void convert_from_vstring() throws ConversionException {
	// Arrange
	Function<VString, String> converter = VTypeFormat.fromVString();

	String test = "Test";
	VString value = ValueFactory.newVString(test, null, null);

	// Act
	String result = converter.apply(value);

	// Assert
	assertEquals(result, test);
    }

    @Test
    public void convert_from_double() throws ConversionException {
	// Arrange
	Function<VDouble, Double> converter = VTypeFormat.fromDouble();

	Double number = new Double(123.456);

	VDouble value = ValueFactory.newVDouble(number);

	// Act
	Double result = converter.apply(value);

	// Assert
	assertEquals(result, number);
    }

    @Test
    public void convert_from_vint() throws ConversionException {
	// Arrange
	Function<VInt, Integer> converter = VTypeFormat.fromVInt();

	Integer number = new Integer(123);

	VInt value = ValueFactory.newVInt(number, null, null, null);

	// Act
	Integer result = converter.apply(value);

	// Assert
	assertEquals(result, number);
    }

    @Test
    public void convert_from_short() throws ConversionException {
	// Arrange
	Function<VShort, Short> converter = VTypeFormat.fromShort();

	Short number = new Short((short) 123);

	VShort value = ValueFactory.newVShort(number, null, null, null);

	// Act
	Short result = converter.apply(value);

	// Assert
	assertEquals(result, number);
    }

    @Test
    public void convert_from_long() throws ConversionException {
	// Arrange
	Function<VLong, Long> converter = VTypeFormat.fromLong();

	Long number = new Long(123);

	VLong value = ValueFactory.newVLong(number, null, null, null);

	// Act
	Long result = converter.apply(value);

	// Assert
	assertEquals(result, number);
    }

}
