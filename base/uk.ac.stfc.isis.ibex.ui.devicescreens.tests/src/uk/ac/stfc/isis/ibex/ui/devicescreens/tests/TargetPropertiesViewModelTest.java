//CHECKSTYLE:OFF

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

package uk.ac.stfc.isis.ibex.ui.devicescreens.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.devicescreens.desc.PropertyDescription;
import uk.ac.stfc.isis.ibex.ui.devicescreens.models.DeviceDescriptionWrapper;
import uk.ac.stfc.isis.ibex.ui.devicescreens.models.EditDeviceScreensDescriptionViewModel;
import uk.ac.stfc.isis.ibex.ui.devicescreens.models.TargetPropertiesViewModel;

/**
 * Unit tests for the DeviceScreensDescriptionViewModel class.
 */
public class TargetPropertiesViewModelTest {

    private TargetPropertiesViewModel viewModel;
    private PropertyDescription property1;
    private String propertyName1 = "Property1";
    private String propertyValue1 = "Value1";
    private String propertyDescription1 = "This is value 1";

    @Before
    public void setUp() {
        property1 = new PropertyDescription(propertyName1, propertyValue1);

        DeviceDescriptionWrapper desc = mock(DeviceDescriptionWrapper.class);
        when(desc.getMacroDescription(propertyName1)).thenReturn(propertyDescription1);

        EditDeviceScreensDescriptionViewModel model = mock(EditDeviceScreensDescriptionViewModel.class);
        when(model.getTargetScreen()).thenReturn(desc);
        
        viewModel = new TargetPropertiesViewModel(model);
    }

    @Test
    public void WHEN_no_property_is_selected_THEN_value_and_description_blank() {
        // Act
        viewModel.setTableSelection(null);

        // Assert
        assertEquals("", viewModel.getValueText());
        assertEquals("", viewModel.getDescriptionText());
    }

    @Test
    public void WHEN_property_is_selected_THEN_value_and_description_change() {
        // Act
        viewModel.setTableSelection(property1);

        // Assert
        assertEquals(propertyValue1, viewModel.getValueText());
        assertEquals(propertyDescription1, viewModel.getDescriptionText());
    }

    @Test
    public void WHEN_no_available_properties_THEN_table_and_text_box_are_disabled() {
        // Act
        viewModel.setPropeties(new ArrayList<>());

        // Assert
        assertEquals(false, viewModel.getTableEnabled());
        assertEquals(false, viewModel.getValueTextEnabled());
    }

    @Test
    public void WHEN_changing_the_selected_property_on_the_viewmodel_THEN_value_changes_on_target() {
        // Act
        viewModel.setTableSelection(property1);
        viewModel.setValueText("Hello");

        // Assert
        assertEquals("Hello", viewModel.getValueText());
        assertEquals("Hello", property1.getValue());
    }
}
//CHECKSTYLE:ON
