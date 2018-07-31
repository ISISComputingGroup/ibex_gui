
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

import java.text.NumberFormat;
import java.util.Arrays;

import org.diirt.util.array.IteratorByte;
import org.diirt.util.array.IteratorDouble;
import org.diirt.util.array.IteratorFloat;
import org.diirt.util.array.IteratorInt;
import org.diirt.vtype.Display;
import org.diirt.vtype.VByteArray;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VDoubleArray;
import org.diirt.vtype.VEnum;
import org.diirt.vtype.VFloatArray;
import org.diirt.vtype.VInt;
import org.diirt.vtype.VIntArray;
import org.diirt.vtype.VLong;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VShort;
import org.diirt.vtype.VString;
import org.diirt.vtype.VType;
import org.diirt.vtype.ValueUtil;

/**
 * This class is responsible for converting PVManager VType in to Java types.
 */
public final class VTypeFormat {
    
    /**
     * Default constructor.
     */
    private VTypeFormat() {
    }

    /**
     * @param <R> conversion source object type
     * @return A converter between the original object and a string with units
     */
	public static <R extends VType> Converter<R, String> defaultFormatter() {
		return new VTypeDefaultFormatter<R>().withUnits;
	}

    /**
     * @param <R> conversion source object type
     * @return A converter between the original object and a string without
     *         units
     */
	public static <R extends VType> Converter<R, String> defaultFormatterNoUnits() {
		return new VTypeDefaultFormatter<R>().noUnits;
	}
	
	/**
     * @param <R> conversion source object type
     * @return a number with no consideration for formatting or precision
     */
	public static <R extends VNumber> Converter<R, Number> toNumber() {
		return new Converter<R, Number>() {
			@Override
			public Number convert(R value) throws ConversionException {
				return value.getValue();
			}
		};
	}
	
	/**
     * @param <R> conversion source object type
     * @return a string of the number supplied with any associated units
     */
	public static <R extends VNumber> Converter<R, String> quantityWithUnits() {
		return new Converter<R, String>() {
			@Override
			public String convert(R value) throws ConversionException {
				return value.getFormat().format(value.getValue()) + " " + value.getUnits();
			}
		};
	}
	
	/**
     * @param <R> conversion source object type
     * @return a number formatted with the precision defined by the underlying
     *         PV
     */
	public static <R extends VNumber> Converter<R, Number> toNumberWithPrecision() {
		return new Converter<R, Number>() {
			@Override
			public Number convert(R value) throws ConversionException {
				VNumber val = value;
				Display display = ValueUtil.displayOf(val);
				if (display != null) {
					NumberFormat formatter = display.getFormat();
					return new Double(formatter.format(value.getValue()));
				}
				return value.getValue();
			}
		};
	}
	
    /**
     * @return Converter from VByteArray to a String
     */
	public static Converter<VByteArray, String> fromVByteArray() {
		return extractBytes()
				.apply(Convert.fromBytes())
				.apply(Convert.trim());
	}	

    /**
     * @return Converter from a VFloatArray to a float array
     */
	public static Converter<VType, float[]> fromVFloatArray() {
		return toVFloatArray()
				.apply(extractFloats());
	}
	
	/**
     * @return Converter from a VDoubleArray to a double array
     */
    public static Converter<VType, double[]> fromVDoubleArray() {
        return toVDoubleArray()
                .apply(extractDoubles());
    }
    
    /**
     * @return Converter from a VIntArray to an int array
     */
    public static Converter<VType, int[]> fromVIntegerArray() {
        return toVIntegerArray()
                .apply(extractIntegers());
    }
	
    /**
     * @return Converter from a VByteArray of zipped, hexed values to a String
     */
	public static Converter<VByteArray, String> fromZippedHexVByteArray() {
		return extractBytes().apply(Convert.fromZippedHex());
	}

    /**
     * @return Converter from a VType to a VFloatArray
     */
	public static Converter<VType, VFloatArray> toVFloatArray() {
		return new Converter<VType, VFloatArray>() {
			@Override
			public VFloatArray convert(VType value) throws ConversionException {
				try {
					return (VFloatArray) value;
				} catch (ClassCastException e) {
					throw new ConversionException(e.getMessage());
				}
			}
		};
	}
	
	/**
     * @return Converter from a VType to a VDoubleArray
     */
    public static Converter<VType, VDoubleArray> toVDoubleArray() {
        return new Converter<VType, VDoubleArray>() {
            @Override
            public VDoubleArray convert(VType value) throws ConversionException {
                try {
                    return (VDoubleArray) value;
                } catch (ClassCastException e) {
                    throw new ConversionException(e.getMessage());
                }
            }
        };
    }
    
    /**
     * @return Converter from a VType to a VIntegerArray
     */
    public static Converter<VType, VIntArray> toVIntegerArray() {
        return new Converter<VType, VIntArray>() {
            @Override
            public VIntArray convert(VType value) throws ConversionException {
                try {
                    return (VIntArray) value;
                } catch (ClassCastException e) {
                    throw new ConversionException(e.getMessage());
                }
            }
        };
    }
	
    /**
     * @return Converter from a VType to a VByteArray
     */
	public static Converter<VType, VByteArray> toVByteArray() {
		return new Converter<VType, VByteArray>() {
			@Override
			public VByteArray convert(VType value) throws ConversionException {
				try {
					return (VByteArray) value;
				} catch (ClassCastException e) {
					throw new ConversionException(e.getMessage());
				}
			}
		};
	}
	
    /**
     * @return Converter from a VType to a VInt
     */
	public static Converter<VType, VInt> toVInt() {
		return new Converter<VType, VInt>() {
			@Override
			public VInt convert(VType value) throws ConversionException {
				try {
					return (VInt) value;
				} catch (ClassCastException e) {
					throw new ConversionException(e.getMessage());
				}
			}
		};
	}

    /**
     * @return Converter from a VInt to a long
     */
	public static Converter<VInt, Long> toLong() {
		return new Converter<VInt, Long>() {
			@Override
			public Long convert(VInt value) throws ConversionException {
				return Long.valueOf(value.getValue());
			}
		};
	}

    /**
     * @param <E> The type of the enumerator
     * @param enumType The class describing the type of the enumerator
     * @return A converter from VEnum to an enumerator of the given type
     */
	public static <E extends Enum<E>> Converter<VEnum, E> toEnum(final Class<E> enumType) {
		return new Converter<VEnum, E>() {
			@Override
			public E convert(VEnum value) throws ConversionException {
				String text = value.getValue();
				
				// Replace characters which can't be used in java enums with underscores.
				text = text.replace(" ", "_");
				text = text.replace("-", "_");
				
				for (Enum<E> item : enumType.getEnumConstants()) {
					if (text.equalsIgnoreCase(item.name())) {
						return Enum.valueOf(enumType, item.name());
					}	
				}	
				
				throw new ConversionException("Could not convert to enum: " + text);
			}
		};
	}
	
    /**
     * @return A converter between VEnum and a String
     */
	public static Converter<VEnum, String> toEnumString() {
		return new Converter<VEnum, String>() {
			@Override
			public String convert(VEnum value) throws ConversionException {
				return value.getValue();
			}
		};
	}
	
    /**
     * @return A converter from a VString to a String
     */
	public static Converter<VString, String> fromVString() {
		return new Converter<VString, String>() {
			@Override
			public String convert(VString value) throws ConversionException {
				if (value == null) {
					throw new ConversionException("value is null");
				}
				
				return value.getValue();
			}
		};
	}

    /**
     * @return A converter from a VDouble to a double
     */
	public static Converter<VDouble, Double> fromDouble() {
		return new Converter<VDouble, Double>() {
			@Override
			public Double convert(VDouble value) {
				return value.getValue();
			}
		};
	}

    /**
     * @return A converter from a VInt to an integer
     */
	public static Converter<VInt, Integer> fromVInt() {
		return new Converter<VInt, Integer>() {
			@Override
			public Integer convert(VInt value) throws ConversionException {
				try {
					return value.getValue();
				} catch (ClassCastException e) {
					throw new ConversionException(e.getMessage());
				}
			}
		};
	}
	
    /**
     * @return A converter from a VEnum to a String
     */
	public static Converter<VEnum, String> enumValue() {
		return new Converter<VEnum, String>() {
			@Override
			public String convert(VEnum value) {
				return value.getValue();
			}
		};
	}

    /**
     * @return A converter from a VShort to a short
     */
	public static Converter<VShort, Short> fromShort() {
		return new Converter<VShort, Short>() {
			@Override
			public Short convert(VShort value) {
				return value.getValue();
			}
		};	
	}	

    /**
     * @return A converter from a VLong to a long
     */
	public static Converter<VLong, Long> fromLong() {
		return new Converter<VLong, Long>() {
			@Override
			public Long convert(VLong value) {
				return value.getValue();
			}
		};	
	}	
	
    /**
     * @return A converter from a VByte array to an array of bytes
     */
	public static Converter<VByteArray, byte[]> extractBytes() {
		return new Converter<VByteArray, byte[]>() {
			@Override
			public byte[] convert(VByteArray value) throws ConversionException {
				if (value == null) {
					throw new ConversionException("value to format was null");
				}
				
				int length = value.getSizes().iterator().nextInt();
				byte[] bytes = new byte[length];
				int count = 0;
				IteratorByte byteIterator = value.getData().iterator();
				for (int i = 0; i < length; i++) {
					bytes[i] = byteIterator.nextByte();
					count++;
					if (bytes[i] == 0) {
						break;
					}
				}
				
				return Arrays.copyOf(bytes, count);
			}
		};
	}	
	
    /**
     * @return A converter from a VFloat array to an array of floats
     */
	public static Converter<VFloatArray, float[]> extractFloats() {
		return new Converter<VFloatArray, float[]>() {
			@Override
			public float[] convert(VFloatArray value) throws ConversionException {
				if (value == null) {
					throw new ConversionException("value to format was null");
				}
				
				int length = value.getSizes().iterator().nextInt();
				float[] floats = new float[length];
				int count = 0;
				IteratorFloat floatIterator = value.getData().iterator();
				for (int i = 0; i < length; i++) {
					floats[i] = floatIterator.nextFloat();
					count++;
				}
				
				return Arrays.copyOf(floats, count);
			}
		};
	}
	
	/**
     * @return A converter from a VDouble array to an array of doubles
     */
    public static Converter<VDoubleArray, double[]> extractDoubles() {
        return new Converter<VDoubleArray, double[]>() {
            @Override
            public double[] convert(VDoubleArray value) throws ConversionException {
                if (value == null) {
                    throw new ConversionException("value to format was null");
                }
                
                int length = value.getSizes().iterator().nextInt();
                double[] doubles = new double[length];
                int count = 0;
                IteratorDouble doubleIterator = value.getData().iterator();
                for (int i = 0; i < length; i++) {
                    doubles[i] = doubleIterator.nextDouble();
                    count++;
                }
                
                return Arrays.copyOf(doubles, count);
            }
        };
    }
    
    /**
     * @return A converter from a VInteger array to an array of ints
     */
    public static Converter<VIntArray, int[]> extractIntegers() {
        return new Converter<VIntArray, int[]>() {
            @Override
            public int[] convert(VIntArray value) throws ConversionException {
                if (value == null) {
                    throw new ConversionException("value to format was null");
                }
                
                int length = value.getSizes().iterator().nextInt();
                int[] ints = new int[length];
                int count = 0;
                IteratorInt intIterator = value.getData().iterator();
                for (int i = 0; i < length; i++) {
                    ints[i] = intIterator.nextInt();
                    count++;
                }
                
                return Arrays.copyOf(ints, count);
            }
        };
    }
}
