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
import java.util.Arrays;
import java.util.List;

import org.diirt.vtype.Display;
import org.diirt.vtype.VBoolean;
import org.diirt.vtype.VBooleanArray;
import org.diirt.vtype.VByte;
import org.diirt.vtype.VDoubleArray;
import org.diirt.vtype.VEnum;
import org.diirt.vtype.VEnumArray;
import org.diirt.vtype.VFloat;
import org.diirt.vtype.VFloatArray;
import org.diirt.vtype.VImage;
import org.diirt.vtype.VInt;
import org.diirt.vtype.VIntArray;
import org.diirt.vtype.VLong;
import org.diirt.vtype.VLongArray;
import org.diirt.vtype.VMultiDouble;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VShort;
import org.diirt.vtype.VStatistics;
import org.diirt.vtype.VString;
import org.diirt.vtype.VStringArray;
import org.diirt.vtype.VTable;
import org.diirt.vtype.VType;
import org.diirt.vtype.ValueFactory;
import org.diirt.util.array.ListBoolean;
import org.diirt.util.array.ListDouble;
import org.diirt.util.array.ListFloat;
import org.diirt.util.array.ListInt;
import org.diirt.util.array.ListLong;
import org.diirt.util.text.NumberFormats;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.conversion.VTypeFormat;

/**
 * This class is responsible for testing the default formatter and quantity
 * conversions.
 * 
 * Byte array values missing as unable to produce from ValueFactory.
 * 
 */
public class VTypeFormatDefaultFormatterAndQuantityTest {

    @Test
    public void default_convert_number_double_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat.defaultFormatter();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Double(123.456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123.456000 unit");
    }

    @Test
    public void default_convert_number_double_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Double(123.456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123.456000 ");
    }

    @Test
    public void default_convert_number_float_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Float(123.456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123.456001 unit");
    }

    @Test
    public void default_convert_number_float_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Float(123.456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123.456001 ");
    }

    @Test
    public void default_convert_number_long_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Long(123456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123456 unit");
    }

    @Test
    public void default_convert_number_long_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Long(123456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123456 ");
    }

    @Test
    public void default_convert_number_integer_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Integer(123456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123456 unit");
    }

    @Test
    public void default_convert_number_integer_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Integer(123456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123456 ");
    }

    @Test
    public void default_convert_number_short_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Short((short) 123);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123 unit");
    }

    @Test
    public void default_convert_number_short_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Short((short) 123);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123 ");
    }

    @Test
    public void default_convert_number_byte_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Byte((byte) 123.456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123 unit");
    }

    @Test
    public void default_convert_number_byte_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Byte((byte) 123.456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123 ");
    }

    @Test
    public void default_convert_double_with_units() throws ConversionException {
	// Arrange
	Converter<VDouble, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(3), 0.0, 0.0, 0.0, 0.0, 0.0);
	VDouble vnum = ValueFactory.newVDouble(123.456, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123.456 unit");
    }

    @Test
    public void default_convert_double_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VDouble, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(3), 0.0, 0.0, 0.0, 0.0, 0.0);

	VDouble vnum = ValueFactory.newVDouble(123.456, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123.456 ");
    }

    @Test
    public void default_convert_float_with_units() throws ConversionException {
	// Arrange
	Converter<VFloat, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
	Float number = new Float(123.456);

	VFloat vnum = ValueFactory.newVFloat(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123.456001 unit");
    }

    @Test
    public void default_convert_float_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VFloat, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
	Float number = new Float(123.456);

	VFloat vnum = ValueFactory.newVFloat(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123.456001 ");
    }

    @Test
    public void default_convert_long_with_units() throws ConversionException {
	// Arrange
	Converter<VLong, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Long number = new Long(123456);

	VLong vnum = ValueFactory.newVLong(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123456 unit");
    }

    @Test
    public void default_convert_long_without_units() throws ConversionException {
	// Arrange
	Converter<VLong, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Long number = new Long(123456);

	VLong vnum = ValueFactory.newVLong(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123456 ");
    }

    @Test
    public void default_convert_integer_with_units() throws ConversionException {
	// Arrange
	Converter<VInt, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Integer number = new Integer(123456);

	VInt vnum = ValueFactory.newVInt(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123456 unit");
    }

    @Test
    public void default_convert_integer_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VInt, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Integer number = new Integer(123456);

	VInt vnum = ValueFactory.newVInt(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123456 ");
    }

    @Test
    public void default_convert_short_with_units() throws ConversionException {
	// Arrange
	Converter<VShort, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Short number = new Short((short) 123);

	VShort vnum = ValueFactory.newVShort(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123 unit");
    }

    @Test
    public void default_convert_short_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VShort, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Short number = new Short((short) 123);

	VShort vnum = ValueFactory.newVShort(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123 ");
    }

    @Test
    public void default_convert_byte_with_units() throws ConversionException {
	// Arrange
	Converter<VByte, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Byte number = new Byte((byte) 123.456);

	VByte vnum = ValueFactory.newVByte(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123 unit");
    }

    @Test
    public void default_convert_byte_without_units() throws ConversionException {
	// Arrange
	Converter<VByte, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Byte number = new Byte((byte) 123.456);

	VByte vnum = ValueFactory.newVByte(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123 ");
    }

    @Test
    public void default_convert_string() throws ConversionException {
	// Arrange
	Converter<VString, String> converter = VTypeFormat.defaultFormatter();
	String string = "Test";
	VString vstring = ValueFactory.newVString(string, null, null);

	// Act
	String result = converter.convert(vstring);

	// Assert
	assertEquals(result, string);
    }

    @Test
    public void default_convert_boolean() throws ConversionException {
	// Arrange
	Converter<VBoolean, String> converter = VTypeFormat.defaultFormatter();

	VBoolean value = ValueFactory.newVBoolean(true, null, null);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "true");
    }

    @Test
    public void default_convert_type_with_units() throws ConversionException {
	// Arrange
	Converter<VType, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Double(123.456);

	VType value = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "123.456000 unit");
    }

    @Test
    public void default_convert_type_without_units() throws ConversionException {
	// Arrange
	Converter<VType, String> converter = VTypeFormat.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Double(123.456);

	VType value = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "123.456000 ");
    }

    @Test
    public void default_convert_table() throws ConversionException {
	// Arrange
	Converter<VTable, String> converter = VTypeFormat.defaultFormatter();

	List<Class<?>> types = Arrays.asList();
	List<String> names = Arrays.asList("Name 1", "Name 2");
	List<Object> values = Arrays.asList();

	VTable value = ValueFactory.newVTable(types, names, values);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "VTable[2x0, [Name 1, Name 2]]");
    }

    @Test
    public void default_convert_statistics_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VStatistics, String> converter = VTypeFormat
		.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(2), 0.0, 0.0, 0.0, 0.0, 0.0);

	VStatistics value = ValueFactory.newVStatistics(5.5, 3.02765, 1, 10,
		10, null, null, display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, value.toString());
    }

    @Test
    public void default_convert_statistics_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VStatistics, String> converter = VTypeFormat
		.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(2), 0.0, 0.0, 0.0, 0.0, 0.0);

	VStatistics value = ValueFactory.newVStatistics(5.5, 3.02765, 1, 10,
		10, null, null, display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, value.toString());
    }

    @Test
    public void default_convert_image() throws ConversionException {
	// Arrange
	Converter<VImage, String> converter = VTypeFormat.defaultFormatter();

	byte[] data = new byte[] {(byte) 0 };

	VImage value = ValueFactory.newVImage(1, 1, data);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, value.toString());
    }

    @Test
    public void default_convert_enum() throws ConversionException {
	// Arrange
	Converter<VEnum, String> converter = VTypeFormat.defaultFormatter();
	String test = "Test";
	List<String> labels = new ArrayList<String>();
	labels.add(test);
	VEnum value = ValueFactory.newVEnum(0, labels, null, null);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, test);
    }

    @Test
    public void default_convert_multi_double() throws ConversionException {
	// Arrange
	Converter<VMultiDouble, String> converter = VTypeFormat
		.defaultFormatter();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);

	VDouble entry = ValueFactory.newVDouble(123.456);
	List<VDouble> values = new ArrayList<>();
	values.add(entry);
	values.add(entry);

	VMultiDouble value = ValueFactory.newVMultiDouble(values, null, null,
		display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, value.toString());
    }

    @Test
    public void default_convert_boolean_array() throws ConversionException {
	// Arrange
	Converter<VBooleanArray, String> converter = VTypeFormat
		.defaultFormatter();

	ListBoolean data = new ListBoolean() {
	    @Override
	    public int size() {
		return 0;
	    }

	    @Override
	    public void setBoolean(int index, boolean value) {
	    }

	    @Override
	    public boolean getBoolean(int index) {
		return false;
	    }
	};
	data.setBoolean(0, false);

	VBooleanArray value = ValueFactory.newVBooleanArray(data, null, null);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_convert_double_array_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VDoubleArray, String> converter = VTypeFormat
		.defaultFormatter();

	ListDouble data = new ListDouble() {
	    @Override
	    public int size() {
		return 0;
	    }

	    @Override
	    public double getDouble(int index) {
		return 0;
	    }
	};

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);

	VDoubleArray value = ValueFactory.newVDoubleArray(data, null, null,
		display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_convert_double_array_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VDoubleArray, String> converter = VTypeFormat
		.defaultFormatter();

	ListDouble data = new ListDouble() {
	    @Override
	    public int size() {
		return 0;
	    }

	    @Override
	    public double getDouble(int index) {
		return 0;
	    }
	};

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);

	VDoubleArray value = ValueFactory.newVDoubleArray(data, null, null,
		display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_convert_enum_array() throws ConversionException {
	// Arrange
	Converter<VEnumArray, String> converter = VTypeFormat
		.defaultFormatter();

	ListInt indexes = new ListInt() {
	    @Override
	    public int size() {
		return 0;
	    }

	    @Override
	    public int getInt(int index) {
		return 0;
	    }
	};
	List<String> labels = new ArrayList<>();
	labels.add("Test");
	VEnumArray value = ValueFactory.newVEnumArray(indexes, labels, null,
		null);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_convert_string_array() throws ConversionException {
	// Arrange
	Converter<VStringArray, String> converter = VTypeFormat
		.defaultFormatter();

	List<String> data = new ArrayList<>();
	data.add("Test");
	VStringArray value = ValueFactory.newVStringArray(data, null, null);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[Test]");
    }

    @Test
    public void default_convert_float_array_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VFloatArray, String> converter = VTypeFormat
		.defaultFormatter();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);

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
	VFloatArray value = ValueFactory.newVFloatArray(data, null, null,
		display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_convert_float_array_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VFloatArray, String> converter = VTypeFormat
		.defaultFormatter();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);

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
	VFloatArray value = ValueFactory.newVFloatArray(data, null, null,
		display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_convert_int_array_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VIntArray, String> converter = VTypeFormat.defaultFormatter();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);

	ListInt values = new ListInt() {
	    @Override
	    public int size() {
		return 0;
	    }

	    @Override
	    public int getInt(int index) {
		return 0;
	    }
	};
	VIntArray value = ValueFactory
		.newVIntArray(values, null, null, display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_convert_int_array_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VIntArray, String> converter = VTypeFormat.defaultFormatter();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);

	ListInt values = new ListInt() {
	    @Override
	    public int size() {
		return 0;
	    }

	    @Override
	    public int getInt(int index) {
		return 0;
	    }
	};
	VIntArray value = ValueFactory
		.newVIntArray(values, null, null, display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_convert_long_array_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VLongArray, String> converter = VTypeFormat
		.defaultFormatter();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);

	ListLong values = new ListLong() {
	    @Override
	    public int size() {
		return 0;
	    }

	    @Override
	    public long getLong(int index) {
		return 0;
	    }
	};
	VLongArray value = ValueFactory.newVLongArray(values, null, null,
		display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_convert_long_array_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VLongArray, String> converter = VTypeFormat
		.defaultFormatter();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);

	ListLong values = new ListLong() {
	    @Override
	    public int size() {
		return 0;
	    }

	    @Override
	    public long getLong(int index) {
		return 0;
	    }
	};
	VLongArray value = ValueFactory.newVLongArray(values, null, null,
		display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_no_units_convert_number_double_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Double(123.456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123.456000");
    }

    @Test
    public void default_no_units_convert_number_double_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Double(123.456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123.456000");
    }

    @Test
    public void default_no_units_convert_number_float_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Float(123.456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123.456001");
    }

    @Test
    public void default_no_units_convert_number_float_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Float(123.456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123.456001");
    }

    @Test
    public void default_no_units_convert_number_long_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Long(123456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123456");
    }

    @Test
    public void default_no_units_convert_number_long_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Long(123456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123456");
    }

    @Test
    public void default_no_units_convert_number_integer_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Integer(123456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123456");
    }

    @Test
    public void default_no_units_convert_number_integer_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Integer(123456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123456");
    }

    @Test
    public void default_no_units_convert_number_short_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Short((short) 123);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123");
    }

    @Test
    public void default_no_units_convert_number_short_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Short((short) 123);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123");
    }

    @Test
    public void default_no_units_convert_number_byte_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Byte((byte) 123.456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123");
    }

    @Test
    public void default_no_units_convert_number_byte_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Byte((byte) 123.456);

	VNumber vnum = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123");
    }

    @Test
    public void default_no_units_convert_double_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VDouble, String> converter = VTypeFormat
		.defaultFormatterNoUnits();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(3), 0.0, 0.0, 0.0, 0.0, 0.0);
	VDouble vnum = ValueFactory.newVDouble(123.456, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123.456");
    }

    @Test
    public void default_no_units_convert_double_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VDouble, String> converter = VTypeFormat
		.defaultFormatterNoUnits();
	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(3), 0.0, 0.0, 0.0, 0.0, 0.0);

	VDouble vnum = ValueFactory.newVDouble(123.456, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123.456");
    }

    @Test
    public void default_no_units_convert_float_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VFloat, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
	Float number = new Float(123.456);

	VFloat vnum = ValueFactory.newVFloat(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123.456001");
    }

    @Test
    public void default_no_units_convert_float_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VFloat, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
	Float number = new Float(123.456);

	VFloat vnum = ValueFactory.newVFloat(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123.456001");
    }

    @Test
    public void default_no_units_convert_long_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VLong, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Long number = new Long(123456);

	VLong vnum = ValueFactory.newVLong(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123456");
    }

    @Test
    public void default_no_units_convert_long_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VLong, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Long number = new Long(123456);

	VLong vnum = ValueFactory.newVLong(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123456");
    }

    @Test
    public void default_no_units_convert_integer_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VInt, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Integer number = new Integer(123456);

	VInt vnum = ValueFactory.newVInt(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123456");
    }

    @Test
    public void default_no_units_convert_integer_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VInt, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Integer number = new Integer(123456);

	VInt vnum = ValueFactory.newVInt(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123456");
    }

    @Test
    public void default_no_units_convert_short_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VShort, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Short number = new Short((short) 123);

	VShort vnum = ValueFactory.newVShort(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123");
    }

    @Test
    public void default_no_units_convert_short_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VShort, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Short number = new Short((short) 123);

	VShort vnum = ValueFactory.newVShort(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123");
    }

    @Test
    public void default_no_units_convert_byte_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VByte, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Byte number = new Byte((byte) 123.456);

	VByte vnum = ValueFactory.newVByte(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123");
    }

    @Test
    public void default_no_units_convert_byte_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VByte, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(0), 0.0, 0.0, 0.0, 0.0, 0.0);
	Byte number = new Byte((byte) 123.456);

	VByte vnum = ValueFactory.newVByte(number, null, null, display);

	// Act
	String result = converter.convert(vnum);

	// Assert
	assertEquals(result, "123");
    }

    @Test
    public void default_no_units_convert_string() throws ConversionException {
	// Arrange
	Converter<VString, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	String string = "Test";
	VString vstring = ValueFactory.newVString(string, null, null);

	// Act
	String result = converter.convert(vstring);

	// Assert
	assertEquals(result, string);
    }

    @Test
    public void default_no_units_convert_boolean() throws ConversionException {
	// Arrange
	Converter<VBoolean, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	VBoolean value = ValueFactory.newVBoolean(true, null, null);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "true");
    }

    @Test
    public void default_no_units_convert_type_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VType, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Double(123.456);

	VType value = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "123.456000");
    }

    @Test
    public void default_no_units_convert_type_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VType, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Double(123.456);

	VType value = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "123.456000");
    }

    @Test
    public void default_no_units_convert_table() throws ConversionException {
	// Arrange
	Converter<VTable, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	List<Class<?>> types = Arrays.asList();
	List<String> names = Arrays.asList("Name 1", "Name 2");
	List<Object> values = Arrays.asList();

	VTable value = ValueFactory.newVTable(types, names, values);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "VTable[2x0, [Name 1, Name 2]]");
    }

    @Test
    public void default_no_units_convert_statistics_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VStatistics, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(2), 0.0, 0.0, 0.0, 0.0, 0.0);

	VStatistics value = ValueFactory.newVStatistics(5.5, 3.02765, 1, 10,
		10, null, null, display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, value.toString());
    }

    @Test
    public void default_no_units_convert_statistics_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VStatistics, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(2), 0.0, 0.0, 0.0, 0.0, 0.0);

	VStatistics value = ValueFactory.newVStatistics(5.5, 3.02765, 1, 10,
		10, null, null, display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, value.toString());
    }

    @Test
    public void default_no_units_convert_image() throws ConversionException {
	// Arrange
	Converter<VImage, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	byte[] data = new byte[] {(byte) 0 };

	VImage value = ValueFactory.newVImage(1, 1, data);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, value.toString());
    }

    @Test
    public void default_no_units_convert_enum() throws ConversionException {
	// Arrange
	Converter<VEnum, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	String test = "Test";
	List<String> labels = new ArrayList<String>();
	labels.add(test);
	VEnum value = ValueFactory.newVEnum(0, labels, null, null);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, test);
    }

    @Test
    public void default_no_units_convert_multi_double()
	    throws ConversionException {
	// Arrange
	Converter<VMultiDouble, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);

	VDouble entry = ValueFactory.newVDouble(123.456);
	List<VDouble> values = new ArrayList<>();
	values.add(entry);
	values.add(entry);

	VMultiDouble value = ValueFactory.newVMultiDouble(values, null, null,
		display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, value.toString());
    }

    @Test
    public void default_no_units_convert_boolean_array()
	    throws ConversionException {
	// Arrange
	Converter<VBooleanArray, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	ListBoolean data = new ListBoolean() {
	    @Override
	    public int size() {
		return 0;
	    }

	    @Override
	    public void setBoolean(int index, boolean value) {
	    }

	    @Override
	    public boolean getBoolean(int index) {
		return false;
	    }
	};
	data.setBoolean(0, false);

	VBooleanArray value = ValueFactory.newVBooleanArray(data, null, null);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_no_units_convert_double_array_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VDoubleArray, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	ListDouble data = new ListDouble() {
	    @Override
	    public int size() {
		return 0;
	    }

	    @Override
	    public double getDouble(int index) {
		return 0;
	    }
	};

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);

	VDoubleArray value = ValueFactory.newVDoubleArray(data, null, null,
		display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_no_units_convert_double_array_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VDoubleArray, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	ListDouble data = new ListDouble() {
	    @Override
	    public int size() {
		return 0;
	    }

	    @Override
	    public double getDouble(int index) {
		return 0;
	    }
	};

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);

	VDoubleArray value = ValueFactory.newVDoubleArray(data, null, null,
		display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_no_units_convert_enum_array()
	    throws ConversionException {
	// Arrange
	Converter<VEnumArray, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	ListInt indexes = new ListInt() {
	    @Override
	    public int size() {
		return 0;
	    }

	    @Override
	    public int getInt(int index) {
		return 0;
	    }
	};
	List<String> labels = new ArrayList<>();
	labels.add("Test");
	VEnumArray value = ValueFactory.newVEnumArray(indexes, labels, null,
		null);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_no_units_convert_string_array()
	    throws ConversionException {
	// Arrange
	Converter<VStringArray, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	List<String> data = new ArrayList<>();
	data.add("Test");
	VStringArray value = ValueFactory.newVStringArray(data, null, null);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[Test]");
    }

    @Test
    public void default_no_units_convert_float_array_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VFloatArray, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);

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
	VFloatArray value = ValueFactory.newVFloatArray(data, null, null,
		display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_no_units_convert_float_array_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VFloatArray, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);

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
	VFloatArray value = ValueFactory.newVFloatArray(data, null, null,
		display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_no_units_convert_int_array_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VIntArray, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);

	ListInt values = new ListInt() {
	    @Override
	    public int size() {
		return 0;
	    }

	    @Override
	    public int getInt(int index) {
		return 0;
	    }
	};
	VIntArray value = ValueFactory
		.newVIntArray(values, null, null, display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_no_units_convert_int_array_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VIntArray, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);

	ListInt values = new ListInt() {
	    @Override
	    public int size() {
		return 0;
	    }

	    @Override
	    public int getInt(int index) {
		return 0;
	    }
	};
	VIntArray value = ValueFactory
		.newVIntArray(values, null, null, display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_no_units_convert_long_array_with_units()
	    throws ConversionException {
	// Arrange
	Converter<VLongArray, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);

	ListLong values = new ListLong() {
	    @Override
	    public int size() {
		return 0;
	    }

	    @Override
	    public long getLong(int index) {
		return 0;
	    }
	};
	VLongArray value = ValueFactory.newVLongArray(values, null, null,
		display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void default_no_units_convert_long_array_without_units()
	    throws ConversionException {
	// Arrange
	Converter<VLongArray, String> converter = VTypeFormat
		.defaultFormatterNoUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);

	ListLong values = new ListLong() {
	    @Override
	    public int size() {
		return 0;
	    }

	    @Override
	    public long getLong(int index) {
		return 0;
	    }
	};
	VLongArray value = ValueFactory.newVLongArray(values, null, null,
		display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "[]");
    }

    @Test
    public void quantity_with_units() throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat.quantityWithUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "unit",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Double(123.456);

	VNumber value = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "123.456000 unit");
    }

    @Test
    public void quantity_with_units_no_units_specified()
	    throws ConversionException {
	// Arrange
	Converter<VNumber, String> converter = VTypeFormat.quantityWithUnits();

	Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "",
		NumberFormats.format(6), 0.0, 0.0, 0.0, 0.0, 0.0);
	Number number = new Double(123.456);

	VNumber value = ValueFactory.newVNumber(number, null, null, display);

	// Act
	String result = converter.convert(value);

	// Assert
	assertEquals(result, "123.456000 ");
    }
}
