
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.devicescreens.desc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.epics.conversion.XMLUtil;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * This class describes the devices element of the device screens XML format.
 *
 * Note any changes here will require corresponding changes to
 * EPICS/schema/configurations/screens.xsd.
 */
@XmlRootElement(name = "devices", namespace = "")
public class DeviceScreensDescription {

    private static final Logger LOG = IsisLog.getLogger(DeviceScreensDescription.class);

	/** The devices that make up the description. */
    @XmlElement(name = "device", type = DeviceDescription.class)
    private ArrayList<DeviceDescription> devices = new ArrayList<>();

    /**
     * Default constructor. Needed for parsing XML.
     */
    public DeviceScreensDescription() {
    }

    /**
     * A copy constructor.
     *
     * @param original the item to copy
     */
    public DeviceScreensDescription(DeviceScreensDescription original) {
        for (DeviceDescription d : original.getDevices()) {
            this.addDevice(new DeviceDescription(d));
        }
    }

    /**
     * Getter for the list of devices.
     *
     * @return the list of devices
     */
    public List<DeviceDescription> getDevices() {
        return devices;
    }

    /**
     * Setter for the list of devices.
     *
     * @param devices the new device list
     */
    public void setDevices(List<DeviceDescription> devices) {
        this.devices = (ArrayList<DeviceDescription>) devices;
    }

    /**
     * Adds a device to the list of device descriptions.
     *
     * @param device the device description to add
     */
    public void addDevice(DeviceDescription device) {
        devices.add(device);
    }

    /**
     * Gets a new device screen description filtered to only include either local or remote devices.
     * @param remote the remote
     * @return the device screen description
     */
    public DeviceScreensDescription getFilteredDeviceScreenDescription(boolean remote) {
    	DeviceScreensDescription devices = new DeviceScreensDescription();
    	for (DeviceDescription device : this.devices) {
    		if (device.getPersist() == remote) {
    			devices.addDevice(device);
    		}
    	}

        return devices;
    }

    @Override
    public String toString() {
        try {
            return XMLUtil.toXml(this, DeviceScreensDescription.class).replaceAll("><", ">\n<");
        } catch (IOException e) {
            LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
            return e.toString();
        }
    }

    /**
     * @return the hashcode of this object.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((devices == null) ? 0 : devices.hashCode());
        return result;
    }

    /**
     * @param obj
     *            the object to compare to.
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        DeviceScreensDescription other = (DeviceScreensDescription) obj;

        if (devices == null) {
            if (other.devices != null) {
                return false;
            }
        } else if (!devices.equals(other.devices)) {
            return false;
        }
        return true;
    }

}
