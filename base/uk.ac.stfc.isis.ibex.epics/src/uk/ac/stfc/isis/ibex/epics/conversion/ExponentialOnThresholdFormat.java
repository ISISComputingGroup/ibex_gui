
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.epics.conversion;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * 
 */
public class ExponentialOnThresholdFormat extends NumberFormat {

    private static final String EXPONENTIAL_PATTERN = "0.####E0";
    private static final double SMALL_NUMBER_THRESHOLD = 0.001;
    private static final double BIG_NUMBER_THRESHOLD = 1000000;
    private static final int INTEGER_EXPO_FRACTIONAL_DIGITS = 4;

    private NumberFormat originalFormat;

    /**
     * 
     */
    public ExponentialOnThresholdFormat(NumberFormat originalFormat) {
        this.originalFormat = originalFormat;
    }

    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
        
        NumberFormat formatToUse = originalFormat;
        if (needsExponentialFormat(number)) {
            formatToUse = getExponentialFormatForDoubles();
        }
        
        return formatToUse.format(number, toAppendTo, pos);
    }

    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
        return originalFormat.format(number, toAppendTo, pos);
    }

    @Override
    public Number parse(String source, ParsePosition parsePosition) {
        return originalFormat.parse(source, parsePosition);
    }

    private boolean needsExponentialFormat(double number) {
        if (number == 0.0) {
            return false;
        }

        return Math.abs(number) < SMALL_NUMBER_THRESHOLD || Math.abs(number) >= BIG_NUMBER_THRESHOLD;
    }

    private NumberFormat getExponentialFormatForDoubles() {
        NumberFormat exponentialFormat = new DecimalFormat(EXPONENTIAL_PATTERN);
        exponentialFormat.setMaximumFractionDigits(originalFormat.getMaximumFractionDigits());
        exponentialFormat.setMinimumFractionDigits(originalFormat.getMinimumFractionDigits());
        return exponentialFormat;
    }
}
