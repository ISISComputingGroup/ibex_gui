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
import uk.ac.stfc.isis.ibex.nicos.ScriptSendStatus;

/**
 * Converter to convert a Script Send Status into a element that can be
 * displayed on the GUI.
 */
public class ScriptSendStatusConverter extends UpdateValueStrategy {

    private static final Logger LOG = IsisLog.getLogger(ScriptSendStatusConverter.class);

    /**
     * Convert the value.
     * 
     * @param scriptSendStatus
     *            the script send status from the model
     * @return GUI element
     */
    @Override
    public Object convert(Object scriptSendStatus) {
        if (scriptSendStatus == null) {
            return "";
        }
        switch ((ScriptSendStatus) scriptSendStatus) {
            case NONE:
                return "";
            case SEND_ERROR:
                return "Error";
            case SENDING:
                return "Sending";
            case SENT:
                return "Sent";
            default:
                LOG.error("Programming error: Script Send Status enum value not coded.");
                return scriptSendStatus.toString();
        }
    }
}