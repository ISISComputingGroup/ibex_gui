
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
package uk.ac.stfc.isis.ibex.ui.devicescreens.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.stfc.isis.ibex.configserver.editing.DefaultName;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.PropertyDescription;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;

/**
 * 
 */
public class DeviceScreensDescriptionViewModel extends ModelObject {
    
    private DeviceScreensDescription original;
    private DeviceDescriptionWrapper selectedScreen = null;
    private PropertyDescription selectedProperty = null;
    private List<DeviceDescriptionWrapper> devices;

    // Proxy objects for data-binding
    private String currentName = "";
    private String currentKey = "";
    private String currentDescription = "";

    public DeviceScreensDescriptionViewModel(DeviceScreensDescription description) {
        // Keep a reference to the original, so we can change it after editing
        original = description;
        // From the description create a list of devices
        devices = new ArrayList<>();
        for (DeviceDescription d : description.getDevices()) {
            devices.add(new DeviceDescriptionWrapper(d));
        }
    }

    public List<DeviceDescriptionWrapper> getScreens() {
        return devices;
    }

    /**
     * @param index
     */
    public void setSelectedScreen(int index) {
        if (index >= 0 && index < devices.size()) {
            // Okay
            firePropertyChange("selectedScreen", selectedScreen, selectedScreen = devices.get(index));
            // Fire property changes for associated values
            firePropertyChange("currentName", currentName, currentName = selectedScreen.getName());
            firePropertyChange("currentKey", currentKey, currentKey = selectedScreen.getKey());
            firePropertyChange("currentDescription", currentDescription,
                    currentDescription = selectedScreen.getDescription());
        } else {
            // Clear the settings
            firePropertyChange("selectedScreen", selectedScreen, selectedScreen = null);
            // Fire property changes for associated values
            firePropertyChange("currentName", currentName, currentName = "");
            firePropertyChange("currentKey", currentKey, currentKey = "");
            firePropertyChange("currentDescription", currentDescription, currentDescription = "");
        }

        // Clear out the stored property information
        setSelectedProperty(-1);
    }

    public DeviceDescriptionWrapper getSelectedScreen() {
        return selectedScreen;
    }

    public String getCurrentName() {
        return currentName;
    }

    public void setCurrentName(String name) {
        if (selectedScreen == null) {
            return;
        }

        selectedScreen.setName(name);
        firePropertyChange("currentName", currentName, currentName = name);
    }

    public String getCurrentKey() {
        return currentKey;
    }

    public void setCurrentKey(String key) {
        if (selectedScreen == null) {
            return;
        }

        selectedScreen.setKey(key);
        // Must update to the corresponding description
        setCurrentDescription(selectedScreen.getDescription());

        firePropertyChange("currentKey", currentKey, currentKey = key);
    }

    public String getCurrentDescription() {
        return currentDescription;
    }

    public void setCurrentDescription(String value) {
        if (selectedScreen == null) {
            return;
        }

        firePropertyChange("currentDescription", currentDescription, currentDescription = value);
    }

    private OpiDescription getOpi(String targetName) {
        String name = Opi.getDefault().descriptionsProvider().guessOpiName(targetName);
        OpiDescription opi = Opi.getDefault().descriptionsProvider().getDescription(name);
        return opi;
    }

    public void deleteScreen(int index) {
        if (index >= 0 && index < devices.size()) {
            List<DeviceDescriptionWrapper> oldList = new ArrayList<>(devices);
            // Delete it
            devices.remove(index);
            firePropertyChange("screens", oldList, devices);
        }
    }

    public void addScreen() {
        List<DeviceDescriptionWrapper> oldList = new ArrayList<>(devices);

        DefaultName namer = new DefaultName("Screen", " ", true);
        List<String> names = new ArrayList<>();
        for (DeviceDescriptionWrapper d : oldList) {
            names.add(d.getName());
        }

        DeviceDescription newScreen = new DeviceDescription();
        newScreen.setName(namer.getUnique(names));
        newScreen.setKey("");
        newScreen.setType("");

        devices.add(new DeviceDescriptionWrapper(newScreen));
        firePropertyChange("screens", oldList, devices);
    }

    public void moveScreenUp(int index) {
        if (index > 0) {
            swapScreens(index, index - 1);
        }
    }

    public void moveScreenDown(int index) {
        if (index < devices.size() - 1) {
            swapScreens(index, index + 1);
        }
    }

    private void swapScreens(int selected, int toSwapWith) {
        List<DeviceDescriptionWrapper> oldOrder = new ArrayList<>(devices);
        Collections.swap(devices, selected, toSwapWith);
        firePropertyChange("screens", oldOrder, devices);
    }

    /**
     * Sets the currently selected property.
     * 
     * @param index the index
     */
    public void setSelectedProperty(int index) {
        if (selectedScreen != null && index != -1) {
            firePropertyChange("selectedProperty", selectedProperty,
                    selectedProperty = selectedScreen.getProperties().get(index));
        }
        else {
            firePropertyChange("selectedProperty", selectedProperty, selectedProperty = null);
        }
    }

    /**
     * @return the selected property value
     */
    public String getSelectedPropertyValue() {
        if (selectedProperty != null) {
            return selectedProperty.getValue();
        }
        return "";
    }

    public void setSelectedPropertyValue(String value) {
        if (selectedProperty != null) {
            String oldVal = selectedProperty.getValue();
            selectedProperty.setValue(value);

            firePropertyChange("selectedPropertyValue", oldVal, value);
        }
    }

    /**
     * @return
     */
    public String getSelectedPropertyDescription() {
        if (selectedScreen != null && selectedProperty != null) {
            return selectedScreen.getOpi().getMacroDescription(selectedProperty.getKey());
        }
        return "";
    }


    public OpiDescription getOpiDescription() {
        if (selectedScreen != null) {
            return selectedScreen.getOpi();
        }
        return null;
    }

    /**
     * Gets the new device description for saving.
     */
    public DeviceScreensDescription getDeviceDescription() {
        DeviceScreensDescription desc = new DeviceScreensDescription();

        for (DeviceDescriptionWrapper d : devices) {
            DeviceDescription device = new DeviceDescription();
            device.setName(d.getName());
            device.setKey(d.getKey());
            device.setType(d.getType());
            for (PropertyDescription p : d.getProperties()) {
                // Only add the filled in ones
                if (p.getValue() != "") {
                    device.addProperty(p);
                }
            }

            desc.addDevice(device);
        }

        return desc;
    }


}
