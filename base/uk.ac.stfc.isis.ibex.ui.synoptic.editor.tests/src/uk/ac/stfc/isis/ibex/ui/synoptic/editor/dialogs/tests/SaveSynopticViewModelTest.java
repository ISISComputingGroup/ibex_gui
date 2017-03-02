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
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs.SaveSynopticViewModel;

/**
 * Tests for the SaveSynopticViewModel.
 */
public class SaveSynopticViewModelTest {

    private SaveSynopticViewModel viewModel;
    private static final String VALID = "Test_Synoptic";
    private static final String DUPLICATE = "Duplicate";
    private static final String STARTS_WITH_LOWER = "test Synoptic";
    private static final String CONTAINS_BAD_CHAR = "Test^%$";
    
    @Test
    public void GIVEN_original_name_WHEN_view_model_created_THEN_synoptic_name_is_original_name() {
        viewModel = new SaveSynopticViewModel(VALID, new ArrayList<>());

        assertEquals(viewModel.getSynopticName(), VALID);
    }

    @Test
    public void GIVEN_valid_starting_name_WHEN_view_model_created_THEN_no_error() {
        viewModel = new SaveSynopticViewModel(VALID, new ArrayList<>());

        assertFalse(viewModel.getError().isError());
    }

    @Test
    public void GIVEN_valid_starting_name_WHEN_view_model_created_THEN_saving_allowed() {
        viewModel = new SaveSynopticViewModel(VALID, new ArrayList<>());

        assertTrue(viewModel.getSavingAllowed());
    }

    @Test
    public void WHEN_blank_name_set_THEN_error() {
        viewModel = new SaveSynopticViewModel(VALID, new ArrayList<>());

        viewModel.setSynopticName("");

        assertTrue(viewModel.getError().isError());
    }

    @Test
    public void WHEN_null_name_set_THEN_error() {
        viewModel = new SaveSynopticViewModel(VALID, new ArrayList<>());

        viewModel.setSynopticName(null);

        assertTrue(viewModel.getError().isError());
    }

    @Test
    public void WHEN_name_starting_with_lower_case_set_THEN_error() {
        viewModel = new SaveSynopticViewModel(VALID, new ArrayList<>());

        viewModel.setSynopticName(STARTS_WITH_LOWER);

        assertTrue(viewModel.getError().isError());
    }

    @Test
    public void WHEN_name_containing_bad_chars_set_THEN_error() {
        viewModel = new SaveSynopticViewModel(VALID, new ArrayList<>());

        viewModel.setSynopticName(CONTAINS_BAD_CHAR);

        assertTrue(viewModel.getError().isError());
    }

    @Test
    public void WHEN_invalid_name_set_THEN_saving_not_allowed() {
        viewModel = new SaveSynopticViewModel(VALID, new ArrayList<>());

        viewModel.setSynopticName(CONTAINS_BAD_CHAR);

        assertFalse(viewModel.getSavingAllowed());
    }

    @Test
    public void GIVEN_invalid_name_WHEN_valid_name_set_THEN_no_error() {
        viewModel = new SaveSynopticViewModel(CONTAINS_BAD_CHAR, new ArrayList<>());

        viewModel.setSynopticName(VALID);

        assertFalse(viewModel.getError().isError());
    }

    @Test
    public void GIVEN_invalid_name_WHEN_valid_name_set_THEN_saving_allowed() {
        viewModel = new SaveSynopticViewModel(CONTAINS_BAD_CHAR, new ArrayList<>());

        viewModel.setSynopticName(VALID);

        assertTrue(viewModel.getSavingAllowed());
    }

    @Test
    public void GIVEN_invalid_starting_name_WHEN_view_model_created_THEN_error() {
        viewModel = new SaveSynopticViewModel(STARTS_WITH_LOWER, new ArrayList<>());

        assertTrue(viewModel.getError().isError());
    }

    @Test
    public void GIVEN_invalid_starting_name_WHEN_view_model_created_THEN_saving_not_allowed() {
        viewModel = new SaveSynopticViewModel(STARTS_WITH_LOWER, new ArrayList<>());

        assertFalse(viewModel.getSavingAllowed());
    }

    @Test
    public void WHEN_duplicate_name_THEN_error() {
        viewModel = new SaveSynopticViewModel(VALID, Arrays.asList(DUPLICATE));

        viewModel.setSynopticName(DUPLICATE);

        assertTrue(viewModel.getError().isError());
    }

    @Test
    public void WHEN_duplicate_name_THEN_saving_allowed() {
        viewModel = new SaveSynopticViewModel(VALID, Arrays.asList(DUPLICATE));

        viewModel.setSynopticName(DUPLICATE);

        assertTrue(viewModel.getSavingAllowed());
    }
}
