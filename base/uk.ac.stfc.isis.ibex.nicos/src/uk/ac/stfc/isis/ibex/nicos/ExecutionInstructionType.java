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
 *
 */
public enum ExecutionInstructionType {

    /**
     * Resume the execution of the current script.
     */
    CONTINUE("continue"),

    /**
     * Pause the execution of the current script.
     */
    BREAK("break"),

    /**
     * Stop the execution of the current script.
     */
    STOP("stop");

    private String command;

    /**
     * Constructor with custom integer value.
     * 
     * @param command
     *            The command associated to this type of instruction.
     */
    ExecutionInstructionType(String command) {
        this.command = command;
    }

    /**
     * @return The integer value representing this break level.
     */
    public String getCommand() {
        return command;
    }
}
