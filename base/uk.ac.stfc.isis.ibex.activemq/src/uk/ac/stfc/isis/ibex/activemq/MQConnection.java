/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.activemq;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.activemq.message.MessageParser;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Handles the connection to a Java Message Service (JMS) Server. Automatically
 * attempts to reestablish connection if it is dropped.
 */
@SuppressWarnings("rawtypes")
public class MQConnection extends ModelObject implements Runnable {

    private static final int ONE_SECOND = 1000;

    private static final Logger LOG = IsisLog.getLogger(MQConnection.class);

    private static final String PROTOCOL = "tcp:";

    private String jmsPort;

    /** JMS Server URL */
    private String jmsUrl;

    /** Server name, <code>null</code> if not connected */
    private String jmsServer = null;

    /** 'run' flag to thread */
    private volatile boolean run = true;

    /** JMS Connection */
    private Connection connection;

    /** JMS Session */
    private Session session;

    /** Indicates whether the Handler is currently connected to the JMS server */
    private boolean connectedToJms;

    /** The recipients of any parsed messages */
    private Map<String, MessageParser> messageParsers = new HashMap<>();

    /**
     * Indicates whether a warning message has been issued since last time
     * server was connected
     */
    private boolean connectionWarnFlag;

    private Thread thread;

    /**
     * Creates an activeMQ connection.
     * 
     * @param initialHost
     *            The initial host instrument to connect to.
     * @param port
     *            The port to read/write the ActiveMQ messages from.
     */
    public MQConnection(String initialHost, String port) {
        jmsPort = port;
        updateURL(initialHost);

        thread = new Thread(this);
        thread.start();
    }

    /**
     * Adds a message parser to this connection.
     * 
     * @param topic
     *            The topic or queue to add the consumer to.
     * 
     * @param messageParser
     *            The parser to add.
     */
    public void addMessageParser(String topic, MessageParser messageParser) {
        messageParsers.put(topic, messageParser);
    }

    /**
     * @return Whether the connection is live or not.
     */
    public synchronized boolean isConnected() {
        return connectedToJms;
    }

    /**
     * Stop listening on this connection.
     */
    public synchronized void stop() {
        run = false;
    }

    /**
     * Establish connection with JMS server and listen for messages to forward
     * on to consumer.
     */
    @SuppressWarnings("checkstyle:emptyblock")
    @Override
    public void run() {
        while (run) {
            if (!isConnected()) {
                // If not connected or if connection is dropped, establish
                // connection with JMS
                try {
                    connect();

                    LOG.info("Connected to JMS server: " + jmsUrl);
                    // Use URL as server name.
                    jmsServer = jmsUrl;
                    setJmsConnectionStatus(true);
                } catch (Exception ex) {
                    if (!connectionWarnFlag) {
                        LOG.warn("Problem connecting to JMS server. Will auto-attempt reconnection.");
                        connectionWarnFlag = true;
                        setJmsConnectionStatus(false);
                    }

                    sleep(ONE_SECOND);
                }
            }
        }
    
        disconnect();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void setJmsConnectionStatus(boolean connected) {
        firePropertyChange("connection", connectedToJms, connectedToJms = connected);
    }

    /**
     * Connect to JMS.
     */
    private void connect() throws JMSException {

        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD, jmsUrl);
        connection = factory.createConnection();

        connection.setExceptionListener(new ExceptionListener() {
            @Override
            public void onException(final JMSException ex) {
                LOG.warn("Lost connection to JMS server: " + jmsUrl + ". Will attempt auto reconnection.");
                setJmsConnectionStatus(false);
            }
        });

        // When server is unavailable, we'll hang in here
        connection.start();

        connectionWarnFlag = false;
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        for (Map.Entry<String, MessageParser> e : messageParsers.entrySet()) {
            Topic topic = session.createTopic(e.getKey());
            e.getValue().setActiveMQConsumer(session.createConsumer(topic));
        }
    }

    /** Disconnect from JMS - free resources. */
    public synchronized void disconnect() {
        try {

            for (MessageParser p : messageParsers.values()) {
                p.closeJMSConsumer();
            }

            if (session != null) {
                session.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (Exception ex) {
            LOG.error("JMS shutdown error: " + ex);
        } finally {
            LOG.info("Successfully disconnected from JMS: " + jmsServer);

            setJmsConnectionStatus(false);
        }
    }

    /**
     * Updates the URL of the connection to a new instrument.
     * 
     * @param host
     *            The new host instrument.
     */
    public void updateURL(String host) {

        if (host.indexOf("//") != 0) {
            host = "//" + host;
        }

        String oldjmsUrl = jmsUrl;
        jmsUrl = PROTOCOL + host + ":" + jmsPort;
        
        if (oldjmsUrl != null && !jmsUrl.equals(oldjmsUrl)) {
            // Disconnect and reconnect
            disconnect();
        }
    }
}
