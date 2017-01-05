
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
package uk.ac.stfc.isis.ibex.logger;

import org.apache.logging.log4j.Logger;

import com.google.common.base.Joiner;

/**
 * Utilities used for logging.
 */
public final class LoggerUtils {

    private LoggerUtils() {
    }

    /**
     * Sends an error to the log along with the stack trace of an exception
     * related to the error.
     * 
     * @param log
     *            The logger to use
     * @param message
     *            The message to accompany the error
     * @param e
     *            The exception whose stack trace will be printed
     */
    public static void logErrorWithStackTrace(Logger log, String message, Exception e) {
        log.error(message + "\n    " + e.getMessage() + "\n    Stack Trace:\n    "
                + Joiner.on("\n    ").join(e.getStackTrace()));
    }
}
