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

package uk.ac.stfc.isis.ibex.log;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.activemq.ActiveMQ;
import uk.ac.stfc.isis.ibex.activemq.ReceiveSession;
import uk.ac.stfc.isis.ibex.activemq.message.IMessageConsumer;
import uk.ac.stfc.isis.ibex.activemq.message.MessageParser;
import uk.ac.stfc.isis.ibex.databases.Rdb;
import uk.ac.stfc.isis.ibex.log.jms.XmlLogMessageParser;
import uk.ac.stfc.isis.ibex.log.message.LogMessage;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;
import uk.ac.stfc.isis.ibex.log.preferences.PreferenceConstants;
import uk.ac.stfc.isis.ibex.log.rdb.LogMessageQuery;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The Model that connects to the database and activeMQ to read log messages.
 */
public class LogModel extends ModelObject implements ILogMessageProducer,
        IMessageConsumer<LogMessage> {
    /**
     * The maximum number of messages (from JMS) that will be stored in a local
     * cache. When the cache size is exceeded, older messages will be dropped
     */
    private static final int MAX_CACHE_MESSAGES = 10000;

    private IPreferenceStore preferenceStore = Log.getDefault().getPreferenceStore();

    private final ReceiveSession logQueue;

    private final MessageParser<LogMessage> parser = new XmlLogMessageParser();

    /** List of subscribers that are to received any new JMS messages */
    private ArrayList<IMessageConsumer<LogMessage>> messageReceivers = new ArrayList<>();

    /** A local cache of the most recent messages received from the JMS. */
    private final ArrayList<LogMessage> jmsMessageCache = new ArrayList<>();

    /**
     * The default constructor. This will connect to activeMQ on startup.
     */
    public LogModel() {
        String topic = preferenceStore.getString(PreferenceConstants.P_JMS_TOPIC);
        logQueue = ActiveMQ.getInstance().getReceiveQueue();
        parser.addMessageConsumer(this);
        logQueue.addMessageParser(topic, parser);
        logQueue.addPropertyChangeListener("connection", passThrough());
    }

    @Override
    public void newMessage(LogMessage logMessage) {
        // Add to local message cache
        if (jmsMessageCache.size() >= MAX_CACHE_MESSAGES) {
            jmsMessageCache.remove(0);
        }
        jmsMessageCache.add(logMessage);

        for (IMessageConsumer<LogMessage> receiver : messageReceivers) {
            receiver.newMessage(logMessage);
        }
    }

    /**
     * Register a message consumer. The consumer's newMessage() method will be
     * called whenever this producer has a new message to deliver.
     */
    @Override
    public void addMessageConsumer(IMessageConsumer<LogMessage> messageReceiver) {
        messageReceivers.add(messageReceiver);
    }

    /**
     * Add a property change listener. The intended purpose is to listen for
     * changes in connection status.
     */
    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        super.addPropertyChangeListener(propertyName, listener);

        if (propertyName.equals("connection")) {
            listener.propertyChange(new PropertyChangeEvent(this, propertyName, null, logQueue.isConnected()));
        }
    }

    /**
     * Producer stores the N most recent messages sent. These can be retrieved
     * so that when a widget that displays starts up it will already have access
     * to all the messages received since program startup.
     */
    @Override
    public List<LogMessage> getAllCachedMessages() {
        return jmsMessageCache;
    }

    /**
     * Stop the connection to activeMQ.
     */
    public void stop() {
        parser.setRunning(false);
    }

    @Override
    public List<LogMessage> search(LogMessageFields field, String value,
	    Calendar from, Calendar to) throws Exception {
        try {
            String schema = preferenceStore.getString(PreferenceConstants.P_MESSAGE_SQL_SCHEMA);
            String user = preferenceStore.getString(PreferenceConstants.P_MESSAGE_SQL_USERNAME);
            String password = preferenceStore.getString(PreferenceConstants.P_MESSAGE_SQL_PASSWORD);

            Rdb rdb = Rdb.connectToDatabase(schema, user, password);
            LogMessageQuery query = new LogMessageQuery(rdb);
            return query.getMessages(field, value, from, to);
        } catch (SQLException ex) {
            throw new Exception(Rdb.getError(ex).toString(), ex);
        }
    }

    @Override
    public void clearMessages() {
        jmsMessageCache.clear();
        for (IMessageConsumer<LogMessage> receiver : messageReceivers) {
            receiver.clearMessages();
        }
    }
}
