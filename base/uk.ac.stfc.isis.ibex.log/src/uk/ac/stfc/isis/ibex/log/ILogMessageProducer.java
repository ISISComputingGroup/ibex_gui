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

import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.List;

import uk.ac.stfc.isis.ibex.activemq.message.IMessageConsumer;
import uk.ac.stfc.isis.ibex.log.message.LogMessage;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;

/**
 * Interface for a class that produces log messages. The producer provides
 * updates of new messages and access to a cache of recently delivered messages.
 */
public interface ILogMessageProducer {

    /**
     * Register a message consumer. The consumer's newMessage() method will be
     * called whenever this producer has a new message to deliver
     * 
     * @param messageReceiver
     *            The object to receive messages.
     */
    void addMessageConsumer(IMessageConsumer<LogMessage> messageReceiver);

    /**
     * Add a property change listener. The intended purpose is to listen for
     * changes in connection status.
     * 
     * @param propertyName
     *            The name of the property to listen for.
     * @param listener
     *            The listener to be called when the property changes.
     */
    void addPropertyChangeListener(String propertyName,
	    PropertyChangeListener listener);

    /**
     * Producer stores the N most recent messages sent. These can be retrieved
     * so that when a widget that displays starts up it will already have access
     * to all the messages received since program startup.
     * 
     * @return A list containing the most recent messages.
     */
    List<LogMessage> getAllCachedMessages();

    /**
     * Search the database of previous log messages.
     * 
     * @param field
     *            The field to search by e.g. sender.
     * @param value
     *            The value to search for e.g. ISISDAE.
     * @param from
     *            The start of the date range to search.
     * @param to
     *            The end of the date range to search.
     * 
     * @return A list of the messages that satisfy the search.
     * 
     * @throws Exception
     *             When the search cannot be made.
     */
    List<LogMessage> search(LogMessageFields field, String value,
	    Calendar from, Calendar to) throws Exception;


    /**
     * Clears the chached messages.
     */
    void clearMessages();
}
