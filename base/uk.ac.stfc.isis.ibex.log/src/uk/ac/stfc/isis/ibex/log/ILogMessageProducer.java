/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.log;

import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.List;

import uk.ac.stfc.isis.ibex.log.message.LogMessage;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;

/**
 * Interface for a class that produces log messages. The producer provides
 * updates of new messages and access to a cache of recently delivered
 * messages.
 */
public interface ILogMessageProducer {
	/**
	 * Register a message consumer. The consumer's newMessage() method will be
	 * called whenever this producer has a new message to deliver.
	 */
	void addMessageConsumer(ILogMessageConsumer messageReceiver);
	
	/**
	 * Add a property change listener. The intended purpose is to listen for changes
	 * in connection status.
	 */
	void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);
	
	/**
	 * Producer stores the N most recent messages sent. These can be retrieved so that
	 * when a widget that displays starts up it will already have access to all the messages 
	 * received since program startup.
	 */
	List<LogMessage> getAllCachedMessages();
	
	/**
	 * Search the database of previous log messages.
	 */
	List<LogMessage> search(LogMessageFields field, String value, Calendar from, Calendar to) throws Exception;
	
	void clearMessages();
}
