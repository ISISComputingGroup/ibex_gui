
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

import java.util.Collection;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;

/**
 * 
 */
public class DeviceScreensDescriptionViewModel extends ModelObject {
    
    private DeviceDescription selectedScreen = null;
    private DeviceScreensDescription description;

    // Proxy objects
    private String currentName = "";
    private String currentKey = "";
    private String currentDescription = "";

    public DeviceScreensDescriptionViewModel(DeviceScreensDescription description) {
        this.description = description;
    }

    public Collection<DeviceDescription> getScreens() {
        return description.getDevices();
    }

    /**
     * @param selectionIndex
     */
    public void setSelectedScreen(int selectionIndex) {
        DeviceDescription newDescription = null;

        try {
            newDescription = description.getDevices().get(selectionIndex);
        } catch (IndexOutOfBoundsException e) {
            newDescription = null;
        }

        firePropertyChange("SelectedScreen", selectedScreen, selectedScreen = newDescription);

        // Update cached values to match selection
        setCurrentName(selectedScreen.getName());
        setCurrentKey(selectedScreen.getKey());
        setCurrentDescription(selectedScreen.getKey());
    }

    public DeviceDescription getSelectedScreen() {
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
        firePropertyChange("currentKey", currentKey, currentKey = key);
        // Must update to the corresponding description
        setCurrentDescription(key);
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

}
