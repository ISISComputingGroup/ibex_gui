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
package uk.ac.stfc.isis.ibex.devicescreens;

import java.util.List;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;

/**
 *
 */
public class DeviceScreenVariablesWithPersistence extends DeviceScreenVariables {

    private boolean persistence = true;

    /**
     * Sets the persistence of this device screen.
     * 
     * @param persistence
     *            the persistence of this device screen.
     */
    public void setPersistence(boolean persistence) {
        this.persistence = persistence;
    }

    /**
     * Gets the persistence of this device screen.
     * 
     * @return the persistence of this device screen.
     */
    public boolean getPersistence() {
        return persistence;
    }

    @Override
    public ForwardingObservable<DeviceScreensDescription> getDeviceScreens() {
        ForwardingObservable<DeviceScreensDescription> forwardingObservable = super.getDeviceScreens();

        return new ForwardingObservable<DeviceScreensDescription>(forwardingObservable) {

            @Override
            public DeviceScreensDescription getValue() {
                DeviceScreensDescription output = new DeviceScreensDescription(super.getValue());
                List<DeviceDescription> deviceDescriptionList = output.getDevices();

                for (DeviceDescription deviceDescription : deviceDescriptionList) {
                    deviceDescription.setPersist(getPersistence());
                }

                return output;
            }
            
        };
            
        
    }
}
