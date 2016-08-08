
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
package uk.ac.stfc.isis.ibex.devicescreens.tests.xmldata;

/**
 * Utility class for testing.
 */
public class DeviceScreensXmlProvider {

    /**
     * The Device Screens XML header.
     */
    public static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<devices xmlns=\"http://epics.isis.rl.ac.uk/schema/screens/1.0/\">";
    
    /**
     * The Device Screens XML ending.
     */
    public static final String ENDING = "</devices>";

    /**
     * Returns the xml string for a single device description with a single
     * property.
     * 
     * @param deviceName the device name
     * @param deviceKey the device key
     * @param deviceType the device type
     * @param propertyKey the property name
     * @param propertyValue the property value
     * @return the xml for a device description
     */
    public static String getXML(String deviceName, String deviceKey, String deviceType, String propertyKey,
            String propertyValue) {
        return HEADER + "<device>" + "<name>"
                + deviceName + "</name>" + "<key>" + deviceKey + "</key>" + "<type>" + deviceType + "</type>"
                + "<properties>"
                + "<property>" + "<key>" + propertyKey + "</key>" + "<value>" + propertyValue + "</value>"
                + "</property>" + "</properties>" + "</device>" + ENDING;
    }
}
