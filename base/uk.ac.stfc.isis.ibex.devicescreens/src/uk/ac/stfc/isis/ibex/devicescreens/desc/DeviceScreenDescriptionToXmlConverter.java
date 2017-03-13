
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
package uk.ac.stfc.isis.ibex.devicescreens.desc;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.devicescreens.DeviceScreens;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.conversion.XMLUtil;
import uk.ac.stfc.isis.ibex.epics.observing.Observable;

/**
 * Converts a DeviceScreenDescription into a string in XML format.
 */
public class DeviceScreenDescriptionToXmlConverter extends Converter<DeviceScreensDescription, String> {

    private final Observable<String> schema;

    /**
     * Constructor that takes the schema to check the device screens against.
     * 
     * @param schema
     *            an observable to get the device screens schema from
     */
    public DeviceScreenDescriptionToXmlConverter(Observable<String> schema) {
        this.schema = schema;
    }

    /**
     * Returns the XML representation of a device screens.
     * 
     * @param value
     *            the input device screens to convert
     * @return the XML representation of the input
     * @throws ConversionException
     *             if a conversion error has occurred
     */
    @Override
    public String convert(DeviceScreensDescription value) throws ConversionException {
        if (Strings.isNullOrEmpty(schema.getValue())) {
            DeviceScreens.LOG.debug("Device screens schema not found, attempting to save anyway.");
        }
        try {
            return XMLUtil.toXml(value, DeviceScreensDescription.class, schema.getValue());
        } catch (JAXBException | SAXException e) {
            throw new ConversionException("Error converting device screens to XML", e);
        }
    }

}
