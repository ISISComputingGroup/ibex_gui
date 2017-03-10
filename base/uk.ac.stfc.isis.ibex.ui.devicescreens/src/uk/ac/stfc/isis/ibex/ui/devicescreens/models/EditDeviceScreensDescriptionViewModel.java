
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import uk.ac.stfc.isis.ibex.configserver.editing.DefaultName;
import uk.ac.stfc.isis.ibex.devicescreens.DeviceScreensModel;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.PropertyDescription;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.opis.DescriptionsProvider;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * The view model for linking the edit panel to the model.
 */
public class EditDeviceScreensDescriptionViewModel extends ModelObject {

    /** Used to display any errors relating to values entered. */
    private final MessageDisplayer messageDisplayer;

    /** Provides information about the OPIs. */
    private DescriptionsProvider provider;

    /** The currently selected screens. */
    private List<DeviceDescriptionWrapper> selectedScreens = new ArrayList<DeviceDescriptionWrapper>();

    /** The screen that we are setting target details on. */
    private DeviceDescriptionWrapper targetScreen = null;

    /** The currently selected property in the table. */
    private PropertyDescription selectedProperty = null;

    /** The screens. */
    private List<DeviceDescriptionWrapper> devices;

    /**
     * The name of the screen.
     */
    private String name;

    /**
     * The value of the key (the OPI that the target is pointing to).
     */
    private String key;

    /**
     * The key description.
     */
    private String description;

    private Boolean persistence;

    private DeviceScreensModel deviceScreensModel;

    private boolean canWriteRemote;

    private boolean enabled;

    private boolean persistenceEnabled;

    private Comparator<DeviceDescriptionWrapper> deviceSorter = new Comparator<DeviceDescriptionWrapper>() {

        @Override
        public int compare(DeviceDescriptionWrapper p1, DeviceDescriptionWrapper p2) {

            int comparator = p1.getName().compareToIgnoreCase(p2.getName());
            if (comparator == 0) {
                comparator = p1.getKey().compareToIgnoreCase(p2.getKey());
            }
            return comparator;
        }
    };

    /**
     * The constructor.
     * 
     * @param deviceScreensModel
     *            the model of device screens description to edit
     * @param provider
     *            supplies the OPI information
     * @param messageDisplayer
     *            displays messages
     */
    public EditDeviceScreensDescriptionViewModel(DeviceScreensModel deviceScreensModel,
            DescriptionsProvider provider, MessageDisplayer messageDisplayer) {
        this.messageDisplayer = messageDisplayer;
        this.provider = provider;

        canWriteRemote = deviceScreensModel.isCanWriteRemote();

        // From the description create a list of devices
        ArrayList<DeviceDescriptionWrapper> screens = new ArrayList<>();
        for (DeviceDescription d : deviceScreensModel.getDeviceScreensDescription().getDevices()) {
            screens.add(new DeviceDescriptionWrapper(d, this.provider));
        }

        setScreens(screens);
        
        this.deviceScreensModel = deviceScreensModel;
    }

    /**
     * Sets the screens.
     *
     * @param screens
     *            the screens to set
     */
    public void setScreens(List<DeviceDescriptionWrapper> screens) {
        screens.sort(deviceSorter);
        
        firePropertyChange("screens", devices, devices = screens);

        checkScreensValid();
    }
    
    /**
     * Gets the screens.
     *
     * @return the devices
     */
    public List<DeviceDescriptionWrapper> getScreens() {
        return devices;
    }

    /**
     * Screen is editable and enabled when we can write remotely or the screen
     * is local
     * 
     * @param persistent
     *            is the screen persistent
     * @return true when enabled; false otherwise
     */
    private boolean isScreenEnabled(boolean persistent) {
        if (this.canWriteRemote) {
            return true;
        }
        return !persistent;
    }

    /**
     * Changes the currently selected screen.
     * 
     * @param screens
     *            the new screens
     */
    public void setSelectedScreens(List<DeviceDescriptionWrapper> screens) {
        firePropertyChange("selectedScreens", selectedScreens, selectedScreens = screens);
        // Update values for associated fields
        if (screens == null || screens.size() > 1) {
            updateName("");
            updateCurrentKey("");
            setDescription("");
            updatePersistence(false);
            setEnabled(false);
        } else {
            setTargetScreen(screens.get(0));
            updateName(targetScreen.getName());
            updateCurrentKey(targetScreen.getKey());
            setDescription(targetScreen.getDescription());
            updatePersistence(targetScreen.getPersistent());
            setEnabled(isScreenEnabled(targetScreen.getPersistent()));
        }

        // Clear out the stored property information
        setSelectedProperty(-1);
    }

    /**
     * @return the screen that the target panel is editing.
     */
    public DeviceDescriptionWrapper getTargetScreen() {
        return targetScreen;
    }

    /**
     * @param screen
     *            the screen that the target panel should edit.
     */
    public void setTargetScreen(DeviceDescriptionWrapper screen) {
        firePropertyChange("targetScreen", targetScreen, targetScreen = screen);
    }

    /**
     * Gets the selected screen.
     *
     * @return the currently selected screen
     */
    public List<DeviceDescriptionWrapper> getSelectedScreens() {
        return selectedScreens;
    }

    /**
     * Sets the current persistence.
     *
     * @param persistence
     *            the new persistence
     */
    public void setPersistence(Boolean persistence) {
        targetScreen.setPersistent(persistence);
        updatePersistence(persistence);

        checkScreensValid();
    }

    /**
     * Gets the current persistence.
     * 
     * @return the current persistence
     */
    public Boolean getPersistence() {
        return persistence;
    }

    /**
     * Update the current persistence setting and raise a change event.
     * 
     * @param newPersistence
     *            the new persiststence setting.
     */
    private void updatePersistence(Boolean newPersistence) {
        firePropertyChange("persistence", persistence, persistence = newPersistence);
    }

    /**
     * Gets the current name.
     *
     * @return the current name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the current name.
     *
     * @param name
     *            the new name to set
     */
    public void setName(String name) {
        targetScreen.setName(name);
        updateName(name);

        checkScreensValid();
    }

    /**
     * Update the current name value and raise a change event.
     * 
     * @param newName
     *            the new name
     */
    private void updateName(String newName) {
        firePropertyChange("name", name, name = newName);
    }

    private void setEnabled(boolean newEnabled) {
        firePropertyChange("enabled", enabled, enabled = newEnabled);
        updatePersistenceEnabled();
    }
    
    /**
     * 
     * @return whether the persistence screen is enabled.
     */
    public boolean getPersistenceEnabled() {
        return persistenceEnabled;
    }

    private void updatePersistenceEnabled() {
        boolean newEnabled = enabled && canWriteRemote;
        firePropertyChange("persistenceEnabled", persistenceEnabled, persistenceEnabled = newEnabled);
    }

    /**
     * 
     * @return whether the target screen is enabled.
     */
    public boolean getEnabled() {
        return enabled;
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
     * Gets the current key.
     *
     * @return the current key
     */
    public String getCurrentKey() {
        return key;
    }

    /**
     * Sets the current key.
     *
     * @param key
     *            the new value to set
     */
    public void setCurrentKey(String key) {
        targetScreen.setKey(key);
        updateCurrentKey(key);
        setDescription(targetScreen.getDescription());
    }

    /**
     * Update the current key value and raise a change event.
     * 
     * @param newKey the new key
     */
    private void updateCurrentKey(String newKey) {
        firePropertyChange("currentKey", key, key = newKey);

        // Must update to the corresponding description
        checkScreensValid();
    }

    /**
     * Gets the current description.
     *
     * @return the current OPI description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Update the current OPI description.
     * 
     * @param value
     *            The value to set the OPI description to.
     */
    private void setDescription(String value) {
        firePropertyChange("description", description, description = value);
    }

    /**
     * Add a blank screen to the list of screens.
     */
    public void addScreen() {
        List<DeviceDescriptionWrapper> newList = new ArrayList<>(devices);

        DefaultName namer = new DefaultName("Screen", "_", false);
        List<String> names = new ArrayList<>();
        for (DeviceDescriptionWrapper d : newList) {
            names.add(d.getName());
        }

        DeviceDescription newScreen = new DeviceDescription(namer.getUnique(names), "", "", false);

        newList.add(new DeviceDescriptionWrapper(newScreen, provider));
        setScreens(newList);
    }

    /**
     * Sets the currently selected property.
     * 
     * @param index the index
     */
    public void setSelectedProperty(int index) {
        if (targetScreen != null && index != -1) {
            firePropertyChange("selectedProperty", selectedProperty,
                    selectedProperty = targetScreen.getProperties().get(index));
        } else {
            firePropertyChange("selectedProperty", selectedProperty, selectedProperty = null);
        }
    }

    /**
     * Gets the selected property value.
     *
     * @return the selected property value
     */
    public String getSelectedPropertyValue() {
        if (selectedProperty != null) {
            return selectedProperty.getValue();
        }
        return "";
    }

    /**
     * Sets the selected property value.
     *
     * @param value
     *            the value to set for the property
     */
    public void setSelectedPropertyValue(String value) {
        if (selectedProperty != null) {
            String oldVal = selectedProperty.getValue();
            selectedProperty.setValue(value);

            firePropertyChange("selectedPropertyValue", oldVal, value);
        }
    }

    /**
     * Gets the selected property description.
     *
     * @return the selected property's description
     */
    public String getSelectedPropertyDescription() {
        if (targetScreen != null && selectedProperty != null) {
            return targetScreen.getMacroDescription(selectedProperty.getKey());
        }
        return "";
    }

    /**
     * Saves the device screens description into the model.
     */
    public void save() {
        DeviceScreensDescription desc = new DeviceScreensDescription();

        for (DeviceDescriptionWrapper d : devices) {
            DeviceDescription device = new DeviceDescription();
            device.setName(d.getName());
            device.setKey(d.getKey());
            device.setType(d.getType());
            device.setPersist(d.getPersistent());

            for (PropertyDescription p : d.getProperties()) {
                // Only add the filled in ones
                if (p.getValue() != "") {
                    device.addProperty(p);
                }
            }

            desc.addDevice(device);
        }

        deviceScreensModel.setDeviceScreensDescription(desc);
    }

    /**
     * @return the list of available OPIs
     */
    public Collection<String> getAvailableOPIs() {
        return provider.getOpiList();
    }

    /**
     * @return the canWriteRemote
     */
    public boolean canWriteRemote() {
        return canWriteRemote;
    }

    /**
     * Deletes the screens and selects the previous item.
     */
    public void deleteSelectedScreens() {
        int newIndex = devices.indexOf(selectedScreens.get(0)) - 1;
        if (newIndex < 0) {
            newIndex = 0;
        }

        List<DeviceDescriptionWrapper> newList = new ArrayList<>(devices);
        // Delete it
        newList.removeAll(selectedScreens);
        setScreens(newList);
        if (newList.size() > 0) {
            setSelectedScreens(new ArrayList<DeviceDescriptionWrapper>(Arrays.asList(devices.get(newIndex))));
        } else {
            setSelectedScreens(null);
        }
    }
}
