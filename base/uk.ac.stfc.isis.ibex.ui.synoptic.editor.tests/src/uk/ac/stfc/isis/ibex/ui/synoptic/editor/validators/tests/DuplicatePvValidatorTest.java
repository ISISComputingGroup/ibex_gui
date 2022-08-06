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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.IO;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.validators.DuplicatePvValidator;

/**
 * Tests for the DuplicatePvValidator
 */
public class DuplicatePvValidatorTest {
    private DuplicatePvValidator validator;
    private PV pvOne;
    private PV pvTwo;
    private List<PV> pvList;

    private static final String PV_NAME = "PV_NAME";
    private static final String PV_ADDRESS = "COMP_NAME";

    @Before
    public void setUp() {
        pvOne = new PV(PV_NAME, PV_ADDRESS, IO.READ);
        pvTwo = new PV(pvOne);
        
        pvList = new ArrayList<PV>(Arrays.asList(pvOne, pvTwo));

        validator = new DuplicatePvValidator();
    }

    @Test
    public void WHEN_pvs_are_exactly_the_same_THEN_validator_contains_error_with_pv_name() {
        validator.checkForDuplicatePVs(pvList);
        
        assertTrue(validator.getError().isError());
        assertTrue(validator.getError().getMessage().contains(PV_NAME));
    }
    
    @Test
    public void WHEN_pvs_have_different_address_but_otherwise_same_THEN_validator_contains_error_with_pv_name() {
        pvList.get(0).setAddress("NEW_ADDRESS");
        validator.checkForDuplicatePVs(pvList);
        
        assertTrue(validator.getError().isError());
        assertTrue(validator.getError().getMessage().contains(PV_NAME));
    }
    
    @Test
    public void WHEN_pvs_have_different_names_but_otherwise_same_THEN_validator_does_not_contain_error_with_pv_name() {
        pvList.get(0).setDisplayName("NEW_NAME");
        validator.checkForDuplicatePVs(pvList);
        
        assertFalse(validator.getError().isError());
        assertNull(validator.getError().getMessage());
    }
    
    @Test
    public void WHEN_pvs_have_different_IO_but_otherwise_same_THEN_validator_does_not_contain_error_with_pv_name() {
        pvList.get(0).recordType().setIO(IO.WRITE);
        validator.checkForDuplicatePVs(pvList);
        
        assertFalse(validator.getError().isError());
        assertNull(validator.getError().getMessage());
    }
}
