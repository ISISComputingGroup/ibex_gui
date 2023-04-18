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

import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMQException;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * A class that wraps the specific ZMQ library to aid in testing.
 */
@SuppressWarnings("checkstyle:FinalClass")  // needs not to be final so that it can be mocked in tests
public class ZMQWrapper {

	private static final Logger LOG = IsisLog.getLogger(ZMQWrapper.class);

    private Optional<Socket> socket = Optional.empty();
    private final ZContext context = new ZContext(1);

    private static final int RECEIVE_TIMEOUT = 500;

    private static final ZMQWrapper INSTANCE = new ZMQWrapper();
    private static final Object ZMQ_COMMS_LOCK = new Object();

    /**
     * Create a ZMQWrapper to wrap the ZMQ library.
     *
     * Private constructor - access via getInstance instead (this class is a singleton).
     */
    private ZMQWrapper() {
    }

    /**
     * Get the instance of this singleton.
     * @return the single instance of this ZMQ wrapper
     */
    public static synchronized ZMQWrapper getInstance() {
    	return INSTANCE;
    }

    /**
     * Gets the lock used by this ZMQ session.
     *
     * While this lock is held, it is guaranteed that no other threads will
     * read or write to ZMQ.
     *
     * It is an error not to acquire the lock before reading/writing to ZMQ.
     * The lock should be held for any group of reads/writes which must be kept together
     * (for example, a message to NICOS and it's response).
     *
     * For example, you should not do:
     *
     * synchronized(zmq.getLock()) {
     *    zmq.send(...);
     * }
     * synchronized(zmq.getLock()) {
     *    zmq.receiveString(...);
     * }
     *
     * As a different message might be read or written in between the read/write pair. Instead, do:
     *
     * synchronized(zmq.getLock()) {
     *     zmq.send(...);
     *     zmq.receiveString(...);
     * }
     *
     * Which guarantees that the write and read will execute with no intermediate ZMQ operations.
     *
     * @return The lock.
     */
    public Object getLock() {
    	return ZMQ_COMMS_LOCK;
    }

    private void checkCommsLockHeld() {
    	if (!Thread.holdsLock(getLock())) {
    		LOG.error("The current thread does not hold the ZMQ comms lock. "
    				+ "This is a programming error and may cause message interleaving. "
    				+ "Attempting to continue anyway but some messages or their replies may not be correctly interpreted.");
    	}
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
    	checkCommsLockHeld();

		// disconnect old session first
		disconnect();
		
		socket = Optional.of(context.createSocket(SocketType.REQ));
		socket.ifPresent(s -> s.setReceiveTimeOut(RECEIVE_TIMEOUT));
		socket.ifPresent(s -> s.connect(connectionUri));
    }

    /**
     * Receive a string from the ZMQ connection.
     *
     * @return The string to receive.
     */
    public String receiveString() {
    	checkCommsLockHeld();
    	return getSocketOrThrow().recvStr();
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
    	checkCommsLockHeld();
    	
        if (more) {
            getSocketOrThrow().sendMore(data);
        } else {
            getSocketOrThrow().send(data);
        }
    }

    /**
     * Disconnect from the ZMQ socket.
     */
    public void disconnect() {
    	checkCommsLockHeld();

    	socket.ifPresent(s -> s.close());
        socket = Optional.empty();
    }
    
    private Socket getSocketOrThrow() {
    	return socket.orElseThrow(() -> new IllegalStateException(
    					"Cannot operate on the ZMQ socket as it does not exist (has probably been closed and not re-opened)."));
    }
}
