
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

package uk.ac.stfc.isis.ibex.devicescreens;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import uk.ac.stfc.isis.ibex.devicescreens.xml.XMLUtil;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;

/**
 * A class to deal with changes in the device screens schema from the
 * Blockserver.
 */
public class ObservingSchemaHandler {

    private final Observer<String> screensSchemaObserver = new BaseObserver<String>() {
        @Override
        public void onValue(String value) {
            // Set the schema
            try {
                XMLUtil.setSchema(value);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Exception e) {
        }

        @Override
        public void onConnectionStatus(boolean isConnected) {
        }
    };

    /**
     * @param variables the device screens variables in use
     */
    public ObservingSchemaHandler(DeviceScreenVariables variables) {
        variables.getDeviceScreensSchema().addObserver(screensSchemaObserver);
    }
}
