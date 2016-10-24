
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

package uk.ac.stfc.isis.ibex.instrument.internal;

import static org.junit.Assert.*;

import java.security.InvalidParameterException;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("checkstyle:methodname")
public class CRC8Test {

    private void calc_and_test(String value, String expected) {
        CRC8 actual = CRC8.fromString(value);
        assertEquals(expected, actual.toString());
    }

    @Before
    public void setUp() {
        
    }

    @Test
    public void GIVEN_empty_string_WHEN_get_CRC8_THEN_exception() {
        try {
            CRC8.fromString("");
        } catch (InvalidParameterException ex) {
            return;
        }
        fail("Should have thrown invalid parameter exception");

    }

    @Test
    public void GIVEN_a_WHEN_get_CRC8_THEN_result_correct() {
        this.calc_and_test("a", "20");
    }
    
    @Test
    public void GIVEN_b_WHEN_get_CRC8_THEN_result_correct() {
        this.calc_and_test("b", "29");
    }

    @Test
    public void GIVEN_string_WHEN_get_CRC8_THEN_result_correct() {
        this.calc_and_test("hello world", "A8");
    }

    @Test
    public void GIVEN_string_returning_hex_letters_WHEN_get_CRC8_THEN_result_correct() {
        this.calc_and_test("NDW1407", "EB");
    }

    @Test
    public void GIVEN_string_which_gives_hex_less_than_10_WHEN_get_CRC8_THEN_result_correct() {
        this.calc_and_test("l", "03");
    }

}

