package uk.ac.stfc.isis.ibex.epics.conversion;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

public final class Convert {
	
	private Convert() { }
	
	public static Converter<String, byte[]> toZippedHex() {
		return asBytes()
				.apply(compress())
				.apply(toHex())
				.apply(fromChars())
				.apply(asBytes());
	}

	public static Converter<byte[], String> fromZippedHex() {
		return fromBytes()
				.apply(asChars())
				.apply(deHex())
				.apply(decompress())
				.apply(fromBytes());
	}
	
	public static Converter<String, String> toHexString() {
		return asBytes()
				.apply(toHex())
				.apply(fromChars());
	}

	public static Converter<String, String> fromHexString() {
		return asChars()
				.apply(deHex())
				.apply(fromBytes());
	}
	
	public static Converter<byte[], byte[]> compress() {
		return new Compressor();
	}
	
	public static Converter<byte[], byte[]> decompress() {
		return new Decompressor();
	}
	
	public static Converter<byte[], char[]> toHex() {
		return new Hexer();
	}
	
	public static Converter<char[], byte[]> deHex() {
		return removeUnsetChars().apply(new Dehexer());
	}
	
	public static Converter<char[], char[]> removeUnsetChars() {
		return new Converter<char[], char[]>() {	
			@Override
			public char[] convert(char[] value) throws ConversionException {				
				return new String(value).trim().toCharArray();
			}
		};
	}
	
	public static Converter<String, String> trim() {
		return new Converter<String, String>() {
			@Override
			public String convert(String value) throws ConversionException {
				return value.trim();
			}
		};
	}
	
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
	
	public static Converter<char[], String> fromChars() {
		return new Converter<char[], String>() {
			@Override
			public String convert(char[] value) {
				return new String(value);				
			}
		};
	}

	public static Converter<String, char[]> asChars() {
		return new Converter<String, char[]>() {
			@Override
			public char[] convert(String value) {
				return value.toCharArray();				
			}
		};
	}
	
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

	public static <T> Converter<T[], Collection<T>> toCollection() {
		return new Converter<T[], Collection<T>>() {
			@Override
			public Collection<T> convert(T[] value) throws ConversionException {
				return Arrays.asList(value);
			}
		};
	}
	
	public static <T> Converter<Collection<T>, T[]> toArray(final T[] arrayOfType) {
		return new Converter<Collection<T>, T[]>() {
			@Override
			public T[] convert(Collection<T> value) throws ConversionException {
				return value.toArray(arrayOfType);
			}
		};
	}
}
