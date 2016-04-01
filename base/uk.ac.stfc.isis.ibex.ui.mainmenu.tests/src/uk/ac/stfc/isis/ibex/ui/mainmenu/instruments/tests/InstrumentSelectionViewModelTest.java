
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

package uk.ac.stfc.isis.ibex.ui.mainmenu.instruments.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.ui.mainmenu.instrument.InstrumentSelectionViewModel;

public class InstrumentSelectionViewModelTest {

    @Test(expected = IllegalArgumentException.class)
    public void initialisation_with_instruments_null_throws() {
        // Act
        new InstrumentSelectionViewModel(null);
    }

    @Test
    public void input_instruments_can_be_empty() {
        // Arrange
        ArrayList<InstrumentInfo> emptyInstruments = new ArrayList<>();

        // Act
        InstrumentSelectionViewModel viewModel = new InstrumentSelectionViewModel(emptyInstruments);

        // Assert
        assertTrue(viewModel.getInstruments().isEmpty());
    }

    @Test
    public void selected_instrument_name_is_initalised_empty() {
        // Arrange
        ArrayList<InstrumentInfo> instruments = new ArrayList<>();
        String emptyString = "";

        // Act
        InstrumentSelectionViewModel viewModel = new InstrumentSelectionViewModel(instruments);

        // Assert
        assertEquals(emptyString, viewModel.getSelectedName());
    }

    @Test
    public void invalid_instrument_name_is_validated_when_set() {
        // Arrange
        ArrayList<InstrumentInfo> instruments = new ArrayList<>();
        String name = "invalid@";
        InstrumentSelectionViewModel viewModel = new InstrumentSelectionViewModel(instruments);

        // Act
        viewModel.setSelectedName(name);

        // Assert
        assertTrue(viewModel.getError().isError());
    }

    @Test
    public void valid_instrument_name_is_validated_when_set() {
        // Arrange
        ArrayList<InstrumentInfo> instruments = new ArrayList<>();
        String name = "NDW123";
        InstrumentSelectionViewModel viewModel = new InstrumentSelectionViewModel(instruments);

        // Act
        viewModel.setSelectedName(name);

        // Assert
        assertFalse(viewModel.getError().isError());
    }

    @Test
    public void get_selected_instrument_returns_correct_instrument_in_input_list() {
        // Arrange
        String expectedName = "expected";
        InstrumentInfo expectedInstrument = new InstrumentInfo(expectedName);
        ArrayList<InstrumentInfo> instruments = new ArrayList<>();
        instruments.add(new InstrumentInfo("instrument0"));
        instruments.add(expectedInstrument);
        instruments.add(new InstrumentInfo("instrument2"));
        
        InstrumentSelectionViewModel viewModel = new InstrumentSelectionViewModel(instruments);
        
        // Act
        viewModel.setSelectedName(expectedName);
        InstrumentInfo result = viewModel.getSelectedInstrument();

        // Assert
        assertEquals(expectedInstrument, result);
    }

    @Test
    public void get_selected_instrument_returns_null_if_name_not_present_in_input_list() {
        // Arrange
        Collection<InstrumentInfo> instruments = createInstruments();
        InstrumentSelectionViewModel viewModel = new InstrumentSelectionViewModel(instruments);

        // Act
        viewModel.setSelectedName("NDW123");
        InstrumentInfo result = viewModel.getSelectedInstrument();

        // Assert
        assertNull(result);
    }

    @Test
    public void instrument_not_in_input_list_is_flagged_as_non_existing() {
        // Arrange
        Collection<InstrumentInfo> instruments = createInstruments();
        InstrumentSelectionViewModel viewModel = new InstrumentSelectionViewModel(instruments);

        // Act
        viewModel.setSelectedName("NDW123");

        // Assert
        assertFalse(viewModel.selectedInstrumentExists());
    }

    @Test
    public void empty_instrument_name_is_flagged_as_non_existing() {
        // Arrange
        Collection<InstrumentInfo> instruments = createInstruments();
        InstrumentSelectionViewModel viewModel = new InstrumentSelectionViewModel(instruments);

        // Act
        viewModel.setSelectedName("");

        // Assert
        assertFalse(viewModel.selectedInstrumentExists());
    }

    @Test
    public void instrument_in_input_list_is_flagged_as_existing() {
        // Arrange
        Collection<InstrumentInfo> instruments = createInstruments();
        InstrumentSelectionViewModel viewModel = new InstrumentSelectionViewModel(instruments);

        // Act
        viewModel.setSelectedName("instrument0");

        // Assert
        assertTrue(viewModel.selectedInstrumentExists());
    }

    @Test
    public void instrument_name_can_be_validated_at_initialisation() {
        // Arrange
        InstrumentSelectionViewModel viewModel = new InstrumentSelectionViewModel(new ArrayList<InstrumentInfo>());
        assertFalse(viewModel.getError().isError());

        // Act
        viewModel.validate();

        // Assert
        assertTrue(viewModel.getError().isError());
    }

    private Collection<InstrumentInfo> createInstruments() {
        ArrayList<InstrumentInfo> instruments = new ArrayList<>();
        instruments.add(new InstrumentInfo("instrument0"));
        instruments.add(new InstrumentInfo("instrument1"));
        instruments.add(new InstrumentInfo("instrument2"));

        return instruments;
    }
}
