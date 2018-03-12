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
 * Enum with different levels for the break command sent to the NICOS server.
 */
public enum BreakLevel {

    /**
     * Break after current command in script.
     */
    AFTER_LINE(1),

    /**
     * Break at next defined breakpoint.
     */
    AFTER_STEP(2),

    /**
     * Break now.
     */
    NOW(3),

    /**
     * Immediately kill script execution.
     */
    IMMEDIATE(5);

    private int numVal;
    /**
     * Constructor with custom integer value.
     * 
     * @param numVal
     *            The value representing this break level.
     */
    BreakLevel(int numVal) {
        this.numVal = numVal;
    }

    /**
     * @return The integer value representing this break level.
     */
    public int getNumVal() {
        return numVal;
    }
}
