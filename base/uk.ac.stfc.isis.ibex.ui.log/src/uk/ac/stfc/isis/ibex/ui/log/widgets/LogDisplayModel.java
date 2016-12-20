
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.log.widgets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import uk.ac.stfc.isis.ibex.activemq.ILogMessageConsumer;
import uk.ac.stfc.isis.ibex.activemq.ILogMessageProducer;
import uk.ac.stfc.isis.ibex.activemq.message.LogMessage;
import uk.ac.stfc.isis.ibex.activemq.message.LogMessageFields;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.ui.AsyncMessageModeratorTask;
import uk.ac.stfc.isis.ibex.ui.log.comparator.LogMessageComparator;
import uk.ac.stfc.isis.ibex.ui.log.filter.LogMessageFilter;
import uk.ac.stfc.isis.ibex.ui.log.save.LogMessageFileWriter;

/**
 * Model to be used by the LogDisplay class. Connects to a message producer to
 * receive new log messages, and passes update notifications on to the display.
 * Stores a cache of recently received messages and provides access to a message
 * searching service.
 */
public class LogDisplayModel extends ModelObject
        implements ILogMessageConsumer, ISearchModel, AsyncMessageModeratorTask {
    /**
     * The maximum number of recent messages to display; older messages are
     * dropped.
     */
    private static final int MAX_LIVE_MESSAGES = 10000;
	
    /** The log message source. */
	private ILogMessageProducer messageProducer;
	
    /** A store of recently received log messages. */
	private List<LogMessage> liveMessageCache;
	
	/**
     * Value indicating the current status of the connection to the message
     * producing service (typically JMS) - true=live, false=down.
     */
	private boolean connectionStatus;
	
    /** Results of the most recently performed search. */
	private List<LogMessage> latestSearchResults;
	
	/**
     * Value indicating whether display should currently be showing search
     * results (true) or live message feed (false).
     */
	private boolean usingSearch;
	
    /**
     * The constructor.
     * 
     * @param messageProducer the source of log messages
     */
	public LogDisplayModel(ILogMessageProducer messageProducer) {
		this.messageProducer = messageProducer;		
		
		this.messageProducer.addMessageConsumer(this);
		this.messageProducer.addPropertyChangeListener("connection", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				connectionStatus = (boolean) event.getNewValue();
				firePropertyChange("connection", event.getOldValue(), event.getNewValue());	
			}
		});
		
		liveMessageCache = new ArrayList<LogMessage>(messageProducer.getAllCachedMessages());
		latestSearchResults = new ArrayList<LogMessage>();
	}
	
	/**
     * Returns the current message cache or latest search results (if search has
     * recently been performed).
     * 
     * @return The messages
     */
	public List<LogMessage> getMessages() {
		if (usingSearch) {
			return latestSearchResults;
		} else {
			return liveMessageCache;
		}
	}

	/**
     * Returns a value indicating the status of the connection to the log
     * message producing service (probably JMS).
     * 
     * @return the status of the connection
     */
	public boolean getConnectionStatus() {
		return connectionStatus;
	}
	
	/**
	 * Clear the local cache of recently received messages (they can still
	 * be retrieved by searching).
	 */
	public void clearMessageCache() {
		if (!usingSearch) {
			liveMessageCache.clear();
			firePropertyChange("message", null, liveMessageCache);
		}
	}

	/**
	 * Clear search results and return to the 'live message' view (i.e. display
	 * messages in the cache).
	 */
	@Override
    public void clearSearch() {
		usingSearch = false;
		latestSearchResults = new ArrayList<LogMessage>();
		firePropertyChange("message", null, liveMessageCache);
		firePropertyChange("displayMode", null, usingSearch);
	}
	
	/**
     * Receive a new log message - add it to the local cache.
     * 
     * @param logMessage the new message to add
     */
	@Override
	public void newMessage(LogMessage logMessage) {
		if (liveMessageCache.size() >= MAX_LIVE_MESSAGES) {
			liveMessageCache.remove(0);
		}

		liveMessageCache.add(logMessage);
		firePropertyChange("message", null, liveMessageCache);
	}

	/**
     * Performs a search, returning all log messages that match the request
     * parameters.
     * 
     * @param field The log message field to search by.
     * @param value Search the 'searchField' field of every record for this
     *            string value
     * @param from Consider only messages that occurred after this time (null =
     *            no limit).
     * @param to Consider only messages that occurred before this time (null =
     *            no limit).
     */
	@Override
	public void search(LogMessageFields field, String value, Calendar from,
			Calendar to) {
		try {
			latestSearchResults = messageProducer
					.search(field, value, from, to);
		} catch (Exception e) {
			firePropertyChange("errorMessage", null, e.getMessage());
		}

		usingSearch = true;
		firePropertyChange("message", null, latestSearchResults);
		firePropertyChange("displayMode", null, usingSearch);
	}

	/**
     * Indicates whether the current set of messages being displayed are search
     * results (true) or recent messages (false).
     * 
     * @return whether in search mode or not
     */
	public boolean isSearchMode() {
		return usingSearch;
	}

	/**
     * Removes the specified log messages from the current view (live messages
     * or recent search results). If live messages are removed, they are
     * permanently gone from the live cache, but may still be retrieved by
     * searching.
     * 
     * @param messages the messages to remove
     */
	public void removeMessagesFromCurrentView(List<LogMessage> messages) {
		List<LogMessage> displayList = getMessages();

		for (LogMessage msg : messages) {
			displayList.remove(msg);
		}

		firePropertyChange("message", null, displayList);
	}

	/**
     * Save a filtered and sorted version of the current display list (cached
     * messages or search results) to the specified file.
     * 
     * @param filename the file to save to
     * @param filters the filters to apply
     * @param comparator the comparator
     */
	public void saveCurrentViewToLogFile(String filename,
			Set<LogMessageFilter> filters, LogMessageComparator comparator) {
		// Select list
		List<LogMessage> displayList = new ArrayList<LogMessage>(getMessages());

		// Filter
		if ((filters != null) && (filters.size() != 0)) {
			for (LogMessageFilter filter : filters) {
				displayList = filter.getFilteredList(displayList);
			}
		}

		// Sort
		if (comparator != null) {
			Collections.sort(displayList, comparator);
		}

		// Write to file; emit an error message on fail
		saveSelectedToLogFile(displayList, filename);
	}

	/**
     * Save a list of log messages to the specified file file.
     * 
     * @param selected the selected messages
     * @param filename the file to save to
     */
	public void saveSelectedToLogFile(List<LogMessage> selected, String filename) {
		// Write to file; emit an error message on fail
		LogMessageFileWriter writer = new LogMessageFileWriter();
		if (!writer.saveLogFile(selected, filename)) {
			String errMsg = "Error saving log file: '" + filename + "'";
			firePropertyChange("errorMessage", null, errMsg);
		}
	}

	@Override
	public void clearMessages() {
		liveMessageCache.clear();
		latestSearchResults.clear();
		
		firePropertyChange("message", null, liveMessageCache);
		firePropertyChange("displayMode", null, latestSearchResults);
	}

    /**
     * Rerun the update task for the live message cache.
     */
    @Override
    public void reQueueTask() {
        firePropertyChange("message", null, liveMessageCache);
    }

}
