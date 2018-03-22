 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2018 Science & Technology Facilities Council.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.zeromq.ZMQException;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.comms.RepeatingJob;
import uk.ac.stfc.isis.ibex.nicos.comms.ZMQSession;
import uk.ac.stfc.isis.ibex.nicos.messages.ExecutionInstruction;
import uk.ac.stfc.isis.ibex.nicos.messages.GetBanner;
import uk.ac.stfc.isis.ibex.nicos.messages.GetLog;
import uk.ac.stfc.isis.ibex.nicos.messages.Login;
import uk.ac.stfc.isis.ibex.nicos.messages.NICOSMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.NicosLogEntry;
import uk.ac.stfc.isis.ibex.nicos.messages.QueueScript;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveBannerMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveLogMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.SendMessageDetails;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.GetScriptStatus;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.ReceiveScriptStatus;

/**
 * The model that holds the connection to NICOS.
 */
public class NicosModel extends ModelObject {

    private static final Logger LOG = IsisLog.getLogger(NicosModel.class);

    /**
     * Error for when a script fails to send.
     */
    public static final String SCRIPT_SEND_FAIL_MESSAGE = "Failed to send script";

    /**
     * Error for when a login fails.
     */
    public static final String FAILED_LOGIN_MESSAGE = "Failed to login: ";

    /**
     * Error for when the protocol received from the server is unrecognised.
     */
    public static final String INVALID_PROTOCOL = "NICOS protocol is invalid";

    /**
     * Error for when the serialiser received from the server is unrecognised.
     */
    public static final String INVALID_SERIALISER = "NICOS serialiser is invalid";
    
    /**
     * Error for when a response was expected but none was received.
     */
    public static final String NO_RESPONSE = "Server did not respond to request.";

    /**
     * The period to ask the server for a status update (in ms).
     */
    private static final long UPDATE_STATUS_TIME = 1000;
    
    private final ZMQSession session;
    private ScriptSendStatus scriptSendStatus = ScriptSendStatus.NONE;
    private String scriptSendErrorMessage = "";
    private ConnectionStatus connectionStatus = ConnectionStatus.DISCONNECTED;
    private String connectionErrorMessage = "";
    private RepeatingJob connectionJob;
	private int lineNumber;
    private ScriptStatus scriptStatus;
	private String currentlyExecutingScript;
	private RepeatingJob updateStatusJob;
    private List<NicosLogEntry> newLogEntries;
    private long lastEntryTime;
    private List<QueuedScript> queuedScripts;

    private static final int MESSAGES_SCALE_FACTOR = 10;
    private static final int MESSAGES_THRESHOLD = 100;

    /**
     * Default constructor.
     * 
     * This will initialise the connection to zeroMQ and login to NICOS.
     * 
     * @param session
     *            the session to use to send and receive messages to and from
     *            the script sever
     * @param connectionJob
     *            the job that will periodically be run to reconnect to NICOS if
     *            a connection has failed. (pulled out of class for testing)
     */
    public NicosModel(ZMQSession session, RepeatingJob connectionJob) {
        this(session, connectionJob, System.currentTimeMillis());
    }

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
     * @param initialTime
     *            the time the model was initialised. Disregards NICOS log
     *            messages prior to this time.
     */
    public NicosModel(ZMQSession session, RepeatingJob connectionJob, long initialTime) {
        this.session = session;
        this.connectionJob = connectionJob;
        this.lastEntryTime = initialTime;

        updateStatusJob = new RepeatingJob("update script status", UPDATE_STATUS_TIME) {
            @Override
            protected IStatus doTask(IProgressMonitor monitor) {
                updateScriptStatus();
                updateLogEntries();
                return Status.OK_STATUS;
            }
        };
        updateStatusJob.setRunning(false);
        this.connectionJob.schedule();
    }

    private void failConnection(String message) {
        setConnectionStatus(ConnectionStatus.FAILED);
        LOG.error(message);
        setConnectionErrorMessage(message);
        connectionJob.setRunning(true);
        updateStatusJob.setRunning(false);
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
        updateStatusJob.setRunning(true);
    }
    
    /**
     * Disconnect the model from the NICOS server.
     */
    public void disconnect() {
        session.disconnect();
        setConnectionStatus(ConnectionStatus.DISCONNECTED);
        setConnectionErrorMessage("");
        connectionJob.setRunning(true);
        updateStatusJob.setRunning(false);
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
     * @return the latest log entries
     */
    public List<NicosLogEntry> getLogEntries() {
        return newLogEntries;
    }

    private void setLogEntries(List<NicosLogEntry> newLogEntries) {
        firePropertyChange("logEntries", this.newLogEntries, this.newLogEntries = newLogEntries);
        this.lastEntryTime = newLogEntries.get(newLogEntries.size() - 1).getTimeStamp();
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
     * Send a command for controlling the execution of the current script.
     * 
     * @param instruction
     *            The execution instruction to send to the server.
     */
    public void sendExecutionInstruction(ExecutionInstruction instruction) {
        SendMessageDetails response = sendMessageToNicos(instruction);
        if (!response.isSent()) {
            updateLogEntries();
            NicosLogEntry error = new NicosLogEntry(new Date(),
                    "Error sending " + instruction.toString() + " command: " + response.getFailureReason() + "\n");
            setLogEntries(Arrays.asList(error));
            getScriptStatus();
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
    
    /**
     * Gets the status of the currently executing script from the server.
     */
	public void updateScriptStatus() {
		ReceiveScriptStatus response = (ReceiveScriptStatus) sendMessageToNicos(new GetScriptStatus()).getResponse();
		if (response == null) {
			failConnection(NO_RESPONSE);
		} else {
            // Status is a tuple (list) of 2 items - execution status and line
            // number.
            setScriptStatus(ScriptStatus.getByValue(response.status.get(0)));
            setLineNumber(response.status.get(1));
			setCurrentlyExecutingScript(response.script);
			setQueuedScripts(response.requests);
		}
	}

    /**
     * Gets the latest messages from the NICOS log.
     */
    public void updateLogEntries() {
        int numMessages = 1;
        long firstEntryTime = 0;
        List<NicosLogEntry> newEntries = new ArrayList<NicosLogEntry>();
        do {
            if (numMessages > MESSAGES_THRESHOLD) {
                newEntries.add(new NicosLogEntry(new Date(),
                        "WARNING: Message volume is too high. Some messages may be ommitted.\n"));
                break;
            }
            ReceiveLogMessage response = (ReceiveLogMessage) sendMessageToNicos(new GetLog(numMessages)).getResponse();
            if (response == null) {
                failConnection(NO_RESPONSE);
                break;
            }
            List<NicosLogEntry> current = response.getEntries();
            if (newEntries.size() == current.size()) {
                // nothing more to fetch
                break;
            }
            
            newEntries = new ArrayList<NicosLogEntry>(current);
            firstEntryTime = newEntries.get(0).getTimeStamp();
            numMessages *= MESSAGES_SCALE_FACTOR;
        } while (firstEntryTime > this.lastEntryTime);

        newEntries = filterOld(newEntries);
        if (!newEntries.isEmpty()) {
            setLogEntries(newEntries);
        }
    }

    private List<NicosLogEntry> filterOld(List<NicosLogEntry> entries) {
        List<NicosLogEntry> filtered = new ArrayList<NicosLogEntry>();
        for (NicosLogEntry entry : entries) {
            if (entry.getTimeStamp() > this.lastEntryTime) {
                filtered.add(entry);
            }
        }
        return filtered;
    }

	private void setLineNumber(int lineNumber) {		
		firePropertyChange("lineNumber", this.lineNumber, this.lineNumber = lineNumber);
	}
	
	/**
	 * The currently executing line number.
	 * @return the line number
	 */
	public int getLineNumber() {
		return lineNumber;
	}
	
	private void setQueuedScripts(List<QueuedScript> newQueuedScripts) {		
		firePropertyChange("queuedScripts", this.queuedScripts, this.queuedScripts = newQueuedScripts);
	}
	
	/**
	 * The currently queued scripts.
	 * @return the queued scripts
	 */
	public List<QueuedScript> getQueuedScripts() {
		return queuedScripts;
	}
	
    private void setScriptStatus(ScriptStatus scriptStatus) {
        firePropertyChange("scriptStatus", this.scriptStatus, this.scriptStatus = scriptStatus);
    }

    /**
     * The current script execution status.
     * 
     * @return the script status
     */
    public ScriptStatus getScriptStatus() {
        return scriptStatus;
    }

	private void setCurrentlyExecutingScript(String script) {
		firePropertyChange("currentlyExecutingScript", this.currentlyExecutingScript, this.currentlyExecutingScript = script);
	}
	
	/**
	 * The currently executing script.
	 * @return the script
	 */
	public String getCurrentlyExecutingScript() {
		return currentlyExecutingScript;
	}
}
