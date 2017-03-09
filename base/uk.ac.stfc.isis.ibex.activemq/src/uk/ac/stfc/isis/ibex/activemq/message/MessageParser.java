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

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * This class will listen for incoming active MQ messages, parse them into the
 * specified IMessage and then notify any consumers.
 * 
 * @param <T>
 *            the type to convert the active MQ messages into
 */
public abstract class MessageParser<T extends IMessage> implements Runnable {

    private static final Logger LOG = IsisLog.getLogger(MessageParser.class);
    private static final int TIMEOUT_50_MS = 50;
    private boolean running = true;
    private Thread thread;
    
    private List<IMessageConsumer<T>> convertedConsumers = new ArrayList<>();

    /** Receives messages from the JMS server */
    private MessageConsumer jmsConsumer;
    
    /**
     * Default constructor, will start a thread listening for active MQ
     * messages.
     */
    public MessageParser() {
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Start and stop the thread.
     * 
     * @param run
     *            true to start, false to stop
     */
    public void setRunning(boolean run) {
        running = run;
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
        jmsConsumer.close();
    }

    /**
     * Checks for any messages on ActiveMQ and converts them.
     */
    @Override
    @SuppressWarnings("checkstyle:emptyblock")
    public void run() {
        System.out.println("Thread Running");
        while (running) {
            try {
                if (jmsConsumer != null) {
                    TextMessage textMessage = (TextMessage) jmsConsumer.receive(TIMEOUT_50_MS);
                    if (textMessage != null) {
                        LOG.trace("Script server message recieved [id: " + textMessage.getJMSCorrelationID() + "]: "
                                + textMessage.getText());
                        T message = parseMessage(
                                new MessageDetails(textMessage.getText(), textMessage.getJMSCorrelationID()));
                        for (IMessageConsumer<T> c : convertedConsumers) {
                            c.newMessage(message);
                        }
                    }
                } else {
                    Thread.sleep(TIMEOUT_50_MS);
                }
            } catch (JMSException | InterruptedException e) {
                // Do nothing; exception will be caught be exception
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
     * @param rawMessage
     *            The active MQ message string to convert into a IMessage.
     * @return The IMessage that has been produced.
     */
    protected abstract T parseMessage(MessageDetails rawMessage);
}
