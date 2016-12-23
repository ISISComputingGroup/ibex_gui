
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
import java.util.regex.Pattern;

import uk.ac.stfc.isis.ibex.configserver.editing.DefaultName;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.PropertyDescription;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.opis.DescriptionsProvider;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * The view model for linking the GUI with the device screens description.
 */
public class DeviceScreensDescriptionViewModel extends ModelObject {

    /** Used to display any errors relating to values entered. */
    private final MessageDisplayer messageDisplayer;

    /** Provides information about the OPIs. */
    private DescriptionsProvider provider;

    /** The currently selected screen. */
    private DeviceDescriptionWrapper selectedScreen = null;

    /** The currently selected property in the table. */
    private PropertyDescription selectedProperty = null;

    /** The screens. */
    private List<DeviceDescriptionWrapper> devices;

    /**
     * Holds the previous name value before it changed either from editing or
     * from switching screens.
     */
    private String previousName = null;

    /**
     * Holds the previous key value before it changed either from editing or
     * from switching screens.
     */
    private String previousKey = null;

    /**
     * Holds the previous description value before it changed either changing
     * the key or from switching screens.
     */
    private String previousDesc = null;

    /**
     * The constructor.
     * 
     * @param description the description to edit
     * @param messageDisplayer a reference to the dialog to allow error messages
     *            to be displayed
     * @param provider supplies the OPI information
     */
    public DeviceScreensDescriptionViewModel(DeviceScreensDescription description, MessageDisplayer messageDisplayer,
            DescriptionsProvider provider) {
        this.messageDisplayer = messageDisplayer;
        this.provider = provider;

        // From the description create a list of devices
        devices = new ArrayList<>();
        for (DeviceDescription d : description.getDevices()) {
            devices.add(new DeviceDescriptionWrapper(d, this.provider));
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
        // As the screen is changing we need to update the stored previous value
        if (selectedScreen != null) {
            previousName = selectedScreen.getName();
            previousKey = (selectedScreen.getKey() == null) ? "" : selectedScreen.getKey();
            previousDesc = (selectedScreen.getDescription() == null) ? "" : selectedScreen.getDescription();
        } else {
            previousName = "";
            previousKey = "";
            previousDesc = "";
        }

        if (index >= 0 && index < devices.size()) {
            // Okay
            changeSelectedScreen(devices.get(index));
        } else {
            // Clear the settings
            changeSelectedScreen(null);
        }
    }

    /**
     * Changes the currently selected screen.
     * 
     * @param screen the new screen
     */
    private void changeSelectedScreen(DeviceDescriptionWrapper screen) {
        firePropertyChange("selectedScreen", selectedScreen, selectedScreen = screen);
        // Update values for associated fields
        if (screen != null) {
            updateCurrentName(selectedScreen.getName());
            updateCurrentKey(selectedScreen.getKey());
            updateCurrentDescription();
        } else {
            updateCurrentName("");
            updateCurrentKey("");
            updateCurrentDescription();
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
        if (selectedScreen != null) {
            return selectedScreen.getName();
        }

        return "";
    }

    /**
     * @param name the new name to set
     */
    public void setCurrentName(String name) {
        if (selectedScreen == null) {
            return;
        }

        selectedScreen.setName(name);
        updateCurrentName(selectedScreen.getName());

        checkScreensValid();
    }

    /**
     * Checks that the parameters entered for all the screens are valid.
     */
    private void checkScreensValid() {
        String errorMessageNames = "Names must be unique and contain only alphanumeric or underscore characters";
        
        Pattern charsOnly = Pattern.compile("\\w+");

        if (!areAllNamesUnique()) {
            messageDisplayer.setErrorMessage("ViewModel", errorMessageNames);
            return;
        }

        for (DeviceDescriptionWrapper d : devices) {
            // Only need to find one error in the names as message covers all
            if (!charsOnly.matcher(d.getName()).matches()) {
                messageDisplayer.setErrorMessage("ViewModel", errorMessageNames);
                return;
            }
            if (d.getKey().length() == 0) {
                String errorMessageTarget =
                        "Device '" + d.getName() + "' is not pointing at a valid target. Please select a target OPI.";
                messageDisplayer.setErrorMessage("ViewModel", errorMessageTarget);
                return;
            }
        }

        // No errors
        messageDisplayer.setErrorMessage("ViewModel", null);
    }


    /**
     * Update the current name value and raise a change event.
     * 
     * @param newName the new name
     */
    private void updateCurrentName(String newName) {
        firePropertyChange("currentName", previousName, previousName = newName);
    }

    /**
     * Checks to see if any names clash.
     * 
     * @return true if a name is a duplicate; false otherwise
     */
    private boolean areAllNamesUnique() {
        List<String> names = new ArrayList<>();

        for (int i = 0; i < devices.size(); ++i) {
            if (names.contains(devices.get(i).getName().trim())) {
                return false;
            }

            names.add(devices.get(i).getName().trim());
        }

        return true;
    }

    /**
     * @return the current key
     */
    public String getCurrentKey() {
        if (selectedScreen != null) {
            return selectedScreen.getKey();
        }

        return "";
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
        updateCurrentKey(key);
    }

    /**
     * Update the current key value and raise a change event.
     * 
     * @param newKey the new key
     */
    private void updateCurrentKey(String newKey) {
        firePropertyChange("currentKey", previousKey, previousKey = newKey);
        // Must update to the corresponding description
        updateCurrentDescription();
        checkScreensValid();
    }

    /**
     * @return the current OPI description
     */
    public String getCurrentDescription() {
        if (selectedScreen != null) {
            return selectedScreen.getDescription();
        }

        return "";
    }

    /**
     * Update the current description value and raise a change event.
     */
    private void updateCurrentDescription() {
        String value = null;
        if (selectedScreen != null) {
            value = selectedScreen.getDescription();
        }

        firePropertyChange("currentDescription", previousDesc, previousDesc = value);
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

        checkScreensValid();
    }

    /**
     * Add a blank screen to the description.
     */
    public void addScreen() {
        List<DeviceDescriptionWrapper> oldList = new ArrayList<>(devices);

        DefaultName namer = new DefaultName("Screen", "_", false);
        List<String> names = new ArrayList<>();
        for (DeviceDescriptionWrapper d : oldList) {
            names.add(d.getName());
        }

        DeviceDescription newScreen = new DeviceDescription();
        newScreen.setName(namer.getUnique(names));
        newScreen.setKey("");
        newScreen.setType("");

        devices.add(new DeviceDescriptionWrapper(newScreen, provider));
        firePropertyChange("screens", oldList, devices);

        checkScreensValid();
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
            return selectedScreen.getMacroDescription(selectedProperty.getKey());
        }
        return "";
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
