/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2025
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

package uk.ac.stfc.isis.ibex.alerts;

import uk.ac.stfc.isis.ibex.epics.writing.Writable;

/**
 * This class sets the alerts control on the blocks within the server.
 */
public class AlertsSetter {
    private final Writable<Double> lowLimitSetter;
    private final Writable<Double> highLimitSetter;
    private final Writable<String> enabledSetter;
    private final Writable<Double> delayInSetter;
    private final Writable<Double> delayOutSetter;

    /**
     * Creates a setter for the given block. The PVs are created at this time as
     * they need some time to connect.
     *
     * @param blockName
     *            The name of the block to set the alerts values on.
     * @param alertsServer
     *            The server object that creates the PVs for the alerts control.
     */
    public AlertsSetter(String blockName, AlertsServer alertsServer) {
        lowLimitSetter = alertsServer.setLowLimit(blockName);
        highLimitSetter = alertsServer.setHighLimit(blockName);
        enabledSetter = alertsServer.setEnabled(blockName);
        delayInSetter = alertsServer.setDelayIn(blockName);
        delayOutSetter = alertsServer.setDelayOut(blockName);
	}

    /**
     * Set the low alert control limit on the block.
     *
     * @param limit
     *            The limit to set
     */
    public void setLowLimit(Double limit) {
        lowLimitSetter.uncheckedWrite(limit);
	}

    /**
     * Set the high alert control limit on the block.
     *
     * @param limit
     *            The high limit to set
     */
    public void setHighLimit(Double limit) {
        highLimitSetter.uncheckedWrite(limit);
	}

    /**
     * Enable run control on the block.
     *
     * @param enabled
     *            whether or not to enable the run control
     */
    public void setEnabled(boolean enabled) {
        enabledSetter.uncheckedWrite(enabled ? "YES" : "NO");
	}
    
    /**
     * Set the delayIn alerts control limit on the block.
     *
     * @param delayIn
     *            The delay In range to set
     */
    public void setDelayIn(Double delayIn) {
        delayInSetter.uncheckedWrite(delayIn);
	}

    /**
     * Set the delayOut alerts control limit on the block.
     *
     * @param delayOut
     *            The delay out range to set
     */
    public void setDelayOut(Double delayOut) {
    	delayOutSetter.uncheckedWrite(delayOut);
	}

}
