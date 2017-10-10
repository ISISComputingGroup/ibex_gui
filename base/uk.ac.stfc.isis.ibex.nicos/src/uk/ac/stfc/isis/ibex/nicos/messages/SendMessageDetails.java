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
package uk.ac.stfc.isis.ibex.nicos.messages;

/**
 * Class to capture details about having sent a message. This is returned when a
 * message is sent to Nagios.
 */
public final class SendMessageDetails {

    private boolean isSent;
    private String failureReason;
    private ReceiveMessage response;

    /**
     * Creates the message details when the send fails.
     *
     * @param failureReason
     *            the failure reason
     * 
     * @return the failure send message details
     */
    public static SendMessageDetails createSendFail(String failureReason) {
        return new SendMessageDetails(false, failureReason, null);
    }

    /**
     * Creates the message details for a send success.
     * 
     * @param received
     *            The message received from NICOS.
     *
     * @return the send message details
     */
    public static SendMessageDetails createSendSuccess(ReceiveMessage received) {
        return new SendMessageDetails(true, "", received);
    }

    /**
     * Constructor.
     * 
     * @param isSent
     *            true if the message has been sent; false otherwise
     * @param failureReason
     *            the reason for a failure
     * @param response
     *            the message that was originally sent to NICOS.
     */
    private SendMessageDetails(boolean isSent, String failureReason, ReceiveMessage response) {
        this.isSent = isSent;
        this.failureReason = failureReason;
        this.response = response;
    }

    /**
     * @return the message that was sent and it's response.
     */
    public ReceiveMessage getResponse() {
        return response;
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

}
