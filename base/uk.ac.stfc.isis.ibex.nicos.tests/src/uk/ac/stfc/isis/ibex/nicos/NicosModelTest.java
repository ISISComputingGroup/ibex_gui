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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.nicos.comms.RepeatingJob;
import uk.ac.stfc.isis.ibex.nicos.comms.ZMQSession;
import uk.ac.stfc.isis.ibex.nicos.messages.Login;
import uk.ac.stfc.isis.ibex.nicos.messages.NICOSMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.QueueScript;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.SendMessageDetails;

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

    private ReceiveMessage receivedMessage = mock(ReceiveMessage.class);
    private NICOSMessage sendMessage = mock(NICOSMessage.class);

    @Before
    public void setUp() {
        model = new NicosModel(zmqSession, connJob);

        model.addPropertyChangeListener("connectionStatus", connectionStatusListener);
        model.addPropertyChangeListener("connectionErrorMessage", connectionErrorListener);

        model.addPropertyChangeListener("scriptSendStatus", scriptStatusListener);
        model.addPropertyChangeListener("scriptSendErrorMessage", scriptErrorListener);
    }

    /**
     * Helper function to connect the model to NICOS.
     */
    private void connectSuccessfully() {
        when(zmqSession.connect(any())).thenReturn(SendMessageDetails.createSendSuccess(receivedMessage));
        when(zmqSession.sendMessage(any())).thenReturn(SendMessageDetails.createSendSuccess(receivedMessage));

        model.connect(new InstrumentInfo("", "", "TEST"));
    }

    @Test
    public void GIVEN_no_connection_WHEN_get_connection_status_THEN_status_is_unconnected() {

        ConnectionStatus result = model.getConnectionStatus();

        assertThat("Connection status", result, is(ConnectionStatus.DISCONNECTED));
    }
    
    @Test
    public void GIVEN_no_connection_WHEN_get_connection_error_message_THEN_message_is_blank() {

        String result = model.getConnectionErrorMessage();

        assertThat("Connection status", result, is(""));
    }

    @Test
    public void GIVEN_failing_session_connection_WHEN_connect_first_called_THEN_first_status_is_connecting() {
        when(zmqSession.connect(any())).thenReturn(SendMessageDetails.createSendFail("BAD"));

        model.connect(new InstrumentInfo("", "", "TEST"));
        verify(connectionStatusListener, times(2)).propertyChange(propertyChangeArgument.capture());

        Object connectionStatus = propertyChangeArgument.getAllValues().get(0).getNewValue();
        assertThat("Connection status", connectionStatus, is(ConnectionStatus.CONNECTING));
    }

    @Test
    public void GIVEN_failing_session_connection_WHEN_connect_called_THEN_final_status_is_failed() {
        when(zmqSession.connect(any())).thenReturn(SendMessageDetails.createSendFail("BAD"));

        model.connect(new InstrumentInfo("", "", "TEST"));
        verify(connectionStatusListener, times(2)).propertyChange(propertyChangeArgument.capture());

        Object connectionStatus = propertyChangeArgument.getAllValues().get(1).getNewValue();
        assertThat("Connection status", connectionStatus, is(ConnectionStatus.FAILED));
    }

    @Test
    public void GIVEN_failing_session_connection_WHEN_connect_called_THEN_final_error_message_is_failure_message() {
        String failureMessage = "FAILED";
        when(zmqSession.connect(any())).thenReturn(SendMessageDetails.createSendFail(failureMessage));

        model.connect(new InstrumentInfo("", "", "TEST"));
        verify(connectionErrorListener).propertyChange(propertyChangeArgument.capture());

        Object connectionError = propertyChangeArgument.getValue().getNewValue();
        assertThat("Connection status", connectionError, is(failureMessage));
    }

    @Test
    public void GIVEN_failing_login_connection_WHEN_connect_called_THEN_final_status_is_failed() {
        when(zmqSession.connect(any())).thenReturn(SendMessageDetails.createSendSuccess(receivedMessage));
        when(zmqSession.sendMessage(any())).thenReturn(SendMessageDetails.createSendFail("FAILED"));

        model.connect(new InstrumentInfo("", "", "TEST"));
        verify(connectionStatusListener, times(2)).propertyChange(propertyChangeArgument.capture());

        Object connectionStatus = propertyChangeArgument.getAllValues().get(1).getNewValue();
        assertThat("Connection status", connectionStatus, is(ConnectionStatus.FAILED));
    }

    @Test
    public void GIVEN_failing_login_connection_WHEN_connect_called_THEN_final_error_message_is_login_failure_message() {
        String failureMessage = "FAILED";
        when(zmqSession.connect(any())).thenReturn(SendMessageDetails.createSendSuccess(receivedMessage));
        when(zmqSession.sendMessage(any())).thenReturn(SendMessageDetails.createSendFail(failureMessage));

        model.connect(new InstrumentInfo("", "", "TEST"));
        verify(connectionErrorListener).propertyChange(propertyChangeArgument.capture());

        String connectionError = (String) propertyChangeArgument.getValue().getNewValue();

        assertTrue(connectionError.startsWith("Failed to login: "));
        assertTrue(connectionError.contains(failureMessage));
    }
    
    @Test
    public void GIVEN_successful_connection_WHEN_connect_called_THEN_final_status_is_connected() {
        connectSuccessfully();
        verify(connectionStatusListener, times(2)).propertyChange(propertyChangeArgument.capture());

        Object connectionStatus = propertyChangeArgument.getAllValues().get(1).getNewValue();
        assertThat("Connection status", connectionStatus, is(ConnectionStatus.CONNECTED));
    }
    
    @Test
    public void GIVEN_successful_connection_WHEN_connect_called_THEN_error_message_is_unchanged() {
        connectSuccessfully();
        verify(connectionErrorListener, never()).propertyChange(propertyChangeArgument.capture());
    }

    @Test
    public void WHEN_connect_called_THEN_session_connect_called() {
        connectSuccessfully();
        verify(zmqSession, times(1)).connect(any());
    }

    @Test
    public void WHEN_connect_called_THEN_login_sent() {
        connectSuccessfully();

        ArgumentCaptor<NICOSMessage> message = ArgumentCaptor.forClass(NICOSMessage.class);
        verify(zmqSession, times(1)).sendMessage(message.capture());
        assertThat(message.getValue(), instanceOf(Login.class));
    }

    @Test
    public void GIVEN_connection_failed_WHEN_connect_called_THEN_login_not_called() {
        when(zmqSession.connect(any())).thenReturn(SendMessageDetails.createSendFail("FAILED"));

        model.connect(new InstrumentInfo("", "", "TEST"));
        verify(zmqSession, never()).sendMessage(sendMessage);
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
        assertThat(running.getValue(), is(false));
    }

    @Test
    public void GIVEN_successful_connection_WHEN_failed_connection_THEN_connection_job_resumed() {
        connectSuccessfully();

        when(zmqSession.connect(any())).thenReturn(SendMessageDetails.createSendFail("FAILED"));
        model.connect(new InstrumentInfo("", "", "TEST"));

        ArgumentCaptor<Boolean> running = ArgumentCaptor.forClass(Boolean.class);
        verify(connJob, times(2)).setRunning(running.capture());
        assertThat(running.getAllValues().get(1), is(true));
    }

    @Test
    public void GIVEN_successful_connection_WHEN_disconnected_THEN_connection_job_resumed() {
        connectSuccessfully();

        model.disconnect();

        ArgumentCaptor<Boolean> running = ArgumentCaptor.forClass(Boolean.class);
        verify(connJob, times(2)).setRunning(running.capture());
        assertThat(running.getAllValues().get(1), is(true));
    }

    @Test
    public void WHEN_disconnect_THEN_status_is_disconnected() {
        model.disconnect();

        ConnectionStatus result = model.getConnectionStatus();

        assertThat("Connection status", result, is(ConnectionStatus.DISCONNECTED));
    }

    @Test
    public void WHEN_disconnect_THEN_session_disconnected() {
        model.disconnect();

        verify(zmqSession, times(1)).disconnect();
    }

    @Test
    public void WHEN_model_created_THEN_script_status_is_none() {
        assertThat(model.getScriptSendStatus(), is(ScriptSendStatus.NONE));
    }

    @Test
    public void GIVEN_successful_connection_WHEN_script_sent_THEN_first_status_is_sending() {
        connectSuccessfully();

        model.sendScript("TEST");

        verify(scriptStatusListener, times(2)).propertyChange(propertyChangeArgument.capture());

        Object scriptStatus = propertyChangeArgument.getAllValues().get(0).getNewValue();
        assertThat(scriptStatus, is(ScriptSendStatus.SENDING));
    }

    @Test
    public void GIVEN_successful_connection_WHEN_script_successfully_sent_THEN_final_status_is_sent() {
        connectSuccessfully();

        model.sendScript("TEST");

        verify(scriptStatusListener, times(2)).propertyChange(propertyChangeArgument.capture());

        Object scriptStatus = propertyChangeArgument.getAllValues().get(1).getNewValue();
        assertThat(scriptStatus, is(ScriptSendStatus.SENT));
    }

    @Test
    public void GIVEN_successful_connection_WHEN_script_unsuccessfully_sent_THEN_final_status_is_failed() {
        connectSuccessfully();

        when(zmqSession.sendMessage(any())).thenReturn(SendMessageDetails.createSendFail("FAILED"));
        model.sendScript("TEST");

        verify(scriptStatusListener, times(2)).propertyChange(propertyChangeArgument.capture());

        Object scriptStatus = propertyChangeArgument.getAllValues().get(1).getNewValue();
        assertThat(scriptStatus, is(ScriptSendStatus.SEND_ERROR));
    }

    @Test
    public void GIVEN_successful_connection_WHEN_script_unsuccessfully_sent_THEN_final_error_status_is_script_failed() {
        connectSuccessfully();

        when(zmqSession.sendMessage(any())).thenReturn(SendMessageDetails.createSendFail("FAILED"));
        model.sendScript("TEST");

        verify(scriptErrorListener).propertyChange(propertyChangeArgument.capture());

        Object scriptError = propertyChangeArgument.getValue().getNewValue();
        assertThat(scriptError, is("Failed to send script"));
    }

    @Test
    public void GIVEN_successful_connection_WHEN_script_sent_THEN_queue_script_message_sent() {
        connectSuccessfully();

        model.sendScript("TEST");

        ArgumentCaptor<NICOSMessage> message = ArgumentCaptor.forClass(NICOSMessage.class);
        verify(zmqSession, times(2)).sendMessage(message.capture());
        assertThat(message.getValue(), instanceOf(QueueScript.class));
    }

}
