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

package uk.ac.stfc.isis.ibex.nicos;

/**
 * The status of sending a script.
 */
public enum ScriptSendStatus {
    /**
     * No script has ever been sent from this model to the current script
     * server.
     */
    NONE,

    /**
     * An error occurred while sending the script.
     */
    SEND_ERROR,

    /**
     * Script is being sent to the server or the script has been sent and it has
     * not confirmed that it has received the script.
     */
    SENDING,

    /**
     * Script has been sent to nicos and nicos has acknowledged the send.
     */
    SENT;

}
