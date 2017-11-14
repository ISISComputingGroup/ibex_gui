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
package uk.ac.stfc.isis.ibex.nicos.comms;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.zeromq.ZMQException;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.nicos.messages.NICOSMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.SendMessageDetails;

/**
 * Used to send and receive messages to NICOS via ZMQ.
 */
public class ZMQSession {

    private static final Logger LOG = IsisLog.getLogger(ZMQSession.class);

    private ZMQWrapper zmq;

    private static final String ZMQ_PROTO = "tcp";
    private static final String ZMQ_PORT = "1301";

    /**
     * Error for when a message could not be converted into the NICOS protocol.
     */
    public static final String FAILED_TO_CONVERT = "Failed to convert sent NICOS message";

    /**
     * Error for when no response has been returned from NICOS.
     */
    public static final String NO_DATA_RECEIVED = "No data received from NICOS";

    /**
     * Error for when an unexpected response has been sent from NICOS.
     */
    public static final String UNEXPECTED_RESPONSE = "Unexpected response from server";

    /**
     * The constructor for the class.
     * 
     * @param zmq
     *            The ZMQ connection.
     * 
     */
    public ZMQSession(ZMQWrapper zmq) {
        this.zmq = zmq;
    }

    /**
     * Connect to ZMQ on the given instrument.
     * 
     * @param instrument
     *            The instrument to connect to.
     * @throws ZMQException
     *             Thrown if a connection cannot be made.
     */
    public void connect(InstrumentInfo instrument) throws ZMQException {
        String connectionString = createConnectionURI(instrument);
        zmq.connect(connectionString);
        LOG.info("Connected to NICOS at " + connectionString);
    }

    private String createConnectionURI(InstrumentInfo instrument) {
        StringBuilder sb = new StringBuilder();
        sb.append(ZMQ_PROTO);
        sb.append("://");
        sb.append(instrument.hostName());
        sb.append(":");
        sb.append(ZMQ_PORT);
        return sb.toString();
    }

    private void sendMultipleMessages(List<String> messages) throws ZMQException {
        if (messages.size() > 0) {
            int i;
            for (i = 0; i < messages.size() - 1; i++) {
                zmq.send(messages.get(i), true);
            }
            zmq.send(messages.get(i), false);
        }
    }

    /**
     * Send a message to the ZMQ.
     * 
     * @param message
     *            The message to send.
     * @return The response.
     */
    public SendMessageDetails sendMessage(NICOSMessage<?> message) {
        try {
            sendMultipleMessages(message.getMulti());
        } catch (ZMQException e) {
            LOG.warn("Failed to send message " + message.toString());
            return SendMessageDetails.createSendFail(e.getMessage());
        } catch (ConversionException e) {
            LOG.warn("Failed to convert message " + message.toString());
            return SendMessageDetails.createSendFail(FAILED_TO_CONVERT);
        }
        return getServerResponse(message);
    }

    private SendMessageDetails getServerResponse(NICOSMessage<?> sentMessage) {
        String status = zmq.receiveString();

        // NICOS protocol leaves the second package empty for future expansion
        // so read and throw away.
        zmq.receiveString();

        String resp = zmq.receiveString();

        if (status == null | resp == null | status == "") {
            LOG.warn("No response from server after sending " + sentMessage.toString());
            return SendMessageDetails.createSendFail(NO_DATA_RECEIVED);
        }

        if (status.equals("ok")) {
            try {
                ReceiveMessage received = sentMessage.parseResponse(resp);
                return SendMessageDetails.createSendSuccess(received);
            } catch (ConversionException e) {
                LOG.warn("Unexpected response from server: " + resp);
                return SendMessageDetails.createSendFail(UNEXPECTED_RESPONSE);
            }
        } else {
            LOG.warn("Error received from server " + resp);
            return SendMessageDetails.createSendFail(resp);
        }
    }

    /**
     * Disconnect from the ZMQ socket.
     */
    public void disconnect() {
        zmq.disconnect();
    }
}
