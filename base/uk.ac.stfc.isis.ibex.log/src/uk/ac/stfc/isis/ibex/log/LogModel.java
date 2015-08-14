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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import uk.ac.stfc.isis.ibex.log.jms.JmsHandler;
import uk.ac.stfc.isis.ibex.log.message.LogMessage;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;
import uk.ac.stfc.isis.ibex.log.rdb.LogMessageQuery;
import uk.ac.stfc.isis.ibex.log.rdb.Rdb;
import uk.ac.stfc.isis.ibex.model.ModelObject;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

public class LogModel extends ModelObject implements ILogMessageProducer,
	ILogMessageConsumer {
    /**
     * The maximum number of messages (from JMS) that will be stored in a local
     * cache. When the cache size is exceeded, older messages will be dropped
     */
    private static final int MAX_CACHE_MESSAGES = 10000;

    private static final String CONNECTION_ERROR_MESSAGE = "Error connecting to MySQL database.";

    private static final String UNKNOWN_DATABASE_MESSAGE = "Unknown database name.";

    private static final String ACCESS_DENIED_MESSAGE = "Database access denied.";

    /** Eclipse Job that manages the background processing of the JmsHandler */
    private Job jmsListenerJob;

    /**
     * Starts and maintains connection to the JMS and forwards on any messages
     * received from it.
     */
    private final JmsHandler jmsHandler = new JmsHandler();

    /** List of subscribers that are to received any new JMS messages */
    private ArrayList<ILogMessageConsumer> messageReceivers = new ArrayList<>();

    /** A local cache of the most recent messages received from the JMS. */
    private final ArrayList<LogMessage> jmsMessageCache = new ArrayList<>();

    public LogModel() {
	jmsHandler.setLogMessageConsumer(this);
	jmsHandler.addPropertyChangeListener("connection", passThrough());
    }

    @Override
    public void newMessage(LogMessage logMessage) {
	// Add to local message cache
	if (jmsMessageCache.size() >= MAX_CACHE_MESSAGES) {
	    jmsMessageCache.remove(0);
	}
	jmsMessageCache.add(logMessage);

	for (ILogMessageConsumer receiver : messageReceivers) {
	    receiver.newMessage(logMessage);
	}
    }

    /**
     * Register a message consumer. The consumer's newMessage() method will be
     * called whenever this producer has a new message to deliver.
     */
    @Override
    public void addMessageConsumer(ILogMessageConsumer messageReceiver) {
	messageReceivers.add(messageReceiver);
    }

    /**
     * Add a property change listener. The intended purpose is to listen for
     * changes in connection status.
     */
    @Override
    public void addPropertyChangeListener(String propertyName,
	    PropertyChangeListener listener) {
	super.addPropertyChangeListener(propertyName, listener);

	if (propertyName.equals("connection")) {
	    listener.propertyChange(new PropertyChangeEvent(this, propertyName,
		    null, jmsHandler.isConnected()));
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
     * Starts a background job that maintains a connection to the Java Messaging
     * Service (JMS) server.
     */
    public void start() {
	jmsListenerJob = new Job("JMS Listener") {
	    @Override
	    protected IStatus run(IProgressMonitor monitor) {
		jmsHandler.run();
		return Status.OK_STATUS;
	    }

	    @Override
	    protected void canceling() {
		jmsHandler.stop();
		super.canceling();
	    }
	};

	jmsListenerJob.schedule();
    }

    public void stop() {
	if (jmsListenerJob != null) {
	    jmsListenerJob.cancel();
	    jmsHandler.stop();
	}
    }

    @Override
    public List<LogMessage> search(LogMessageFields field, String value,
	    Calendar from, Calendar to) throws Exception {
	try {
	    Rdb rdb = Rdb.connectToDatabase();
	    LogMessageQuery query = new LogMessageQuery(rdb);
	    return query.getMessages(field, value, from, to);
	} catch (SQLException ex) {
	    throw new Exception(errorMessage(ex), ex);
	}
    }

    @Override
    public void clearMessages() {
	jmsMessageCache.clear();
	for (ILogMessageConsumer receiver : messageReceivers) {
	    receiver.clearMessages();
	}
    }

    private String errorMessage(SQLException ex) {
	if (ex instanceof CommunicationsException) {
	    return CONNECTION_ERROR_MESSAGE;
	}

	if (ex.getMessage().startsWith("Unknown database")) {
	    return UNKNOWN_DATABASE_MESSAGE;
	}

	return ACCESS_DENIED_MESSAGE;
    }
}
