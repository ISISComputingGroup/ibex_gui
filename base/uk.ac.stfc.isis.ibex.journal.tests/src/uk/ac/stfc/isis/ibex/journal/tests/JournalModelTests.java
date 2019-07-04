
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
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

package uk.ac.stfc.isis.ibex.journal.tests;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.journal.JournalSearchParameters;

/**
 * Tests parts of the journal model.
 *
 */
@SuppressWarnings("checkstyle:methodname")
public class JournalModelTests {
    private JournalSearchParameters params;
    private static final String TEST_STRING = "Tuturu!";
    
    @Before
    public void setup() {
        params = new JournalSearchParameters();
    }
    
    @Test
    public void test_WHEN_parameter_fields_set_once_THEN_parameters_set() {
        params.setSearchString(Optional.of(TEST_STRING));
        
        assertEquals(params.getSearchString(), Optional.of(TEST_STRING));
    }
    
    @Test(expected = IllegalStateException.class)
    public void test_WHEN_parameter_fields_set_twice_THEN_exception_thrown() {
        params.setSearchString(Optional.of(TEST_STRING));
        params.setNumbers(Optional.of(0), Optional.of(1));
    }
}
