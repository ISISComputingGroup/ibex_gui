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

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.IO;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.validators.ComponentValidator;

/**
 * Tests for the ComponentValidator
 */
public class ComponentValidatorTest {
    private ComponentDescription component;
    private ComponentValidator validator;
    private PV pv;

    private static final String COMP_NAME = "COMP_NAME";
    private static final String COMP_NAME_TWO = "COMP_TWO";
    private static final String PV_NAME = "PV_NAME";
    private static final String PV_NAME_TWO = "PV_TWO";
    private static final String PV_ADDRESS = "VALID_ADDRESS";
    private static final String BAD_PV_ADDRESS = " ";

    @Before
    public void setUp() {
        component = new ComponentDescription();
        component.setName(COMP_NAME);

        pv = new PV(PV_NAME, PV_ADDRESS, IO.READ);
        component.addPV(pv);

        validator = new ComponentValidator(component);
    }

    @Test
    public void WHEN_bad_pv_address_THEN_validator_contains_error() {
        pv.setAddress(BAD_PV_ADDRESS);

        assertTrue(validator.getError().isError());
    }

    @Test
    public void WHEN_bad_pv_address_THEN_validator_message_contains_component_name() {
        pv.setAddress(BAD_PV_ADDRESS);

        assertTrue(validator.getError().getMessage().contains(COMP_NAME));
    }

    @Test
    public void WHEN_two_bad_pvs_THEN_validator_message_contains_both_name() {
        pv.setAddress(BAD_PV_ADDRESS);

        PV second_pv = new PV(PV_NAME_TWO, PV_ADDRESS, IO.READ);
        component.addPV(second_pv);
        second_pv.setAddress(BAD_PV_ADDRESS);

        assertTrue(validator.getError().getMessage().contains(PV_NAME));
        assertTrue(validator.getError().getMessage().contains(PV_NAME_TWO));
    }

    @Test
    public void GIVEN_bad_pv_WHEN_pv_removed_THEN_validator_does_not_contains_error() {
        pv.setAddress(BAD_PV_ADDRESS);

        component.removePV(pv);

        assertFalse(validator.getError().isError());
    }

    @Test
    public void GIVEN_bad_pv_WHEN_component_name_changed_THEN_validator_message_contains_new_name() {
        pv.setAddress(BAD_PV_ADDRESS);

        component.setName(COMP_NAME_TWO);

        assertTrue(validator.getError().getMessage().contains(COMP_NAME_TWO));
    }
}
