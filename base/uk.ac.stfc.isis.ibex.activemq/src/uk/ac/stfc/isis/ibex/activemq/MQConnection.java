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

import java.net.ConnectException;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Handles the connection to a Java Message Service (JMS) Server. Automatically
 * attempts to reestablish connection if it is dropped.
 */
public class MQConnection extends ModelObject implements Runnable {

    private static final int ONE_SECOND = 1000;

    private static final Logger LOG = IsisLog.getLogger(MQConnection.class);

    private static final String PROTOCOL = "tcp:";

    /** JMS Server URL */
    private String jmsUrl;

    /** Server name, <code>null</code> if not connected */
    private String jmsServer = null;

    /** 'run' flag to thread */
    private volatile boolean run = true;

    /** JMS Connection */
    private Connection connection;

    /** Indicates whether the Handler is currently connected to the JMS server */
    private boolean connectedToJms;

    private String username;

    private String password;

    /**
     * Indicates whether a warning message has been issued since last time
     * server was connected
     */
    private boolean connectionWarnFlag;

    private Thread thread;

    private String connectionError = "";

    /**
     * Creates an activeMQ connection.
     *
     * @param initialHost
     *            The initial host instrument to connect to.
     * @param username
     *            The username to connect with.
     * @param password
     *            The password to connect with.
     */
    public MQConnection(String initialHost, String username, String password) {
        setCredentials(username, password);
        updateURL(initialHost);

        thread = new Thread(this, "ActiveMQ Connection");
        thread.start();
    }

    /**
     * Sets the credentials for the connection.
     *
     * @param username
     *            The username to connect with.
     * @param password
     *            The password to connect with.
     */
    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
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
    @Override
    public void run() {
        while (run) {
            if (!isConnected()) {
                // If not connected or if connection is dropped, establish
                // connection with JMS
                try {
                    connect();
                    setConnectionError("");

                    LOG.info("Connected to JMS server: " + jmsUrl);
                    // Use URL as server name.
                    jmsServer = jmsUrl;
                    setJmsConnectionStatus(true);
                } catch (Exception ex) {
                    if (ex.getCause() != null && (ex.getCause() instanceof SecurityException
                            || ex.getCause() instanceof ConnectException)) {
                        setConnectionError("Problem connecting to JMS server. '" + ex.getCause().getMessage()
                                + ".'  Will auto-attempt reconnection.");
                    } else {
                        setConnectionError("Problem connecting to JMS server. Will auto-attempt reconnection.");
                    }
                    connectionWarnFlag = true;
                    setJmsConnectionStatus(false);
                }
            }
            sleep(ONE_SECOND);
        }

        disconnect();
    }

    /**
     * Set the connection error, blank no error.
     *
     * @param connectionError
     *            connection error
     */
    private synchronized void setConnectionError(String connectionError) {
        if (!connectionWarnFlag) {
            LOG.warn(connectionError);
        }
        firePropertyChange("connectionError", this.connectionError, this.connectionError = connectionError);
    }

    /**
     * Gets the current connection error, blank no error.
     *
     * @return the connection error
     */
    public String getConnectionError() {
        return connectionError;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
        }
    }

    private synchronized void setJmsConnectionStatus(boolean connected) {
        firePropertyChange("connection", connectedToJms, connectedToJms = connected);
    }

    /**
     * Connect to JMS.
     */
    private void connect() throws JMSException {

        if (connection != null) {
            connection.close();
        }
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(username,
                password, jmsUrl);
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
    }

    /** Disconnect from JMS - free resources. */
    public synchronized void disconnect() {
        try {
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
     * @return The activeMQ connection.
     */
    public Connection getConnection() {
        return connection;
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
        jmsUrl = PROTOCOL + host + ":" + ActiveMQ.JMS_PORT;

        if (oldjmsUrl != null && !jmsUrl.equals(oldjmsUrl)) {
            // Disconnect and reconnect
            disconnect();
            connectionWarnFlag = false;
        }
    }
}
