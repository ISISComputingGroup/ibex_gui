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
package uk.ac.stfc.isis.ibex.ui.nicos.models;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.databinding.UpdateValueStrategy;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.nicos.ConnectionStatus;

/**
 * Converter to convert a connection Status into a element that can be displayed
 * on the GUI.
 */
public class ConnectionStatusConverter extends UpdateValueStrategy {

    private static final Logger LOG = IsisLog.getLogger(ConnectionStatusConverter.class);

    /**
     * Convert the value.
     * 
     * @param connectionStatus
     *            the connection status from the model
     * @return GUI element
     */
    @Override
    public Object convert(Object connectionStatus) {
        if (connectionStatus == null) {
            return "";
        }
        switch ((ConnectionStatus) connectionStatus) {
            case DISCONNECTED:
                return "Disconnected";
            case CONNECTING:
                return "Connecting ...";
            case FAILED:
                return "Failed to Connect";
            case CONNECTED:
                return "Connected";
            default:
                LOG.error("Programming error: Connection Status enum value not coded.");
                return connectionStatus.toString();
        }
    }
}