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

import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

/**
 *
 */
final class DeviceScreensDescriptionConverterFromPersistence
        extends Converter<DeviceScreensDescription, DeviceScreensDescription> {
    @Override
    public DeviceScreensDescription convert(DeviceScreensDescription deviceScreens) throws ConversionException {

        List<DeviceDescription> persistentDevices = new ArrayList<>();

        DeviceScreensDescription localDevices = new DeviceScreensDescription();

        for (DeviceDescription device : deviceScreens.getDevices()) {
            
            if (device.getPersist()) {
                persistentDevices.add(device);
            } else {

                localDevices.addDevice(device);
            }

        }

        DeviceScreens.getInstance().getVariables().getLocalDeviceScreens().updateValue(localDevices);
        deviceScreens.setDevices(persistentDevices);

        System.out.println(
                "FromPersistence: Size of persistent screens list is now " + deviceScreens.getDevices().size());

        return deviceScreens;
    }
}