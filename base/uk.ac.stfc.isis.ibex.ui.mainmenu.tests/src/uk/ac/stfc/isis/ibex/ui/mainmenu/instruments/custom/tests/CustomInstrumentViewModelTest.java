
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

package uk.ac.stfc.isis.ibex.ui.mainmenu.instruments.custom.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.ui.mainmenu.instrument.custom.CustomIntrumentViewModel;

public class CustomInstrumentViewModelTest {
    @Test
    public void instrument_name_is_initialised_correctly() {
        // Arrange
        String name = "a_name";

        // Act
        CustomIntrumentViewModel viewModel = new CustomIntrumentViewModel(name);

        // Assert
        assertEquals(name, viewModel.getInstrumentName());
    }

    @Test
    public void pv_prefix_is_empty_at_initialisation() {
        // Arrange
        String name = "a_name";
        String emptyString = "";

        // Act
        CustomIntrumentViewModel viewModel = new CustomIntrumentViewModel(name);

        // Assert
        assertEquals(emptyString, viewModel.getPvPrefix());
    }

    @Test
    public void selected_instrument_is_configured_correctly() {
        // Arrange
        String expectedName = "instrument_name";
        String exptectedPVPrefix = "pv_prefix";
        CustomIntrumentViewModel viewModel = new CustomIntrumentViewModel(expectedName);
        viewModel.setPvPrefix(exptectedPVPrefix);

        // Act
        InstrumentInfo instrument = viewModel.getSelectedInstrument();

        // Assert
        assertEquals(expectedName, instrument.name());
        assertEquals(exptectedPVPrefix, instrument.pvPrefix());
    }

    @Test
    public void invalid_pv_prefix_is_validated_when_set() {
        // Arrange
        String expectedName = "instrument_name";
        String pvPrefix = "invalid@";
        CustomIntrumentViewModel viewModel = new CustomIntrumentViewModel(expectedName);

        // Act
        viewModel.setPvPrefix(pvPrefix);

        // Assert
        assertTrue(viewModel.getError().isError());
    }
    
    @Test
    public void valid_pv_prefix_is_validated_when_set() {
        // Arrange
        String expectedName = "instrument_name";
        String pvPrefix = "valid";
        CustomIntrumentViewModel viewModel = new CustomIntrumentViewModel(expectedName);

        // Act
        viewModel.setPvPrefix(pvPrefix);

        // Assert
        assertFalse(viewModel.getError().isError());
    }

    @Test
    public void pv_prefix_can_be_validated_at_initialisation() {
        // Arrange
        String expectedName = "instrument_name";
        CustomIntrumentViewModel viewModel = new CustomIntrumentViewModel(expectedName);
        assertFalse(viewModel.getError().isError());

        // Act
        viewModel.validate();

        // Assert
        assertTrue(viewModel.getError().isError());
    }

}
