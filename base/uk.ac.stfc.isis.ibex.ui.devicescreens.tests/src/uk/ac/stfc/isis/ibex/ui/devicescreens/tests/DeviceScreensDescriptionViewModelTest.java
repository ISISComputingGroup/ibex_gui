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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.devicescreens.DeviceScreensModel;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.PropertyDescription;
import uk.ac.stfc.isis.ibex.opis.DescriptionsProvider;
import uk.ac.stfc.isis.ibex.opis.desc.MacroInfo;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;
import uk.ac.stfc.isis.ibex.ui.devicescreens.models.DeviceDescriptionWrapper;
import uk.ac.stfc.isis.ibex.ui.devicescreens.models.EditDeviceScreensDescriptionViewModel;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * Unit tests for the DeviceScreensDescriptionViewModel class.
 */
public class DeviceScreensDescriptionViewModelTest {

    private DeviceScreensDescription description;
    private EditDeviceScreensDescriptionViewModel viewModel;
    private String deviceName = "Device";
    private String opiName1 = "Test Opi 1";
    private String opiDescription1 = "This is OPI 1";
    private String opiName2 = "Test Opi 2";
    private String opiDescription2 = "This is OPI 1";
    private String propertyName1 = "Property1";
    private String propertyValue1 = "Value1";
    private String propertyName2 = "Property2";
    private String propertyValue2 = "Value2";
    private List<MacroInfo> macros;
    MessageDisplayer displayer;

    @Before
    public void setUp() {
        macros = new ArrayList<>();
        macros.add(new MacroInfo(propertyName1, propertyValue1));
        macros.add(new MacroInfo(propertyName2, propertyValue2));
        
        description = new DeviceScreensDescription();
        for (int i = 1; i <= 2; ++i) {
            DeviceDescription device = new DeviceDescription();
            device.setName(deviceName + i);
            device.setKey(opiName1);
            device.setType("OPI");
            device.addProperty(new PropertyDescription(propertyName1, propertyValue1));
            device.addProperty(new PropertyDescription(propertyName2, propertyValue2));

            description.addDevice(device);
        }
        
        List<String> propertyNames = new ArrayList<>();
        propertyNames.add(propertyName1);
        propertyNames.add(propertyName2);

        // Set up the mocks
        displayer = mock(MessageDisplayer.class);
        
        OpiDescription opiDesc1 = mock(OpiDescription.class);
        when(opiDesc1.getMacros()).thenReturn(macros);
        when(opiDesc1.getDescription()).thenReturn(opiDescription1);
        when(opiDesc1.getKeys()).thenReturn(propertyNames);

        OpiDescription opiDesc2 = mock(OpiDescription.class);
        when(opiDesc2.getMacros()).thenReturn(macros);
        when(opiDesc2.getDescription()).thenReturn(opiDescription2);
        when(opiDesc2.getKeys()).thenReturn(propertyNames);
        
        DescriptionsProvider provider = mock(DescriptionsProvider.class);
        when(provider.guessOpiName(opiName1)).thenReturn(opiName1);
        when(provider.guessOpiName(opiName2)).thenReturn(opiName2);
        when(provider.getDescription(opiName1)).thenReturn(opiDesc1);
        when(provider.getDescription(opiName2)).thenReturn(opiDesc2);
        
        DeviceScreensModel model = mock(DeviceScreensModel.class);
        when(model.getDeviceScreensDescription()).thenReturn(description);

        viewModel = new EditDeviceScreensDescriptionViewModel(model, provider, displayer);
    }

    @Test
    public void WHEN_view_model_constructed_THEN_produces_deep_copy_of_description() {
        // The original description is not modified by changes applied via the
        // view model

        // Act
        List<DeviceDescriptionWrapper> first = viewModel.getScreens().subList(0, 1);
        viewModel.setSelectedScreens(first);
        viewModel.setName("new name");
        viewModel.setKey(opiName2);

        // Assert that nothing changed in the original
        assertEquals(2, description.getDevices().size());
        assertFalse("new name".equals(description.getDevices().get(0).getName()));
        assertFalse(opiName2.equals(description.getDevices().get(0).getKey()));
        assertFalse("new value".equals(description.getDevices().get(0).getProperties().get(0).getValue()));
    }

    @Test
    public void WHEN_changing_the_selected_screen_THEN_edit_control_contents_changed() {
        // Act
        List<DeviceDescriptionWrapper> first = viewModel.getScreens().subList(1, 2);
        viewModel.setSelectedScreens(first);
        
        // Assert
        assertEquals(deviceName + "2", viewModel.getName());
        assertEquals(opiName1, viewModel.getKey());
        assertEquals(opiDescription1, viewModel.getDescription());
    }

    @Test
    public void WHEN_clearing_the_selected_screen_THEN_edit_control_contents_cleared_to_blank() {
        // Act
        viewModel.setSelectedScreens(null);

        // Assert
        assertEquals("", viewModel.getName());
        assertEquals("", viewModel.getKey());
        assertEquals("", viewModel.getDescription());
    }

    @Test
    public void WHEN_setting_multiple_screens_THEN_edit_control_contents_cleared_to_blank() {
        // Act
        List<DeviceDescriptionWrapper> screens = viewModel.getScreens().subList(0, 2);
        viewModel.setSelectedScreens(screens);

        // Assert
        assertEquals("", viewModel.getName());
        assertEquals("", viewModel.getKey());
        assertEquals("", viewModel.getDescription());
    }

    @Test
    public void WHEN_setting_multiple_screens_THEN_edit_controls_disabled() {
        // Act
        List<DeviceDescriptionWrapper> screens = viewModel.getScreens().subList(0, 2);
        viewModel.setSelectedScreens(screens);

        // Assert
        assertEquals(false, viewModel.getEnabled());
    }

    @Test
    public void WHEN_setting_multiple_screens_THEN_target_is_null() {
        // Act
        List<DeviceDescriptionWrapper> screens = viewModel.getScreens().subList(0, 2);
        viewModel.setSelectedScreens(screens);

        // Assert
        assertEquals(null, viewModel.getTargetScreen());
    }

    @Test
    public void WHEN_setting_singular_screen_THEN_target_set() {
        // Act
        List<DeviceDescriptionWrapper> first = viewModel.getScreens().subList(0, 1);
        viewModel.setSelectedScreens(first);

        // Assert
        assertEquals(first.get(0), viewModel.getTargetScreen());
    }

    @Test
    public void WHEN_the_name_is_changed_on_the_control_THEN_name_is_changed_on_the_target() {
        // Act
        viewModel.setTargetScreen(viewModel.getScreens().get(0));

        viewModel.setName("NewName");

        // Assert
        assertEquals("NewName", viewModel.getName());
        assertEquals("NewName", viewModel.getTargetScreen().getName());
    }

    @Test
    public void
            WHEN_the_key_is_changed_on_the_control_THEN_key_is_changed_on_the_target_and_blank_properties_returned() {
        // Act
        viewModel.setTargetScreen(viewModel.getScreens().get(0));
        viewModel.setKey(opiName2);

        // Assert
        assertEquals(opiName2, viewModel.getKey());
        assertEquals(opiDescription2, viewModel.getTargetScreen().getDescription());
        assertEquals("", viewModel.getTargetScreen().getProperties().get(0).getValue());
        assertEquals("", viewModel.getTargetScreen().getProperties().get(1).getValue());
    }

    @Test
    public void GIVEN_single_selected_screen_WHEN_delete_selection_called_THEN_screen_removed() {
        // Arrange
        List<DeviceDescriptionWrapper> firstScreen = viewModel.getScreens().subList(0, 1);
        viewModel.setSelectedScreens(firstScreen);

        // Act
        viewModel.deleteSelectedScreens();

        // Assert
        assertEquals(1, viewModel.getScreens().size());
    }

    @Test
    public void GIVEN_multiple_selected_screens_WHEN_delete_selection_called_THEN_screens_removed() {
        // Arrange
        List<DeviceDescriptionWrapper> screens = viewModel.getScreens().subList(0, 2);
        viewModel.setSelectedScreens(screens);

        // Act
        viewModel.deleteSelectedScreens();

        // Assert
        assertEquals(0, viewModel.getScreens().size());
    }

    @Test
    public void GIVEN_all_screens_selected_WHEN_delete_selection_called_THEN_target_is_null() {
        // Arrange
        List<DeviceDescriptionWrapper> screens = viewModel.getScreens().subList(0, 2);
        viewModel.setSelectedScreens(screens);

        // Act
        viewModel.deleteSelectedScreens();

        // Assert
        assertEquals(0, viewModel.getScreens().size());
        assertEquals(null, viewModel.getTargetScreen());
        assertEquals(false, viewModel.getEnabled());
    }

    @Test
    public void WHEN_screen_added_THEN_new_scree_created_with_blank_key_and_description() {
        // Act
        viewModel.addScreen();

        // Assert
        assertEquals(3, viewModel.getScreens().size());
        assertEquals("", viewModel.getScreens().get(2).getKey());
        assertEquals("", viewModel.getScreens().get(2).getDescription());
    }

    @Test
    public void WHEN_setting_blank_target_THEN_error_triggered() {
        // Arrange
        String expectedSource = "ViewModel";
        String expectedMsg = "Device 'Device1' is not pointing at a valid target. Please select a target OPI.";

        // Act
        viewModel.setTargetScreen(viewModel.getScreens().get(0));
        viewModel.setKey("");

        // Assert
        verify(displayer, times(0)).setErrorMessage(expectedSource, "");
        verify(displayer).setErrorMessage(expectedSource, expectedMsg);
    }
}
//CHECKSTYLE:ON
