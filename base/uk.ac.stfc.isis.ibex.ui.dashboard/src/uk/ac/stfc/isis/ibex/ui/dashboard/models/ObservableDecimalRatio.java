
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

package uk.ac.stfc.isis.ibex.ui.dashboard.models;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Pair;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;

/**
 * A class to translate an observable pair of numbers into a string
 * representation of their ratio, also an observable.
 */
public class ObservableDecimalRatio extends TransformingObservable<Pair<Number, Number>, String> {

	private static final NumberFormat FORMAT = DecimalFormat.getInstance(Locale.ENGLISH);
    private static final int DEFAULT_MAX_INTEGER_DIGITS = 3;
    private static final int DEFAULT_MAX_FRACTION_DIGITS = 3;
	
    /**
     * Creates a new instance of the class. Maps an input observable pair of
     * numbers into an observable string representation of their ratio.
     * 
     * @param source the pair of numbers to observe
     */
	public ObservableDecimalRatio(ClosableObservable<Pair<Number, Number>> source) {
        this(source, DEFAULT_MAX_INTEGER_DIGITS, DEFAULT_MAX_FRACTION_DIGITS);
	}
	
    /**
     * Creates a new instance of the class. Maps an input observable pair of
     * numbers into an observable string representation of their ratio.
     * 
     * @param source the pair of numbers to observe
     * @param maxIntegerDigits how many digits to be used for the integer part
     * @param maxFractionDigits how many digits to be used for the fraction part
     */
    public ObservableDecimalRatio(ClosableObservable<Pair<Number, Number>> source, int maxIntegerDigits,
            int maxFractionDigits) {
        FORMAT.setMaximumIntegerDigits(maxIntegerDigits);
        FORMAT.setMaximumFractionDigits(maxFractionDigits);
        setSource(source);

    }

	@Override
	protected String transform(Pair<Number, Number> value) {		
		if (value.first == null || value.second == null) {
			return "Unknown";
		}
		
		return format(value.first) + " / " + format(value.second);
	}

	private String format(Number value) {
		return FORMAT.format(value);
	}
}
