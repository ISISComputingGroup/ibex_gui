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
package uk.ac.stfc.isis.ibex.activemq.message;

import java.util.ArrayList;
import java.util.List;

import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.TextMessage;

/**
 * This class will listen for incoming active MQ messages, parse them into the
 * specified IMessage and then notify any consumers.
 * 
 * @param <T>
 *            the type to convert the active MQ messages into
 */
public abstract class MessageParser<T extends IMessage> implements Runnable {

    private static final int TIMEOUT_50_MS = 50;
    private boolean running = true;
    private Thread thread = null;
    
    private List<IMessageConsumer<T>> convertedConsumers = new ArrayList<>();

    /** Receives messages from the JMS server */
    private MessageConsumer jmsConsumer;

    /**
     * Start and stop the thread.
     * 
     * @param run
     *            true to start, false to stop
     */
    public void setRunning(boolean run) {
    	if (run && jmsConsumer == null) {
    		throw new IllegalStateException("Cannot start running without a consumer");
    	}
        running = run;
        if (running && (thread == null || !thread.isAlive())) {
        	thread = new Thread(this, "ActiveMQ MessageParser");
            thread.start();
        }
    }
    
    /**
     * Sets ActiveMQ consumer to get messages from.
     * 
     * @param consumer
     *            The active MQ consumer.
     */
    public void setActiveMQConsumer(MessageConsumer consumer) {
        jmsConsumer = consumer;
    }

    /**
     * Closes the ActiveMQ consumer that this is listening to.
     * 
     * @throws JMSException
     *             Thrown if close fails.
     */
    public void closeJMSConsumer() throws JMSException {
        setRunning(false);
        jmsConsumer.close();
        
        /**
         * Once the consumer is closed, we can't do anything with it
         * and it can never be reopened. Additionally there is no
         * isClosed() on a consumer so we can't tell that it's closed
         * after the fact.
         * 
         * Hence, set it to null here.
         * 
         * Failure to do this will result in the thread going CPU-bound
         * and eventually leaking memory while it tries repeatedly to connect
         * 
         */
        jmsConsumer = null;
    }

    /**
     * Checks for any messages on ActiveMQ and converts them.
     */
    @Override
    @SuppressWarnings("checkstyle:emptyblock")
    public void run() {
        while (running) {
            try {
                if (jmsConsumer != null) {
                    TextMessage textMessage = (TextMessage) jmsConsumer.receive(TIMEOUT_50_MS);
                    if (textMessage != null) {
                        T message = parseMessage(textMessage.getText());
                        for (IMessageConsumer<T> c : convertedConsumers) {
                            c.newMessage(message);
                        }
                    }
                } else {
                    Thread.sleep(TIMEOUT_50_MS);
                }
            } catch (JMSException | InterruptedException e) {
                // Do nothing; exception will be caught by exception
                // listener (see connect())
            }
        }
    }

    /**
     * Adds an IMessage consumer to this connection.
     * 
     * @param messageConsumer
     *            The consumer to add.
     */
    public void addMessageConsumer(IMessageConsumer<T> messageConsumer) {
        convertedConsumers.add(messageConsumer);
    }

    /**
     * The method used to convert the ActiveMQ message into an IMessage.
     * 
     * @param string
     *            The active MQ message string to convert into a IMessage.
     * @return The IMessage that has been produced.
     */
    protected abstract T parseMessage(String string);
}
