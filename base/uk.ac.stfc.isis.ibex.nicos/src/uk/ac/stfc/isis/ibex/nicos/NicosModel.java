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

import org.zeromq.ZMQException;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.comms.RepeatingJob;
import uk.ac.stfc.isis.ibex.nicos.comms.ZMQSession;
import uk.ac.stfc.isis.ibex.nicos.messages.GetBanner;
import uk.ac.stfc.isis.ibex.nicos.messages.Login;
import uk.ac.stfc.isis.ibex.nicos.messages.NICOSMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.QueueScript;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveBannerMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.SendMessageDetails;

/**
 * The model that holds the connection to NICOS.
 */
public class NicosModel extends ModelObject {

    private static final String SCRIPT_SEND_FAIL_MESSAGE = "Failed to send script";
    private static final String FAILED_LOGIN_MESSAGE = "Failed to login: ";

    private static final String INVALID_PROTOCOL = "NICOS protocol is invalid";
    private static final String INVALID_SERIALISER = "NICOS serialiser is invalid";

    private final ZMQSession session;

    private ScriptSendStatus scriptSendStatus = ScriptSendStatus.NONE;
    private String scriptSendErrorMessage = "";

    private ConnectionStatus connectionStatus = ConnectionStatus.DISCONNECTED;
    private String connectionErrorMessage = "";
    
    private RepeatingJob connectionJob;

    /**
     * Constructor for the model.
     * 
     * This will initialise the connection to zeroMQ and login to NICOS.
     * 
     * @param session
     *            the session to use to send and receive messages to and from
     *            the script sever
     * @param connectionJob
     *            the job that will periodically be run to reconnect to NICOS if
     *            a connection has failed. (pulled out of class for testing)
     * 
     */
    public NicosModel(ZMQSession session, RepeatingJob connectionJob) {
        this.session = session;
        this.connectionJob = connectionJob;
        this.connectionJob.schedule();
    }

    private void failConnection(String message) {
        setConnectionStatus(ConnectionStatus.FAILED);
        setConnectionErrorMessage(message);
        connectionJob.setRunning(true);
    }

    /**
     * Connect the model to a NICOS server.
     * 
     * @param instrument
     *            The instrument to connect to.
     */
    public void connect(InstrumentInfo instrument) {
        setConnectionStatus(ConnectionStatus.CONNECTING);
        setConnectionErrorMessage("");

        try {
            session.connect(instrument);
        } catch (ZMQException e) {
            failConnection(e.getMessage());
            return;
        }

        GetBanner getBanner = new GetBanner();
        SendMessageDetails response = sendMessageToNicos(getBanner);
        if (!response.isSent()) {
            failConnection(response.getFailureReason());
            return;
        } else {
            ReceiveBannerMessage banner = (ReceiveBannerMessage) response.getResponse();
            if (!banner.protocolValid()) {
                failConnection(INVALID_PROTOCOL);
                return;
            } else if (!banner.serializerValid()) {
                failConnection(INVALID_SERIALISER);
                return;
            }
        }

        SendMessageDetails loginSendMessageDetails = sendMessageToNicos(new Login());
        if (!loginSendMessageDetails.isSent()) {
            failConnection(FAILED_LOGIN_MESSAGE + loginSendMessageDetails.getFailureReason());
            return;
        }

        setConnectionStatus(ConnectionStatus.CONNECTED);
        connectionJob.setRunning(false);
    }
    
    /**
     * Disconnect the model from the NICOS server.
     */
    public void disconnect() {
        session.disconnect();
        setConnectionStatus(ConnectionStatus.DISCONNECTED);
        setConnectionErrorMessage("");
        connectionJob.setRunning(true);
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
     * Send a script to Nicos. Do not wait for a reply the acknowledgement can
     * be found in script send status.
     * 
     * @param script
     *            to queue
     */
    public void sendScript(String script) {
        setScriptSendStatus(ScriptSendStatus.SENDING);
        QueueScript nicosMessage = new QueueScript("ScriptFromGUI", script);
        SendMessageDetails scriptSendMessageDetails = sendMessageToNicos(nicosMessage);
        if (!scriptSendMessageDetails.isSent()) {
            setScriptSendStatus(ScriptSendStatus.SEND_ERROR);
            setScriptSendErrorMessage(SCRIPT_SEND_FAIL_MESSAGE);
        } else {
            setScriptSendStatus(ScriptSendStatus.SENT);
        }
    }

    /**
     * Send a message to Nicos
     * 
     * @param nicosMessage
     *            message to send
     * @return details about the sending of that message
     */
    private SendMessageDetails sendMessageToNicos(NICOSMessage<?> nicosMessage) {
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
