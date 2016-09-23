
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
package uk.ac.stfc.isis.ibex.ui.dashboard.tests.models;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Pair;
import uk.ac.stfc.isis.ibex.ui.dashboard.models.ObservableDecimalRatio;

@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class ObservableDecimalRatioTest {

    private ClosableObservable<Pair<Number, Number>> mockSource;

    @Before
    public void setUp() {
        // Arrange
        mockSource = mock(ClosableObservable.class);
        when(mockSource.currentError()).thenReturn(null);
        when(mockSource.isConnected()).thenReturn(true);
    }

    private void setSourceNumbers(double first, double second) {
        Pair<Number, Number> pair = new Pair<Number, Number>(first, second);
        when(mockSource.getValue()).thenReturn(pair);
    }

    @Test
    public void
            GIVEN_integer_source_numbers_WHEN_they_are_converted_with_default_settings_THEN_formatting_is_correct() {
        // Arrange
        setSourceNumbers(3, 4);

        // Act
        ObservableDecimalRatio ratio = new ObservableDecimalRatio(mockSource);

        // Assert
        assertEquals("3 / 4", ratio.getValue());
    }

    @Test
    public void
            GIVEN_fractional_source_numbers_WHEN_they_are_converted_with_default_settings_THEN_formatting_is_correct() {
        // Arrange
        setSourceNumbers(123.456, 456.789);

        // Act
        ObservableDecimalRatio ratio = new ObservableDecimalRatio(mockSource);

        // Assert
        assertEquals("123.456 / 456.789", ratio.getValue());
    }

    @Test
    public void
            GIVEN_fractional_source_numbers_exceeding_max_fractional_digits_WHEN_they_are_converted_with_default_settings_THEN_formatting_is_correct() {
        // Arrange
        setSourceNumbers(123.4567, 456.7899);

        // Act
        ObservableDecimalRatio ratio = new ObservableDecimalRatio(mockSource);

        // Assert
        assertEquals("123.457 / 456.79", ratio.getValue());
    }

    @Test
    public void
            GIVEN_fractional_source_numbers_exceeding_max_integer_digits_WHEN_they_are_converted_with_default_settings_THEN_formatting_is_correct() {
        // Arrange
        setSourceNumbers(1234.567, 4567.899);

        // Act
        ObservableDecimalRatio ratio = new ObservableDecimalRatio(mockSource);

        // Assert
        assertEquals("234.567 / 567.899", ratio.getValue());
    }

    @Test
    public void
            GIVEN_integer_source_numbers_WHEN_they_are_converted_with_custom_settings_THEN_formatting_is_correct() {
        // Arrange
        setSourceNumbers(3, 4);
        int maxIntDigits = 5;
        int maxFracDigits = 4;

        // Act
        ObservableDecimalRatio ratio = new ObservableDecimalRatio(mockSource, maxIntDigits, maxFracDigits);

        // Assert
        assertEquals("3 / 4", ratio.getValue());
    }

    @Test
    public void
            GIVEN_fractional_source_numbers_WHEN_they_are_converted_with_custom_settings_THEN_formatting_is_correct() {
        // Arrange
        setSourceNumbers(12345.4567, 45656.7899);
        int maxIntDigits = 5;
        int maxFracDigits = 4;

        // Act
        ObservableDecimalRatio ratio = new ObservableDecimalRatio(mockSource, maxIntDigits, maxFracDigits);

        // Assert
        assertEquals("12,345.4567 / 45,656.7899", ratio.getValue());
    }

    @Test
    public void
            GIVEN_fractional_source_numbers_exceeding_max_fractional_digits_WHEN_they_are_converted_with_custom_settings_THEN_formatting_is_correct() {
        // Arrange
        setSourceNumbers(123.45678, 456.78992);
        int maxIntDigits = 5;
        int maxFracDigits = 4;

        // Act
        ObservableDecimalRatio ratio = new ObservableDecimalRatio(mockSource, maxIntDigits, maxFracDigits);

        // Assert
        assertEquals("123.4568 / 456.7899", ratio.getValue());
    }

    @Test
    public void
            GIVEN_fractional_source_numbers_exceeding_max_integer_digits_WHEN_they_are_converted_with_custom_settings_THEN_formatting_is_correct() {
        // Arrange
        setSourceNumbers(123456.567, 456712.899);
        int maxIntDigits = 5;
        int maxFracDigits = 4;

        // Act
        ObservableDecimalRatio ratio = new ObservableDecimalRatio(mockSource, maxIntDigits, maxFracDigits);

        // Assert
        assertEquals("23,456.567 / 56,712.899", ratio.getValue());
    }
}
