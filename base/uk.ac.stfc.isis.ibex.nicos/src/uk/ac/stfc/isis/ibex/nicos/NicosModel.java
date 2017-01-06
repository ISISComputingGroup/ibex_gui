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

package uk.ac.stfc.isis.ibex.nicos;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import uk.ac.stfc.isis.ibex.activemq.ActiveMQ;
import uk.ac.stfc.isis.ibex.activemq.SendReceiveSession;
import uk.ac.stfc.isis.ibex.activemq.message.IMessageConsumer;
import uk.ac.stfc.isis.ibex.activemq.message.MessageParser;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The model that holds the connection to nicos.
 */
public class NicosModel extends ModelObject implements IMessageConsumer<NicosMessage> {

    /**
     * The command that allows you to log in to nicos.
     */
    public static final String LOGIN =
            "{\"command\": \"authenticate\", \"login\": \"ibex\", \"passwd\": \"a2eed0a7fcb214a497052435191b5264cca5b687\", \"display\": \"TEST\"}";

    private final SendReceiveSession connection;

    private final MessageParser<NicosMessage> parser = new NicosMessageParser();

    /**
     * Constructor for the model.
     * 
     * This will initialise the connection to the appropriate ActiveMQ Queue and
     * login to nicos.
     */
    public NicosModel() {
        connection = ActiveMQ.getInstance().getSendReceiveQueue("ss_admin");
        parser.addMessageConsumer(this);
        connection.addMessageParser(parser);
        connection.addPropertyChangeListener("connection", passThrough());
        connection.addPropertyChangeListener("connection", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ((Boolean) evt.getNewValue()) {
                    connection.sendMessage(LOGIN);
                }
            }
        });
        connection.sendMessage(LOGIN);
    }

    @Override
    public void newMessage(NicosMessage nicosMessage) {
        System.out.println("New data on ss_admin: " + nicosMessage.getData());
    }

    @Override
    public void clearMessages() {
        //
    }
}
