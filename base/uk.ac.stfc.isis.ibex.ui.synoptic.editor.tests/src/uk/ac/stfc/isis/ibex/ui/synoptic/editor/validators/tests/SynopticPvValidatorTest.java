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
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.validators.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.validators.SynopticPvValidator;

/**
 * Tests for the ComponentValidator
 */
public class SynopticPvValidatorTest {
    private SynopticPvValidator validator;
    private PV pv;

    private static final String PV_NAME = "PV_NAME";
    private static final String PV_NAME_TWO = "PV_TWO";
    private static final String PV_ADDRESS = "COMP_NAME";
    private static final String BAD_PV_ADDRESS = " ";

    @Before
    public void setUp() {
        pv = new PV(PV_NAME, PV_ADDRESS, null);

        validator = new SynopticPvValidator(pv);
    }

    @Test
    public void WHEN_bad_pv_address_THEN_validator_contains_error() {
        pv.setAddress(BAD_PV_ADDRESS);

        assertTrue(validator.getError().isError());
    }

    @Test
    public void WHEN_bad_pv_corrected_THEN_validator_does_not_contains_error() {
        pv.setAddress(BAD_PV_ADDRESS);
        pv.setAddress(PV_ADDRESS);

        assertFalse(validator.getError().isError());
    }

    @Test
    public void WHEN_bad_pv_address_THEN_validator_message_contains_pv_name() {
        pv.setAddress(BAD_PV_ADDRESS);

        assertTrue(validator.getError().getMessage().contains(PV_NAME));
    }

    @Test
    public void GIVEN_bad_pv_address_WHEN_pv_name_changed_THEN_validator_message_contains_new_pv_name() {
        pv.setAddress(BAD_PV_ADDRESS);

        pv.setDisplayName(PV_NAME_TWO);

        assertFalse(validator.getError().getMessage().contains(PV_NAME));
        assertTrue(validator.getError().getMessage().contains(PV_NAME_TWO));
    }
}
