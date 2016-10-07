
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
    
    private DeviceDescriptionWrapper selectedScreen = null;
    private DeviceScreensDescription description;
    private DeviceScreensDescription original;
    private PropertyDescription selectedProperty = null;
    private List<DeviceDescriptionWrapper> devices;

    // Proxy objects
    private String currentName = "";
    private String currentKey = "";
    private String currentDescription = "";
    private String currentPropertyValue = "";
    private OpiDescription currentOpi = null;

    public DeviceScreensDescriptionViewModel(DeviceScreensDescription description) {
        // Keep a reference to the original, so we can change it after editing
        original = description;
        // From the description create a list of devices
        devices = new ArrayList<>();
        for (DeviceDescription d : description.getDevices()) {
            devices.add(new DeviceDescriptionWrapper(d));
        }

        // Create a copy of the description, so we can cancel the changes
        // this.description = new DeviceScreensDescription(description);
    }

    public List<DeviceDescriptionWrapper> getScreens() {
        return devices;
    }

    /**
     * @param index
     */
    public void setSelectedScreen(int index) {
        // TODO: Need to check index is in bounds
        firePropertyChange("selectedScreen", selectedScreen, selectedScreen = devices.get(index));

        // Fire property changes for associated values
        firePropertyChange("currentName", currentName, currentName = selectedScreen.getName());
        firePropertyChange("currentKey", currentKey, currentKey = selectedScreen.getKey());
        firePropertyChange("currentDescription", currentDescription,
                currentDescription = selectedScreen.getDescription());
        // Clear out the stored property information
        setSelectedProperty(-1);

//        DeviceDescription newDescription = new DeviceDescription();
//        
//        // Valid selection
//        try {
//            newDescription = description.getDevices().get(selectionIndex);
//        } catch (IndexOutOfBoundsException e) {
//            // No item is selected - may be it was deleted
//            // Clear the cached values
//            setCurrentName("");
//            setCurrentKey("");
//            setCurrentDescription("");
//            return;
//        }
//
//        firePropertyChange("selectedScreen", selectedScreen, selectedScreen = newDescription);
//        // Clear the selected property
//        setSelectedProperty(-1);
//        // Update cached values to match selection
//        setCurrentName(selectedScreen.getName());
//        setCurrentKey(selectedScreen.getKey());
//        setCurrentDescription(selectedScreen.getKey());
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
        setCurrentDescription(key);
        // Set the OPI description
        setOpiDescription(key);

        firePropertyChange("currentKey", currentKey, currentKey = key);
    }

    public String getCurrentDescription() {
        return currentDescription;
    }

    public void setCurrentDescription(String key) {
        if (selectedScreen == null) {
            return;
        }

        firePropertyChange("currentDescription", currentDescription, currentDescription = getOpi(key).getDescription());
    }

    private OpiDescription getOpi(String targetName) {
        String name = Opi.getDefault().descriptionsProvider().guessOpiName(targetName);
        OpiDescription opi = Opi.getDefault().descriptionsProvider().getDescription(name);
        return opi;
    }

    public void deleteScreen(int index) {
        if (index >= 0 && index < description.getDevices().size()) {
            List<DeviceDescription> oldList = new ArrayList<>(description.getDevices());
            // Delete it
            description.getDevices().remove(index);
            firePropertyChange("screens", oldList, description.getDevices());
        }
    }

    public void addScreen() {
        List<DeviceDescription> oldList = new ArrayList<>(description.getDevices());

        DefaultName namer = new DefaultName("Screen", " ", true);
        List<String> names = new ArrayList<>();
        for (DeviceDescription d : oldList) {
            names.add(d.getName());
        }

        DeviceDescription newScreen = new DeviceDescription();
        newScreen.setName(namer.getUnique(names));
        newScreen.setKey("");
        newScreen.setType("");

        description.getDevices().add(newScreen);
        firePropertyChange("screens", oldList, description.getDevices());
    }

    public void moveScreenUp(int index) {
        if (index > 0) {
            swapScreens(index, index - 1);
        }
    }

    public void moveScreenDown(int index) {
        if (index < description.getDevices().size() - 1) {
            swapScreens(index, index + 1);
        }
    }

    private void swapScreens(int selected, int toSwapWith) {
        List<DeviceDescription> oldOrder = new ArrayList<>(description.getDevices());
        Collections.swap(description.getDevices(), selected, toSwapWith);
        firePropertyChange("screens", oldOrder, description.getDevices());
    }

    /**
     * Sets the currently selected property.
     * 
     * @param index the index
     */
    public void setSelectedProperty(int index) {
        if (selectedScreen != null && index != -1) {
            // setSelectedPropertyValue(null);
            firePropertyChange("selectedProperty", selectedProperty,
                    selectedProperty = selectedScreen.getProperties().get(index));
        }
        else {
            // setSelectedPropertyValue(null);
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

    public void setOpiDescription(String name) {
        currentOpi = getOpi(name);
    }

    public OpiDescription getOpiDescription() {
        if (selectedScreen != null) {
            return selectedScreen.getOpi();
        }
        return null;
    }

}
