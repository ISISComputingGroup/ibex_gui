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
import uk.ac.stfc.isis.ibex.epics.observing.Observable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The backend model for the device screens.
 */
public class DeviceScreensModel extends ModelObject {
    private DeviceScreensDescription deviceScreensDescription;
    private Writable<DeviceScreensDescription> writableDeviceScreenDescriptions;

    private DeviceScreensDescription localDevices;

    public DeviceScreensModel(Observable<DeviceScreensDescription> observableDeviceScreensDescription,
            Writable<DeviceScreensDescription> writableDeviceScreenDescriptions) {

        localDevices = new DeviceScreensDescription();
        this.writableDeviceScreenDescriptions = writableDeviceScreenDescriptions;

        observableDeviceScreensDescription.addObserver(new Observer<DeviceScreensDescription>() {

            @Override
            public void update(DeviceScreensDescription value, Exception error, boolean isConnected) {
                //
            }

            @Override
            public void onValue(DeviceScreensDescription remoteDeviceScreenDescription) {

                System.out.println("... " + remoteDeviceScreenDescription.getDevices().size());
                DeviceScreensDescription copy = new DeviceScreensDescription(remoteDeviceScreenDescription);

                for (DeviceDescription device : copy.getDevices()) {
                    device.setPersist(true);
                }

                for (DeviceDescription device : localDevices.getDevices()) {
                    DeviceDescription deviceCopy = new DeviceDescription(device);
                    deviceCopy.setPersist(false);
                    copy.addDevice(deviceCopy);
                }

                updateDeviceScreensDescription(copy);
            }

            @Override
            public void onError(Exception e) {
                //
            }

            @Override
            public void onConnectionStatus(boolean isConnected) {
                //
            }
        });
    }

    public DeviceScreensDescription getDeviceScreensDescription() {
        return deviceScreensDescription;
    }

    private void updateDeviceScreensDescription(DeviceScreensDescription deviceScreensDescription) {
        firePropertyChange("deviceScreensDescription", this.deviceScreensDescription,
                this.deviceScreensDescription = deviceScreensDescription);
    }

    public void setDeviceScreensDescription(DeviceScreensDescription deviceScreensDescription) {

        this.deviceScreensDescription = deviceScreensDescription;

        DeviceScreensDescription remoteDevices = new DeviceScreensDescription();

        localDevices = new DeviceScreensDescription();

        for (DeviceDescription device : deviceScreensDescription.getDevices()) {

            if (device.getPersist()) {
                remoteDevices.addDevice(device);
            } else {
                localDevices.addDevice(device);
            }

        }

        writableDeviceScreenDescriptions.write(remoteDevices);
        System.out.println("writable: " + remoteDevices.getDevices().size());
    }

}
