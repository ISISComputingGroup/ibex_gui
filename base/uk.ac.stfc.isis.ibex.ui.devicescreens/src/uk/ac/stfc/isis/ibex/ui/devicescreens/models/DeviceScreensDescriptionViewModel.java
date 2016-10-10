
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
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * The view model for linking the GUI with the device screens description.
 */
public class DeviceScreensDescriptionViewModel extends ModelObject {

    /** Used to display any errors relating to values entered. */
    private final MessageDisplayer messageDisplayer;

    /** The currently selected screen. */
    private DeviceDescriptionWrapper selectedScreen = null;

    /** The currently selected property in the table. */
    private PropertyDescription selectedProperty = null;

    /** The screens. */
    private List<DeviceDescriptionWrapper> devices;

    /**
     * A proxy object for the current name to allow simple data-binding to work.
     */
    private String currentName = "";

    /**
     * A proxy object for the current key to allow simple data-binding to work.
     */
    private String currentKey = "";

    /**
     * A proxy object for the current description to allow simple data-binding
     * to work.
     */
    private String currentDescription = "";

    /**
     * The constructor.
     * 
     * @param description the description to edit
     * @param messageDisplayer a reference to the dialog to allow error messages
     *            to be displayed
     */
    public DeviceScreensDescriptionViewModel(DeviceScreensDescription description, MessageDisplayer messageDisplayer) {
        this.messageDisplayer = messageDisplayer;

        // From the description create a list of devices
        devices = new ArrayList<>();
        for (DeviceDescription d : description.getDevices()) {
            devices.add(new DeviceDescriptionWrapper(d));
        }
    }

    /**
     * @return the devices
     */
    public List<DeviceDescriptionWrapper> getScreens() {
        return devices;
    }

    /**
     * Changes the selected screen.
     * 
     * @param index the index of the selected screen
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

    /**
     * @return the currently selected screen
     */
    public DeviceDescriptionWrapper getSelectedScreen() {
        return selectedScreen;
    }

    /**
     * @return the current name
     */
    public String getCurrentName() {
        return currentName;
    }

    /**
     * @param name the new name to set
     */
    public void setCurrentName(String name) {
        if (selectedScreen == null) {
            return;
        }

        // Check for duplicate names
        if (isDuplicateName(name)) {
            messageDisplayer.setErrorMessage("ViewModel", "Duplicate names are not allowed");
        } else {
            messageDisplayer.setErrorMessage("ViewModel", null);
        }

        selectedScreen.setName(name);
        firePropertyChange("currentName", currentName, currentName = name);
    }

    /**
     * Checks to see if this name matches one of the existing names.
     * 
     * @param name the name to check
     * @return true is the name is a duplicate; false otherwise
     */
    private boolean isDuplicateName(String name) {
        for (int i = 0; i < devices.size(); ++i) {
            if (name.trim().equalsIgnoreCase(devices.get(i).getName().trim())) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return the current key
     */
    public String getCurrentKey() {
        return currentKey;
    }

    /**
     * @param key the new value to set
     */
    public void setCurrentKey(String key) {
        if (selectedScreen == null) {
            return;
        }

        if (key == null) {
            key = "";
        }

        selectedScreen.setKey(key);
        // Must update to the corresponding description
        setCurrentDescription(selectedScreen.getDescription());

        firePropertyChange("currentKey", currentKey, currentKey = key);
    }

    /**
     * @return the current OPI description
     */
    public String getCurrentDescription() {
        return currentDescription;
    }

    /**
     * @param value the description to set
     */
    private void setCurrentDescription(String value) {
        if (selectedScreen == null) {
            return;
        }

        firePropertyChange("currentDescription", currentDescription, currentDescription = value);
    }

    /**
     * Deletes a screen.
     * 
     * @param index the index of the screen to delete
     */
    public void deleteScreen(int index) {
        if (index >= 0 && index < devices.size()) {
            List<DeviceDescriptionWrapper> oldList = new ArrayList<>(devices);
            // Delete it
            devices.remove(index);
            firePropertyChange("screens", oldList, devices);
        }
    }

    /**
     * Add a blank screen to the description.
     */
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

    /**
     * Move a screen towards the front of the list.
     * 
     * @param index the index of the screen to move
     */
    public void moveScreenUp(int index) {
        if (index > 0) {
            swapScreens(index, index - 1);
        }
    }

    /**
     * Move a screen towards the back of the list.
     * 
     * @param index the index of the screen to move
     */
    public void moveScreenDown(int index) {
        if (index < devices.size() - 1) {
            swapScreens(index, index + 1);
        }
    }

    /**
     * Swap two devices in the list.
     * 
     * @param selected the index of the selected item
     * @param toSwapWith the index of the item to swap with
     */
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
        } else {
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

    /**
     * @param value the value to set for the property
     */
    public void setSelectedPropertyValue(String value) {
        if (selectedProperty != null) {
            String oldVal = selectedProperty.getValue();
            selectedProperty.setValue(value);

            firePropertyChange("selectedPropertyValue", oldVal, value);
        }
    }

    /**
     * @return the selected property's description
     */
    public String getSelectedPropertyDescription() {
        if (selectedScreen != null && selectedProperty != null) {
            return selectedScreen.getOpi().getMacroDescription(selectedProperty.getKey());
        }
        return "";
    }

    /**
     * @return the OPI description
     */
    public OpiDescription getOpiDescription() {
        if (selectedScreen != null) {
            return selectedScreen.getOpi();
        }
        return null;
    }

    /**
     * Gets the new constructed device description for saving.
     * 
     * @return The description
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
