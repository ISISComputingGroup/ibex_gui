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
    private String propertyDescription1 = "This is value 1";
    private String propertyName2 = "Property2";
    private String propertyValue2 = "Value2";
    private String propertyDescription2 = "This is value 2";
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
        when(opiDesc1.getMacroDescription(propertyName1)).thenReturn(propertyDescription1);
        when(opiDesc1.getMacroDescription(propertyName2)).thenReturn(propertyDescription2);

        OpiDescription opiDesc2 = mock(OpiDescription.class);
        when(opiDesc2.getMacros()).thenReturn(macros);
        when(opiDesc2.getDescription()).thenReturn(opiDescription2);
        when(opiDesc2.getKeys()).thenReturn(propertyNames);
        when(opiDesc2.getMacroDescription(propertyName1)).thenReturn(propertyDescription1);
        when(opiDesc2.getMacroDescription(propertyName2)).thenReturn(propertyDescription2);
        
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
    public void constructor_produces_deep_copy_of_description() {
        // The original description is not modified by changes applied via the
        // view model

        // Act
        List<DeviceDescriptionWrapper> first = viewModel.getScreens().subList(0, 1);
        viewModel.setSelectedScreens(first);
        viewModel.setName("new name");
        viewModel.setKey(opiName2);
        viewModel.setSelectedProperty(0);
        viewModel.setSelectedPropertyValue("new value");

        // Assert that nothing changed in the original
        assertEquals(2, description.getDevices().size());
        assertFalse("new name".equals(description.getDevices().get(0).getName()));
        assertFalse(opiName2.equals(description.getDevices().get(0).getKey()));
        assertFalse("new value".equals(description.getDevices().get(0).getProperties().get(0).getValue()));
    }

    @Test
    public void changing_the_selected_screen_updates_name_key_etc() {
        // Act
        List<DeviceDescriptionWrapper> first = viewModel.getScreens().subList(1, 2);
        viewModel.setSelectedScreens(first);
        
        // Assert
        assertEquals(deviceName + "2", viewModel.getName());
        assertEquals(opiName1, viewModel.getKey());
        assertEquals(opiDescription1, viewModel.getDescription());
    }

    @Test
    public void clearing_the_selected_screen_unsets_name_key_etc() {
        // Act
        viewModel.setSelectedScreens(null);

        // Assert
        assertEquals("", viewModel.getName());
        assertEquals("", viewModel.getKey());
        assertEquals("", viewModel.getDescription());
    }

    @Test
    public void setting_multiple_screens_unsets_name_key_etc() {
        // Act
        List<DeviceDescriptionWrapper> screens = viewModel.getScreens().subList(0, 2);
        viewModel.setSelectedScreens(screens);

        // Assert
        assertEquals("", viewModel.getName());
        assertEquals("", viewModel.getKey());
        assertEquals("", viewModel.getDescription());
    }

    @Test
    public void setting_multiple_screens_disables_controls() {
        // Act
        List<DeviceDescriptionWrapper> screens = viewModel.getScreens().subList(0, 2);
        viewModel.setSelectedScreens(screens);

        // Assert
        assertEquals(false, viewModel.getEnabled());
    }

    @Test
    public void setting_multiple_screens_causes_null_target() {
        // Act
        List<DeviceDescriptionWrapper> screens = viewModel.getScreens().subList(0, 2);
        viewModel.setSelectedScreens(screens);

        // Assert
        assertEquals(null, viewModel.getTargetScreen());
    }

    @Test
    public void setting_singular_screen_sets_target() {
        // Act
        List<DeviceDescriptionWrapper> first = viewModel.getScreens().subList(0, 1);
        viewModel.setSelectedScreens(first);

        // Assert
        assertEquals(first.get(0), viewModel.getTargetScreen());
    }

    @Test
    public void changing_the_name_propagates() {
        // Act
        viewModel.setTargetScreen(viewModel.getScreens().get(0));

        viewModel.setName("NewName");

        // Assert
        assertEquals("NewName", viewModel.getName());
        assertEquals("NewName", viewModel.getTargetScreen().getName());
    }

    @Test
    public void changing_the_key_propagates_and_wipes_stored_properties() {
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
    public void selecting_property_allows_access_to_value_and_description() {
        // Act
        viewModel.setTargetScreen(viewModel.getScreens().get(0));
        viewModel.setSelectedProperty(0);

        // Assert
        assertEquals(propertyValue1, viewModel.getSelectedPropertyValue());
        assertEquals(propertyDescription1, viewModel.getSelectedPropertyDescription());
    }

    @Test
    public void changing_selected_property_value_propagates() {
        // Act
        viewModel.setTargetScreen(viewModel.getScreens().get(0));
        viewModel.setSelectedProperty(0);
        viewModel.setSelectedPropertyValue("Hello");

        // Assert
        assertEquals("Hello", viewModel.getSelectedPropertyValue());
        assertEquals("Hello", viewModel.getScreens().get(0).getProperties().get(0).getValue());
    }

    @Test
    public void delete_single_screen_works() {
        // Arrange
        List<DeviceDescriptionWrapper> firstScreen = viewModel.getScreens().subList(0, 1);
        viewModel.setSelectedScreens(firstScreen);

        // Act
        viewModel.deleteSelectedScreens();

        // Assert
        assertEquals(1, viewModel.getScreens().size());
    }

    @Test
    public void delete_all_screens_works() {
        // Arrange
        List<DeviceDescriptionWrapper> screens = viewModel.getScreens().subList(0, 2);
        viewModel.setSelectedScreens(screens);

        // Act
        viewModel.deleteSelectedScreens();

        // Assert
        assertEquals(0, viewModel.getScreens().size());
    }

    @Test
    public void delete_all_screens_causes_null_target() {
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
    public void add_screen_creates_screen_with_blank_key_and_description() {
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
