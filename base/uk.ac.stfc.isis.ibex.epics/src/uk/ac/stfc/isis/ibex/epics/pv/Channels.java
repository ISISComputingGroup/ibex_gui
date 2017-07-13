
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

package uk.ac.stfc.isis.ibex.epics.pv;

import org.diirt.vtype.VByteArray;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VEnum;
import org.diirt.vtype.VInt;
import org.diirt.vtype.VLong;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VShort;
import org.diirt.vtype.VString;
import org.diirt.vtype.VType;

import uk.ac.stfc.isis.ibex.epics.conversion.Convert;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.conversion.DateTimeFormatter;
import uk.ac.stfc.isis.ibex.epics.conversion.DefaultStringConverter;
import uk.ac.stfc.isis.ibex.epics.conversion.ElapsedTimeFormatter;
import uk.ac.stfc.isis.ibex.epics.conversion.VTypeFormat;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ConvertingObservable;
import uk.ac.stfc.isis.ibex.epics.pvmanager.PVManagerObservable;
import uk.ac.stfc.isis.ibex.epics.pvmanager.PVManagerWritable;
import uk.ac.stfc.isis.ibex.epics.writing.BaseWritable;
import uk.ac.stfc.isis.ibex.epics.writing.ForwardingWritable;

/**
 * Contains all the different channel types used for communicating with PVs.
 *
 */
public class Channels {
		
	public static class Default {
		public static ClosableObservable<String> reader(String pvAddress) {
			return convertObservablePV(pvAddress, VType.class, VTypeFormat.defaultFormatter());
		}
		
		public static ClosableObservable<String> readerNoUnits(String pvAddress) {
			return convertObservablePV(pvAddress, VType.class, VTypeFormat.defaultFormatterNoUnits());
		}
		
        public static BaseWritable<String> bytesWriter(String pvAddress) {
			return convertWritablePV(pvAddress, byte[].class, Convert.asBytes());
		}
		
        public static BaseWritable<String> objectWriter(String pvAddress) {
			return convertWritablePV(pvAddress, Object.class, new DefaultStringConverter());
		}
	}
	

	/**
	 * String I/O.
	 */
	public static class Strings {		
		public static ClosableObservable<String> reader(String pvAddress) {
			return convertObservablePV(pvAddress, VString.class, VTypeFormat.fromVString());
		}
		
		public static ClosableObservable<String> charWaveformReader(String pvAddress) {
			return convertObservablePV(pvAddress, VByteArray.class, VTypeFormat.fromVByteArray());
		}
		
		public static ClosableObservable<String> compressedCharWaveformReader(String pvAddress) {
			return convertObservablePV(pvAddress, VByteArray.class, VTypeFormat.fromZippedHexVByteArray());
		}
		
        public static BaseWritable<String> writer(String pvAddress) {
			return new PVManagerWritable<>(new PVInfo<>(pvAddress, String.class));
		}
		
        public static BaseWritable<String> charWaveformWriter(String pvAddress) {
			return convertWritablePV(pvAddress, byte[].class, Convert.asBytes());
		}
		
        public static BaseWritable<String> compressedCharWaveformWriter(String pvAddress) {
			return convertWritablePV(pvAddress, byte[].class, Convert.toZippedHex());
		}
	}
	
	/**
	 * Integer I/O.
	 */
	public static class Integers {
		public static ClosableObservable<Integer> reader(String pvAddress) {
			return convertObservablePV(pvAddress, VInt.class, VTypeFormat.fromVInt());
		}
		
		public static ClosableObservable<int[]> arrayReader(String pvAddress) {
            return convertObservablePV(pvAddress, VType.class, VTypeFormat.fromVIntegerArray());
        }
		
		public static BaseWritable<Integer> writer(String pvAddress) {
            return new PVManagerWritable<>(new PVInfo<>(pvAddress, Integer.class));
        }
	}
	
	/**
	 * Float I/O.
	 */
	public static class Floats {
		public static ClosableObservable<float[]> reader(String pvAddress) {
			return convertObservablePV(pvAddress, VType.class, VTypeFormat.fromVFloatArray());
		}
	}

	/**
	 * Double I/O.
	 */
	public static class Doubles {
		public static ClosableObservable<Double> reader(String pvAddress) {
			return convertObservablePV(pvAddress, VDouble.class, VTypeFormat.fromDouble());
		}
		
        public static BaseWritable<Double> writer(String pvAddress) {
			return new PVManagerWritable<>(new PVInfo<>(pvAddress, Double.class));
		}
        
        public static ClosableObservable<double[]> arrayReader(String pvAddress) {
            return convertObservablePV(pvAddress, VType.class, VTypeFormat.fromVDoubleArray());
        }
	}

	/**
	 * Short I/O.
	 */
	public static class Shorts {
		public static ClosableObservable<Short> reader(String pvAddress) {
			return convertObservablePV(pvAddress, VShort.class, VTypeFormat.fromShort());
		}
	}
	
	/**
	 * Long I/O.
	 */
	public static class Longs {
		public static ClosableObservable<Long> reader(String pvAddress) {
			return convertObservablePV(pvAddress, VLong.class, VTypeFormat.fromLong());
		}
		
        public static BaseWritable<Long> writer(String pvAddress) {
			return new PVManagerWritable<>(new PVInfo<>(pvAddress, Long.class));
		}
	}
	
	/**
	 * Time readers.
	 */
	public static class Times {
		public static ClosableObservable<String> elapsedTimeReader(String pvAddress) {
			return convertObservablePV(pvAddress, VInt.class, VTypeFormat.toLong().apply(new ElapsedTimeFormatter()));
		}
		
		public static ClosableObservable<String> dateTimeReader(String pvAddress) {
			return convertObservablePV(pvAddress, VString.class, VTypeFormat.fromVString().apply(new DateTimeFormatter()));
		}
	}
	
	/**
	 * Number readers.
	 */
	public static class Numbers {
		public static ClosableObservable<Number> reader(String pvAddress) {
			return convertObservablePV(pvAddress, VNumber.class, VTypeFormat.toNumber());
		}
		
		public static ClosableObservable<String> readerWithUnits(String pvAddress) {
			return convertObservablePV(pvAddress, VNumber.class, VTypeFormat.quantityWithUnits());
		}
		
		public static ClosableObservable<Number> readerWithPrecision(String pvAddress) {
			return convertObservablePV(pvAddress, VNumber.class, VTypeFormat.toNumberWithPrecision());
		}
	}
	
	/**
	 * Enum readers.
	 */
	public static class Enums {
		public static <E extends Enum<E>> ClosableObservable<E> reader(String pvAddress, Class<E> enumType) {
			return convertObservablePV(pvAddress, VEnum.class, VTypeFormat.toEnum(enumType));
		}
		
		public static ClosableObservable<String> reader(String pvAddress) {
			return convertObservablePV(pvAddress, VEnum.class, VTypeFormat.toEnumString());
		}
	}
	
	/**
	 * Boolean reader.
	 */
	public static class Booleans {
		public static ClosableObservable<Boolean> reader(String pvAddress) {
			return new ConvertingObservable<>(Enums.reader(pvAddress), Convert.toBoolean());
		}
	}
	
	private static <V extends VType, T> ClosableObservable<T> convertObservablePV(String pvAddress, Class<V> pvType, Converter<V, T> converter) {
		return new ConvertingObservable<>(new PVManagerObservable<>(new PVInfo<>(pvAddress, pvType)), converter);		
	}

    private static <V, T> BaseWritable<T> convertWritablePV(String pvAddress, Class<V> pvType,
            Converter<T, V> converter) {
        return new ForwardingWritable<>(new PVManagerWritable<>(new PVInfo<>(pvAddress, pvType)), converter);
		
	}
}
