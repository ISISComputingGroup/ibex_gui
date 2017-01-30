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

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

/**
 * This class defines a no-op converter between two DeviceScreensDescription
 * classes.
 */
public class DeviceScreensDescriptionConverterToPersistence
        extends Converter<DeviceScreensDescription, DeviceScreensDescription> {

    /**
     * 
     * A no-op converter between two DeviceScreensDescription classes.
     * 
     * @param deviceScreensDescription
     * @return
     * @throws ConversionException
     */
    @Override
    public DeviceScreensDescription convert(DeviceScreensDescription deviceScreensDescription) throws ConversionException {

        if (deviceScreensDescription != null) {
            DeviceScreensDescription copy = new DeviceScreensDescription(deviceScreensDescription);

            for (DeviceDescription device : copy.getDevices()) {
                device.setPersist(true);
            }

            for (DeviceDescription device : DeviceScreens.getInstance().getVariables()
                    .getNonPersistentDeviceScreens()) {
                device.setPersist(false);
                copy.addDevice(device);
            }
            return copy;
        } else {
            throw new ConversionException("Can't convert null");
        }
    }

}
