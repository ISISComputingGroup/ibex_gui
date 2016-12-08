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
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.IO;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv.PvDetailViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv.PvListViewModel;

/**
 * Tests for the PvViewModel
 */
public class PvDetailViewModelTest {

    private PvDetailViewModel viewModel;
    private PvListViewModel synoptic;

    private static String UNIQUE_NAME = "NOT_TEST";

    private static String VALID_ADDRESS = "TEST_DESC";
    private static String INVALID_ADDRESS = "";

    private static final PV preExistingPV = new PV("PREEXISTING", "ADDRESS", IO.READ);
    private static final PV startingPV = new PV("TEST", "NONE", IO.READ);

    @Before
    public void setUp() {
        synoptic = mock(PvListViewModel.class);
        
        List<PV> startingPVs = new ArrayList<>();
        
        startingPVs.add(preExistingPV);

        when(synoptic.getList()).thenReturn(startingPVs);

        viewModel = new PvDetailViewModel(synoptic);

        // Start with a valid PV
        viewModel.showPV(startingPV);
    }
    
    @Test
    public void WHEN_null_pv_selected_THEN_selection_invisible() {
        viewModel.showPV(null);

        assertFalse(viewModel.getSelectionVisible());
    }

    @Test
    public void WHEN_valid_address_entered_THEN_error_text_cleared() {
        viewModel.setPvAddress(VALID_ADDRESS);

        assertEquals(viewModel.getErrorText(), "");
    }

    @Test
    public void WHEN_invalid_address_entered_THEN_error_text() {
        viewModel.setPvAddress(INVALID_ADDRESS);

        assertNotEquals(viewModel.getErrorText(), "");
    }
    
    @Test
    public void WHEN_unique_name_entered_THEN_error_text_cleared() {
        viewModel.setPvName(UNIQUE_NAME);

        assertEquals(viewModel.getErrorText(), "");
    }

    @Test
    public void GIVEN_non_unique_name_WHEN_unique_name_entered_THEN_error_text_cleared() {
        viewModel.setPvName(preExistingPV.displayName());
        viewModel.setPvName(UNIQUE_NAME);

        assertEquals(viewModel.getErrorText(), "");
    }

    @Test
    public void WHEN_non_unique_name_entered_THEN_error_text() {
        viewModel.setPvName(preExistingPV.displayName());

        assertNotEquals(viewModel.getErrorText(), "");
    }

    @Test
    public void GIVEN_non_unique_name_WHEN_IO_updated_THEN_error_text_cleared() {
        viewModel.setPvName(preExistingPV.displayName());
        viewModel.setPvMode(IO.WRITE);

        assertEquals(viewModel.getErrorText(), "");
    }

    @Test
    public void GIVEN_bad_address_and_non_unique_name_WHEN_name_updated_to_be_unique_THEN_error_text_not_cleared() {
        viewModel.setPvAddress(INVALID_ADDRESS);
        viewModel.setPvName(preExistingPV.displayName());

        viewModel.setPvName(UNIQUE_NAME);

        assertNotEquals(viewModel.getErrorText(), "");
    }

}
