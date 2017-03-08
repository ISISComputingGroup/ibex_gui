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

import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import uk.ac.stfc.isis.ibex.activemq.SendMessageDetails;
import uk.ac.stfc.isis.ibex.activemq.SendReceiveSession;
import uk.ac.stfc.isis.ibex.activemq.message.IMessageConsumer;
import uk.ac.stfc.isis.ibex.activemq.message.MessageParser;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The model that holds the connection to nicos.
 */
public class NicosModel extends ModelObject implements IMessageConsumer<NicosMessage> {

    private static final Logger LOG = IsisLog.getLogger(NicosModel.class);

    /**
     * The command that allows you to log in to nicos.
     */
    static final String LOGIN =
            "{\"command\": \"authenticate\", \"login\": \"ibex\", \"passwd\": \"a2eed0a7fcb214a497052435191b5264cca5b687\", \"display\": \"TEST\"}";

    /**
     * A template for the command that allows you to queue a script in nicos.
     * replace the string with the script you want to run.
     */
    static final String QUEUE_SCRIPT_COMMAND_TEMPLATE =
            "{\"command\": \"start\", \"name\": \"script_from_gui\", \"code\": \"%s\"}";

    private static final String SCRIPT_SEND_FAIL_MESSAGE = "Failed to send script";

    private final SendReceiveSession session;

    private ScriptSendStatus scriptSendStatus = ScriptSendStatus.NONE;

    private SendMessageDetails scriptSendMessageDetails;

    private String scriptSendErrorMessage;


    /**
     * Constructor for the model.
     * 
     * This will initialise the connection to the appropriate ActiveMQ Queue and
     * login to nicos.
     * 
     * @param session
     *            the session to use to send messages to the script sever
     */
    public NicosModel(SendReceiveSession session) {
        this.session = session;
        MessageParser<NicosMessage> parser = new NicosMessageParser();
        parser.addMessageConsumer(this);
        this.session.addMessageParser(parser);
        this.session.addPropertyChangeListener("connection", passThrough());
        this.session.addPropertyChangeListener("connection", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ((Boolean) evt.getNewValue()) {
                    if (!session.sendMessage(LOGIN).isSent()) {
                        LOG.error("Error when sending log in message to Nicos");
                    }
                }
            }
        });
        session.sendMessage(LOGIN);
    }

    @Override
    public void newMessage(NicosMessage nicosMessage) {
        System.out.println("New data on ss_admin: " + nicosMessage.getPayload());

        if (nicosMessage.isReplyTo(scriptSendMessageDetails)) {
            if (nicosMessage.isSuccess()) {
                setScriptSendStatus(ScriptSendStatus.SENT);
                setScriptSendErrorMessage("");
            } else {
                setScriptSendStatus(ScriptSendStatus.SEND_ERROR);
                setScriptSendErrorMessage(nicosMessage.getPayload());
            }
        }
    }



    @Override
    public void clearMessages() {
        //
    }


    /**
     * Get the status of the last script that was sent.
     * 
     * @return the status of sending a script
     */
    public ScriptSendStatus getScriptSendStatus() {
        return scriptSendStatus;
    }

    private void setScriptSendStatus(ScriptSendStatus scriptSendStatus) {
        firePropertyChange("scriptSendStatus", this.scriptSendStatus, this.scriptSendStatus = scriptSendStatus);
    }

    /**
     * Send a script to nicos. Do not wait for a reply the acknowledgement can
     * be found in script send status.
     * 
     * @param script
     *            to queue
     */
    public void sendScript(String script) {
        setScriptSendStatus(ScriptSendStatus.SENDING);

        Gson gson = new Gson();

        String messageForNicos = String.format(QUEUE_SCRIPT_COMMAND_TEMPLATE, gson.toJson(script));
        SendMessageDetails sendMessageStatus = this.session.sendMessage(messageForNicos);
        if (sendMessageStatus.isSent()) {
            scriptSendMessageDetails = sendMessageStatus;
            return;
        }

        setScriptSendStatus(ScriptSendStatus.SEND_ERROR);
        setScriptSendErrorMessage(SCRIPT_SEND_FAIL_MESSAGE);
    }

    /**
     * Get the last error message received when queueing a script.
     * 
     * Blank for no error message.
     * 
     * @return the script send error message
     */
    public String getScriptSendErrorMessage() {
        return scriptSendErrorMessage;
    }

    /**
     * Set the script error message and fire a property change.
     * 
     * @param sciptSendErrorMessage
     *            the new error message
     */
    private void setScriptSendErrorMessage(String sciptSendErrorMessage) {
        firePropertyChange("scriptSendErrorMessage", this.scriptSendErrorMessage,
                this.scriptSendErrorMessage = sciptSendErrorMessage);
    }
}
