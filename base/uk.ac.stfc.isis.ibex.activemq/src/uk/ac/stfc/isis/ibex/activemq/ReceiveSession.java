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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.activemq.message.MessageParser;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * An ActiveMQ Session that is able to receive messages from any number of
 * ActiveMQ topics/queues.
 * 
 * This is only used for reading from queues, not writing to them.
 */
@SuppressWarnings("rawtypes")
public class ReceiveSession extends ModelObject {
    private static final Logger LOG = IsisLog.getLogger(ReceiveSession.class);

    protected MQConnection connection;
    protected Session session;

    /** The recipients of any parsed messages */
    private Map<String, MessageParser> messageParsers = new HashMap<>();

    /**
     * The constructor for the session.
     * 
     * This will add a listener to make sure that the session is properly
     * connected/reconnected along with the ActiveMQ connection.
     * 
     * @param connection
     *            The ActiveMQ connection to connect to.
     */
    public ReceiveSession(MQConnection connection) {
        this.connection = connection;

        connection.addPropertyChangeListener("connection", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Boolean connected = (Boolean) evt.getNewValue();
                if (connected) {
                    connect();
                } else {
                    disconnect();
                }
            }
        });

        connection.addPropertyChangeListener("connection", passThrough());
        connection.addPropertyChangeListener("connectionError", passThrough());
    }

    /**
     * Adds a message parser to this connection.
     * 
     * @param topicName
     *            The name of the topic or queue to add the consumer to.
     * 
     * @param messageParser
     *            The parser to add.
     */
    public void addMessageParser(String topicName, MessageParser messageParser) {
        messageParsers.put(topicName, messageParser);
        if (connection.isConnected()) {
            addConsumer(topicName, messageParser);
        }
    }

    /**
     * Cleans up this session, ensuring all consumers are closed.
     */
    protected void disconnect() {
        try {
            for (MessageParser p : messageParsers.values()) {
                p.closeJMSConsumer();
            }

            if (session != null) {
                session.close();
            }

        } catch (Exception ex) {
            LOG.error("JMS shutdown error: " + ex);
        }

    }

    /**
     * Connect the session to the activeMQ connection.
     */
    public void connect() {
        try {
            throwingConnect();
        } catch (Exception e) {
            LOG.warn("Failed to connect to ActiveMQ " + e);
        }
    }

    /**
     * Connect the session to the activeMQ connection.
     * 
     * @throws JMSException
     *             Thrown if the connection cannot be made.
     */
    protected void throwingConnect() throws JMSException {

        session = connection.getConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);

        for (Map.Entry<String, MessageParser> e : messageParsers.entrySet()) {
            addConsumer(e.getKey(), e.getValue());
        }
    }

    private void addConsumer(String dest, MessageParser parser) {
        try {
            Topic topic = session.createTopic(dest);
            addConsumer(topic, parser);
        } catch (JMSException e) {
            LOG.warn("Problem adding consumer to " + dest.toString() + " " + e.getMessage());
        }
    }

    /**
     * Add a consumer to the session.
     * 
     * @param dest
     *            The destination that the messages will come from.
     * @param parser
     *            The parser that will consume the messages.
     * @throws JMSException
     *             Thrown if the consumer cannot be added.
     */
    protected void addConsumer(Destination dest, MessageParser parser) throws JMSException {
        MessageConsumer consumer = session.createConsumer(dest);
        parser.setActiveMQConsumer(consumer);
    }

    /**
     * @return True if the session is connected, false otherwise.
     */
    public boolean isConnected() {
        return connection.isConnected();
    }

    /**
     * Gets the connection error from the connection.
     *
     * @return the connection error or blank if no error or connection
     */
    public String getConnectionError() {
        if (this.connection == null) {
            return "";
        }
        return this.connection.getConnectionError();
    }

}
