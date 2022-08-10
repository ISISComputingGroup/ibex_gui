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
import java.util.Objects;

import org.apache.logging.log4j.Logger;
import org.zeromq.ZMQException;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.nicos.messages.NICOSMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.SentMessageDetails;

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
        synchronized (zmq.getLock()) {
        	zmq.connect(connectionString);
        }
    }

    private String createConnectionURI(InstrumentInfo instrument) {
        return String.format("%s://%s:%s", ZMQ_PROTO, instrument.hostName(), ZMQ_PORT);
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
     * @param <TSEND> 
     *            The type of message parameters to send.
     * @param <TRESP> 
     *            The type of message response to expect.       
     * @return The response.
     */
    public <TSEND, TRESP> SentMessageDetails<TRESP> sendMessage(NICOSMessage<TSEND, TRESP> message) {
    	synchronized (zmq.getLock()) {
	        try {
	        	sendMultipleMessages(message.getMulti());
	        } catch (ZMQException e) {
	            LOG.warn("Failed to send message " + message.toString() + ". Exception was: " + e.getMessage());
	            return SentMessageDetails.createSendFail(e.getMessage());
	        } catch (ConversionException e) {
	            LOG.warn("Failed to convert message " + message.toString());
	            return SentMessageDetails.createSendFail(FAILED_TO_CONVERT);
	        }
	        return getServerResponse(message);
    	}
    }

    private <TSEND, TRESP> SentMessageDetails<TRESP> getServerResponse(NICOSMessage<TSEND, TRESP> sentMessage) {    	
    	sentMessage.receiveResponse(zmq);
    	var status = sentMessage.getResponseStatus();
    	var resp = sentMessage.getResponse();
        
        if (status == null || resp == null || Objects.equals(status, "")) {
            return SentMessageDetails.createSendFail(NO_DATA_RECEIVED);
        }
        
        if (status.equals("ok")) {
            try {
                TRESP received = sentMessage.parseResponse(resp);
                return SentMessageDetails.<TRESP>createSendSuccess(received);
            } catch (ConversionException e) {
                LOG.warn("Unexpected response from server: " + resp);
                return SentMessageDetails.createSendFail(UNEXPECTED_RESPONSE);
            }
        } else {
            LOG.warn("Error received from server " + resp + ", status was " + status);
            return SentMessageDetails.createSendFail(resp);
        }
    }

    /**
     * Disconnect from the ZMQ socket.
     */
    public void disconnect() {
    	// Using the lock here means that the server will properly finish it's last message before disconnecting.
    	synchronized (zmq.getLock()) {
    		zmq.disconnect();
    	}
    }
}
