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

import uk.ac.stfc.isis.ibex.activemq.SendMessageDetails;
import uk.ac.stfc.isis.ibex.activemq.message.IMessage;

/**
 * A Message that has been received from NICOS.
 * 
 * THIS IS DESERIALISED FROM JSON AND SO THE CONSTRUCTOR MAY NOT BE CALLED
 */
public abstract class ReceiveMessage implements IMessage {
    private String messageId;

    private boolean success;

    /**
     * A constructor for a basic message.
     * 
     * @param messageId
     *            message id of the returned message, this can be correlated
     *            with a sent id
     * @param success
     *            true if message is for a success; false otherwise
     * 
     */
    public ReceiveMessage(String messageId, boolean success) {
        this.messageId = messageId;
        this.success = success;
    }

    /**
     * @return true if the message is for success operation; false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Matches the sent message details to this message to see if if this is a
     * response from the original message.
     * 
     * @param scriptSendMessageStatus
     *            the details from the send step
     * @return true if this message if the reply to the sent message; false
     *         otherwise
     */
    public boolean isReplyTo(SendMessageDetails scriptSendMessageStatus) {
        if (scriptSendMessageStatus == null) {
            return false;
        }
        return scriptSendMessageStatus.hasMessageId(this.messageId);
    }

    /**
     * Set the message Id.
     * 
     * @param messageId
     *            the messageId to set
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * @return generic message for this replay
     */
    public abstract String getMessage();
    
    /**
     * @return string representation
     */
    @Override
    public String toString() {
        return "[ID " + this.messageId + "] '" + getMessage() + "'";
    }

}
