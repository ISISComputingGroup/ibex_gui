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

package uk.ac.stfc.isis.ibex.activemq.message;

import java.beans.PropertyChangeListener;


/**
 * Interface for a class that produces log messages. The producer provides
 * updates of new messages and access to a cache of recently delivered messages.
 */
public interface IMessageProducer<T extends IMessage> {
    /**
     * Register a message consumer. The consumer's newMessage() method will be
     * called whenever this producer has a new message to deliver.
     */
    void addMessageConsumer(IMessageConsumer<T> messageReceiver);

    /**
     * Add a property change listener. The intended purpose is to listen for
     * changes in connection status.
     */
    void addPropertyChangeListener(String propertyName,
	    PropertyChangeListener listener);

    void clearMessages();
}
