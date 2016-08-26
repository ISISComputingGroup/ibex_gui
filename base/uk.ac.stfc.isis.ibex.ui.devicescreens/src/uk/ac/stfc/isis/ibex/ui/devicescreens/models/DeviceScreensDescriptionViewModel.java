
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
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * 
 */
public class DeviceScreensDescriptionViewModel extends ModelObject {
    
    private DeviceScreensDescription description;
    private List<String> screenNames;
    private String name = "Hello";

    public DeviceScreensDescriptionViewModel(DeviceScreensDescription description) {
        this.description = description;
        screenNames = new ArrayList<>();
        for (DeviceDescription s : description.getDevices()) {
            screenNames.add(s.getName());
        }
    }

    public Collection<String> getScreenNames() {
        return screenNames;
    }

    public void addScreen(String name) {
        List<String> namesBefore = new ArrayList<>(screenNames);
        screenNames.add(name);
        firePropertyChange("screenNames", namesBefore, screenNames);
    }

    public String getName() {
        return name;
    }

}
