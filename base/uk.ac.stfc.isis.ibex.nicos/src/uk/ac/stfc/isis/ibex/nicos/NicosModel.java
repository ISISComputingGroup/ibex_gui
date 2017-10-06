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

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.messages.Login;
import uk.ac.stfc.isis.ibex.nicos.messages.NicosSendMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.QueueScript;
import uk.ac.stfc.isis.ibex.nicos.messages.SendMessageDetails;

/**
 * The model that holds the connection to nicos.
 */
public class NicosModel extends ModelObject {

    private static final Logger LOG = IsisLog.getLogger(NicosModel.class);

    /**
     * The command that allows you to log in to nicos.
     */
    private static final String SCRIPT_SEND_FAIL_MESSAGE = "Failed to send script";

    private final ZMQSession session;

    private ScriptSendStatus scriptSendStatus = ScriptSendStatus.NONE;

    private SendMessageDetails scriptSendMessageDetails;
    private SendMessageDetails loginSendMessageDetails;

    private String scriptSendErrorMessage = "";

    private ConnectionStatus connectionStatus = ConnectionStatus.DISCONNECTED;
    private String connectionErrorMessage = "";

    /**
     * Constructor for the model.
     * 
     * This will initialise the connection to the appropriate ActiveMQ Queue and
     * login to nicos.
     * 
     * @param session
     *            the session to use to send messages to the script sever
     */
    public NicosModel(ZMQSession session) {
        this.session = session;
//        MessageParser<ReceiveMessage> parser = new NicosMessageParser();
//        parser.addMessageConsumer(this);
        connect();
//        this.session.addMessageParser(parser);
//        this.session.addPropertyChangeListener("connection", new PropertyChangeListener() {
//
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                Boolean isConnected = (Boolean) evt.getNewValue();
//                connectedChange(isConnected);
//            }
//
//        });
//        this.session.addPropertyChangeListener("connectionError", new PropertyChangeListener() {
//
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                setConnectionErrorMessage((String) evt.getNewValue());
//            }
//
//        });

//        setConnectionErrorMessage(this.session.getConnectionError());
//        connectedChange(this.session.isConnected());
    }

//    @Override
//    public void newMessage(ReceiveMessage nicosMessage) {
//        LOG.info("New data on ss_admin: " + nicosMessage.toString());
//
//        if (nicosMessage.isReplyTo(scriptSendMessageDetails)) {
//            if (nicosMessage.isSuccess()) {
//                setScriptSendStatus(ScriptSendStatus.SENT);
//                setScriptSendErrorMessage("");
//            } else {
//                setScriptSendStatus(ScriptSendStatus.SEND_ERROR);
//                setScriptSendErrorMessage(nicosMessage.getMessage());
//            }
//        } else if (nicosMessage.isReplyTo(loginSendMessageDetails)) {
//            if (nicosMessage.isSuccess()) {
//                setConnectionStatus(ConnectionStatus.CONNECTED);
//                setConnectionErrorMessage("");
//            } else {
//                LOG.error("Error returned from Nicos on login: " + nicosMessage.getMessage());
//                setConnectionStatus(ConnectionStatus.FAILED);
//                setConnectionErrorMessage("Can not log in: " + nicosMessage.getMessage());
//            }
//        }
//    }

    private void connect() {
        try {
            loginSendMessageDetails = sendMessageToNicos(new Login());
        } catch (ConversionException e) {
            setConnectionErrorMessage("Failed to parse JSON");
        }
        if (!loginSendMessageDetails.isSent()) {
            LOG.error("Error when sending log in message to Nicos: \'" + loginSendMessageDetails.getFailureReason()
                    + "\'");
            setConnectionStatus(ConnectionStatus.FAILED);
            setConnectionErrorMessage("Can not send login message: " + loginSendMessageDetails.getFailureReason());
        }
    }

//    /**
//     * The connection status has changed. Login if connected.
//     * 
//     * @param isConnected
//     *            is the server connected
//     */
//    private void connectedChange(Boolean isConnected) {
//        // new connection so reset script send status
//        setScriptSendStatus(ScriptSendStatus.NONE);
//        setScriptSendErrorMessage("");
//
//        if (isConnected) {
//            LOG.info("Logging in to nicos");
//            setConnectionStatus(ConnectionStatus.CONNECTING);
//            setConnectionErrorMessage("");
//            loginSendMessageDetails = sendMessageToNicos(new Login());
//            if (!loginSendMessageDetails.isSent()) {
//                LOG.error("Error when sending log in message to Nicos: \'" + loginSendMessageDetails.getFailureReason()
//                        + "\'");
//                setConnectionStatus(ConnectionStatus.FAILED);
//                setConnectionErrorMessage("Can not send login message: " + loginSendMessageDetails.getFailureReason());
//            }
//        } else {
//            setConnectionStatus(ConnectionStatus.DISCONNECTED);
//        }
//    }


//    @Override
//    public void clearMessages() {
//        // messages are not stored so there is no need to clear them.
//    }


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
     * Send a script to Nicos. Do not wait for a reply the acknowledgement can
     * be found in script send status.
     * 
     * @param script
     *            to queue
     */
    public void sendScript(String script) {
        setScriptSendStatus(ScriptSendStatus.SENDING);
        QueueScript nicosMessage = new QueueScript("ScriptFromGUI", script);
        this.scriptSendMessageDetails = sendMessageToNicos(nicosMessage);
        if (!this.scriptSendMessageDetails.isSent()) {
            setScriptSendStatus(ScriptSendStatus.SEND_ERROR);
            setScriptSendErrorMessage(SCRIPT_SEND_FAIL_MESSAGE);
        } else {
            System.out.println(this.scriptSendMessageDetails.getFailureReason());
        }
    }

    /**
     * Send a message to Nicos
     * 
     * @param nicosMessage
     *            message to send
     * @return details about the sending of that message
     */
    private SendMessageDetails sendMessageToNicos(NicosSendMessage nicosMessage) {
        return session.sendMessage(nicosMessage);
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

    /**
     * @return the Script Server connection status
     */
    public ConnectionStatus getConnectionStatus() {
        return this.connectionStatus;
    }

    /**
     * Set the connection status
     * 
     * @param connectionStatus
     *            the new connection status
     */
    private void setConnectionStatus(ConnectionStatus connectionStatus) {
        firePropertyChange("connectionStatus", this.connectionStatus, this.connectionStatus = connectionStatus);
    }

    /**
     * Get the last error message received when connecting.
     * 
     * Blank for no error message.
     * 
     * @return the log in error message
     */
    public String getConnectionErrorMessage() {
        return connectionErrorMessage;
    }

    /**
     * Set the connection error message and fire a property change.
     * 
     * @param connectionErrorMessage
     *            the new error message
     */
    private void setConnectionErrorMessage(String connectionErrorMessage) {
        firePropertyChange("connectionErrorMessage", this.connectionErrorMessage,
                this.connectionErrorMessage = connectionErrorMessage);
    }
}
