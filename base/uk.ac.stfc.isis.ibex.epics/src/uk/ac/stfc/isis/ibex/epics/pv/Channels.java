package uk.ac.stfc.isis.ibex.epics.pv;

import org.epics.vtype.VByteArray;
import org.epics.vtype.VDouble;
import org.epics.vtype.VEnum;
import org.epics.vtype.VInt;
import org.epics.vtype.VLong;
import org.epics.vtype.VNumber;
import org.epics.vtype.VShort;
import org.epics.vtype.VString;
import org.epics.vtype.VType;

import uk.ac.stfc.isis.ibex.epics.conversion.Convert;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.conversion.DateTimeFormatter;
import uk.ac.stfc.isis.ibex.epics.conversion.DefaultStringConverter;
import uk.ac.stfc.isis.ibex.epics.conversion.ElapsedTimeFormatter;
import uk.ac.stfc.isis.ibex.epics.conversion.VTypeFormat;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ConvertingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.PVInfo;
import uk.ac.stfc.isis.ibex.epics.pvmanager.PVManagerObservable;
import uk.ac.stfc.isis.ibex.epics.pvmanager.PVManagerWritable;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableWritable;
import uk.ac.stfc.isis.ibex.epics.writing.ConvertingWritable;

public class Channels {
		
	public static class Default {
		public static ClosableCachingObservable<String> reader(String pvAddress) {
			return convertObservablePV(pvAddress, VType.class, VTypeFormat.defaultFormatter());
		}
		
		public static ClosableCachingObservable<String> readerNoUnits(String pvAddress) {
			return convertObservablePV(pvAddress, VType.class, VTypeFormat.defaultFormatterNoUnits());
		}
		
		public static ClosableWritable<String> bytesWriter(String pvAddress) {
			return convertWritablePV(pvAddress, byte[].class, Convert.asBytes());
		}
		
		public static ClosableWritable<String> objectWriter(String pvAddress) {
			return convertWritablePV(pvAddress, Object.class, new DefaultStringConverter());
		}
	}
	
	// Strings
	public static class Strings {		
		public static ClosableCachingObservable<String> reader(String pvAddress) {
			return convertObservablePV(pvAddress, VString.class, VTypeFormat.fromVString());
		}
		
		public static ClosableCachingObservable<String> charWaveformReader(String pvAddress) {
			return convertObservablePV(pvAddress, VByteArray.class, VTypeFormat.fromVByteArray());
		}
		
		public static ClosableCachingObservable<String> compressedCharWaveformReader(String pvAddress) {
			return convertObservablePV(pvAddress, VByteArray.class, VTypeFormat.fromZippedHexVByteArray());
		}
		
		public static ClosableWritable<String> writer(String pvAddress) {
			return new PVManagerWritable<>(new PVInfo<>(pvAddress, String.class));
		}
		
		public static ClosableWritable<String> charWaveformWriter(String pvAddress) {
			return convertWritablePV(pvAddress, byte[].class, Convert.asBytes());
		}
		
		public static ClosableWritable<String> compressedCharWaveformWriter(String pvAddress) {
			return convertWritablePV(pvAddress, byte[].class, Convert.toZippedHex());
		}
	}
	
	// Integers
	public static class Integers {
		public static ClosableCachingObservable<Integer> reader(String pvAddress) {
			return convertObservablePV(pvAddress, VInt.class, VTypeFormat.fromVInt());
		}
	}
	
	// Floats
	public static class Floats {
		public static ClosableCachingObservable<float[]> reader(String pvAddress) {
			return convertObservablePV(pvAddress, VType.class, VTypeFormat.fromVFloatArray());
		}
	}

	// Doubles
	public static class Doubles {
		public static ClosableCachingObservable<Double> reader(String pvAddress) {
			return convertObservablePV(pvAddress, VDouble.class, VTypeFormat.fromDouble());
		}
		
		public static ClosableWritable<Double> writer(String pvAddress) {
			return new PVManagerWritable<>(new PVInfo<>(pvAddress, Double.class));
		}
	}

	// Shorts
	public static class Shorts {
		public static ClosableCachingObservable<Short> reader(String pvAddress) {
			return convertObservablePV(pvAddress, VShort.class, VTypeFormat.fromShort());
		}
	}
	
	// Doubles
	public static class Longs {
		public static ClosableCachingObservable<Long> reader(String pvAddress) {
			return convertObservablePV(pvAddress, VLong.class, VTypeFormat.fromLong());
		}
		
		public static ClosableWritable<Long> writer(String pvAddress) {
			return new PVManagerWritable<>(new PVInfo<>(pvAddress, Long.class));
		}
	}
	
	// Times
	public static class Times {
		public static ClosableCachingObservable<String> elapsedTimeReader(String pvAddress) {
			return convertObservablePV(pvAddress, VInt.class, VTypeFormat.toLong().apply(new ElapsedTimeFormatter()));
		}
		
		public static ClosableCachingObservable<String> dateTimeReader(String pvAddress) {
			return convertObservablePV(pvAddress, VString.class, VTypeFormat.fromVString().apply(new DateTimeFormatter()));
		}
	}
	
	// Numbers
	public static class Numbers {
		public static ClosableCachingObservable<Number> reader(String pvAddress) {
			return convertObservablePV(pvAddress, VNumber.class, VTypeFormat.toNumber());
		}
		
		public static ClosableCachingObservable<String> readerWithUnits(String pvAddress) {
			return convertObservablePV(pvAddress, VNumber.class, VTypeFormat.quantityWithUnits());
		}
	}
	
	// Enums
	public static class Enums {
		public static <E extends Enum<E>> ClosableCachingObservable<E> reader(String pvAddress, Class<E> enumType) {
			return convertObservablePV(pvAddress, VEnum.class, VTypeFormat.toEnum(enumType));
		}
		
		public static ClosableCachingObservable<String> reader(String pvAddress) {
			return convertObservablePV(pvAddress, VEnum.class, VTypeFormat.toEnumString());
		}
	}
	
	// Booleans
	public static class Booleans {
		public static ClosableCachingObservable<Boolean> reader(String pvAddress) {
			return new ConvertingObservable<>(Enums.reader(pvAddress), Convert.toBoolean());
		}
	}
	
	private static <V extends VType, T> ClosableCachingObservable<T> convertObservablePV(String pvAddress, Class<V> pvType, Converter<V, T> converter) {
		return new ConvertingObservable<>(new PVManagerObservable<>(new PVInfo<>(pvAddress, pvType)), converter);
		
	}

	private static <V, T> ClosableWritable<T> convertWritablePV(String pvAddress, Class<V> pvType, Converter<T, V> converter) {
		return new ConvertingWritable<>(new PVManagerWritable<>(new PVInfo<>(pvAddress, pvType)), converter);
		
	}
}
