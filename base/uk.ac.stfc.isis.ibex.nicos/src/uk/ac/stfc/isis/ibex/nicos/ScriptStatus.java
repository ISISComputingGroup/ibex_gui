 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2018 Science & Technology Facilities Council.
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
package uk.ac.stfc.isis.ibex.nicos;

/**
 * Contains possible run states for a script in NICOS.
 */
public enum ScriptStatus {
    /**
     * Nothing started, last script raised exception.
     */
    IDLEEXC(-2, "Idle (last script failed)"),

    /**
     * Nothing started.
     */
    IDLE(-1, "Idle"),

    /**
     * Execution running.
     */
    RUNNING(0, "Running"),

    /**
     * Execution halted, in break function.
     */
    INBREAK(1, "Paused"),

    /**
     * Stop exception raised, waiting for propagation.
     */
    STOPPING(2, "Stopping"),
    
    /**
     * Invalid state requested.
     */
    INVALID(-5, "Invalid");

    private int numVal;
    private String description;

    /**
     * Constructor with custom integer value.
     * 
     * @param numVal
     *            The value representing this status.
     * @param desc
     *            A description of the status.
     */
    ScriptStatus(int numVal, String desc) {
        this.numVal = numVal;
        this.description = desc;
    }

    /**
     * @return The integer value representing this status.
     */
    public int getNumVal() {
        return numVal;
    }

    /**
     * @return The description of this status.
     */
    public String getDesc() {
        return description;
    }

    /**
     * Return the Enum object associated to the given value.
     * 
     * @param search
     *            The value to search by
     * @return The associated Enum object
     */
    public static ScriptStatus getByValue(int search) {
        for (ScriptStatus status : values()) {
            if (status.getNumVal() == search) {
                return status;
            }
        }
        return INVALID;
    }
}
