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
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv.PvDetailViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv.PvListViewModel;

/**
 * Tests for the PvViewModel.
 */
public class PvDetailViewModelTest {

    private PvDetailViewModel viewModel;
    private PvListViewModel synoptic;

    private static String VALID_ADDRESS = "TEST_DESC";
    private static String INVALID_ADDRESS = "";

    @Before
    public void setUp() {
        synoptic = mock(PvListViewModel.class);

        viewModel = new PvDetailViewModel(synoptic);
    }
    
    @Test
    public void WHEN_null_pv_selected_THEN_selection_invisible() {
        viewModel.showPV(null);

        assertFalse(viewModel.getSelectionVisible());
        assertEquals(PvDetailViewModel.NO_SELECTION_TEXT, viewModel.getErrorText());
    }

    @Test
    public void WHEN_valid_address_entered_THEN_no_error() {
        viewModel.setPvAddress(VALID_ADDRESS);

        assertEquals(true, viewModel.getErrorText().isEmpty());
    }

    @Test
    public void WHEN_invalid_address_entered_THEN_error_text() {
        viewModel.setPvAddress(INVALID_ADDRESS);

        assertEquals(false, viewModel.getErrorText().isEmpty());
    }
}
