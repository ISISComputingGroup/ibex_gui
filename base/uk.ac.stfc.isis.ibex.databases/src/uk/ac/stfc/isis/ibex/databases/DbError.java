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
package uk.ac.stfc.isis.ibex.databases;

/**
 * Types of error that can arise when connecting to an external database.
 */
public enum DbError {
    /**
     * Motors are moving.
     */
    CONNECTION_ERROR("Could not establish connection to database"),
    /**
     * Motors are stationary.
     */
    UNKNOWN_DB("Unknown database name"),
    /**
     * Motor state is unknown.
     */
    ACCESS_DENIED("Access to database denied");

    private final String message;

    /**
     * Constructor that allows attaching a message to the error type.
     * 
     * @param message The message.
     */
    DbError(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}

