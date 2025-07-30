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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.zeromq.ZMQException;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.comms.RepeatingJob;
import uk.ac.stfc.isis.ibex.nicos.comms.ZMQSession;
import uk.ac.stfc.isis.ibex.nicos.messages.DequeueScript;
import uk.ac.stfc.isis.ibex.nicos.messages.ExecutionInstruction;
import uk.ac.stfc.isis.ibex.nicos.messages.GetBanner;
import uk.ac.stfc.isis.ibex.nicos.messages.GetLog;
import uk.ac.stfc.isis.ibex.nicos.messages.Login;
import uk.ac.stfc.isis.ibex.nicos.messages.NICOSMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.NicosLogEntry;
import uk.ac.stfc.isis.ibex.nicos.messages.QueueScript;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveBannerMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveLogMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveLoginMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveNullMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.SendReorderedQueue;
import uk.ac.stfc.isis.ibex.nicos.messages.SentMessageDetails;
import uk.ac.stfc.isis.ibex.nicos.messages.UpdateScript;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.GetScriptStatus;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.ReceiveScriptStatus;

/**
 * The model that holds the connection to NICOS.
 */
public class NicosModel extends ModelObject {

    private static final Logger LOG = IsisLog.getLogger(NicosModel.class);

    /**
     * The period to ask the server for a status update (in ms).
     */
    private static final long UPDATE_STATUS_TIME = 1000;

    /**
     * Maximum number of messages to fetch at a time.
     */
    private static final int MESSAGES_THRESHOLD = 1000;

	private static final int MESSAGES_SCALE_FACTOR = 10;

    private final ZMQSession session;
    private RepeatingJob connectionJob;
	private int lineNumber;
	private String scriptName;
    private ScriptStatus scriptStatus;
	private String currentlyExecutingScript;
	private RepeatingJob updateStatusJob;
    private List<NicosLogEntry> newLogEntries = new ArrayList<>();
    private long lastEntryTime;
    private List<QueuedScript> queuedScripts = new ArrayList<>();

    // Start with a 'connection failed' error. This will get cleared when
    // a successful connection gets made.
	private NicosErrorState error = NicosErrorState.CONNECTION_FAILED;

    /**
     * Default constructor.
     *
     * This will initialise the connection to zeroMQ and login to NICOS.
     *
     * @param session
     *            the session to use to send and receive messages to and from
     *            the script server
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
        setError(NicosErrorState.CONNECTION_FAILED, message);
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

        try {
            session.connect(instrument);
        } catch (ZMQException e) {
            setError(NicosErrorState.CONNECTION_FAILED);
            return;
        }

        GetBanner getBanner = new GetBanner();
        SentMessageDetails<ReceiveBannerMessage> bannerSentMessageDetails = sendMessageToNicos(getBanner);
        if (!bannerSentMessageDetails.isSent()) {
            failConnection(bannerSentMessageDetails.getFailureReason());
            return;
        } else {
            ReceiveBannerMessage banner = bannerSentMessageDetails.getResponse();
            if (!banner.protocolValid()) {
            	setError(NicosErrorState.INVALID_PROTOCOL);
                return;
            } else if (!banner.serializerValid()) {
            	setError(NicosErrorState.INVALID_SERIALISER);
                return;
            }
        }

        SentMessageDetails<ReceiveLoginMessage> loginSentMessageDetails = sendMessageToNicos(new Login());
        if (!loginSentMessageDetails.isSent()) {
        	setError(NicosErrorState.FAILED_LOGIN, loginSentMessageDetails.getFailureReason());
            return;
        } else {
        	LOG.info(String.format("Successfully connected to NICOS on %s (%s)", instrument.name(), instrument.hostName()));
        	setError(NicosErrorState.NO_ERROR);
        }
        connectionJob.setRunning(false);
        updateStatusJob.setRunning(true);
    }

    /**
     * Disconnect the model from the NICOS server.
     */
    public void disconnect() {
        session.disconnect();
        setError(NicosErrorState.CONNECTION_FAILED);
        connectionJob.setRunning(true);
        updateStatusJob.setRunning(false);
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
     * Send a script to NICOS. Do not wait for a reply the acknowledgement can
     * be found in script send status.
     *
     * @param script
     *            to queue
     */
    public void sendScript(QueuedScript script) {
        QueueScript nicosMessage = new QueueScript(script);
        SentMessageDetails<String> scriptSentMessageDetails = sendMessageToNicos(nicosMessage);
        if (!scriptSentMessageDetails.isSent()) {
            setError(NicosErrorState.SCRIPT_SEND_FAIL);
        }
    }

    private void setError(NicosErrorState error) {
    	setError(error, "");
    }

    private void setError(NicosErrorState error, String additionalInformation) {
    	// Don't spam connection failures repeatedly to the log - these are expected in the case where the NICOS
    	// IOC has not been added to the configuration
    	if (error != NicosErrorState.NO_ERROR 
    			&& !(this.error == NicosErrorState.CONNECTION_FAILED && error == NicosErrorState.CONNECTION_FAILED)) {
            LOG.error("NICOS error: " + error.toString() + ", " + Strings.nullToEmpty(additionalInformation));
    	}
    	
    	firePropertyChange("error", this.error, this.error = error);
    }

    /**
     * Gets the last error in communication with NICOS.
     * @return the error
     */
    public NicosErrorState getError() {
    	return Optional.ofNullable(error).orElse(NicosErrorState.NO_ERROR);
    }

    /**
     * Send a command for controlling the execution of the current script.
     *
     * @param instruction
     *            The execution instruction to send to the server.
     */
    public void sendExecutionInstruction(ExecutionInstruction instruction) {
        SentMessageDetails<ReceiveNullMessage> executionInstructionSentMessageDetails = sendMessageToNicos(instruction);
        if (!executionInstructionSentMessageDetails.isSent()) {
            updateLogEntries();
            NicosLogEntry error = new NicosLogEntry(new Date(),
                    "Error sending " + instruction.toString() + " command: " + executionInstructionSentMessageDetails.getFailureReason() + "\n");
            setLogEntries(Arrays.asList(error));
            getScriptStatus();
        }
    }

    /**
     * Send a message to NICOS.
     *
     * @param nicosMessage
     *            message to send
     * @return details about the sending of that message
     */
    private <TSEND, TRESP> SentMessageDetails<TRESP> sendMessageToNicos(NICOSMessage<TSEND, TRESP> nicosMessage) {
        return session.sendMessage(nicosMessage);
    }

    /**
     * Gets the status of the currently executing script from the server.
     */
	public void updateScriptStatus() {
		SentMessageDetails<ReceiveScriptStatus> message = sendMessageToNicos(new GetScriptStatus());
		ReceiveScriptStatus scriptStatusSentMessageDetails = message.getResponse();
		if (scriptStatusSentMessageDetails == null || !message.isSent()) {
			disconnect();  //  We should always be able to get a response from this message. If we can't, NICOS is probably down.
		} else {
            // Status is a tuple (list) of 2 items - execution status and line number.
            setScriptStatus(ScriptStatus.getByValue(scriptStatusSentMessageDetails.status.get(0)));
            setLineNumber(scriptStatusSentMessageDetails.status.get(1));
            setCurrentlyExecutingScript(scriptStatusSentMessageDetails.script);
			setScriptName(scriptStatusSentMessageDetails.scriptname);
			setQueuedScripts(scriptStatusSentMessageDetails.requests);
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
            SentMessageDetails<ReceiveLogMessage> message = sendMessageToNicos(new GetLog(numMessages));
			ReceiveLogMessage logSentMessageDetails = message.getResponse();

            if (logSentMessageDetails == null || !message.isSent()) {
                setError(NicosErrorState.NO_RESPONSE);
                break;
            } else {
            	setError(NicosErrorState.NO_ERROR);
            }
            List<NicosLogEntry> current = logSentMessageDetails.getEntries();
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

    /**
     * Filters old log entries out from a nicos log.
     *
     * @param entries the original list of entries
     * @return a new list with old entries removed
     */
    private List<NicosLogEntry> filterOld(List<NicosLogEntry> entries) {
        return entries.stream()
        		.filter(e -> e.getTimeStamp() > this.lastEntryTime)
        		.collect(Collectors.toList());
    }

    /**
     * Set which line number is currently executing.
     *
     * @param lineNumber the line number
     */
	private void setLineNumber(int lineNumber) {
		firePropertyChange("lineNumber", this.lineNumber, this.lineNumber = lineNumber);
	}

	/**
	 * The currently executing line number.
	 *
	 * @return the line number
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * Sets the name of the currently executing script.
	 *
	 * @param scriptName of the current running script
	 */
	private void setScriptName(String scriptName) {
		firePropertyChange("scriptName", this.scriptName, this.scriptName = scriptName);
	}

	/**
	 * A formatted string representation of the current running script name to display on the user interface.
	 *
	 * @return a formatted string representation of the current script name to display on the user interface
	 */
	public String getScriptName() {
		return scriptName;
	}

	/**
	 * Sets the currently executing script text.
	 *
	 * @param script the current script
	 */
	private void setCurrentlyExecutingScript(String script) {
		firePropertyChange("currentlyExecutingScript", this.currentlyExecutingScript, this.currentlyExecutingScript = script);
	}

	/**
	 * The currently executing script text.
	 *
	 * @return the script
	 */
	public String getCurrentlyExecutingScript() {
		return currentlyExecutingScript;
    }

	/**
	 * Set the list of scripts in the nicos queue.
	 *
	 * @param newQueuedScripts the new list of scripts
	 */
	private void setQueuedScripts(List<QueuedScript> newQueuedScripts) {
		firePropertyChange("queuedScripts", this.queuedScripts, this.queuedScripts = newQueuedScripts);
	}

	/**
	 * The currently queued scripts.
	 *
	 * @return the queued scripts
	 */
	public List<QueuedScript> getQueuedScripts() {
        return Collections.unmodifiableList(queuedScripts);
    }

	/**
	 * The status of the currently executing script.
	 *
	 * @param scriptStatus the status
	 */
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

    /**
     * Dequeue script.
     *
     * @param reqid
     *            ID of script to dequeue
     */
    public void dequeueScript(String reqid) {
        sendMessageToNicos(new DequeueScript(reqid));
    }

    /**
     * Update the content of a script in the queue.
     *
     * @param script The script to update
     */
    public void updateScript(QueuedScript script) {
        sendMessageToNicos(new UpdateScript(script));
    }

    /**
     * Send reordered list of reqids to NICOS.
     *
     * @param listOfScriptIDs
     *            list of IDs of scripts
     */
	public void sendReorderedQueue(List<String> listOfScriptIDs) {
        sendMessageToNicos(new SendReorderedQueue(listOfScriptIDs));
	}
}
