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

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMQException;

/**
 * A class that wraps the specific ZMQ library to aid in testing.
 */
public class ZMQWrapper {

    private Socket socket;
    private final Context context;

    private static final int RECEIVE_TIMEOUT = 500;

    /**
     * Create a ZMQWrapper to wrap the ZMQ library.
     */
    public ZMQWrapper() {
        context = ZMQ.context(1);
    }

    /**
     * Connect to the ZMQ session.
     * 
     * @param connectionUri
     *            The URI that we want to connect to.
     * @throws ZMQException
     *             if cannot connect.
     */
    public void connect(String connectionUri) throws ZMQException {
        socket = context.socket(ZMQ.REQ);
        socket.setReceiveTimeOut(RECEIVE_TIMEOUT);
        socket.connect(connectionUri);
    }

    /**
     * Receive a string from the ZMQ connection.
     * 
     * @return The string to receive.
     */
    public String receiveString() {
        return socket.recvStr();
    }

    /**
     * Send a string to the ZMQ connection.
     * 
     * @param data
     *            The string to send.
     * @param more
     *            True if more data is to follow.
     */
    public void send(String data, Boolean more) {
        if (more) {
            socket.sendMore(data);
        } else {
            socket.send(data);
        }
    }

    /**
     * Disconnect from the ZMQ socket.
     */
    public void disconnect() {
        socket.close();
    }
}
