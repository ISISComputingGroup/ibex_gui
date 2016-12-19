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
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.component.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.component.ComponentDetailViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;

/**
 * Tests for the ComponentDetailViewModel.
 */
public class ComponentDetailViewModelTest {

    private ComponentDetailViewModel viewModel;
    private SynopticViewModel synoptic;

    private String preexistingComp = "preexisting";

    private String compName = "test_component";

    private ComponentDescription comp;

    @Before
    public void setUp() {
        SynopticDescription synopticdesc = mock(SynopticDescription.class);
        
        ArrayList<String> existingComps = new ArrayList<String>();
        existingComps.add(preexistingComp);

        when(synopticdesc.getComponentNameListWithChildren()).thenReturn(existingComps);
                
        synoptic = mock(SynopticViewModel.class);
        when(synoptic.getSynoptic()).thenReturn(synopticdesc);

        comp = new ComponentDescription();
        comp.setName(compName);

        viewModel = new ComponentDetailViewModel(synoptic);

        viewModel.setComponent(comp);

    }

    @Test
    public void WHEN_null_component_selected_THEN_selection_invisible() {
        viewModel.setComponent(null);

        assertEquals(viewModel.getSelectionVisible(), false);
    }

    @Test
    public void WHEN_component_selected_THEN_selection_visible() {
        viewModel.setComponent(comp);

        assertEquals(viewModel.getSelectionVisible(), true);
    }

    @Test
    public void WHEN_setting_name_with_trailing_whitespace_THEN_whitespace_removed() {
        viewModel.setComponentName(compName + " ");

        assertEquals(viewModel.getComponentName(), compName);
    }

    @Test
    public void GIVEN_pre_exisiting_comp_WHEN_setting_non_unique_name_THEN_error() {
        viewModel.setComponentName(preexistingComp);

        assertTrue(viewModel.getError().isError());
    }

    @Test
    public void GIVEN_pre_exisiting_comp_WHEN_setting_non_unique_name_with_trailing_whitespace_THEN_error() {
        viewModel.setComponentName(preexistingComp + " ");

        assertTrue(viewModel.getError().isError());
    }

}
