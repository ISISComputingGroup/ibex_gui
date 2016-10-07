
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
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.opis.desc.MacroInfo;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;

/**
 * A helpful wrapper for DeviceDescription objects.
 * 
 * It brings together all the data required to display a DeviceDescription for
 * editing in the GUI.
 */
public class DeviceDescriptionWrapper {

    private DeviceDescription device;
    private List<PropertyDescription> properties;
    private String name = "";
    private String key = "";
    private OpiDescription opi = null;

    /**
     * The constructor.
     * 
     * @param device the device description to wrap
     */
    public DeviceDescriptionWrapper(DeviceDescription device) {
        this.device = new DeviceDescription(device);
        name = this.device.getName();
        key = this.device.getKey();

        mergeProperties();
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
        
        // On a key change clear any set properties as they are not longer needed
        device.clearProperties();
        mergeProperties();
    }

    public String getDescription() {
        if (opi != null) {
            return opi.getDescription();
        }
        return "";
    }

    public List<PropertyDescription> getProperties() {
        return properties;
    }

    public OpiDescription getOpi() {
        return opi;
    }

    public String getType() {
        return device.getType();
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

    private OpiDescription getOpi(String targetName) {
        String name = Opi.getDefault().descriptionsProvider().guessOpiName(targetName);
        OpiDescription opi = Opi.getDefault().descriptionsProvider().getDescription(name);
        return opi;
    }

    @Override
    public int hashCode() {
        return name.hashCode() ^ key.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

}
