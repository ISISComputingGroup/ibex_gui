
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.runcontrol;

import uk.ac.stfc.isis.ibex.epics.writing.Writer;

/**
 * This class sets the run control on the blocks within the server.
 */
public class EditableRunControlSetting {
    private Writer<String> lowLimitSetter;
    private Writer<String> highLimitSetter;
    private Writer<String> enabledSetter;

    /**
     * Creates a setter for the given block. The PVs are created at this time as
     * they need some time to connect.
     * 
     * @param blockName
     *            The name of the block to set the run control on.
     * @param runControlServer
     *            The server object that creates the PVs for the run control.
     */
	public EditableRunControlSetting(String blockName, RunControlServer runControlServer) {
        lowLimitSetter = runControlServer.blockRunControlLowLimitSetter(blockName);
        highLimitSetter = runControlServer.blockRunControlHighLimitSetter(blockName);
        enabledSetter = runControlServer.blockRunControlEnabledSetter(blockName);
	}

    /**
     * Set the low run control limit on the block.
     * 
     * @param limit
     *            The limit to set
     */
    public void setLowLimit(String limit) {
        lowLimitSetter.uncheckedWrite(limit);
	}
	
    /**
     * Set the high run control limit on the block.
     * 
     * @param limit
     *            The high limit to set
     */
    public void setHighLimit(String limit) {
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

}
