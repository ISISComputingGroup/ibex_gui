
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

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

/**
 * Provides a series of converters for common PV types.
 */
public final class Convert {
	
	private Convert() { }
	
    /**
     * @return A converter from a String to a zipped and hexed byte array
     */
	public static Converter<String, byte[]> toZippedHex() {
		return asBytes()
				.apply(compress())
				.apply(toHex())
				.apply(fromChars())
				.apply(asBytes());
	}

    /**
     * @return A converter from a zipped and hexed byte array to a String
     */
	public static Converter<byte[], String> fromZippedHex() {
		return fromBytes()
				.apply(asChars())
				.apply(deHex())
				.apply(decompress())
				.apply(fromBytes());
	}

    /**
     * @return A converter from a String to a hexed String
     */
	public static Converter<String, String> toHexString() {
		return asBytes()
				.apply(toHex())
				.apply(fromChars());
	}

    /**
     * @return A converter from a hexed String to a dehexed String
     */
	public static Converter<String, String> fromHexString() {
		return asChars()
				.apply(deHex())
				.apply(fromBytes());
	}

    /**
     * @return A converter to compress a byte array
     */
	public static Converter<byte[], byte[]> compress() {
		return new Compressor();
	}

    /**
     * @return A converter to decompress a byte array
     */
	public static Converter<byte[], byte[]> decompress() {
		return new Decompressor();
	}

    /**
     * @return A converter to hex a byte array to a char array
     */
	public static Converter<byte[], char[]> toHex() {
		return new Hexer();
	}

    /**
     * @return A converter to dehex a char array to a byte array
     */
	public static Converter<char[], byte[]> deHex() {
		return removeUnsetChars().apply(new Dehexer());
	}
	
    /**
     * @return A converter to remove white space from a char array
     */
	public static Converter<char[], char[]> removeUnsetChars() {
		return new Converter<char[], char[]>() {	
			@Override
			public char[] convert(char[] value) throws ConversionException {				
				return new String(value).trim().toCharArray();
			}
		};
	}

    /**
     * @return A converter to remove white space from a String
     */
	public static Converter<String, String> trim() {
		return new Converter<String, String>() {
			@Override
			public String convert(String value) throws ConversionException {
				return value.trim();
			}
		};
	}

    /**
     * @return A converter from a byte array to a String
     */
	public static Converter<byte[], String> fromBytes() {
		return new Converter<byte[], String>() {
			@Override
			public String convert(byte[] value) throws ConversionException {
				try {
					return new String(value, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new ConversionException(e.getMessage());
				}				
			}
		};
	}	

    /**
     * @return A converter from a char array to a String
     */
	public static Converter<char[], String> fromChars() {
		return new Converter<char[], String>() {
			@Override
			public String convert(char[] value) {
				return new String(value);				
			}
		};
	}

    /**
     * @return A converter from a String to an array of characters
     */
	public static Converter<String, char[]> asChars() {
		return new Converter<String, char[]>() {
			@Override
			public char[] convert(String value) {
				return value.toCharArray();				
			}
		};
	}
	
    /**
     * @return A converter from a String to a byte array
     */
	public static Converter<String, byte[]> asBytes() {
		return new Converter<String, byte[]>() {
			@Override
			public byte[] convert(String value) throws ConversionException {
				try {
					return value.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new ConversionException(e.getMessage());
				}
			}
		};
	}

    /**
     * @return A converter from a String to a boolean value
     */
	public static Converter<String, Boolean> toBoolean() {
		return new Converter<String, Boolean>() {
			@Override
			public Boolean convert(String value) throws ConversionException {
				String val = value.toLowerCase(Locale.ENGLISH);
				switch (val) {
					case "yes":
						return true;
					case "no":
						return false;
					default:
						throw new ConversionException("Could not pass boolean string: " + val);
				}
			}
		};
	}

    /**
     * @param <T>
     *            The array/collection type
     * @return A converter from an array to a collection
     */
	public static <T> Converter<T[], Collection<T>> toCollection() {
		return new Converter<T[], Collection<T>>() {
			@Override
			public Collection<T> convert(T[] value) throws ConversionException {
				return Arrays.asList(value);
			}
		};
	}

    /**
     * @param arrayOfType
     *            The array to convert
     * @param <T>
     *            The array/collection type
     * @return A converter from a collection to an array
     */
	public static <T> Converter<Collection<T>, T[]> toArray(final T[] arrayOfType) {
		return new Converter<Collection<T>, T[]>() {
			@Override
			public T[] convert(Collection<T> value) throws ConversionException {
				return value.toArray(arrayOfType);
			}
		};
	}
}
