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
package uk.ac.stfc.isis.ibex.nicos;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.zeromq.ZMQException;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.nicos.comms.RepeatingJob;
import uk.ac.stfc.isis.ibex.nicos.comms.ZMQSession;
import uk.ac.stfc.isis.ibex.nicos.messages.GetBanner;
import uk.ac.stfc.isis.ibex.nicos.messages.GetLog;
import uk.ac.stfc.isis.ibex.nicos.messages.Login;
import uk.ac.stfc.isis.ibex.nicos.messages.NICOSMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.NicosLogEntry;
import uk.ac.stfc.isis.ibex.nicos.messages.QueueScript;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveBannerMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveLogMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveLoginMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.SentMessageDetails;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.GetScriptStatus;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.ReceiveScriptStatus;

@SuppressWarnings({ "unchecked", "rawtypes" })  // Mockito doesn't handle generic types nicely.
public class NicosModelTest {
    private ZMQSession zmqSession = mock(ZMQSession.class);
    private RepeatingJob connJob = mock(RepeatingJob.class);

    private NicosModel model;

    private PropertyChangeListener connectionStatusListener = mock(PropertyChangeListener.class);
    private PropertyChangeListener connectionErrorListener = mock(PropertyChangeListener.class);

    private PropertyChangeListener scriptStatusListener = mock(PropertyChangeListener.class);
    private PropertyChangeListener scriptErrorListener = mock(PropertyChangeListener.class);

    private ArgumentCaptor<PropertyChangeEvent> propertyChangeArgument =
            ArgumentCaptor.forClass(PropertyChangeEvent.class);

    private ReceiveBannerMessage bannerResponse = mock(ReceiveBannerMessage.class);
    private ReceiveLoginMessage loginResponse = mock(ReceiveLoginMessage.class);

    private Answer<List<NicosLogEntry>> incrementalLog = new Answer<List<NicosLogEntry>>() {
        // Simulate responses to requests for log entries with
        // increasing volume
        private int count = 0;

        @Override
        public List<NicosLogEntry> answer(InvocationOnMock invocation) {
            int lastEntryTime = 1050;
            int n = (int) Math.pow(10, count);
            count++;
            return createNEntries(n, lastEntryTime);
        }
    };

    @Before
    public void setUp() {
    	
        model = new NicosModel(zmqSession, connJob, 0);
        
        model.addPropertyChangeListener("connectionStatus", connectionStatusListener);
        model.addPropertyChangeListener("connectionErrorMessage", connectionErrorListener);

        model.addPropertyChangeListener("scriptSendStatus", scriptStatusListener);
        model.addPropertyChangeListener("scriptSendErrorMessage", scriptErrorListener);
    }

    /**
     * Helper method to create a valid banner response.
     */
    private void createBannerResponse() {
        when(bannerResponse.protocolValid()).thenReturn(true);
        when(bannerResponse.serializerValid()).thenReturn(true);

        when(zmqSession.sendMessage(isA(GetBanner.class)))
                .thenReturn(SentMessageDetails.createSendSuccess(bannerResponse));
    }

    /**
     * Helper function to connect the model to NICOS.
     */
    private void connectSuccessfully() {
        createBannerResponse();
        when(zmqSession.sendMessage(isA(Login.class)))
                .thenReturn(SentMessageDetails.createSendSuccess(loginResponse));

        model.connect(new InstrumentInfo("", "", "TEST"));
    }

    /**
     * Create a list of n log entries for simulating a response from the nicos
     * log. Entries are 1 ms apart for simplicity.
     * 
     * @param n
     *            The number of log entries in the message
     * @param lastTime
     *            The timestamp of the final message
     * @return A list of n consecutive Nicos log entries.
     */
    private List<NicosLogEntry> createNEntries(int n, long lastTime) {
        List<NicosLogEntry> entries = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Date now1 = new Date(lastTime - i);
            NicosLogEntry entry = new NicosLogEntry(now1, "message at " + (lastTime - i));
            entries.add(entry);
        }
        Collections.reverse(entries); // Reverse so that timestamps are in
                                      // ascending order.
        return entries;
    }

    @Test
    public void GIVEN_failing_session_connection_WHEN_connect_called_THEN_final_status_is_failed() {
        doThrow(new ZMQException(1)).when(zmqSession).connect(any(InstrumentInfo.class));

        model.connect(new InstrumentInfo("", "", "TEST"));
        assertEquals("Connection status", NicosErrorState.CONNECTION_FAILED, model.getError());
    }

    @Test
    public void GIVEN_failing_session_connection_WHEN_connect_called_THEN_final_error_message_is_failure_message() {
        String failureMessage = "FAILED";
        doThrow(new ZMQException(failureMessage, 1)).when(zmqSession).connect(any(InstrumentInfo.class));

        model.connect(new InstrumentInfo("", "", "TEST"));

        assertEquals("Connection status", NicosErrorState.CONNECTION_FAILED, model.getError());
    }

    @Test
    public void GIVEN_failing_banner_message_WHEN_connect_called_THEN_final_status_is_failed() {
        when(zmqSession.sendMessage(isA(GetBanner.class))).thenReturn(SentMessageDetails.createSendFail("FAILED"));

        model.connect(new InstrumentInfo("", "", "TEST"));
        
        assertEquals("Connection status", NicosErrorState.CONNECTION_FAILED, model.getError());
    }

    @Test
    public void GIVEN_failing_banner_message_WHEN_connect_called_THEN_final_error_message_is_failure() {
        String failureMessage = "FAILED";
        when(zmqSession.sendMessage(isA(GetBanner.class)))
                .thenReturn(SentMessageDetails.createSendFail(failureMessage));

        model.connect(new InstrumentInfo("", "", "TEST"));

        assertTrue(model.getError().equals(NicosErrorState.CONNECTION_FAILED));
    }

    @Test
    public void GIVEN_incorrect_protocol_WHEN_connect_called_THEN_final_error_message_is_protocol_invalid() {
        when(bannerResponse.protocolValid()).thenReturn(false);
        when(bannerResponse.serializerValid()).thenReturn(true);
        when(zmqSession.sendMessage(isA(GetBanner.class)))
                .thenReturn(SentMessageDetails.createSendSuccess(bannerResponse));

        model.connect(new InstrumentInfo("", "", "TEST"));
        assertTrue(model.getError().equals(NicosErrorState.INVALID_PROTOCOL));
    }

    @Test
    public void GIVEN_incorrect_serialiser_WHEN_connect_called_THEN_final_error_message_is_serialiser_invalid() {
        when(bannerResponse.protocolValid()).thenReturn(true);
        when(bannerResponse.serializerValid()).thenReturn(false);
        when(zmqSession.sendMessage(isA(GetBanner.class)))
                .thenReturn(SentMessageDetails.createSendSuccess(bannerResponse));

        model.connect(new InstrumentInfo("", "", "TEST"));

        assertTrue(model.getError().equals(NicosErrorState.INVALID_SERIALISER));
    }

    @Test
    public void GIVEN_failing_login_connection_WHEN_connect_called_THEN_final_status_is_failed() {
        createBannerResponse();
        when(zmqSession.sendMessage(isA(Login.class))).thenReturn(SentMessageDetails.createSendFail("FAILED"));

        model.connect(new InstrumentInfo("", "", "TEST"));

        assertEquals("Connection status", NicosErrorState.FAILED_LOGIN, model.getError());
    }

    @Test
    public void GIVEN_failing_login_connection_WHEN_connect_called_THEN_final_error_message_is_login_failure_message() {
        createBannerResponse();
        String failureMessage = "FAILED";
        when(zmqSession.sendMessage(isA(Login.class))).thenReturn(SentMessageDetails.createSendFail(failureMessage));

        model.connect(new InstrumentInfo("", "", "TEST"));

        assertTrue(model.getError().equals(NicosErrorState.FAILED_LOGIN));
    }
    
    @Test
    public void GIVEN_successful_connection_WHEN_connect_called_THEN_final_status_is_connected() {
        connectSuccessfully();
        assertEquals("Connection status", NicosErrorState.NO_ERROR, model.getError());
    }
    
    @Test
    public void GIVEN_successful_connection_WHEN_connect_called_THEN_error_message_is_unchanged() {
        connectSuccessfully();
        verify(connectionErrorListener, never()).propertyChange(propertyChangeArgument.capture());
    }

    @Test
    public void WHEN_connect_called_THEN_session_connect_called() {
        connectSuccessfully();
        verify(zmqSession, times(1)).connect(any(InstrumentInfo.class));
    }

    @Test
    public void WHEN_connect_called_THEN_login_sent() {
        connectSuccessfully();

        ArgumentCaptor<NICOSMessage> message = ArgumentCaptor.forClass(NICOSMessage.class);
        verify(zmqSession, times(2)).sendMessage(message.capture());
        assertThat(message.getValue(), instanceOf(Login.class));
    }

    @Test
    public void WHEN_connect_called_THEN_banner_message_sent() {
        connectSuccessfully();

		ArgumentCaptor<NICOSMessage> message = ArgumentCaptor.forClass(NICOSMessage.class);
        verify(zmqSession, times(2)).sendMessage(message.capture());
        assertThat(message.getAllValues().get(0), instanceOf(GetBanner.class));
    }

    @Test
    public void GIVEN_connection_failed_WHEN_connect_called_THEN_banner_message_not_called() {
        doThrow(new ZMQException(1)).when(zmqSession).connect(any(InstrumentInfo.class));

        model.connect(new InstrumentInfo("", "", "TEST"));
        verify(zmqSession, never()).sendMessage(Mockito.any());
    }

    @Test
    public void GIVEN_banner_failed_WHEN_connect_called_THEN_login_not_called() {
        when(zmqSession.sendMessage(isA(GetBanner.class))).thenReturn(SentMessageDetails.createSendFail("FAILED"));

        model.connect(new InstrumentInfo("", "", "TEST"));
        ArgumentCaptor<NICOSMessage> message = ArgumentCaptor.forClass(NICOSMessage.class);
        verify(zmqSession, times(1)).sendMessage(message.capture());
        assertThat(message.getValue(), instanceOf(GetBanner.class));
    }

    @Test
    public void WHEN_model_created_THEN_connection_job_scheduled() {
        verify(connJob, times(1)).schedule();
    }

    @Test
    public void GIVEN_successful_connection_WHEN_connect_created_THEN_connection_job_paused() {
        connectSuccessfully();

        ArgumentCaptor<Boolean> running = ArgumentCaptor.forClass(Boolean.class);
        verify(connJob, times(1)).setRunning(running.capture());
        assertEquals(false, running.getValue());
    }

    @Test
    public void GIVEN_successful_connection_WHEN_disconnected_THEN_connection_job_resumed() {
        connectSuccessfully();

        model.disconnect();

        ArgumentCaptor<Boolean> running = ArgumentCaptor.forClass(Boolean.class);
        verify(connJob, times(2)).setRunning(running.capture());
        assertEquals(true, running.getAllValues().get(1));
    }

    @Test
    public void WHEN_disconnect_THEN_status_is_disconnected() {
        model.disconnect();

        NicosErrorState result = model.getError();

        assertEquals("Connection status", NicosErrorState.CONNECTION_FAILED, result);
    }

    @Test
    public void WHEN_disconnect_THEN_session_disconnected() {
        model.disconnect();

        verify(zmqSession, times(1)).disconnect();
    }

    @Test
    public void GIVEN_successful_connection_WHEN_script_unsuccessfully_sent_THEN_final_error_status_is_script_failed() {
        connectSuccessfully();

        when(zmqSession.sendMessage(isA(QueueScript.class))).thenReturn(SentMessageDetails.createSendFail("FAILED"));
        model.sendScript(new QueuedScript());

        assertEquals(NicosErrorState.SCRIPT_SEND_FAIL, model.getError());
    }

    @Test
    public void GIVEN_successful_connection_WHEN_script_sent_THEN_queue_script_message_sent() {
        connectSuccessfully();
        when(zmqSession.sendMessage(isA(QueueScript.class)))
                .thenReturn(SentMessageDetails.createSendSuccess(""));

        model.sendScript(new QueuedScript());

        ArgumentCaptor<NICOSMessage> message = ArgumentCaptor.forClass(NICOSMessage.class);
        verify(zmqSession, atLeast(0)).sendMessage(message.capture());
        assertThat(message.getValue(), instanceOf(QueueScript.class));
    }
    
    @Test
    public void GIVEN_successful_connection_but_no_reply_WHEN_get_script_status_THEN_connection_fail() {
        connectSuccessfully();
        
        when(zmqSession.sendMessage(isA(GetScriptStatus.class))).thenReturn(SentMessageDetails.createSendSuccess(null));
        
        model.updateScriptStatus();
        
        assertEquals(NicosErrorState.CONNECTION_FAILED, model.getError());
    }
    
    @Test
    public void GIVEN_successful_connection_WHEN_get_script_status_THEN_line_number_extracted_from_second_element_of_list() {
        connectSuccessfully();
        
        ReceiveScriptStatus response = new ReceiveScriptStatus();
        
        int linenum = 10;
        
        response.status = Arrays.asList(0, linenum);
        
        when(zmqSession.sendMessage(isA(GetScriptStatus.class))).thenReturn(SentMessageDetails.createSendSuccess(response));

        model.updateScriptStatus();
        
        assertEquals(linenum, model.getLineNumber());
    }
    
    @Test
    public void GIVEN_successful_connection_WHEN_get_script_status_THEN_script_extracted() {
        connectSuccessfully();
        
        ReceiveScriptStatus response = new ReceiveScriptStatus();
        
        String script = "This is a really nice script \n with a newline in it.";
        
        response.status = Arrays.asList(0, 0);
        response.script = script;
        
        when(zmqSession.sendMessage(isA(GetScriptStatus.class))).thenReturn(SentMessageDetails.createSendSuccess(response));

        model.updateScriptStatus();
        
        assertEquals(script, model.getCurrentlyExecutingScript());
    }
    
    @Test
    public void GIVEN_successful_connection_WHEN_get_script_status_THEN_script_name_extracted() {
    	connectSuccessfully();
    	
    	ReceiveScriptStatus response = new ReceiveScriptStatus();
    	
        response.status = Arrays.asList(0, 0);
        response.script = "Contents of script";
    	response.scriptname = "My Script";
    	
        when(zmqSession.sendMessage(isA(GetScriptStatus.class))).thenReturn(SentMessageDetails.createSendSuccess(response));

        model.updateScriptStatus();
        
        assertEquals("My Script", model.getScriptName());
    	
    }
    
    @Test
    public void GIVEN_successful_connection_WHEN_get_script_status_THEN_queued_scripts_extracted() {
        connectSuccessfully();
        
        ReceiveScriptStatus response = new ReceiveScriptStatus();
        
        QueuedScript queuedScript = new QueuedScript(); 
        queuedScript.setCode("This is a really nice script \n with a newline in it.");
        queuedScript.setName("instrument_script");
        queuedScript.reqid = "weifj9032l90djk239";
        queuedScript.user = "IBEX";
        
        response.status = Arrays.asList(0, 0);
        response.script = "";
        response.requests = Arrays.asList(queuedScript);
        
        when(zmqSession.sendMessage(isA(GetScriptStatus.class))).thenReturn(SentMessageDetails.createSendSuccess(response));

        model.updateScriptStatus();
        
        assertEquals(queuedScript, model.getQueuedScripts().get(0));
    }

    @Test
    public void GIVEN_successful_connection_WHEN_update_log_THEN_log_messages_parsed_correctly() {
        // Arrange
        connectSuccessfully();

        ReceiveLogMessage response = mock(ReceiveLogMessage.class);
        when(zmqSession.sendMessage(isA(GetLog.class))).thenReturn(SentMessageDetails.createSendSuccess(response));

        when(response.getEntries()).thenReturn(createNEntries(1, 1));
        List<NicosLogEntry> expected = createNEntries(1, 1);

        // Act
        model.updateLogEntries();
        List<NicosLogEntry> actual = model.getLogEntries();

        // Assert
        assertEquals(expected.get(0).toString(), actual.get(0).toString());
    }

    @Test
    public void GIVEN_message_contains_old_entries_WHEN_updating_log_THEN_model_only_reads_new_entries() {
        // Arrange
        connectSuccessfully();

        ReceiveLogMessage response = mock(ReceiveLogMessage.class);
        when(zmqSession.sendMessage(isA(GetLog.class))).thenReturn(SentMessageDetails.createSendSuccess(response));

        when(response.getEntries()).thenReturn(createNEntries(1, 1));
        model.updateLogEntries();

        // Act
        when(response.getEntries()).thenReturn(createNEntries(1, 2));
        List<NicosLogEntry> expected = createNEntries(1, 2);

        model.updateLogEntries();
        List<NicosLogEntry> actual = model.getLogEntries();

        // Assert
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).toString(), actual.get(0).toString());
    }

    @Test
    public void
            GIVEN_first_entry_in_response_after_latest_entry_read_WHEN_updating_log_THEN_query_for_more_entries() {
        // Arrange
        connectSuccessfully();

        ReceiveLogMessage response = mock(ReceiveLogMessage.class);
        when(zmqSession.sendMessage(isA(GetLog.class))).thenReturn(SentMessageDetails.createSendSuccess(response));

        // Receive an initial message with timestamp to compare against
        Date initialDate = new Date(1000);
        NicosLogEntry initialEntry = new NicosLogEntry(initialDate, "initial entry message");
        when(response.getEntries()).thenReturn(Arrays.asList(initialEntry));
        model.updateLogEntries();

        // Reset method invocation count for later verification
        reset(response);

        // Act
        when(response.getEntries()).thenAnswer(incrementalLog);
        model.updateLogEntries();

        // Assert
        // Response should be requested 2 times with increasing volume
        verify(response, times(3)).getEntries();

        // initial entry was at timestamp 1000;
        // 50 new entries from timestamp 1001 - 1050
        int expected = 50;
        int actual = model.getLogEntries().size();
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_entry_volume_greater_than_threshold_WHEN_updating_log_THEN_model_throws_away_excess() {
        // Arrange
        connectSuccessfully();
        int threshold = 1000;

        ReceiveLogMessage response = mock(ReceiveLogMessage.class);
        when(zmqSession.sendMessage(isA(GetLog.class))).thenReturn(SentMessageDetails.createSendSuccess(response));


        // Act
        when(response.getEntries()).thenAnswer(incrementalLog);
        model.updateLogEntries();

        // Assert
        int expected = threshold + 1; // Add 1 for appended error message
        int actual = model.getLogEntries().size();
        assertEquals(expected, actual);
        
    }
}
