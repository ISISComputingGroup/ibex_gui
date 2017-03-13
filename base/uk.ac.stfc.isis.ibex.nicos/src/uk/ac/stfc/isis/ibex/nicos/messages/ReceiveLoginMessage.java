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

/**
 * A Login Message that has been received from NICOS.
 * 
 * THIS IS DESERIALISED FROM JSON AND SO THE CONSTRUCTOR MAY NOT BE CALLED
 */
public class ReceiveLoginMessage extends ReceiveMessage {
    private class UserLevel {
        // this is serialised from python
        @SuppressWarnings("checkstyle:membername")
        Integer user_level;

        /**
         * @param userLevel
         *            the user level
         */
        UserLevel(Integer userLevel) {
            super();
            this.user_level = userLevel;
        }

    }

    /** called payload to match up with message from script server */
    private UserLevel payload = new UserLevel(null);

    /**
     * A constructor for a basic message.
     * 
     * @param userLevel
     *            The user level to set.
     * @param messageId
     *            message id of the returned message, this can be correlated
     *            with a sent id
     * @param success
     *            true if message is for a success; false otherwise
     * 
     */
    public ReceiveLoginMessage(Integer userLevel, String messageId, boolean success) {
        super(messageId, success);
        this.payload = new UserLevel(userLevel);
    }
    
    /**
     * @return The user level of the logged in user.
     */
    public Integer getUserLevel() {
        if (this.payload == null) {
            return null;
        }
        return this.payload.user_level;
    }

    /**
     * @return
     */
    @Override
    public String getMessage() {
        if (getUserLevel() != null) {
            return "Logged in";
        } else {
            return "Initial connect";
        }
    }

}
