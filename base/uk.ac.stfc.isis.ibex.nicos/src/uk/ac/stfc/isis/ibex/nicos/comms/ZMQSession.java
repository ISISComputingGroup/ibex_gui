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

import org.zeromq.ZMQException;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.nicos.messages.NICOSMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.SendMessageDetails;

/**
 * Used to send and receive messages to NICOS via ZMQ.
 */
public class ZMQSession {

    private ZMQWrapper zmq;

    private static final String ZMQ_PROTO = "tcp";
    private static final String ZMQ_PORT = "1301";

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
        zmq.connect(createConnectionURI(instrument));
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
    public SendMessageDetails sendMessage(NICOSMessage message) {
        try {
            sendMultipleMessages(message.getMulti());
        } catch (ZMQException e) {
            return SendMessageDetails.createSendFail(e.getMessage());
        } catch (ConversionException e) {
            return SendMessageDetails.createSendFail("Failed to convert sent NICOS message");
        }
        return getServerResponse(message);
    }

    private SendMessageDetails getServerResponse(NICOSMessage sentMessage) {
        String status = zmq.receiveString();
        zmq.receiveString(); // Do not care
        String resp = zmq.receiveString();

        if (status == null | resp == null | status == "") {
            return SendMessageDetails.createSendFail("No data received from NICOS");
        }

        if (status.equals("ok")) {
            try {
                ReceiveMessage received = sentMessage.parseResponse(resp);
                return SendMessageDetails.createSendSuccess(received);
            } catch (ConversionException e) {
                return SendMessageDetails.createSendFail("Unexpected response from server");
            }
        } else {
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
