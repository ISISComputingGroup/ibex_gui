
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
package uk.ac.stfc.isis.ibex.ui.log.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 */
public enum LogMessageSeverity {
    
    /**
     * A message which provides information.
     */
    INFO("Information"),
    
    /**
     * A message which has minor severity.
     */
    MINOR("Minor"),
    
    /**
     * A message which has major severity.
     */
    MAJOR("Major");

    private String text;

    private static final List<String> ALLTOSTRING;
    static {
        ALLTOSTRING = new ArrayList<>();
        for (LogMessageSeverity method : LogMessageSeverity.values()) {
            ALLTOSTRING.add(method.toString());
        }
    }

    /**
     * Instantiates a new log message severity.
     *
     * @param text the text
     */
    LogMessageSeverity(String text) {
        this.text = text;
    }

    /**
     * Gets all of the severities as a list of strings.
     *
     * @return the list
     */
    public static List<String> allToString() {
        return Collections.unmodifiableList(ALLTOSTRING);
    }

    @Override
    public String toString() {
        return text;
    }
}
