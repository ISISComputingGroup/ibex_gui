
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
import java.util.function.Function;

/**
 * Provides a series of Functions for common PV types.
 */
public final class Convert {
	
	private Convert() { }
	
    /**
     * @return A Function from a String to a zipped and hexed byte array
     */
	public static Function<String, byte[]> toZippedHex() {
		return asBytes()
				.andThen(compress())
				.andThen(toHex())
				.andThen(fromChars())
				.andThen(asBytes());
	}

    /**
     * @return A Function from a zipped and hexed byte array to a String
     */
	public static Function<byte[], String> fromZippedHex() {
		return fromBytes()
				.andThen(asChars())
				.andThen(deHex())
				.andThen(decompress())
				.andThen(fromBytes());
	}

    /**
     * @return A Function from a String to a hexed String
     */
	public static Function<String, String> toHexString() {
		return asBytes()
				.andThen(toHex())
				.andThen(fromChars());
	}

    /**
     * @return A Function from a hexed String to a dehexed String
     */
	public static Function<String, String> fromHexString() {
		return asChars()
				.andThen(deHex())
				.andThen(fromBytes());
	}

    /**
     * @return A Function to compress a byte array
     */
	public static Function<byte[], byte[]> compress() {
		return new Compressor();
	}

    /**
     * @return A Function to decompress a byte array
     */
	public static Function<byte[], byte[]> decompress() {
		return new Decompressor();
	}

    /**
     * @return A Function to hex a byte array to a char array
     */
	public static Function<byte[], char[]> toHex() {
		return new Hexer();
	}

    /**
     * @return A Function to dehex a char array to a byte array
     */
	public static Function<char[], byte[]> deHex() {
		return removeUnsetChars().andThen(new Dehexer());
	}
	
    /**
     * @return A Function to remove white space from a char array
     */
	public static Function<char[], char[]> removeUnsetChars() {
		return fromChars().andThen(trim()).andThen(asChars());
	}

    /**
     * @return A Function to remove white space from a String
     */
	public static Function<String, String> trim() {
		return String::trim;
	}

    /**
     * @return A Function from a byte array to a String
     */
	public static Function<byte[], String> fromBytes() {
		return new Function<byte[], String>() {
			@Override
			public String apply(byte[] value) throws ConversionException {
				try {
					return new String(value, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new ConversionException(e.getMessage());
				}				
			}
		};
	}	

    /**
     * @return A Function from a char array to a String
     */
	public static Function<char[], String> fromChars() {
		return String::new;
	}

    /**
     * @return A Function from a String to an array of characters
     */
	public static Function<String, char[]> asChars() {
		return String::toCharArray;
	}
	
    /**
     * @return A Function from a String to a byte array
     */
	public static Function<String, byte[]> asBytes() {
		return new Function<String, byte[]>() {
			@Override
			public byte[] apply(String value) throws ConversionException {
				try {
					return value.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new ConversionException(e.getMessage());
				}
			}
		};
	}

    /**
     * @return A Function from a String to a boolean value
     */
	public static Function<String, Boolean> toBoolean() {
		return new Function<String, Boolean>() {
			@Override
			public Boolean apply(String value) throws ConversionException {
				String val = value.toLowerCase(Locale.ENGLISH);
				switch (val) {
					case "yes":
						return true;
					case "no":
						return false;
					default:
						throw new ConversionException("Could not parse boolean string: " + val);
				}
			}
		};
	}

    /**
     * @param <T>
     *            The array/collection type
     * @return A Function from an array to a collection
     */
	public static <T> Function<T[], Collection<T>> toCollection() {
		return Arrays::asList;
	}

    /**
     * @param arrayOfType
     *            The array to convert
     * @param <T>
     *            The array/collection type
     * @return A Function from a collection to an array
     */
	public static <T> Function<Collection<T>, T[]> toArray(final T[] arrayOfType) {
		return value -> value.toArray(arrayOfType);
	}
}
