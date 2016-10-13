
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
package uk.ac.stfc.isis.ibex.epics.tests.conversion;

import static org.junit.Assert.assertEquals;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.conversion.ExponentialOnThresholdFormat;

@SuppressWarnings({ "checkstyle:methodname" })
public class ExponentialOnThresholdFormatTest {

    private static final String THREE_FRACTION_DIGITS_PATTERN = "0.000";
    private static final String FIVE_FRACTION_DIGITS_PATTERN = "0.00000";
    private static final String INTEGER_PATTERN = "#.##";

    private NumberFormat threeFractionalDigitsFormat = new DecimalFormat(THREE_FRACTION_DIGITS_PATTERN);
    private NumberFormat fiveFractionalDigitsFormat = new DecimalFormat(FIVE_FRACTION_DIGITS_PATTERN);
    private NumberFormat integerFormat = new DecimalFormat(INTEGER_PATTERN);

    private void verifyNonExponentialFormat(double value, String expected, NumberFormat sourceFormat) {
        // Arrange
        NumberFormat format = new ExponentialOnThresholdFormat(sourceFormat);

        // Act-Assert
        assertEquals(expected, format.format(value));
        assertEquals(sourceFormat.format(value), format.format(value));
    }

    private void verifyExponentialFormat(double value, String expected, NumberFormat sourceFormat) {
        // Arrange
        NumberFormat format = new ExponentialOnThresholdFormat(sourceFormat);

        // Act-Assert
        assertEquals(expected, format.format(value));
    }

    private void verifyNonExponentialFormat(int value, String expected, NumberFormat sourceFormat) {
        // Arrange
        NumberFormat format = new ExponentialOnThresholdFormat(sourceFormat);

        // Act-Assert
        assertEquals(expected, format.format(value));
        assertEquals(sourceFormat.format(value), format.format(value));
    }

    private void verifyExponentialFormat(int value, String expected, NumberFormat sourceFormat) {
        // Arrange
        NumberFormat format = new ExponentialOnThresholdFormat(sourceFormat);

        // Act-Assert
        assertEquals(expected, format.format(value));
    }

    @Test
    public void
            GIVEN_value_0_1_above_lower_threshold_WHEN_formatting_with_precision_3_THEN_no_exponential_notation_used() {
        // Arrange
        double value = 0.1;
        String expected = "0.100";

        // Act-Assert
        verifyNonExponentialFormat(value, expected, threeFractionalDigitsFormat);
    }

    @Test
    public void
            GIVEN_negative_value_0_1_above_lower_threshold_WHEN_formatting_with_precision_3_THEN_no_exponential_notation_used() {
        // Arrange
        double value = -0.1;
        String expected = "-0.100";

        // Act-Assert
        verifyNonExponentialFormat(value, expected, threeFractionalDigitsFormat);
    }

    @Test
    public void
            GIVEN_value_0_01_above_lower_threshold_WHEN_formatting_with_precision_3_THEN_no_exponential_notation_used() {
        // Arrange
        double value = 0.01;
        String expected = "0.010";

        // Act-Assert
        verifyNonExponentialFormat(value, expected, threeFractionalDigitsFormat);
    }

    @Test
    public void
            GIVEN_value_equal_to_lower_threshold_WHEN_formatting_with_precision_3_THEN_no_exponential_notation_used() {
        // Arrange
        double value = 0.001;
        String expected = "0.001";

        // Act-Assert
        verifyNonExponentialFormat(value, expected, threeFractionalDigitsFormat);
    }

    @Test
    public void
            GIVEN_negative_value_equal_to_lower_threshold_WHEN_formatting_with_precision_3_THEN_no_exponential_notation_used() {
        // Arrange
        double value = -0.001;
        String expected = "-0.001";

        // Act-Assert
        verifyNonExponentialFormat(value, expected, threeFractionalDigitsFormat);
    }

    @Test
    public void
            GIVEN_value_above_lower_threshold_with_extra_fractioanl_digits_WHEN_formatting_with_precision_3_THEN_no_exponential_notation_used_and_some_digits_are_lost() {
        // Arrange
        double value = 0.01234;
        String expected = "0.012";

        // Act-Assert
        verifyNonExponentialFormat(value, expected, threeFractionalDigitsFormat);
    }

    @Test
    public void
            GIVEN_value_above_lower_threshold_with_integer_part_WHEN_formatting_with_precision_5_THEN_no_exponential_notation_used() {
        // Arrange
        double value = 1234.56;
        String expected = "1234.56000";

        // Act-Assert
        verifyNonExponentialFormat(value, expected, fiveFractionalDigitsFormat);
    }

    @Test
    public void
            GIVEN_value_above_lower_threshold_with_integer_part_and_extra_fractional_digits_WHEN_formatting_with_precision_3_THEN_no_exponential_notation_used() {
        // Arrange
        double value = 12.5678;
        String expected = "12.568";

        // Act-Assert
        verifyNonExponentialFormat(value, expected, threeFractionalDigitsFormat);
    }

    @Test
    public void
            GIVEN_value_below_lower_threshold_WHEN_formatting_with_precision_3_THEN_exponential_notation_used() {
        // Arrange
        double value = 0.0009;
        String expected = "9.000E-4";

        // Act-Assert
        verifyExponentialFormat(value, expected, threeFractionalDigitsFormat);
    }

    @Test
    public void GIVEN_very_small_value_WHEN_formatting_with_precision_5_THEN_exponential_notation_used() {
        // Arrange
        double value = 0.000000001234567;
        String expected = "1.23457E-9";

        // Act-Assert
        verifyExponentialFormat(value, expected, fiveFractionalDigitsFormat);
    }

    @Test
    public void GIVEN_very_small_negative_value_WHEN_formatting_with_precision_5_THEN_exponential_notation_used() {
        // Arrange
        double value = -0.000000001234567;
        String expected = "-1.23457E-9";

        // Act-Assert
        verifyExponentialFormat(value, expected, fiveFractionalDigitsFormat);
    }

    @Test
    public void
            GIVEN_value_just_below_higher_threshold_WHEN_formatting_with_precision_3_THEN_no_exponential_notation_used() {
        // Arrange
        double value = 999999.1234;
        String expected = "999999.123";

        // Act-Assert
        verifyNonExponentialFormat(value, expected, threeFractionalDigitsFormat);
    }
    
    @Test
    public void
            GIVEN_negative_value_just_below_higher_threshold_WHEN_formatting_with_precision_3_THEN_no_exponential_notation_used() {
        // Arrange
        double value = -999999.1234;
        String expected = "-999999.123";

        // Act-Assert
        verifyNonExponentialFormat(value, expected, threeFractionalDigitsFormat);
    }

    @Test
    public void
            GIVEN_value_equal_to_higher_threshold_WHEN_formatting_with_precision_3_THEN_exponential_notation_used() {
        // Arrange
        double value = 1000000;
        String expected = "1.000E6";

        // Act-Assert
        verifyExponentialFormat(value, expected, threeFractionalDigitsFormat);
    }

    @Test
    public void
            GIVEN_negative_value_equal_to_higher_threshold_WHEN_formatting_with_precision_3_THEN_exponential_notation_used() {
        // Arrange
        double value = -1000000;
        String expected = "-1.000E6";

        // Act-Assert
        verifyExponentialFormat(value, expected, threeFractionalDigitsFormat);
    }

    @Test
    public void GIVEN_value_above_higher_threshold_WHEN_formatting_with_precision_5_THEN_exponential_notation_used() {
        // Arrange
        double value = 123456789;
        String expected = "1.23457E8";

        // Act-Assert
        verifyExponentialFormat(value, expected, fiveFractionalDigitsFormat);
    }

    @Test
    public void
            GIVEN_negative_value_above_higher_threshold_WHEN_formatting_with_precision_5_THEN_exponential_notation_used() {
        // Arrange
        double value = -123456789;
        String expected = "-1.23457E8";

        // Act-Assert
        verifyExponentialFormat(value, expected, fiveFractionalDigitsFormat);
    }

    @Test
    public void GIVEN_value_0_WHEN_formatting_THEN_no_exponential_notation_used() {
        // Arrange
        double value = 0;
        String expected = "0.000";

        // Act-Assert
        verifyNonExponentialFormat(value, expected, threeFractionalDigitsFormat);
    }

    @Test
    public void GIVEN_negative_value_0_WHEN_formatting_THEN_no_exponential_notation_used() {
        // Arrange
        double value = -0;
        String expected = "0.000";

        // Act-Assert
        verifyNonExponentialFormat(value, expected, threeFractionalDigitsFormat);
    }

    @Test
    public void GIVEN_integer_below_higher_threshold_WHEN_formatting_THEN_no_exponential_notation_used() {
        // Arrange
        int value = 13;
        String expected = "13";

        // Act-Assert
        verifyNonExponentialFormat(value, expected, integerFormat);
    }

    @Test
    public void GIVEN_negative_integer_below_higher_threshold_WHEN_formatting_THEN_no_exponential_notation_used() {
        // Arrange
        int value = -13;
        String expected = "-13";

        // Act-Assert
        verifyNonExponentialFormat(value, expected, integerFormat);
    }

    @Test
    public void
            GIVEN_integer_just_below_higher_threshold_WHEN_formatting_THEN_no_exponential_notation_used() {
        // Arrange
        int value = 999999;
        String expected = "999999";

        // Act-Assert
        verifyNonExponentialFormat(value, expected, integerFormat);
    }

    @Test
    public void
            GIVEN_integer_equal_to_higher_threshold_WHEN_formatting_THEN_no_exponential_notation_used() {
        // Arrange
        int value = 1000000;
        String expected = "1000000";

        // Act-Assert
        verifyExponentialFormat(value, expected, integerFormat);
    }

    @Test
    public void GIVEN_negative_integer_equal_to_higher_threshold_WHEN_formatting_THEN_no_exponential_notation_used() {
        // Arrange
        int value = -1000000;
        String expected = "-1000000";

        // Act-Assert
        verifyExponentialFormat(value, expected, integerFormat);
    }

    @Test
    public void GIVEN_integer_above_higher_threshold_WHEN_formatting_THEN_no_exponential_notation_used() {
        // Arrange
        int value = 123456789;
        String expected = "123456789";

        // Act-Assert
        verifyExponentialFormat(value, expected, integerFormat);
    }

    @Test
    public void GIVEN_negative_integer_above_higher_threshold_WHEN_formatting_THEN_no_exponential_notation_used() {
        // Arrange
        int value = -123456789;
        String expected = "-123456789";

        // Act-Assert
        verifyExponentialFormat(value, expected, integerFormat);
    }

    @Test
    public void GIVEN_integer_equal_to_0_WHEN_formatting_THEN_no_exponential_notation_used() {
        // Arrange
        int value = 0;
        String expected = "0";

        // Act-Assert
        verifyNonExponentialFormat(value, expected, integerFormat);
    }

    @Test
    public void GIVEN_negative_integer_equal_to_0_WHEN_formatting_THEN_no_exponential_notation_used() {
        // Arrange
        int value = -0;
        String expected = "0";

        // Act-Assert
        verifyNonExponentialFormat(value, expected, integerFormat);
    }

}
