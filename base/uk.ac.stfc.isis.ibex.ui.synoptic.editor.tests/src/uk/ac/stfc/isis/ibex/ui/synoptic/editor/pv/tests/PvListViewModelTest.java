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

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.IO;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv.PvListViewModel;

/**
 * Tests for the PvListViewModel
 */
public class PvListViewModelTest {
    PvListViewModel viewModel;

    PV firstTestPv = new PV("TEST1", "NONE", IO.READ);
    PV secondTestPv = new PV("TEST2", "NONE", IO.READ);
    PV thirdTestPv = new PV("TEST3", "NONE", IO.READ);

    private void createPvList() {
        ComponentDescription comp = new ComponentDescription();
        comp.addPV(thirdTestPv);
        comp.addPV(secondTestPv);
        comp.addPV(firstTestPv);
        
        viewModel.changeComponent(comp);
    }

    @Before
    public void setUp() {

        SynopticViewModel synoptic = mock(SynopticViewModel.class);
        
        viewModel = new PvListViewModel(synoptic);
    }

    @Test
    public void WHEN_component_changed_THEN_no_pv_selected() {
        viewModel.changeComponent(new ComponentDescription());

        assertEquals(viewModel.getSelectedPV(), null);
    }

    @Test
    public void GIVEN_null_pv_WHEN_pv_selected_THEN_buttons_disabled() {
        viewModel.setSelectedPV(null);

        assertFalse(viewModel.getDeleteEnabled());
        assertFalse(viewModel.getDownEnabled());
        assertFalse(viewModel.getUpEnabled());
    }

    @Test
    public void GIVEN_first_pv_in_list_WHEN_pv_selected_THEN_up_button_disabled_others_enabled() {
        createPvList();

        viewModel.setSelectedPV(firstTestPv);

        assertFalse(viewModel.getUpEnabled());

        assertTrue(viewModel.getDeleteEnabled());
        assertTrue(viewModel.getDownEnabled());
    }

    @Test
    public void GIVEN_second_pv_in_list_WHEN_pv_selected_THEN_all_buttons_enabled() {
        createPvList();

        viewModel.setSelectedPV(secondTestPv);

        assertTrue(viewModel.getUpEnabled());
        assertTrue(viewModel.getDeleteEnabled());
        assertTrue(viewModel.getDownEnabled());
    }

    @Test
    public void GIVEN_last_pv_in_list_WHEN_pv_selected_THEN_down_button_disabled_others_enabled() {
        createPvList();

        viewModel.setSelectedPV(thirdTestPv);

        assertFalse(viewModel.getDownEnabled());

        assertTrue(viewModel.getDeleteEnabled());
        assertTrue(viewModel.getUpEnabled());
    }

    @Test
    public void WHEN_pv_removed_THEN_no_pv_selected() {
        createPvList();

        viewModel.setSelectedPV(thirdTestPv);
        viewModel.removeSelectedPV();

        assertEquals(viewModel.getSelectedPV(), null);
    }

    @Test
    public void GIVEN_no_pv_selected_WHEN_pv_added_THEN_new_pv_selected() {
        createPvList();

        viewModel.setSelectedPV(null);
        PV newPv = viewModel.addNewPV();

        assertEquals(viewModel.getSelectedPV(), newPv);
    }

    @Test
    public void GIVEN_pv_selected_WHEN_pv_added_THEN_new_pv_selected() {
        createPvList();

        viewModel.setSelectedPV(firstTestPv);
        PV newPv = viewModel.addNewPV();

        assertEquals(viewModel.getSelectedPV(), newPv);
    }
}
