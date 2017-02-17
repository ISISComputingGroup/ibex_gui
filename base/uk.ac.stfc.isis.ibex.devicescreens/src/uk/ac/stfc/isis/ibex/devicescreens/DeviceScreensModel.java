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
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The backend model for the device screens.
 */
public class DeviceScreensModel extends ModelObject {
    private DeviceScreensDescription deviceScreensDescription;
    private SameTypeWriter<DeviceScreensDescription> writableDeviceScreenDescriptions;

    private DeviceScreensDescription localDevices;
    private boolean canWriteRemote;

    /**
     * @return the canWriteRemoteDeviceScreens
     */
    public boolean isCanWriteRemote() {
        return canWriteRemote;
    }

    /**
     * @param canWriteRemoteDeviceScreens
     *            the canWriteRemoteDeviceScreens to set
     */
    private void setCanWriteRemote(boolean canWriteRemoteDeviceScreens) {
        firePropertyChange("canWriteRemote", this.canWriteRemote,
                this.canWriteRemote = canWriteRemoteDeviceScreens);
    }

    /**
     * Constructor.
     * 
     * @param observableDeviceScreensDescription
     *            the observable of a device screen description
     * @param writableDeviceScreenDescriptions
     *            the writable of a device screen description
     */
    public DeviceScreensModel(Observable<DeviceScreensDescription> observableDeviceScreensDescription,
            Writable<DeviceScreensDescription> writableDeviceScreenDescriptions) {

        localDevices = new DeviceScreensDescription();

        this.writableDeviceScreenDescriptions = new SameTypeWriter<DeviceScreensDescription>() {
            @Override
            public void onCanWriteChanged(boolean canWrite) {
                setCanWriteRemote(canWrite);
            };
        };

        this.writableDeviceScreenDescriptions.writeTo(writableDeviceScreenDescriptions);
        writableDeviceScreenDescriptions.subscribe(this.writableDeviceScreenDescriptions);

        observableDeviceScreensDescription.addObserver(new Observer<DeviceScreensDescription>() {

            @Override
            public void update(DeviceScreensDescription value, Exception error, boolean isConnected) {
                if (value != null) {
                    onValue(value);
                }
            }

            @Override
            public void onValue(DeviceScreensDescription remoteDeviceScreenDescription) {

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
                return;
            }

            @Override
            public void onConnectionStatus(boolean isConnected) {
                return;
            }
        });
    }

    /**
     * Getter for the device screens description.
     * 
     * @return the device screens description
     */
    public DeviceScreensDescription getDeviceScreensDescription() {
        return deviceScreensDescription;
    }

    private void updateDeviceScreensDescription(DeviceScreensDescription deviceScreensDescription) {
        firePropertyChange("deviceScreensDescription", this.deviceScreensDescription,
                this.deviceScreensDescription = deviceScreensDescription);
    }

    /**
     * Setter for the device screens description.
     * 
     * @param deviceScreensDescription
     *            the device screens description to set
     */
    public void setDeviceScreensDescription(DeviceScreensDescription deviceScreensDescription) {

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

        updateDeviceScreensDescription(deviceScreensDescription);
    }

}
