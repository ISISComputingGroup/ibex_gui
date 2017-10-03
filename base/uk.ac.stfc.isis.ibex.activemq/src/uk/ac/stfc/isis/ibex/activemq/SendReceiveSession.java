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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.activemq.message.MessageParser;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Used to send and receive messages to ActiveMQ. The queue to send messages on
 * must be provided, a temporary receiving queue will then be created.
 */
@SuppressWarnings("rawtypes")
public class SendReceiveSession extends ReceiveSession {

    private static final Logger LOG = IsisLog.getLogger(ReceiveSession.class);

    private TemporaryQueue tempQueue;

    private final String sendQueue;

    private MessageProducer producer;

    private List<MessageParser> parsers = new ArrayList<>();

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
    public SendReceiveSession(MQConnection mqConnection, String sendQueue) {
        super(mqConnection);

        this.sendQueue = sendQueue;
    }

    /**
     * Adds a message parser to the temporary queue created by this session.
     * 
     * @param parser
     *            The parser to add.
     */
    public void addMessageParser(MessageParser parser) {
        try {
            parsers.add(parser);
            if (connection.isConnected()) {
                addConsumer(tempQueue, parser);
            }
        } catch (JMSException e) {
            LOG.warn("Problem adding consumer to temporary queue " + e);
        }
    }

    @Override
    protected void throwingConnect() throws JMSException {
        super.throwingConnect();

        // The write queue
        Destination adminQueue = session.createQueue(sendQueue);
        producer = session.createProducer(adminQueue);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        // The read queue
        tempQueue = session.createTemporaryQueue();

        for (MessageParser p : parsers) {
            addConsumer(tempQueue, p);
        }

    }

    /**
     * Sends a message to the writing queue.
     * 
     * @param text
     *            The message to send.
     * @return message details for sent message
     */
    public SendMessageDetails sendMessage(String text) {
        if (session == null) {
            return SendMessageDetails.createSendFail("No connection to script server.");
        }

        String messageId = getNextCorrelationID();
        try {
            TextMessage txtMessage = session.createTextMessage();
            txtMessage.setText(text);
            txtMessage.setJMSReplyTo(tempQueue);
            txtMessage.setJMSCorrelationID(messageId);
            producer.send(txtMessage);
            return SendMessageDetails.createSendSuccess(messageId);
        } catch (JMSException e) {
            LOG.warn("Failed to send message " + text);
            return SendMessageDetails.createSendFail(e.getLocalizedMessage());
        }
    }

    /**
     * @return a new correlation ID
     */
    private String getNextCorrelationID() {
        Random random = new Random(System.currentTimeMillis());
        long randomLong = random.nextLong();
        return Long.toHexString(randomLong);
    }

    @Override
    protected void disconnect() {
        try {
            for (MessageParser p : parsers) {
                p.closeJMSConsumer();
            }
        } catch (JMSException ex) {
            LOG.error("JMS shutdown error: " + ex);
        }
    }

}
