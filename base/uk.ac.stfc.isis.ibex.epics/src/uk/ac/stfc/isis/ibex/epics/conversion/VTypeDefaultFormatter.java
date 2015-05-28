package uk.ac.stfc.isis.ibex.epics.conversion;

import org.epics.vtype.SimpleValueFormat;
import org.epics.vtype.VByteArray;
import org.epics.vtype.VNumber;
import org.epics.vtype.VString;
import org.epics.vtype.VType;
import org.epics.vtype.ValueFormat;

public class VTypeDefaultFormatter<T extends VType> {

	public final Converter<T, String> withUnits = new WithUnits<T>();
	public final Converter<T, String> noUnits = new NoUnits<T>();
	
	private class WithUnits<R extends VType> extends Converter<R, String> {
		@Override
		public String convert(VType value) throws ConversionException {	

			if (value instanceof VNumber) {
				VNumber vnum = (VNumber) value;	 
				return asString(vnum) + " " + vnum.getUnits();
			}
			
			return format(value);
		}
	}

	private class NoUnits <R extends VType> extends Converter<R, String> {
		@Override
		public String convert(VType value) throws ConversionException {	

			if (value instanceof VNumber) {
				VNumber vnum = (VNumber) value;	 
				return asString(vnum);
			}
			
			return format(value);
		}
	}
	
	public String asString(VNumber vnum) {
		return vnum.getFormat().format(vnum.getValue());
	}

	private static String defaultValueFormat(Object value) {
		//Note for arrays this will only display items [0...10]
		ValueFormat valueFormat = new SimpleValueFormat(10);	
		return valueFormat.format(value);	
	}

	private static String format(VType value) throws ConversionException {
		if (value instanceof VString) {
			return ((VString) value).getValue();
		}
		
		if (value instanceof VByteArray) {
			return VTypeFormat.fromVByteArray().convert((VByteArray)value);
		}
		
		return defaultValueFormat(value);
	}
}
