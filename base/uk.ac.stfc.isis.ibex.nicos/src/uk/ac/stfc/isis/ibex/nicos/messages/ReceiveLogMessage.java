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

package uk.ac.stfc.isis.ibex.nicos.messages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A log message that has been received from NICOS. This should contain a list
 * of the last n messages in the NICOS console (where n is specified in the
 * query).
 * 
 * THIS IS DESERIALISED FROM JSON AND SO THE CONSTRUCTOR MAY NOT BE CALLED
 */
@SuppressWarnings({ "checkstyle:magicnumber" })
public class ReceiveLogMessage implements ReceiveMessage {

    private List<NicosLogEntry> entries;

    /**
     * Constructor, takes a list of log messages (each made up of a list of
     * fields).
     * 
     * @param log The message containing the log entries
     */
    public ReceiveLogMessage(String[][] log) {
        List<NicosLogEntry> entries = new ArrayList<NicosLogEntry>();
        for (int i = 0; i < log.length; i++) {
            String[] currentLine = log[i];
            Date timestamp = new Date((long) Double.parseDouble(currentLine[1]) * 1000);
            String message = currentLine[3];
            entries.add(new NicosLogEntry(timestamp, message));
        }
        this.entries = entries;
    }

    /**
     * @return The entries in this log update message.
     */
    public List<NicosLogEntry> getEntries() {
        return entries;
    }
}
