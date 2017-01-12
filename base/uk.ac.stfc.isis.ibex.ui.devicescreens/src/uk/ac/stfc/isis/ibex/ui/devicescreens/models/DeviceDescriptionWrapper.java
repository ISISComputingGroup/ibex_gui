
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
import java.util.List;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.PropertyDescription;
import uk.ac.stfc.isis.ibex.opis.DescriptionsProvider;
import uk.ac.stfc.isis.ibex.opis.desc.MacroInfo;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;

/**
 * A helpful wrapper for DeviceDescription objects.
 * 
 * It brings together all the data required to display a DeviceDescription for
 * editing in the GUI.
 */
public class DeviceDescriptionWrapper {

    /** The original device description. */
    private DeviceDescription device;

    /** Provides information about the OPIs. */
    private DescriptionsProvider provider;

    /** The properties for the device. */
    private List<PropertyDescription> properties;

    /** The screen name. */
    private String name = "";

    /** The key is the the OPI name or custom screen name. */
    private String key = "";

    /** Either an OPI or a custom screen. */
    private String type = "";

    /** The OPI description for the current key. */
    private OpiDescription opi = null;

    private String persistence = null;

    /**
     * The constructor.
     * 
     * @param device the device description to wrap
     * @param provider supplies the OPI information
     */
    public DeviceDescriptionWrapper(DeviceDescription device, DescriptionsProvider provider) {
        this.provider = provider;
        this.device = new DeviceDescription(device);
        name = this.device.getName();
        key = this.device.getKey();

        // Default to OPI.
        type = "OPI";

        mergeProperties();
    }

    /**
     * @return the current name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the new value to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the current key
     */
    public String getKey() {
        return key;
    }
    
    /**
     * @param key the new value to set
     */
    public void setKey(String key) {
        this.key = key;
        
        // On a key change clear any set properties as they are not longer needed
        device.clearProperties();
        mergeProperties();
    }

    /**
     * @return the OPI's description text
     */
    public String getDescription() {
        if (opi != null) {
            return opi.getDescription();
        }
        return "";
    }

    /**
     * @return the list of properties for this device
     */
    public List<PropertyDescription> getProperties() {
        return properties;
    }

    /**
     * Gets the property value.
     * 
     * @param key the name of the property
     * @return the property value for the specified key
     */
    public String getMacroDescription(String key) {
        if (opi != null && opi.getKeys().contains(key)) {
            return opi.getMacroDescription(key);
        }

        return "";
    }

    /**
     * @return the type of device screen (e.g. OPI)
     */
    public String getType() {
        return type;
    }

    /**
     * Take the set properties for the device and combine them with the OPI's
     * list of macros.
     */
    private void mergeProperties() {
        // Look up the OPI properties for the device and merge any set values
        properties = new ArrayList<>();
        
        opi = getOpi(key);
        if (opi != null) {
            // If the value has been set previously then use that value
            // If it hasn't been set then add a property with that name
            // but with a blank value
            for (MacroInfo m : opi.getMacros()) {
                String name = m.getName();
                // Set to blank
                properties.add(new PropertyDescription(name, ""));
                for (PropertyDescription p : device.getProperties()) {
                    if (name.equals(p.getKey())) {
                        // Set to value
                        properties.set(properties.size() - 1, p);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Finds the OPI description from the OPI name supplied.
     * 
     * @param targetName the name to look for
     * @return the corresponding OPI description
     */
    private OpiDescription getOpi(String targetName) {
        String name = provider.guessOpiName(targetName);
        OpiDescription opi = provider.getDescription(name);
        return opi;
    }

    @Override
    public int hashCode() {
        return name.hashCode() ^ key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DeviceDescriptionWrapper)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        DeviceDescriptionWrapper other = (DeviceDescriptionWrapper) obj;
        return other.key.equals(key) && other.name.equals(name);
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * @return
     */
    public String getPersistence() {
        System.out.println("DEBUG 6 (returning persistence as " + persistence + ")");
        return persistence;
    }

    public void setPersistence(String persistence) {
        System.out.println("DEBUG 7 (setting persistence as " + persistence + ")");
        this.persistence = persistence;
    }

}
