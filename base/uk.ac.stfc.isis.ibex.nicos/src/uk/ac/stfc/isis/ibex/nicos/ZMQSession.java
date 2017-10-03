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
package uk.ac.stfc.isis.ibex.nicos;

import java.util.Arrays;
import java.util.List;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import uk.ac.stfc.isis.ibex.activemq.SendMessageDetails;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.messages.NicosSendMessage;

/**
 * Used to send and receive messages to ActiveMQ. The queue to send messages on
 * must be provided, a temporary receiving queue will then be created.
 */

public class ZMQSession extends ModelObject {

    private final Socket socket;
    private final Context context;

    /**
     * The constructor for the class.
     * 
     * Creates the session and sets up the sending connection.
     * 
     * @param mqConnection
     *            The ActiveMQ connection to connect the session to.
     * @param sendQueue
     *            The name of the ActiveMQ Queue that data should be sent on.
     */
    public ZMQSession() {
        context = ZMQ.context(1);

        socket = context.socket(ZMQ.REQ);
        socket.connect("tcp://127.0.0.1:1301");

        sendMultipleMessages(Arrays.asList("getbanner", "", ""));
        System.out.println(getServerResponse().getFailureReason());
    }

    private void sendMultipleMessages(List<String> messages) {
        int i;
        for (i = 0; i < messages.size() - 1; i++) {
            socket.sendMore(messages.get(i));
        }
        socket.send(messages.get(i));
    }

    public SendMessageDetails sendMessage(NicosSendMessage message) {
        try {
            sendMultipleMessages(message.getMulti());
        } catch (ConversionException e) {
            return SendMessageDetails.createSendFail("Failed to convert NICOS message");
        }
        return getServerResponse();
    }

    private SendMessageDetails getServerResponse() {
        String status = socket.recvStr();
        socket.recvStr(); // Do not care
        String message = socket.recvStr();

        if (status.equals("ok")) {
            return SendMessageDetails.createSendSuccess(message);
        } else {
            return SendMessageDetails.createSendFail(message);
        }
    }

    protected void disconnect() {
        socket.close();
        context.term();
    }

}
