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
package uk.ac.stfc.isis.ibex.activemq;

/**
 * Class to capture details about having sent a message. This is returned when a
 * message is sent to Nagios.
 */
public class SendMessageDetails {

    private boolean isSent;
    private String failureReason;
    private String messageId;

    /**
     * Creates the message details when the send fails.
     *
     * @param failureReason
     *            the failure reason
     * @param messageId
     *            the message id
     * @return the failure send message details
     */
    public static SendMessageDetails createSendFail(String failureReason, String messageId) {
        return new SendMessageDetails(false, failureReason, messageId);
    }

    /**
     * Creates the message details for a send success.
     *
     * @param messageId
     *            the message id
     * @return the send message details
     */
    public static SendMessageDetails createSendSuccess(String messageId) {
        return new SendMessageDetails(true, "", messageId);
    }

    /**
     * Constructor.
     * 
     * @param isSent
     *            true if the message has been sent; false otherwise
     * @param failureReason
     *            the reason for a failure
     * @param messageId
     *            the message id sent to JMS to coordinate messages and replies
     */
    public SendMessageDetails(boolean isSent, String failureReason, String messageId) {
        super();
        this.isSent = isSent;
        this.failureReason = failureReason;
        this.messageId = messageId;
    }

    /**
     * @return the isSent
     */
    public boolean isSent() {
        return isSent;
    }

    /**
     * @return the failureReason
     */
    public String getFailureReason() {
        return failureReason;
    }

    /**
     * Message has the same message ID.
     * 
     * @param messageId
     *            the message ID to check
     * @return true if has the same message id; false otherwise
     */
    public boolean hasMessageId(String messageId) {
        if (messageId == null) {
            return false;
        }
        return this.messageId.equals(messageId);
    }

}
