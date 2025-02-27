
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2024
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.epics.conversion;

import java.text.NumberFormat;
import java.util.function.Function;

import org.diirt.vtype.SimpleValueFormat;
import org.diirt.vtype.VByteArray;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VString;
import org.diirt.vtype.VType;
import org.diirt.vtype.ValueFormat;

/**
 * The default formatter for VType to String.
 * 
 * @param <T> the type of the input
 */
public class VTypeDefaultFormatter<T extends VType> {

    private static final int ARRAY_ELEMENTS_TO_DISPLAY = 10;

    /**
     * Returns a converter for formatting the input and its units.
     */
    public final Function<T, String> withUnits = new WithUnits<T>();

    /**
     * Returns a converter for formatting the input without units.
     */
	public final Function<T, String> noUnits = new NoUnits<T>();
	
	private final class WithUnits<R extends VType> implements Function<R, String> {
		@Override
		public String apply(VType value) throws ConversionException {	

			if (value instanceof VNumber) {
				VNumber vnum = (VNumber) value;	 
				return asString(vnum) + " " + vnum.getUnits();
			}
			
			return format(value);
		}
	}

	private final class NoUnits<R extends VType> implements Function<R, String> {
		@Override
		public String apply(VType value) throws ConversionException {	
		
			if (value instanceof VNumber) {
				VNumber vnum = (VNumber) value;
				String asisStringValue = String.valueOf(vnum.getValue());
				String formattedStringValue = asString(vnum);
				/*
				 * For a numeric value, if formatting increases the length as compared to the original, then we are only padding zeroes.
				 * In this case we don't format
				 */
				return (asisStringValue.length() < formattedStringValue.length()) ? asisStringValue : formattedStringValue;
				//return asString(vnum);
			}
			return format(value);
		}
	}
	
    private String asString(VNumber vnum) {
        NumberFormat exponentialWhenNeededFormat = new ExponentialOnThresholdFormat(vnum.getFormat());
        return exponentialWhenNeededFormat.format(vnum.getValue());
	}

	private static String defaultValueFormat(Object value) {
		//Note for arrays this will only display items [0...10]
        ValueFormat valueFormat = new SimpleValueFormat(ARRAY_ELEMENTS_TO_DISPLAY);
		return valueFormat.format(value);	
	}

	private static String format(VType value) throws ConversionException {
		if (value instanceof VString) {
			return ((VString) value).getValue();
		}
		
		if (value instanceof VByteArray) {
			return VTypeFormat.fromVByteArray().apply((VByteArray) value);
		}
		
		return defaultValueFormat(value);
	}
}
