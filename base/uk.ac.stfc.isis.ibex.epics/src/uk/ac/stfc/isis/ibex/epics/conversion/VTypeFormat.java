
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
import java.util.function.Function;

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
	public static <R extends VType> Function<R, String> defaultFormatter() {
		return new VTypeDefaultFormatter<R>().withUnits;
	}

    /**
     * @param <R> conversion source object type
     * @return A converter between the original object and a string without
     *         units
     */
	public static <R extends VType> Function<R, String> defaultFormatterNoUnits() {
		return new VTypeDefaultFormatter<R>().noUnits;
	}
	
	/**
     * @param <R> conversion source object type
     * @return a number with no consideration for formatting or precision
     */
	public static <R extends VNumber> Function<R, Number> toNumber() {
		return new Function<R, Number>() {
			@Override
			public Number apply(R value) throws ConversionException {
				return value.getValue();
			}
		};
	}
	
	/**
     * @param <R> conversion source object type
     * @return a string of the number supplied with any associated units
     */
	public static <R extends VNumber> Function<R, String> quantityWithUnits() {
		return new Function<R, String>() {
			@Override
			public String apply(R value) throws ConversionException {
				return value.getFormat().format(value.getValue()) + " " + value.getUnits();
			}
		};
	}
	
	/**
     * @param <R> conversion source object type
     * @return a number formatted with the precision defined by the underlying
     *         PV
     */
	public static <R extends VNumber> Function<R, Number> toNumberWithPrecision() {
		return new Function<R, Number>() {
			@Override
			public Number apply(R value) throws ConversionException {
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
	public static Function<VByteArray, String> fromVByteArray() {
		return extractBytes()
				.andThen(Convert.fromBytes())
				.andThen(Convert.trim());
	}	

    /**
     * @return Converter from a VFloatArray to a float array
     */
	public static Function<VType, float[]> fromVFloatArray() {
		return toVFloatArray()
				.andThen(extractFloats());
	}
	
	/**
     * @return Converter from a VDoubleArray to a double array
     */
    public static Function<VType, double[]> fromVDoubleArray() {
        return toVDoubleArray()
                .andThen(extractDoubles());
    }
    
    /**
     * @return Converter from a VIntArray to an int array
     */
    public static Function<VType, int[]> fromVIntegerArray() {
        return toVIntegerArray()
                .andThen(extractIntegers());
    }
	
    /**
     * @return Converter from a VByteArray of zipped, hexed values to a String
     */
	public static Function<VByteArray, String> fromZippedHexVByteArray() {
		return extractBytes().andThen(Convert.fromZippedHex());
	}

    /**
     * @return Converter from a VType to a VFloatArray
     */
	public static Function<VType, VFloatArray> toVFloatArray() {
		return new Function<VType, VFloatArray>() {
			@Override
			public VFloatArray apply(VType value) throws ConversionException {
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
    public static Function<VType, VDoubleArray> toVDoubleArray() {
        return new Function<VType, VDoubleArray>() {
            @Override
            public VDoubleArray apply(VType value) throws ConversionException {
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
    public static Function<VType, VIntArray> toVIntegerArray() {
        return new Function<VType, VIntArray>() {
            @Override
            public VIntArray apply(VType value) throws ConversionException {
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
	public static Function<VType, VByteArray> toVByteArray() {
		return new Function<VType, VByteArray>() {
			@Override
			public VByteArray apply(VType value) throws ConversionException {
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
	public static Function<VType, VInt> toVInt() {
		return new Function<VType, VInt>() {
			@Override
			public VInt apply(VType value) throws ConversionException {
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
	public static Function<VInt, Long> toLong() {
		return new Function<VInt, Long>() {
			@Override
			public Long apply(VInt value) throws ConversionException {
				return Long.valueOf(value.getValue());
			}
		};
	}

    /**
     * @param <E> The type of the enumerator
     * @param enumType The class describing the type of the enumerator
     * @return A converter from VEnum to an enumerator of the given type
     */
	public static <E extends Enum<E>> Function<VEnum, E> toEnum(final Class<E> enumType) {
		return new Function<VEnum, E>() {
			@Override
			public E apply(VEnum value) throws ConversionException {
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
	public static Function<VEnum, String> toEnumString() {
		return new Function<VEnum, String>() {
			@Override
			public String apply(VEnum value) throws ConversionException {
				return value.getValue();
			}
		};
	}
	
    /**
     * @return A converter from a VString to a String
     */
	public static Function<VString, String> fromVString() {
		return new Function<VString, String>() {
			@Override
			public String apply(VString value) throws ConversionException {
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
	public static Function<VDouble, Double> fromDouble() {
		return new Function<VDouble, Double>() {
			@Override
			public Double apply(VDouble value) {
				return value.getValue();
			}
		};
	}

    /**
     * @return A converter from a VInt to an integer
     */
	public static Function<VInt, Integer> fromVInt() {
		return new Function<VInt, Integer>() {
			@Override
			public Integer apply(VInt value) throws ConversionException {
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
	public static Function<VEnum, String> enumValue() {
		return new Function<VEnum, String>() {
			@Override
			public String apply(VEnum value) {
				return value.getValue();
			}
		};
	}

    /**
     * @return A converter from a VShort to a short
     */
	public static Function<VShort, Short> fromShort() {
		return new Function<VShort, Short>() {
			@Override
			public Short apply(VShort value) {
				return value.getValue();
			}
		};	
	}	

    /**
     * @return A converter from a VLong to a long
     */
	public static Function<VLong, Long> fromLong() {
		return new Function<VLong, Long>() {
			@Override
			public Long apply(VLong value) {
				return value.getValue();
			}
		};	
	}	
	
    /**
     * @return A converter from a VByte array to an array of bytes
     */
	public static Function<VByteArray, byte[]> extractBytes() {
		return new Function<VByteArray, byte[]>() {
			@Override
			public byte[] apply(VByteArray value) throws ConversionException {
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
	public static Function<VFloatArray, float[]> extractFloats() {
		return new Function<VFloatArray, float[]>() {
			@Override
			public float[] apply(VFloatArray value) throws ConversionException {
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
    public static Function<VDoubleArray, double[]> extractDoubles() {
        return new Function<VDoubleArray, double[]>() {
            @Override
            public double[] apply(VDoubleArray value) throws ConversionException {
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
    public static Function<VIntArray, int[]> extractIntegers() {
        return new Function<VIntArray, int[]>() {
            @Override
            public int[] apply(VIntArray value) throws ConversionException {
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
